import os
import argparse
parser = argparse.ArgumentParser()
parser.add_argument("--file", help="file from which data is to be extracted")
parser.add_argument("--rules", help="rules for extraction")
parser.add_argument("--b", help="0 for single file, 1 for batch")
#parser.add_argument("--i", help="0 for non scanned, 1 for scanned", default=0)

args = parser.parse_args()
scriptPath = os.path.dirname(os.path.abspath(__file__))
if(args.file):
	if(args.rules):
		if(args.b=='0'):
			cmd="python3 " + scriptPath+ "/pdfmine-linebyline.py --file single/"+args.file+" --rules "+args.rules+" --storein extracted/single"
			print(cmd)
			try:
				os.system(cmd)
			except Exception as e:
				print("Caught")
				raise e
			

		else:
			root=scriptPath+"/pdf/"+args.file
			if(not os.path.exists(scriptPath+"/extracted/"+args.file)):
				os.mkdir(scriptPath+"/extracted/"+args.file)
			for subdir,dirs,files in os.walk(root):
				for file in files:
					cmd="python3 "+ scriptPath+"/pdfmine-linebyline.py --file "+args.file+"/"+file+" --rules "+args.rules+" --storein extracted/"+args.file
					print(cmd)
					os.system(cmd)

		print("done")

	else:
		print("Rules not entered")
		exit()

else:
	print("File not entered")
	exit()