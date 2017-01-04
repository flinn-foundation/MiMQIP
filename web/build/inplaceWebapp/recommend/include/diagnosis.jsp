<%@page import="java.util.List"%>

<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<%
Boolean invalid = false;
String jspSelf = request.getRequestURL().toString();

if (request.getParameter("invalid") != null){
  	if(request.getParameter("invalid").equals("y")) 
  		invalid = true;
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
        </table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#CAD8DF">
		<td><img src="/recommend/images/s.gif" width="1" height="1" alt="" border="0"></td>
		<td>&nbsp;</td>
		<% out.print(WriteTableHeader("Diagnosis", jspSelf)); %>
		<% out.print(WriteTableHeader("Stage", jspSelf)); %>
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
String criteria = "false";
String authcode = flinn.util.CookieHandler.getCookie("authcode", request);

flinn.recommend.beans.RecommendDiagnosisBean[] diagnosisList = null;
flinn.recommend.dao.DaoRecommendManager dm = new flinn.recommend.dao.DaoRecommendManager();
ResponseSessionContainerBean userSession = dm.getSession(authcode, request);
int lastactivity = -1;
Boolean isSuperAdmin = false;
if (flinn.util.AdminRole.isAdmin(userSession))isSuperAdmin = true;

try{
	diagnosisList = dm.findAllDiagnoses(); //function call to get diagnosis

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
	dm.LOG.debug("Unable to open connection for findAllDiagnoses");
}
finally{dm.disposeConnection("findAllDiagnoses");}

if (diagnosisList != null){
for (int i=0; i<diagnosisList.length; i++) {
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
		<td><p class="tableText"><%= diagnosisList[i].getDiagnosis()%></p></td>
		<td>&nbsp;</td>
		<td><p class="tableText"><%= diagnosisList[i].getStage()%></p></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td><a href="/recommend/diagnosis_detail.jsp?id=<% out.print(diagnosisList[i].getDiagnosisid());%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;detail</a></td>
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
public String WriteTableHeader (String name, String page) {
    StringBuilder outString = new StringBuilder();
    outString.append("<td nowrap><p class='tableHeader'>");
    outString.append(name);
    outString.append("</p></td>");
    outString.append("<td>&nbsp;</td>");
    return outString.toString();
}

%>

