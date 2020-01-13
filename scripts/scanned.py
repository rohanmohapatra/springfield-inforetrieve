#For tesseract OCR
from PIL import Image
import pytesseract
import builtins
pytesseract.pytesseract.tesseract_cmd = 'C:/Program Files (x86)/Tesseract-OCR/tesseract.exe'
tessdata_dir_config = '--tessdata-dir "C:\\Program Files (x86)\\Tesseract-OCR\\tessdata"'

#For image extraction
import sys
import os
import argparse
from pathlib import Path
parser = argparse.ArgumentParser()
parser.add_argument("--file", help="file from which data is to be extracted")
parser.add_argument("--b", help="0 for single file, 1 for batch")
#scriptPath = os.path.dirname(os.path.abspath(__file__))
#scriptPath = "../upload"
scriptPath = os.path.join(Path(__file__).parent.parent,"upload")
#parser.add_argument("--i", help="0 for non scanned, 1 for scanned", default=0)

args = parser.parse_args()


def bin_open(filename, mode='rb'):       # note, the default mode now opens in binary
    return original_open(filename, mode)

def do_ocr(f,n_jpg):
    for i in range(n_jpg):
        img=Image.open(scriptPath+'/ext_images/'+str(i)+'.jpg')

        text=pytesseract.image_to_string(img,config=tessdata_dir_config)
        f.write(text)
    f.close()

#Function to extract images from pdf
def ext_img(pdf):
    startmark = b"\xff\xd8"
    startfix = 0
    endmark = b"\xff\xd9"
    endfix = 2
    i = 0
    print("In ext_img")
    njpg = 0
    while(True):
        istream = pdf.find(b"stream", i)
        if(istream < 0):
            break
        istart = pdf.find(startmark, istream, istream+20)
        if(istart < 0):
            i = istream+20
            continue
        iend = pdf.find(b"endstream", istart)
        if(iend < 0):
            raise Exception("Didn't find end of stream!")
        iend = pdf.find(endmark, iend-20)
        if(iend < 0):
            raise Exception("Didn't find end of JPG!")
         
        istart += startfix
        iend += endfix
        print("JPG %d from %d to %d" % (njpg, istart, iend))
        jpg = pdf[istart:iend]
        jpgfile = open(scriptPath+"/ext_images/%d.jpg" % njpg, "wb")
        jpgfile.write(jpg)
        jpgfile.close()
         
        njpg += 1
        i = iend
    print(njpg)
    return njpg

# Extract jpg's from pdf's
if(args.file):
    if(args.b=='0'):
        pdf = open(scriptPath+"/pdf/single/"+args.file, "rb").read()
        print(scriptPath+"/pdf/single/"+args.file)
        n_jpg=ext_img(pdf)
        print(scriptPath+"/ocr-text/single/"+args.file[:-3]+"txt")
        try:
            f=open(scriptPath+"/ocr-text/single/"+args.file[:-3]+"txt", 'w', encoding='utf-8')
        except:
            print("Never Entered")

        print("OCR for"+ args.file)
        do_ocr(f,n_jpg)

    else:
        root=scriptPath+"/pdf/"+args.file
        for subdir,dirs,files in os.walk(root):
            for file in files:
                pdf = open(root+"/"+file, "rb").read()
                n_jpg=ext_img(pdf)
                if(not os.path.exists(scriptPath+"/ocr-text/"+args.file)):
                    os.mkdir(scriptPath+"/ocr-text/"+args.file)

                f=open(scriptPath+"/ocr-text/"+args.file+"/"+file[:-3]+"txt", 'w', encoding='utf-8')
                print("OCR for "+ args.file+"/"+file)
                do_ocr(f,n_jpg)
