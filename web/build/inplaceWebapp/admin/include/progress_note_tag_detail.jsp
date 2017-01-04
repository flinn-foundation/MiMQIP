<%@page contentType="text/html" %> 
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<%
  int ptid = -1;
  int adminid = -1;
  String serverName = request.getServerName();
  int tagid = -1;
  Boolean isAdmin = false, isSuperAdmin = false;
  Boolean editable = false;
  Boolean postType = false;
  Boolean hasEdit = false;
  String jspSelf = request.getRequestURI().toString();
  String tagName = "", tagDesc = "";
  int lastactivity = -1;
  
  String authcode = flinn.util.CookieHandler.getCookie("authcode", request);
  flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();
  flinn.beans.response.ResponseSessionContainerBean userSession = dm.getSession(authcode, request);
	try{
		//function call to update user's last activity
		lastactivity = dm.updateLastActivity(userSession);
		dm.commitConnection("updateLastActivity");
	}
	catch(Exception e) {
		dm.rollbackConnection("updateLastActivity");
		dm.LOG.debug("Unable to commit changes to updateLastActivity");
	}
	
	adminid = userSession.getUser().getAppuserid(); 

  
  if (request.getParameter("id") != null){
	  tagid = Integer.parseInt(request.getParameter("id"));
  }
  
  if (flinn.util.AdminRole.isFacilityAdmin(userSession))isAdmin = true;
  if (flinn.util.AdminRole.isAdmin(userSession))isSuperAdmin = true;
  
  if (request.getMethod() != null){
	  	if(request.getMethod().equals("POST")) postType = true;
  }
  
  if (request.getParameter("edit") != null){
	  	if(request.getParameter("edit").equals("y")) hasEdit = true;
  }
  
  if (request.getParameter("Name") != null){
	  tagName = request.getParameter("Name");
  }
  
  if (request.getParameter("Description") != null){
	  tagDesc = request.getParameter("Description");
  }
  
  
  if (isAdmin && postType) {
		flinn.beans.ProgressNoteTagBean input= new flinn.beans.ProgressNoteTagBean();
		input.setProgressnotetagid(tagid);
		input.setProgressnotetag(tagName);
		input.setProgressnotetagdescription(tagDesc);
		
		Boolean isValid = false;
		
		if (request.getParameter("Valid") != null){
			if (request.getParameter("Valid").equals("1")){isValid = true;}
		}
		input.setValid(isValid);
		    	
    	if (tagid > 0){//Update lab test
    		try{
        		ptid = dm.updateNoteTag(input, userSession);
    		}
    		catch(Exception e) {
    			dm.rollbackConnection("updateNoteTag");
    		}
    	}
    	else{ //Create lab test    		
    		try{
    			ptid = dm.createNoteTag(input, userSession);
    		}
    		catch(Exception e) {
    			dm.rollbackConnection("createNoteTag");
    		}
    	}
    	tagid = ptid;
        
      if (ptid > 0) {
	      if (tagid > 0) {  response.sendRedirect("/admin/progress_note_tag_detail.jsp?id="+ptid+"&reason=progressnotetag+changes+saved");    	  
	      } else {  		response.sendRedirect("/admin/progress_note_tag_detail.jsp?id="+ptid+"&reason=progressnotetag+created"); 
	      }
	    } else {
			response.sendRedirect("/admin/error.jsp?reason="+ptid);     	
	    }
  }


flinn.beans.ProgressNoteTagBean adminTag = new flinn.beans.ProgressNoteTagBean();
if (tagid > 0){

try{
	adminTag = dm.getNoteTag(tagid);
}
catch(Exception e) {
	dm.LOG.debug("Unable to open connection getNoteTag");
}


String title = adminTag.getProgressnotetag();
tagName = title;
tagid = adminTag.getProgressnotetagid();
tagDesc = adminTag.getProgressnotetagdescription();

editable = false;   // Whether or not this is to edit the information
}
if (isAdmin && hasEdit) editable = true;
%>

	<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CAD8DF">
<% if (editable) { %>
<script type="text/javascript" language="JavaScript" src="common/js/form_validate.js"></script>
<FORM method="POST" action="<% out.print(jspSelf); %>" name="adminform">
<input type="hidden" name="id" value="<% if (tagid > 0) out.print(tagid); else out.print(0); %>">
<input type="hidden" name="staff" value="<% out.print(request.getParameter("staff")); %>">
<% } %>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Progress Note Tag Name</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='Name' maxlength='20' value='"); %><% if (tagid > 0)out.print(tagName.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Progress Note Tag Description</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<textarea rows='4' cols='40' name='Description'>");  
			if (tagid > 0)out.print(tagDesc.replace("&","&amp;").replace("'","&#039;"));  
			if (editable) out.print("</textarea>"); %></p></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>	
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Valid?</p></td>
		<td align="left"><p><%
if (editable) {
	out.print("<input type='radio' name='Valid' value='1'");
  if(tagid == 0)adminTag.setValid(true);
  if (adminTag.getValid()) out.print(" CHECKED");
  out.print("> valid<br>\n");
  out.print("<input type='radio' name='Valid' value='0'");
  if (!adminTag.getValid()) out.print(" CHECKED");
  out.print("> invalid<br>\n");
} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (adminTag.getValid()) { out.print("valid"); } else { out.print("<span class=\"formTextRed\">invalid</span>"); } 
} if(tagid == 0)adminTag.setValid(false);%></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	
<% if (!editable) { %>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>

<%
  }

if (isAdmin && !editable) {
  %>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=37 alt="" border="0"></td>
		<td>&nbsp;</td>
		<td>
			<table border="0" cellspacing="0" cellpadding="4">
			<tr>
				<td>
					<div class="updateText"><a href="/admin/progress_note_tag_detail.jsp?id=<% out.print(tagid); %>&edit=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Edit</a></div>
				</td>
			</tr>
			</table>
		</td>
		<td>&nbsp;</td>
	</tr>
  <%
} 
if (editable) {
  %>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=37 alt="" border="0"></td>
		<td>&nbsp;</td>
		<td>
			<table border="0" cellspacing="0" cellpadding="4">
			<tr>
				<td>
                                        <div class="updateText"><a href="javascript:formsubmit('adminform')" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Save Updates</a></div>
                                </td>
                                <td>
                                        <div class="updateText"><a href="<%
if (tagid > 0) {
  out.print(jspSelf); 
  out.print("?id="+tagid); 
} else {
	out.print("/admin/progress_note_tag.jsp");
}

%>" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Cancel Updates</a></div></td>
			</tr>
			</table>
		</td>
		<td>&nbsp;</td>
	</tr>
  <%
} 
%>
<% 
dm.disposeConnection("progress_note_detail");
if (editable) { %>
</FORM>	
<script language="JavaScript" type="text/javascript">
  function formsubmit(frmname) {
    if(document.forms[frmname]) {
      if (document.forms[frmname].onsubmit()) {
        document.forms[frmname].submit();
      }
    }
  }

  
  var frmvalidator  = new Validator("adminform");
<% if (editable) { %>
  frmvalidator.addValidation("Name","req","Please enter a name for the progress note tag");
  frmvalidator.addValidation("Name","maxlen=20","Max length for progress note tag name is 20");
  frmvalidator.addValidation("Description","req","Please enter a description for the progress note tag");
  frmvalidator.addValidation("Description","maxlen=80","Max length for progress note tag description is 80");  
<% } %>

</script>
<% } %>
	</table>

