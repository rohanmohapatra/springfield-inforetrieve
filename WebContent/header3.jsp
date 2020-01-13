<div class="wrapper">
<div class="header">
    <h1>InfoRetrieve<a class="baselink" href="#"></a></h1>
    Making over a billion documents usable! Serving billions of texts per month.</div>
</div>
<center>
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
<%
    }
%></div>
<br><br>