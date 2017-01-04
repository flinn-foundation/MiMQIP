<html>
<head>
</head>
<body>
<h2>Cookies</h2>
<%
javax.servlet.http.Cookie[] cookies = request.getCookies();
if (cookies != null) {
  for (int i=0; i < java.lang.reflect.Array.getLength(cookies); i++) { %><%=cookies[i].getName()%> = <%=cookies[i].getValue()%><br/><% }
}
%>
<h2>Headers</h2>
<%
java.util.Enumeration<String> enu = request.getHeaderNames();
if (enu != null) while (enu.hasMoreElements()) {
  String head = (String) enu.nextElement();
  %><%=head%> = <%=request.getHeader(head)%><br/><%
}
%>
<h2>Parameters</h2>
<%
enu = request.getParameterNames();
if (enu != null) while (enu.hasMoreElements()) {
  String head = (String) enu.nextElement();
  %><%=head%> = <%=request.getParameter(head)%><br/><%
}
%>
<h2>Attributes</h2>
<%
enu = request.getAttributeNames();
if (enu != null) while (enu.hasMoreElements()) {
  String head = (String) enu.nextElement();
  %><%=head%> = <%=request.getAttribute(head).toString()%><br/><%
}
%>
<h2>Other</h2>
Scheme = <%=request.getScheme()%><br/>
Protocol = <%=request.getProtocol()%><br/>
Remote Address = <%=request.getRemoteAddr()%><br/>
Remote Port = <%=request.getRemotePort()%><br/>
ServerName = <%=request.getServerName()%><br/>
Secure = <%=request.isSecure()%><br/>
Content Type = <%=request.getContentType()%><br/>
