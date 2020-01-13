import pdfquery
import argparse
import os.path
import os

parser = argparse.ArgumentParser()
parser.add_argument("--file", help="file from which data is to be extracted")
parser.add_argument("--rules", help="rules for extraction")

args = parser.parse_args()
scriptPath = os.path.dirname(os.path.abspath(__file__))
header=[]

if args.file:
	try:
		pdf = pdfquery.PDFQuery(scriptPath+"/pdf/"+args.file)
		pdf.load(0)
	except:
		print("Could not open specified file")
		exit()

	if args.rules:
		with open(scriptPath+'/rules/'+args.rules) as f:
			headers=f.read().split(',+,')
	else:
		print("Could not open rules")
		exit()

else:
	print("File to extract data from not specified")

#headers=["Your first name and initial","spouse","Last name","Home address (number and street)","City, town or post office, state, and ZIP code","Your social security number"]

ext=open(scriptPath+'/extracted/'+args.file.split('.')[0]+'.txt','w')

for header in headers:
	#left_corner, bottom_corner-30, left_corner+150, bottom_corner
	try:
		label2 = pdf.pq('LTTextLineHorizontal:contains("'+header+'")')
		left_corner = float(label2.attr('x0')) 
		bottom_corner = float(label2.attr('y0'))
		right_corner=float(label2.attr('x1'))
		top_corner=float(label2.attr('y1'))-2

		bottom_corner-=25

		while(True):
			name = pdf.pq('LTTextLineHorizontal:in_bbox("%s, %s, %s, %s")' % (left_corner, bottom_corner, right_corner, top_corner)).text()
			
			if(name):
				ext.write(header+':'+name+'\n')
				print(header,name)
				break
			bottom_corner-=1
			top_corner-=1
	
	except:
		print('Error while extracting for',header,'. Skipping',header)

ext.close()
