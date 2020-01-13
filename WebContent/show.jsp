<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="css/upload_style.css" rel="stylesheet" type="text/css" />
<link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet"> 
<style>
#MakeIt
{
	display: block;
    width: 150px;
    height: 25px;
    background: #0A2746;
    padding: 10px;
    text-decoration: none;
    border-radius: 5px;
    color: white;
    
    font-family: 'Montserrat',sans-serif;
    font-size: 15px;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Extracted Fields</title>
<style>
table {
  border-collapse: collapse;
}
table, th, td {
    border: 1px solid black;
    font-family: 'Montserrat',sans-serif;
    font-size: 15px;
}
th, td {
  padding: 15px;
  text-align: left;
}
</style>
</head>
<body>
${requestScope.required}
<%@ include file = "header2.jsp" %>
    <center>
        <h2>${requestScope.message}</h2>
    </center>
    ${requestScope.text} 
    
</body>
</html>