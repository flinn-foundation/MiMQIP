<%@page import="java.util.List"%>

<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<%
Boolean invalid = false;
String jspSelf = request.getRequestURL().toString();
Boolean qsOrder = false, qsOrder1 = false, qsOrder2 = false;

if (request.getParameter("invalid") != null){
  	if(request.getParameter("invalid").equals("y")) 
  		invalid = true;
}

if (request.getParameter("order") != null) 
{
    if (request.getParameter("order").equals("0"))
    	qsOrder = true;

    if (request.getParameter("order").equals("1"))
        qsOrder1 = true;

    if (request.getParameter("order").equals("2"))
	qsOrder2 = true;
} else {
    	qsOrder = true;
}

%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="3" width="30%" bgcolor="#CAD8DF"><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
		<%
		if (!invalid) { %>
				<td width="1" bgcolor="#FFFFFF"><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
				<td colspan="2" width="30%" bgcolor="#CAD8DF"><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
		<% } else { %>
				<td colspan="3" width="30%"><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
		<% } %>
				<td colspan="2" width="40%"><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
	</tr>
	<tr>
	<td width="1" nowrap bgcolor="#CAD8DF"><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
	<td width="13" nowrap bgcolor="#CAD8DF"><img src="/recommend/images/s.gif" width="13" height="1" alt="" border="0"></td>
	<td width="168" nowrap bgcolor="#CAD8DF"><a href="/recommend/rule_detail.jsp?id=0&edit=y" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Add Rule</a></td>

	<td width="1" nowrap bgcolor="#FFFFFF"><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
	<td width="13" nowrap bgcolor="#CAD8DF"><img src="/recommend/images/s.gif" width="13" height="1" alt="" border="0"></td>
<% if (!invalid) { %>
	<td width="168" nowrap bgcolor="#CAD8DF"><a href="/recommend/rule.jsp?invalid=y<% 		if (request.getParameter("order") != null){
		out.print("&order="+request.getParameter("order")); }
	%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Show All Rules</a></td>
<% } else { %>
	<td width="168" nowrap bgcolor="#CAD8DF"><a href="/recommend/rule.jsp?<% 		if (request.getParameter("order") != null){
		out.print("order="+request.getParameter("order")); }
	%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Show Active Rules</a></td>
<% } %>
	<td colspan="2"></td>
	</tr>
        </table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#CAD8DF">
		<td><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
		<td>&nbsp;</td>
		<% out.print(WriteTableHeader("Rule Type", qsOrder, jspSelf, "0")); %>
		<% out.print(WriteTableHeader("Rule Name", qsOrder1, jspSelf, "1")); %>
		<% out.print(WriteTableHeader("Priority", qsOrder2, jspSelf, "2")); %>
		<td nowrap><p class="tableHeader">&nbsp;</p></td>
		<td>&nbsp;</td>
		<td nowrap><p class="tableHeader">&nbsp;</p></td>
		<td colspan="2">&nbsp;&nbsp;</td>
	</tr>
	<tr bgcolor="#CAD8DF">
		<td colspan="13"><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
	</tr>
	
<%
boolean first = true;
String orderby = "RuleType, Priority ASC";
String criteria = "false";
String authcode = flinn.util.CookieHandler.getCookie("authcode", request);

if (qsOrder1) 
    orderby = "RuleName DESC";
if (qsOrder2) 
    orderby = "Priority ASC";


List<flinn.recommend.beans.response.ResponseRuleBean> ruleList = null;
flinn.recommend.dao.DaoRecommendManager dm = new flinn.recommend.dao.DaoRecommendManager();
flinn.beans.request.RequestContainerBean input = new flinn.beans.request.RequestContainerBean();
ResponseSessionContainerBean userSession = dm.getSession(authcode, request);
int lastactivity = -1;
Boolean isSuperAdmin = false;
if (flinn.util.AdminRole.isAdmin(userSession))isSuperAdmin = true;

try{
	ruleList = dm.findAllRules(input, userSession, orderby); //function call to get rules

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
	dm.LOG.debug("Unable to open connection for findAllRules");
}
finally{dm.disposeConnection("findAllRules");}

if (ruleList != null){
for (int i=0; i<ruleList.size(); i++) {
  if (!first) {
    %>
	<tr>
		<td bgcolor="#CAD8DF"><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
		<td colspan="10" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
	</tr>
    <%
  }
  first = false;
  %>
	<tr>
		<td bgcolor="#CAD8DF"><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
		<td>&nbsp;</td>
		<td><p class="tableText"><%= ruleList.get(i).getRuletype()%></p></td>
		<td>&nbsp;</td>
		<td><p class="tableText"><%= ruleList.get(i).getRulename()%></p></td>
		<td>&nbsp;</td>
		<td><p class="tableText"><%= ruleList.get(i).getPriority()%></p></td>
		<td>&nbsp;</td>
		<td><a href="/recommend/rule_detail.jsp?id=<% out.print(ruleList.get(i).getRuleid());%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;detail</a></td>
	</tr>

  <%
}
}
%>
	<tr bgcolor="#CAD8DF">
		<td colspan="13"><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
	</tr>
	</table>
	
<%!
public String WriteTableHeader (String name, Boolean selected, String page, String order) {
    StringBuilder outString = new StringBuilder();
    outString.append("<td nowrap><p class='tableHeader'>");
    if (!selected){
        outString.append("<a href='"+page+"?order="+order+"' class='tableHeader'>");
    }
    outString.append(name);
    if (!selected){
        outString.append("</a>");
    }
    outString.append("</p></td>");
    outString.append("<td>&nbsp;</td>");
    return outString.toString();
}

%>

