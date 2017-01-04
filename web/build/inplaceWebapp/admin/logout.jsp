<%
String serverName = request.getServerName();
String admin_protocol = "http";

response.sendRedirect(admin_protocol+"://"+serverName+"/patient-search.jsp");  

%>
