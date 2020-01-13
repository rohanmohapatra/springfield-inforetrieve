<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/upload_style.css" rel="stylesheet" type="text/css" />
<link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet"> 
<style>
#section2{
	display:none;
}
/*Content*/
#MakeIt
{
	display: block;
    width: 200px;
    height: 25px;
    background: #0A2746;
    padding: 10px;
    text-decoration: none;
    border-radius: 5px;
    color: white;
    
    font-family: 'Montserrat',sans-serif;
    font-size: 15px;
}
input[type='text']{
    font-family: 'Montserrat',sans-serif;
    font-size: 15px;
    border:none;
    border-bottom: 1px solid grey;
    padding-bottom: 5px;
}
input[type='password']{
    font-family: 'Montserrat',sans-serif;
    font-size: 15px;
    border:none;
    border-bottom: 1px solid grey;
    padding-bottom: 5px;
}
input[type='email']{
    font-family: 'Montserrat',sans-serif;
    font-size: 15px;
    border:none;
    border-bottom: 1px solid grey;
    /*padding-bottom: 5px;*/

}
input[type='text']:focus{
    outline:0 !important;
}
input[type='password']:focus{
    outline:0 !important;
}
input[type='email']:focus{
    outline:0 !important;
}
input.submit{
    font-family: 'Montserrat',sans-serif;
    font-size: 18px;
    width:50%; 
    height:10%; 
    text-align:center;  
    border: 0;
    background-color:#0A2746; 
    color:white; 
    border-radius:2px;
    box-shadow: 0 2px 4px 0 rgba(0,0,0,0.2),0 3px 15px 0 rgba(0,0,0,0.19);
}
input.submit:hover{
    box-shadow: 0 4px 4px 0 rgba(0,0,0,0.2),0 9px 15px 0 rgba(0,0,0,0.19);
}
.value{
        border:0;
        font-family: 'Montserrat',sans-serif;
        font-size: 12px;
        text-align: center;
        font-weight: 600;
        background: white; 
        color: gray;
        padding-top:15px;
        padding-right: 40px;
        padding-left: 40px;
        cursor: pointer;
        padding-bottom: 20px;
}

.value:focus{
    outline:0 !important;

}

@keyframes animationDiv{
    from {opacity: 0;}
    to {opacity: 1;}
    
}
.single{
    display: block;
    animation-name: animationDiv;
    animation-duration: 0.2s;
    
}
.multiple{
    display: none;
    animation-name: animationDiv;
    animation-duration: 0.2s;
    
}
input[type='email']{
    font-family: 'Montserrat',sans-serif;
    font-size: 15px;
}
.loginup{
        height: 450px;
        margin-top: -50px;
        margin-bottom: 50px;
}
.choice
{
	width: 30%;
	height: 90%;
	margin-top: 10%;
	margin-bottom: 10%;
	background-color: white;
	color: grey;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19);
}
body{
        margin: 0;
        background-image: url(img/114291-text-editor.png);
        background-image: linear-gradient(to bottom, rgba(255,255,255,0.95) 0%,rgba(255,255,255,1) 90%),url(imgs/loginbgr.png);
        background-attachment: fixed;       
}
ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #333;
}

li {
  float: left;
}

li a {
  display: block;
  color: white;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
}

li a:hover:not(.active) {
  background-color: #111;
}

.active {
  background-color: #4CAF50;
}
/*Content Ends here*/

</style>
<title>File Upload</title>
</head>
<body>
<%@ include file = "header.jsp" %>
<center>
        <div class="loginup">
            <div class="choice">
                <button class="value" style="padding-bottom: 20px;border-bottom :2px solid grey;color : #0A2746;">Single Document</button>
                <button class="value" ">Multiple Documents</button>
                <div class="single">
                    <br/><br/>
                    <form method="post" action="UploadSingle" enctype="multipart/form-data"> 
                    <input type="checkbox" name="isImage" value="Image"> PDF is a Scanned Document with Images<br><br>
            		Please Select a Input File:<input type="file" name="uploadFile" id="inputfile"/><br><br><br>
            		
            		
            		<div id="RuleDiv">
            		Please Select a Rule File: <input type="file" name="uploadFile2" id="inputfile"/>
            		</div>
            		<br/><br/>
            		<input type="submit" value="Upload" class="submit"/>
        			</form>
                </div>
                <script>
                var rulecb = document.getElementById("Rulecb");
                var rulediv = document.getElementById("RuleDiv");
                function check(){
                	rulecb.checked = true;
                	rulediv.style.display = "none";
                }
                if (!rulecb.checked)
                	{
                	rulediv.style.display = "block";
                		
                	}
                </script>
                <div class="multiple">

                    <br/><br/><br/>
                    <form method="post" action="UploadMultiple" enctype="multipart/form-data" id = "multipleForm">
                    <input type="checkbox" name="isImage" value="Image"> PDF is a Scanned Document with Images<br><br>
                    <input type="file" id="filepicker" name="fileList" webkitdirectory>
                    <!-- <ul id="listing"></ul> --><br>
                    <br>
                    Please Select a Rule File: <input type="file" name="uploadFile2" id="inputfile"/>
                    <br>
                    <br>
                    
                    <input type="submit" value="Upload" class="submit">
                    </form>
                    <script>
                    document.getElementById("filepicker").addEventListener("change", function(event) {
                    	  //let output = document.getElementById("listing");
                    	  let mult = document.getElementById("multipleForm");
                    	  let files = event.target.files;
                    	  /*
                    	  var relativePath = files[0].webkitRelativePath;
  						  var folder = relativePath.split("/")[0];
  						  var folderText = document.createElement("input");
  						  folderText.setAttribute('type','text');
  						  folderText.setAttribute('value',folder);
  						  folderText.setAttribute('disabled','true');
  						folderText.setAttribute('name','folderText');
  						  mult.insertBefore(folderText,mult.childNodes[2]);*/
  						  mult.insertBefore(document.createElement("br"),mult.childNodes[2]);
  						mult.insertBefore(document.createElement("br"),mult.childNodes[2]);
  						  mult.insertBefore(document.createElement("br"),mult.childNodes[4]);
  						 mult.insertBefore(document.createElement("br"),mult.childNodes[4]);
                    	  for (let i=0; i<files.length; i++) {
                    	    //let item = document.createElement("li");
                    	    //item.innerHTML = URL.createObjectURL(files[i]);
                    	    
                    	    var hidden = document.createElement("input");
                    	    hidden.setAttribute('type','hidden');
                    	    hidden.setAttribute( 'value',URL.createObjectURL(files[i]));
                    	    //output.appendChild(item);
                    	    mult.appendChild(hi);
                    	  };
                    	}, false);
                    </script>

                    <br/><br/><br/>
                        
                </div>
            </div>
        </div>
            <script>
                var button = document.querySelectorAll(".value");
                var signupdiv=document.querySelector(".single");
                var logindiv=document.querySelector(".multiple");
                for(var i=0;i<button.length;i++)
                    {
                        if(i){
                            button[i].addEventListener("click",changeDiv1,false);
                        }
                        else{
                            button[i].addEventListener("click",changeDiv2,false);
                        }
                    }
                function changeDiv2(element){
                    signupdiv.style.display="block";
                    logindiv.style.display="none";
                    element.target.style.borderBottom="2px solid grey";
                    element.target.style.color="#0A2746";
                    button[1].style.borderBottom="0";
                    button[1].style.color="grey";
                }
                function changeDiv1(element){
                    logindiv.style.display="block";
                    signupdiv.style.display="none";
                    element.target.style.borderBottom="2px solid grey";
                    element.target.style.color="#0A2746";
                    button[0].style.borderBottom="0";
                    button[0].style.color="grey";
                    
                }

            </script>
		</center>
<!-- 
    <center>
    <div id = "section1">
    <h1>Upload Document Here</h1>
    <hr width="100%" size="5px" color ="black">
    <br><br>
        <form method="post" action="UploadServlet" enctype="multipart/form-data"> 
            Select file to upload: <input type="file" name="uploadFile" id="inputfile" ${requestScope.dis} multiple />
            <br/><br/>
            <input type="submit" value="Upload" id="inputsubmit"/>
        </form>
       </div>
       <h3>${requestScope.message}</h3>
       <br><br>
       <div id= " section2" style="display:none${requestScope.text};">
       <h1>Define Rules Here</h1>
        <hr width="100%" size="5px" color ="black">
        <h4>
        Please Upload Rules to Extract Data<br>
Choose which headers you want to extract !<br>
        Example : 
    	Header Fields to Add - booking number,amount,contact person etc.</h4>
        <img src="invoice.png" width="20%">
    	<br><br>
    	<form method="post" action="UploadRules" enctype="multipart/form-data">
    		
    		<input id="hello" type="hidden" name="fname" value="${requestScope.fileName}" /> 
            Select file to upload: <input type="file" name="uploadRule" />
            <br/><br/>
            
            <input type="submit" value="Upload" />
        </form>
       </div>
    </center> -->

</body>

</html>