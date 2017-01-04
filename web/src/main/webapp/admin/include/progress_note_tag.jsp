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
		<td width="225" nowrap bgcolor="#CAD8DF"><a href="/admin/progress_note_tag_detail.jsp?id=0&edit=y" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Add Progress Note Tag</a></td>

		<td width="1" nowrap bgcolor="#FFFFFF"><img src="/s.gif" width="1" height="1" alt="" border="0"></td>
		<td width="13" nowrap bgcolor="#CAD8DF"><img src="/s.gif" width="13" height="1" alt="" border="0"></td>
<% if (!invalid) { %>
		<td width="225" nowrap bgcolor="#CAD8DF"><a href="/admin/progress_note_tag.jsp?invalid=y<% 		if (request.getParameter("order") != null){
			out.print("&order="+request.getParameter("order")); }
		%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Show All Progress Note Tags</a></td>
						
<% } else { %>
		<td width="225" nowrap bgcolor="#CAD8DF"><a href="/admin/progress_note_tag.jsp?<% 		if (request.getParameter("order") != null){
			out.print("order="+request.getParameter("order")); }
		%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Show Active Progress Note Tags</a></td>
	
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
Tag Name
<%
if(request.getParameter("order") != null) {
  out.print("</a>");
}
%></p></td>
		<td>&nbsp;</td>
		<td nowrap colspan="4"><p class="tableHeader">
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

boolean first = true;
String orderby = "TagName";
String criteria = "false";
String authcode = flinn.util.CookieHandler.getCookie("authcode", request);

if (request.getParameter("order") != null) {
	if (request.getParameter("order").equals("1")) orderby = "TagDescription ASC";
}

flinn.beans.response.ResponseProgressNoteTagsContainerBean rcb = new flinn.beans.response.ResponseProgressNoteTagsContainerBean();
flinn.beans.ProgressNoteTagBean[] tagList = null;
flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();
flinn.beans.request.RequestContainerBean input = new flinn.beans.request.RequestContainerBean();
ResponseSessionContainerBean userSession = dm.getSession(authcode, request);
int lastactivity = -1;

try{
rcb = (flinn.beans.response.ResponseProgressNoteTagsContainerBean)dm.findAllNoteTags(input, userSession, invalid); //function call to return user
tagList =  rcb.getTags();
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
	dm.LOG.debug("Unable to open connection for findAllNoteTags");
}
finally{dm.disposeConnection("findAllNoteTags");}

if (tagList != null)
{
for (int i=0; i<tagList.length; i++) {
  flinn.beans.ProgressNoteTagBean tagBean = tagList[i];
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
		<td><p class="tableText<% if (data_is_not_valid(tagBean) > 0){ out.print("Inactive"); }%>"><%= tagBean.getProgressnotetag()%></p></td>
		<td>&nbsp;</td>
		<td><p class="tableText<% if (data_is_not_valid(tagBean) > 0){ out.print("Inactive"); }%>"><%= tagBean.getProgressnotetagid()%></p></td>
		<td>&nbsp;</td>
		<td><a href="/admin/progress_note_tag_detail.jsp?id=<% out.print(tagBean.getProgressnotetagid());%>" class="subLink"><img src="/admin/images/blue_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;detail</a></td>
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
public int data_is_not_valid(flinn.beans.ProgressNoteTagBean bean) throws Exception {
  if (!bean.getValid()) {
	  	return 1;
	  }
  return 0;
}%>
