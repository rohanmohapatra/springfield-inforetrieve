<%
session.setAttribute("UserName", null);
session.invalidate();
response.sendRedirect("login.jsp");
%>