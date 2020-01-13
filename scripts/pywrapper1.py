import os
import argparse
from pathlib import Path
parser = argparse.ArgumentParser()
parser.add_argument("--file", help="file from which data is to be extracted")
parser.add_argument("--rules", help="rules for extraction")
parser.add_argument("--b", help="0 for single file, 1 for batch")
#parser.add_argument("--i", help="0 for non scanned, 1 for scanned", default=0)

args = parser.parse_args()
scriptPath = os.path.dirname(os.path.abspath(__file__))
filePath = os.path.join(Path(__file__).parent.parent,"upload")
if(args.file):
	if((args.b=='0' or args.b=='1')):
		if(args.b=='0'):
			if(args.file[-3:]=='pdf'):
				cmd="python3 "+scriptPath+"/pdfmine-linebyline.py --file single/"+args.file+" --rules "+args.rules+" --storein extracted/single"
			elif(args.file[-4:]=='docx'):
				cmd="python3 wordmine.py --file single/"+args.file+" --rules "+args.rules+" --storein extracted/single"
			print(cmd)
			os.system(cmd)

		elif args.b=='1':
			root=filePath+"/pdf/"+args.file
			if(not os.path.exists(filePath+"/extracted/"+args.file)):
				os.mkdir(filePath+"/extracted/"+args.file)
			for subdir,dirs,files in os.walk(root):
				for file in files:
					cmd="python3 "+scriptPath+"/pdfmine-linebyline.py --file "+args.file+"/"+file+" --rules "+args.rules+" --storein extracted/"+args.file
					print(cmd)
					os.system(cmd)

	else:
		if args.b=='2':
			cmd="python3 "+scriptPath+"/scanned.py --file "+args.file+" --b 0"
			print(cmd)
			os.system(cmd)
			print("done")

		elif args.b=='3':
			cmd="python3 "+scriptPath+"/scanned.py --file "+args.file+" --b 1"
			print(cmd)
			os.system(cmd)
			print("done")

		else:
			print("Rules not entered")
			exit()

else:
	print("File not entered")
	exit()