<%@page import="java.util.List"%>
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="3" width="30%" bgcolor="#CAD8DF"><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
<%
Boolean invalid = false;
String jspSelf = request.getRequestURI().toString();

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
		<td width="190" nowrap bgcolor="#CAD8DF"><a href="/admin/treatment_detail.jsp?id=0&edit=y" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Add Treatment</a></td>

		<td width="1" nowrap bgcolor="#FFFFFF"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
		<td width="13" nowrap bgcolor="#CAD8DF"><img src="/s.gif" width="13" height="1" alt="" border="0"></td>
<% if (!invalid) { %>
		<td width="190" nowrap bgcolor="#CAD8DF"><a href="/admin/treatment.jsp?invalid=y<% 		if (request.getParameter("order") != null){
			out.print("&order="+request.getParameter("order")); }
		%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Show All Treatments</a></td>
						
<% } else { %>
		<td width="190" nowrap bgcolor="#CAD8DF"><a href="/admin/treatment.jsp?<% 		if (request.getParameter("order") != null){
			out.print("order="+request.getParameter("order")); }
		%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Show Active Treatments</a></td>
	
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
Treatment Name
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
Treatment Abbreviation
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
Treatment Group
<%
if (qsOrder2 != 1){
	  out.print("</a>");
}
%></p></td>
		<td>&nbsp;</td>
		<td nowrap><p class="tableHeader"></p></td>
		<td>&nbsp;</td>
		<td nowrap><p class="tableHeader">&nbsp;</p></td>
		<td colspan="2">&nbsp;&nbsp;</td>
	</tr>
	<tr bgcolor="#CAD8DF">
		<td colspan="13"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
	</tr>
	
<%
boolean first = true;
String orderby = "TreatmentName";
String criteria = "false";
String authcode = flinn.util.CookieHandler.getCookie("authcode", request);

if (request.getParameter("order") != null) {
	if (request.getParameter("order").equals("1")) orderby = "T.TreatmentAbbr ASC";
	if (request.getParameter("order").equals("2")) orderby = "TG.TreatmentGroupName ASC";
}

List<flinn.beans.response.ResponseTreatmentBean> dataList = null;
flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();
ResponseSessionContainerBean userSession = dm.getSession(authcode, request);
int lastactivity = -1;
Boolean isAdmin = false, isSuperAdmin = false;

if (flinn.util.AdminRole.isAdmin(userSession))
{
	  isSuperAdmin = true;
	  if(flinn.util.AdminRole.isFacilityAdmin(userSession))isAdmin = true;
}

try{
	dataList = dm.findAllTreatments(null, userSession, !invalid, isSuperAdmin, orderby); //function call to return user
		try{
			//function call to update user's last activity
			lastactivity = dm.updateLastActivity(userSession);
			dm.commitConnection("updateLastActivity");
		}
		catch(Exception e) {
			dm.rollbackConnection("updateLastActivity");
			dm.LOG.debug("Unable to commit changes to updateLastActivity");
		}
	}
	catch(Exception e) {
		dm.LOG.debug("Unable to open connection for findAllTreatments");
	}
	finally{dm.disposeConnection("findAllTreatments");}

if (dataList != null){
for (int i=0; i<dataList.size(); i++) {
  if (!first) {
    %>
	<tr>
		<td bgcolor="#CAD8DF"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
		<td colspan="10" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
	</tr>
    <%
  }
  first = false;
  %>
	<tr>
		<td bgcolor="#CAD8DF"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
		<td>&nbsp;</td>
		<td><p class="tableText<% if (data_is_not_valid(dataList.get(i)) > 0) out.print("Inactive"); %>"><%= dataList.get(i).getTreatmentname()%></p></td>
		<td>&nbsp;</td>
		<td><p class="tableText<% if (data_is_not_valid(dataList.get(i)) > 0) out.print("Inactive"); %>"><%= dataList.get(i).getTreatmentabbreviation()%></p></td>
		<td>&nbsp;</td>
		<td><p class="tableText<% if (data_is_not_valid(dataList.get(i)) > 0) out.print("Inactive"); %>"><%= dataList.get(i).getGroup().getTreatmentgroupabbreviation()%></p></td>
		<td>&nbsp;</td>
		<td><a href="/admin/treatment_detail.jsp?id=<% out.print(dataList.get(i).getTreatmentid());%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;detail</a></td>
	</tr>

  <%
}
}
%>
	<tr bgcolor="#CAD8DF">
		<td colspan="13"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
	</tr>
	</table>
	
<%!
public int data_is_not_valid(flinn.beans.response.ResponseTreatmentBean bean) throws Exception {
  if (!bean.getValid()) {
	  	return 1;
	  }
  return 0;
}%>
