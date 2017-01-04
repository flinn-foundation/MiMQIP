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
		<td width="220" nowrap bgcolor="#CAD8DF"><a href="/admin/treatment_group_detail.jsp?id=0&edit=y" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Add Treatment Group</a></td>

		<td width="1" nowrap bgcolor="#FFFFFF"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
		<td width="13" nowrap bgcolor="#CAD8DF"><img src="/s.gif" width="13" height="1" alt="" border="0"></td>
<% if (!invalid) { %>
		<td width="220" nowrap bgcolor="#CAD8DF"><a href="/admin/treatment_group.jsp?invalid=y<% 		if (request.getParameter("order") != null){
			out.print("&order="+request.getParameter("order")); }
		%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Show All Treatment Groups</a></td>
						
<% } else { %>
		<td width="220" nowrap bgcolor="#CAD8DF"><a href="/admin/treatment_group.jsp?<% 		if (request.getParameter("order") != null){
			out.print("order="+request.getParameter("order")); }
		%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Show Active Treatment Groups</a></td>
	
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
if (request.getParameter("order") != null) {
  if (request.getParameter("invalid") != null) {
    out.print("<a href='"+jspSelf+"?invalid=y&order=0' class='tableHeader'>");
  } else {
    out.print("<a href='"+jspSelf+"?order=0' class='tableHeader'>");
  }
}
%>
Treatment Group Name
<%
if(request.getParameter("order") != null) {
  out.print("</a>");
}
%></p></td>
		<td>&nbsp;</td>
		<td nowrap><p class="tableHeader">
<%
if (request.getParameter("order") != null) {
	if (!request.getParameter("order").equals("1")) {
	  if (request.getParameter("invalid") != null) {
	    out.print("<a href='"+jspSelf+"?invalid=y&order=1' class='tableHeader'>");
	  } else {
		  out.print("<a href='"+jspSelf+"?order=1' class='tableHeader'>");
	  }
	}
}
%>
Treatment Group Abbreviation
<%
if (request.getParameter("order") != null) {
	if (!request.getParameter("order").equals("1")) {
	  out.print("</a>");
	}
}
%></p></td>
		<td>&nbsp;</td>
		<td nowrap><p class="tableHeader">
<%
if (request.getParameter("order") != null) {
	if (!request.getParameter("order").equals("2")) {
	  if (request.getParameter("invalid") != null) {
	    out.print("<a href='"+jspSelf+"?invalid=y&order=2' class='tableHeader'>");
	  } else {
		out.print("<a href='"+jspSelf+"?order=2' class='tableHeader'>");
	  }
	}
}
%>

<%
//Empty
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
String orderby = "TreatmentGroupName";
String criteria = "false";
String authcode = flinn.util.CookieHandler.getCookie("authcode", request);

if (request.getParameter("order") != null) {
	if (request.getParameter("order").equals("1")) orderby = "TreatmentGroupAbbr DESC";
}
else {orderby = "TreatmentGroupName DESC";}

List<flinn.beans.TreatmentGroupBean> dataList = null;
flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();
ResponseSessionContainerBean userSession = dm.getSession(authcode, request);
int lastactivity = -1;
Boolean isAdmin = false, isSuperAdmin = false;
if (flinn.util.AdminRole.isAdmin(userSession))
{
	  isSuperAdmin = true;
	  if(flinn.util.AdminRole.isFacilityAdmin(userSession))isAdmin = true;
}
flinn.beans.request.RequestContainerBean input = new flinn.beans.request.RequestContainerBean();
flinn.beans.response.ResponseTreatmentGroupContainerBean adminTreatmentGroup;

try{
	adminTreatmentGroup = (flinn.beans.response.ResponseTreatmentGroupContainerBean)dm.findAllTreatmentGroups(input, userSession, !invalid, isSuperAdmin, orderby); //function call to return user
	dataList = java.util.Arrays.asList(adminTreatmentGroup.getGroups());
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
		dm.LOG.debug("Unable to open connection for findAllTreatmentGroups");
	}
	finally{dm.disposeConnection("findAllTreatmentGroups");}
	
if(dataList != null){
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
		<td><p class="tableText<% if (data_is_not_valid(dataList.get(i)) > 0) out.print("Inactive"); %>"><%= dataList.get(i).getTreatmentgroupname()%></p></td>
		<td>&nbsp;</td>
		<td><p class="tableText<% if (data_is_not_valid(dataList.get(i)) > 0) out.print("Inactive"); %>"><%= dataList.get(i).getTreatmentgroupabbreviation()%></p></td>
		<td>&nbsp;</td>
		<td></td>
		<td>&nbsp;</td>
		<td><a href="/admin/treatment_group_detail.jsp?id=<% out.print(dataList.get(i).getTreatmentgroupid());%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;detail</a></td>
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
public int data_is_not_valid(flinn.beans.TreatmentGroupBean bean) throws Exception {
  if (!bean.getValid()) {
	  	return 1;
	  }
  return 0;
}%>
