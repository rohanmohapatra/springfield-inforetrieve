#import urllib.request
#f = urllib.request.urlopen("http://localhost:8080/SpringField/GetProgress?yes+done")
#print (f.read())
#import requests
#r = requests.get('http://localhost:8080/SpringField/GetProgress?yes+done')
#print(r.status_code)
# Result:
#200


import httplib2

resp, content = httplib2.Http().request("http://localhost:8080/SpringField/GetProgress?Test4/Sample_3_IIT.pdf+done")
print(resp)
