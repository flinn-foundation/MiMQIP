<%@page import="flinn.util.AdminRole"%>
<%@page import="flinn.util.export.RecommendExport"%>
<%
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

// set header to initiate file download using the filename export.txt
response.setHeader("Content-disposition", "attachment; filename=export.txt" );
RecommendExport export = new RecommendExport();
out.println(export.generateExport());

%>
