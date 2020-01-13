<div class="wrapper">
<div class="header">
    <h1>InfoRetrieve<a class="baselink" href="#"></a></h1>
    Making over a billion documents usable! Serving billions of texts per month.</div>
</div>
<center>
<script>
window.onbeforeunload = function() {
	window.location = "http://localhost:8080/SpringField/upload.jsp";
	document.getElementById("div1").style.display = "none";
	console.log("Refreshed");
	
}
</script>
<div id = "div1" style="font-family: 'Montserrat',sans-serif;
    font-size: 10px;
    padding: 10px;display:block">
${requestScope.message}
</div>
</center>
<script type="text/javascript">
function showIt() {
	  document.getElementById("div1").style.display = "none";
	}
	setTimeout("showIt()", 2000); // after 1 sec

<!--

//-->
</script>
<div style="float:right;font-family: 'Montserrat',sans-serif;
    font-size: 18px; 
    border: 2px black;
    padding: 10px;"> 
<%
    if ((session.getAttribute("UserName") == null) || (session.getAttribute("UserName") == "")) {
%>
You are not logged in<br/>
<a href="login.jsp">Please Login</a>
<%} else {
%>
Welcome <%=session.getAttribute("UserName")%>
<a href='logout.jsp'>Log out</a><br>
<a href='DisplayResults' target="_blank" id = "MakeIt">View Your Processing...</a><br>
<a href='EditRules' target="_blank" id = "MakeIt">Edit Your Rules</a>
<%
    }
%></div>
<br><br>