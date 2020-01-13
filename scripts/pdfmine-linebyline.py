from pdfminer.layout import LAParams
from pdfminer.converter import PDFPageAggregator
from pdfminer.pdfparser import PDFParser
from pdfminer.pdfdocument import PDFDocument
from pdfminer.pdfpage import PDFPage
from pdfminer.pdfpage import PDFTextExtractionNotAllowed
from pdfminer.pdfinterp import PDFResourceManager
from pdfminer.pdfinterp import PDFPageInterpreter
from pdfminer.pdfdevice import PDFDevice
import os
import csv
import argparse
import webbrowser
from pathlib import Path
#import urllib2
parser = argparse.ArgumentParser()
parser.add_argument("--file", help="file from which data is to be extracted")
parser.add_argument("--rules", help="rules for extraction")
parser.add_argument("--storein", help="path where to store results")
args = parser.parse_args()
#scriptPath = os.path.dirname(os.path.abspath(__file__))
#scriptPath = "../upload"
scriptPath = os.path.join(Path(__file__).parent.parent,"upload")
rules=[]
dir_list = os.listdir(scriptPath) 
  
print("Files and directories in '", scriptPath, "' :")  
  
# print the list 
print(dir_list) 

if args.file:
    print(scriptPath+"/pdf/"+args.file)
    try:
        fp = open(scriptPath+"/pdf/"+args.file, 'rb')
        
		# Create a PDF parser object associated with the file object.
        parser = PDFParser(fp)
		# Create a PDF document object that stores the document structure.
		# Supply the password for initialization.
        document = PDFDocument(parser)
    except:
        print("Could not open specified file")
        exit()

    if args.rules:
        with open(scriptPath+'/rules/'+args.rules) as f:
            rules=f.read().split(',+,')
    else:
        print("Could not open rules")
        exit()

else:
    print("File to extract data from not specified")
    exit()


# Check if the document allows text extraction. If not, abort.
if not document.is_extractable:
    raise PDFTextExtractionNotAllowed

# Create a PDF resource manager object that stores shared resources.
rsrcmgr = PDFResourceManager()
# Create a PDF device object.
device = PDFDevice(rsrcmgr)
# Create a PDF interpreter object.
interpreter = PDFPageInterpreter(rsrcmgr, device)

# Set parameters for analysis.
laparams = LAParams(line_margin=0.2)
# Create a PDF page aggregator object.
device = PDFPageAggregator(rsrcmgr, laparams=laparams)
interpreter = PDFPageInterpreter(rsrcmgr, device)

#number of headers
num_of_headers=len(rules)
#dictionary to store header-value pairs
d={}
#index of header currently being searched
header_index=0
#list of words in the current header
currheader=rules[header_index].split()
print(currheader)
#index of word of header currently being searched
curr_word_index=0
#number of words in current header
curr_num_of_words=len(currheader)
#number of lines of values which have been searched for current header
value_count=0
#header for which values are being searched
header_for_which_to_search_values=''
#flag to say search for values, not headers
extract_value=False
#flag to stop search
final_terminate=False
#flag to say whether all headers have been found
last_header_found=False

for page in PDFPage.create_pages(document):
    if(final_terminate==True):
        break

    interpreter.process_page(page)
    # receive the LTPage object for the page.
    layout = device.get_result()

    for child in layout:
        if(final_terminate==True):
            break

        go_to_next=False
    
        if(child.__class__.__name__=='LTTextBoxHorizontal'):
            #print(child)
            text=child.get_text()
            #skip empty lines
            if(not text.strip()):
                continue

            #searching for headers, not values
            if(not extract_value):
                #print("Searching for header word",currheader[curr_word_index])
                #print("\tin",text)
                while(currheader[curr_word_index] in text):
                    #print("\tfound",currheader[curr_word_index])
                    curr_word_index+=1

                    #if entire header is found
                    if(curr_word_index==curr_num_of_words):
                        #print("Entire header",currheader,"found")
                        header_for_which_to_search_values=header_index
                        d[rules[header_for_which_to_search_values]]=[]
                        extract_value=True
                        header_index+=1

                        if(header_index==num_of_headers):
                            #print("\nAll headers found, stopping search")
                            last_header_found=True
                            curr_word_index=0
                            value_count=0
                            break
                        currheader=rules[header_index].split()
                        curr_word_index=0
                        curr_num_of_words=len(currheader)
                        value_count=0
                        #print()
                        #print("New header list:",currheader)

            #searching for values for rules[header_for_which_to_search_values]
            else:
                if(value_count<5):
                    #print("Searching value for",rules[header_for_which_to_search_values])
                    #print("\tin",text)

                    if(last_header_found==False):
                        while(currheader[curr_word_index] in text):
                            #print("\tWhile searching for values, found next header word:",currheader[curr_word_index])
                            curr_word_index+=1
                            extract_value=False     #switch to finding header

                            #if entire header is found
                            if(curr_word_index==curr_num_of_words):
                                header_for_which_to_search_values=header_index
                                d[rules[header_for_which_to_search_values]]=[]
                                extract_value=True
                                header_index+=1

                                if(header_index==num_of_headers):
                                    #print("\nAll headers found")
                                    last_header_found=True
                                    go_to_next=True
                                    curr_word_index=0
                                    value_count=0
                                    break
                                currheader=rules[header_index].split()
                                curr_word_index=0
                                curr_num_of_words=len(currheader)
                                value_count=0
                                #print()
                                #print("New header list:",currheader)

                    if(go_to_next==True):
                        continue
                    if(curr_word_index==0):
                        d[rules[header_for_which_to_search_values]].append(text.strip())
                        value_count+=1
                        if(value_count==5):
                            if(last_header_found==True):
                                final_terminate=True
                            extract_value=False

'''
        except:
            print("Some error")
        
print()
for i in d:
    print(i,":"," ".join(d[i]))
'''
with open(scriptPath+"/"+args.storein+'/'+args.file.split('/')[-1][:-3]+'csv','w') as f:
    for key in d.keys():
        f.write("%s,%s\n"%(key," ".join(d[key])))

with open(scriptPath+"/"+args.storein+'/'+args.file.split('/')[-1][:-3]+'txt','w') as f:
    for key in d.keys():
        f.write("%s:%s\n"%(key," ".join(d[key])))
print(args.file)
x = args.file.split("/")
if(x[0]=="single"):
    webbrowser.open("http://localhost:8080/GetProgress?"+x[1]+"+done",new=2)
else:
    webbrowser.open("http://localhost:8080/GetProgress?"+args.file+"+done",new=2)

