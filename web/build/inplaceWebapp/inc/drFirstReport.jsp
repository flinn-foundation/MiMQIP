<%@ page import="flinn.util.DrFirstUtils"%><%
String serverName = request.getServerName();
String admin_protocol = "https";

String authcode = flinn.util.CookieHandler.getCookie("authcode", request);
String userID = null;
String drfUrl = "";
String logout = "https://"+serverName+"/inc/drFirstClose.jsp";

if (authcode == null || authcode.equals("")){
        return;
}
else {
        flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();
        flinn.beans.response.ResponseSessionContainerBean userSession = dm.getSession(authcode, request);

        if (userSession != null){
        	userID = userSession.getUser().getSettings().get("UserExternalID");
        }else{
            return;
        }
	drfUrl = DrFirstUtils.buildDrFirstReportURL(userID, logout);
	response.sendRedirect(drfUrl);
	
}
	
%>

<html>

<head>
</head>

<body>
<p>
Incorrect or missing userId.<br/>
Incorrect or missing userId.<br/>
Incorrect or missing userId.<br/>
Incorrect or missing userId.<br/>
Incorrect or missing userId.<br/>
Incorrect or missing userId.<br/>
</p>
</body>

</html>

