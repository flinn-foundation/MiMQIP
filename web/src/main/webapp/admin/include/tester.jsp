<%@page import="java.util.List"%>
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="3" width="30%" bgcolor="#CAD8DF"><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
<%
Boolean invalid = false;
String jspSelf = request.getRequestURL().toString();

if (request.getParameter("invalid") != null){
  	if(request.getParameter("invalid").equals("y")) invalid = true;
}

if (!invalid) { %>
		<td width="1" bgcolor="#FFFFFF"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
		<td colspan="2" width="30%" bgcolor="#CAD8DF"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
<% } else { %>
		<td colspan="3" width="30%"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
<% } %>
		<td colspan="2" width="40%"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
	</tr>
	<tr>
		<td width="1" nowrap bgcolor="#CAD8DF"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
		<td width="13" nowrap bgcolor="#CAD8DF"><img src="/s.gif" width="13" height="1" alt="" border="0"></td>
		<td width="168" nowrap bgcolor="#CAD8DF"><a href="/admin/user_detail.jsp?id=0&edit=y" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Add User</a></td>

		<td width="1" nowrap bgcolor="#FFFFFF"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
		<td width="13" nowrap bgcolor="#CAD8DF"><img src="/s.gif" width="13" height="1" alt="" border="0"></td>
<% if (!invalid) { %>		
		<td width="168" nowrap bgcolor="#CAD8DF"><a href="/admin/user.jsp?invalid=y<% 		if (request.getParameter("order") != null){
			out.print("&order="+request.getParameter("order")); }
		%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Show All Users</a></td>
						
<% } else { %>
		<td width="168" nowrap bgcolor="#CAD8DF"><a href="/admin/user.jsp?<% 		if (request.getParameter("order") != null){
			out.print("order="+request.getParameter("order")); }
		%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Show Active Users</a></td>
<% } %>
		<td colspan="2"></td>
	</tr>
        </table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#CAD8DF">
		<td><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
		<td>&nbsp;</td>
		<td nowrap><p class="tableHeader">
<%
int qsOrder = -1, qsOrder1 = -1, qsOrder2 = -1;

if (request.getParameter("order") != null) {
	if (request.getParameter("order").equals("0")) {qsOrder = 1;}
	if (request.getParameter("order").equals("1")) {qsOrder1 = 1;}
	if (request.getParameter("order").equals("2")) {qsOrder2 = 1;}
}
if (qsOrder != 1){
  if (request.getParameter("invalid") != null) {
    out.print("<a href='"+jspSelf+"?invalid=y&order=0' class='tableHeader'>");
  } else {
    out.print("<a href='"+jspSelf+"?order=0' class='tableHeader'>");
  }
}
%>
User Login
<%
if (qsOrder != 1){
  out.print("</a>");
}
%></p></td>
		<td>&nbsp;</td>
		<td nowrap><p class="tableHeader">
<%
if (qsOrder1 != 1){
	  if (request.getParameter("invalid") != null) {
	    out.print("<a href='"+jspSelf+"?invalid=y&order=1' class='tableHeader'>");
	  } else {
		  out.print("<a href='"+jspSelf+"?order=1' class='tableHeader'>");
	  }
	}
%>
Last Login
<%
if (qsOrder1 != 1){
	  out.print("</a>");
	}

%></p></td>
		<td>&nbsp;</td>
		<td nowrap><p class="tableHeader">
<%
if (qsOrder2 != 1){
	  if (request.getParameter("invalid") != null) {
	    out.print("<a href='"+jspSelf+"?invalid=y&order=2' class='tableHeader'>");
	  } else {
		out.print("<a href='"+jspSelf+"?order=2' class='tableHeader'>");
	  }
	}
%>
Last Action
<%
if (qsOrder2 != 1){
	  out.print("</a>");
}

%></p></td>
		<td>&nbsp;</td>
		<td nowrap><p class="tableHeader">Order</p></td>
		<td>&nbsp;</td>
		<td nowrap><p class="tableHeader">&nbsp;</p></td>
		<td colspan="2">&nbsp;&nbsp;</td>
	</tr>
	<tr bgcolor="#CAD8DF">
		<td colspan="13"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
	</tr>
	
<%
boolean first = true;
String orderby = "Login";
String criteria = "false";
String authcode = flinn.util.CookieHandler.getCookie("authcode", request);

if (request.getParameter("order") != null) {
	if (request.getParameter("order").equals("1")) orderby = "LastLoggedIn DESC";
	if (request.getParameter("order").equals("2")) orderby = "LastActivity DESC";
}

flinn.beans.response.ResponsePrescriptionSearchContainerBean userList = null;
flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();

ResponseSessionContainerBean userSession = dm.getSession(authcode, request);

Boolean isAdmin = false, isSuperAdmin = false;
if (flinn.util.AdminRole.isAdmin(userSession))
{
	  isSuperAdmin = true;
	  if(flinn.util.AdminRole.isFacilityAdmin(userSession))isAdmin = true;
}

int lastactivity = -1;
lastactivity = dm.updateLastActivity(userSession);
flinn.beans.request.RequestContainerBean rqcont = new flinn.beans.request.RequestContainerBean();
flinn.beans.request.RequestPatientBean rpbean = new flinn.beans.request.RequestPatientBean();
flinn.beans.request.RequestPrescriptionSearchBean psbean = new flinn.beans.request.RequestPrescriptionSearchBean();
rpbean.setPatientid(1);
psbean.setPatientid(1);
rqcont.setPatient(rpbean);
rqcont.setPrescriptionsearch(psbean);
userList = (flinn.beans.response.ResponsePrescriptionSearchContainerBean)dm.searchPrescriptions(rqcont, userSession); //function call to return user
%>



	<tr bgcolor="#CAD8DF">
		<td colspan="13"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
	</tr>
	</table>
	

