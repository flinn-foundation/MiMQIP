<%@page import="flinn.util.AdminRole"%>
<%
String admin_section = "Manage Messages";
String admin_subsection = "Messages";
String sub_section = "";
String userlogin = null;
String role = null;
int roleid = -1;

String authcode = flinn.util.CookieHandler.getCookie("authcode", request);
if (authcode == null || authcode.equals("")){ 
	response.sendRedirect("/sample");
	return;	
}
else {
	flinn.recommend.dao.DaoRecommendManager dm = new flinn.recommend.dao.DaoRecommendManager();
	flinn.beans.response.ResponseSessionContainerBean userSession = dm.getSession(authcode, request);

	if (userSession != null){	
		userlogin = userSession.getUser().getLogin();
	
		flinn.beans.AppUserRoleBean[] ar = userSession.getUser().getRoles();
		flinn.beans.AppUserRoleBean userRole = (flinn.beans.AppUserRoleBean)ar[0];
		role = userRole.getApprole();
		roleid = userRole.getApproleid();
		
		if (roleid < AdminRole.ADMIN_ROLE){
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

<jsp:include page="/recommend/include/header.jsp">
	<jsp:param name="admin_section" value="<%=admin_section%>" />
	<jsp:param name="admin_subsection" value="<%=admin_subsection%>" />
	<jsp:param name="sub_section" value="<%=sub_section%>" />
</jsp:include>
	<td bgcolor="#E6E5E3" width="200" colspan="2" valign="top">
<jsp:include page="/recommend/include/nav.jsp">
	<jsp:param name="admin_section" value="<%=admin_section%>" />
	<jsp:param name="admin_subsection" value="<%=admin_subsection%>" />
	<jsp:param name="sub_section" value="<%=sub_section%>" />
	<jsp:param name="role" value="<%=role%>" />
</jsp:include>
	</td>
	<td valign="top">
<div class="pageHolder">
<jsp:include page="/recommend/include/message.jsp" />
</div>
	</td>
<jsp:include page="/recommend/include/footer.jsp">
	<jsp:param name="userlogin" value="<%=userlogin%>" />
	<jsp:param name="role" value="<%=role%>" />
</jsp:include>
