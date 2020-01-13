<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet"> 
<link rel="stylesheet" type="text/css" href="css/login_style.css">
<link href="css/upload_style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<!--Webpage Starts-->
<html>
<head>
<meta charset="ISO-8859-1">
<title>SpringField | Login/SignUp</title><!--Title of the webpage-->
<style>
input.submit{
    font-family: 'Montserrat',sans-serif;
    font-size: 18px;
    width:50%; 
    height:10%; 
    text-align:center;  
    border: 0;
    background-color: #0A2746; 
    color:white; 
    border-radius:2px;
    box-shadow: 0 2px 4px 0 rgba(0,0,0,0.2),0 3px 15px 0 rgba(0,0,0,0.19);
}
body{
        margin: 0;
        background-image: url(img/114291-text-editor.png);
        background-image: linear-gradient(to bottom, rgba(255,255,255,0.95) 0%,rgba(255,255,255,1) 90%),url(imgs/loginbgr.png);
        background-attachment: fixed;       
}
</style>
</head>
<meta name="viewport" content="width=device-width"><!--Responsive Webpage-->
<body>		
<%@ include file = "header3.jsp" %>
<%
					session.setAttribute("UserName", null);
					session.invalidate();
%>
		<!--Content of the page Starts here-->
		<center>
        <div class="loginup">
            <div class="choice">
                <button class="value">SIGN UP</button>
                <button class="value" style="padding-bottom: 20px;border-bottom :2px solid grey;color :#0A2746;">LOGIN</button>
                <div class="signup">
                    <br/><br/>
                    
                    <form action="SignUp" method="post" id="signup_form">
                    <input type="text" placeholder="Name" style=" width:70%; height:5%;" name="name" />

                    <br/><br/><br/>
                    <input type="email" placeholder="Email Id" style=" width:70%; height:5%;" name="email" />

                    <br/><br/><br/>
                    <input type="text" placeholder="Username" style=" width:70%; height:5%;" name="user" />

                    <br/><br/><br/>
                    <input type="password" placeholder="Password" style=" width:70%; height:5%;" name="pass" />

                    <br/><br/><br/>
                    <input type="submit" value="SIGN UP" class="submit"></form>
                </div>
                <div class="login">

                    <br/><br/><br/>
                    <form action="Login" method="post" id="login_form">
                    <input type="text" required placeholder="Username" style=" width:70%; height:5%;"oninvalid="this.setCustomValidity('Please Enter valid Username')"
 oninput="setCustomValidity('')" name="loginuser" />

                    <br/><br/><br/>
                    <input type="password" placeholder="Password" style=" width:70%; height:5%;" required oninvalid="this.setCustomValidity('Please Enter valid Password')"
 oninput="setCustomValidity('')" name="loginpass"/>

                    <br/><br/><br/>
                        <input type="submit" value="LOGIN" class="submit"></form>
                </div>
            </div>
        </div>
            <script>
                var button = document.querySelectorAll(".value");
                var signupdiv=document.querySelector(".signup");
                var logindiv=document.querySelector(".login");
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
            
		<!--Content of the page Ends here-->
	</body>
	<!--Body Ends-->
</html>
<!--Webpage Ends-->