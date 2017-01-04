<%@page import="flinn.util.AdminRole"%>
<%
String admin_section = "Manage Criteria";
String admin_subsection = "Criteria";
String sub_section = "";
String userlogin = "u";
String role = "r";
int roleid = -1;

String authcode = flinn.util.CookieHandler.getCookie("authcode", request);
if (authcode == null || authcode.equals("")){ 
	response.sendRedirect("/sample");
	return;	
}
else {
	flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();
	flinn.beans.response.ResponseSessionContainerBean userSession = dm.getSession(authcode, request);

	if (userSession != null){	
		userlogin = userSession.getUser().getLogin();
	
		flinn.beans.AppUserRoleBean[] ar = userSession.getUser().getRoles();
		flinn.beans.AppUserRoleBean userRole = (flinn.beans.AppUserRoleBean)ar[0];
		role = userRole.getApprole();
		roleid = userRole.getApproleid();
		
		if (roleid < AdminRole.ADMIN_ROLE || roleid > AdminRole.FACILITY_ADMIN_ROLE){
			response.sendRedirect("/sample");
			return;	
		}
	}
	else{
		response.sendRedirect("/sample");
		return;	
	}
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>Flinn Foundation Recommend Administration Tool</title>
        <link rel="stylesheet" href="admin.css" type="text/css">
        <link rel="icon" href="/favicon.ico">
        <link rel="shortcut icon" href="/favicon.ico">
        <script type="text/javascript" language="JavaScript" src="js/admin_util.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.0/jquery.min.js"></script>
        <script type="text/javascript" src="/js/jquery.cookie.min.js"></script>
        <script type="text/javascript" src="/js/globalUtils.js"></script>
    </head>

    <body leftmargin=0 topmargin=0 bgcolor="#ffffff">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="670" align="left"><img src="images/admin_header.gif" width=670 height=96 alt="" border="0"></td>
            </tr>
        </table>


        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr valign="top" bgColor="#7F7F7F">
                <td><p class="pageTitle"><span class="pageHeader">Criteria</span></p></td>
            </tr>
	</table>

        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr valign="top">
	<td valign="top">
<div class="pageHolder">
<jsp:include page="include/criteria_detail.jsp" />
</div>
	</td>
</tr>
</table>
</body>
</html>
