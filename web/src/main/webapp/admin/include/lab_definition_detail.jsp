<%@page contentType="text/html" %> 
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<%
  int ltid = -1;
  int adminid = -1;
  String serverName = request.getServerName();
  int labtestid = -1;
  Boolean isAdmin = false, isSuperAdmin = false;
  Boolean editable = false;
  Boolean postType = false;
  Boolean hasEdit = false;
  String date_include_date = "";
  String jspSelf = request.getRequestURI().toString();
  java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  String ltName = "", ltFormat = "";
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
	  labtestid = Integer.parseInt(request.getParameter("id"));
  }
  
  if (request.getParameter("Name") != null){
	  ltName = request.getParameter("Name");
  }
  
  if (request.getParameter("Format") != null){
	  ltFormat = request.getParameter("Format");
  }  
 
  if(flinn.util.AdminRole.isFacilityAdmin(userSession))isAdmin = true;
  if (flinn.util.AdminRole.isAdmin(userSession))isSuperAdmin = true;
  
  if (request.getMethod() != null){
	  	if(request.getMethod().equals("POST")) postType = true;
  }
  
  if (request.getParameter("edit") != null){
	  	if(request.getParameter("edit").equals("y")) hasEdit = true;
  }
  
  
  if (isAdmin && postType) {
		flinn.beans.request.RequestContainerBean rqcont = new flinn.beans.request.RequestContainerBean();
		flinn.beans.request.RequestLabBean rqLabBean = new flinn.beans.request.RequestLabBean();
		flinn.beans.LabTestBean input= new flinn.beans.LabTestBean();
		
		input.setLabtestid(labtestid);
		input.setLabtestname(ltName);
		java.util.HashMap<String, String> details = new java.util.HashMap<String, String>();
		details.put("Format", ltFormat.replace("\n", "||").replace("\r", "||").replace("||||", "||"));
		input.setDetails(details);
		Boolean isValid = false;
		
		if (request.getParameter("Valid") != null){
			if (request.getParameter("Valid").equals("1")){isValid = true;}
		}
		input.setValid(isValid);
		input.setStartdate(flinn.util.AdminFunctions.parse_date_snippet("StartDate",request));
		input.setDiscontinuedate(flinn.util.AdminFunctions.parse_date_snippet("DiscontinueDate",request));
		    	
    	if (labtestid > 0){//Update lab test
    	    rqLabBean.setLabtest(input);
    		rqcont.setLab(rqLabBean);
    		flinn.beans.response.ResponseLabTestsContainerBean rspBean= new flinn.beans.response.ResponseLabTestsContainerBean();

    		try{
        		rspBean = (flinn.beans.response.ResponseLabTestsContainerBean)dm.updateLabTest(rqcont, userSession);
        		flinn.beans.LabTestBean[] ltBean = rspBean.getLabtests();
        		ltid = ltBean[0].getLabtestid();
    		}
    		catch(Exception e) {
    			dm.rollbackConnection("updateLabTest");
    			dm.LOG.debug("Unable to commit changes to updateLabTest");
    		}
    	}
    	else{ //Create lab test
    		try{
        	    rqLabBean.setLabtest(input);
        		rqcont.setLab(rqLabBean);
        		ltid = dm.createLabTest(rqcont, userSession);
    		}
    		catch(Exception e) {
    			dm.rollbackConnection("createLabTest");
    			dm.LOG.debug("Unable to commit changes to createLabTest");
    		}
    	}
    	labtestid = ltid;
        
      if (ltid > 0) {
	      if (labtestid > 0) {  		response.sendRedirect("/admin/lab_definition_detail.jsp?id="+ltid+"&reason=labtest+changes+saved");    	  
	      } else {  		response.sendRedirect("/admin/lab_definition_detail.jsp?id="+ltid+"&reason=labtest+created"); 
	      }
	    } else {
			response.sendRedirect("/admin/error.jsp?reason="+ltid);     	
	    }
  }


flinn.beans.LabTestBean adminLab = new flinn.beans.LabTestBean();
if (labtestid > 0){

try{
	adminLab = dm.findLabTest(labtestid);
}
catch(Exception e) {
	dm.LOG.debug("Unable to open connection findLabTest");
}
String title = adminLab.getLabtestname();
ltName = title;
labtestid = adminLab.getLabtestid();
ltFormat = adminLab.getDetails().get("Format");
ltFormat.replace("||","\n");

editable = false;   // Whether or not this is to edit the information
}
if (isAdmin && hasEdit) editable = true;
%>

	<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CAD8DF">
<% if (editable) { %>
<script type="text/javascript" language="JavaScript" src="common/js/form_validate.js"></script>
<FORM method="POST" action="<% out.print(jspSelf); %>" name="adminform">
<input type="hidden" name="id" value="<% if (labtestid > 0) out.print(labtestid); else out.print(0); %>">
<input type="hidden" name="staff" value="<% out.print(request.getParameter("staff")); %>">
<% } %>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Lab Test Name</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='Name' maxlength='50' value='"); %><% if (labtestid > 0)out.print(ltName.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Lab Test Format</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<textarea rows='4' cols='80' name='Format'>"); %><% 
			if (labtestid > 0)
				{
				 if (editable)out.print(ltFormat.replace("&","&amp;").replace("'","&#039;").replace("||","\n")); 
				 else out.print(ltFormat.replace("&","&amp;").replace("'","&#039;").replace("||","<br/>")); 
				}
			
			%><% if (editable) out.print("</textarea>"); %></p></td>
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
  if(labtestid == 0)adminLab.setValid(true);
  if (adminLab.getValid()) out.print(" CHECKED");
  out.print("> valid<br>\n");
  out.print("<input type='radio' name='Valid' value='0'");
  if (!adminLab.getValid()) out.print(" CHECKED");
  out.print("> invalid<br>\n");
} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (adminLab.getValid()) { out.print("valid"); } else { out.print("<span class=\"formTextRed\">invalid</span>"); } 
} if(labtestid == 0)adminLab.setValid(false);%></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Start Date<br><em>when the account should become active</em></p></td>
		<td align="right"><p><% 
if (editable) {
  out.print("<p class='formText' style='text-align:left;'>");
  out.print("(YYYY-MM-DD hh:mm:ss)<br>");
  if (adminLab.getStartdate() != null || labtestid != 0) {
    date_include_date = adminLab.getStartdate();
  } else {
    date_include_date = flinn.util.DateString.now();
  }

  out.print(flinn.util.AdminFunctions.edit_date_snippet("StartDate","formTextNarrow",date_include_date));

} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (flinn.util.DateString.interpret(adminLab.getStartdate()) != null) {
    if (df.parse(flinn.util.DateString.now()).compareTo(df.parse(adminLab.getStartdate())) < 0) {
    	out.print("<span class='formTextRed'>"+adminLab.getStartdate()+"</span>");
    } else {
    	out.print(flinn.util.AdminFunctions.formatNulls(adminLab.getStartdate())); 
    }
  } else {
	  out.print(flinn.util.AdminFunctions.formatNulls(adminLab.getStartdate())); 
  }
} %></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Discontinue Date<br><em>when the account should expire</em></p></td>
		<td><p><% 
if (editable) {
	out.print("<p class='formText' style='text-align:left;'>");
	out.print("(YYYY-MM-DD hh:mm:ss)<br>");
  if (adminLab != null) {
    date_include_date = adminLab.getDiscontinuedate();
  } else {
    date_include_date = "0000-00-00 00:00:00";
  }

  out.print(flinn.util.AdminFunctions.edit_date_snippet("DiscontinueDate","formTextNarrow",date_include_date));

} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (flinn.util.DateString.interpret(adminLab.getDiscontinuedate()) != null) {

    if (df.parse(flinn.util.DateString.now()).compareTo(df.parse(adminLab.getDiscontinuedate())) > 0) {
    	out.print("<span class='formTextRed'>"+adminLab.getDiscontinuedate()+"</span>");
    } else {
    	out.print(flinn.util.AdminFunctions.formatNulls(adminLab.getDiscontinuedate()));
    }
  } else {
	  out.print(flinn.util.AdminFunctions.formatNulls(adminLab.getDiscontinuedate()));
  }
} %></p></td>
		<td>&nbsp;</td>
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
					<div class="updateText"><a href="/admin/lab_definition_detail.jsp?id=<% out.print(labtestid); %>&edit=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Edit</a></div>
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
if (labtestid > 0) {
  out.print(jspSelf); 
  out.print("?id="+labtestid); 
} else {
	out.print("/admin/lab_definition.jsp");
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
dm.disposeConnection("lab_definition_detail");
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
  frmvalidator.addValidation("Name","req","Please enter a name for the lab test");
  frmvalidator.addValidation("Name","maxlen=50","Max length for lab test name is 50");
  frmvalidator.addValidation("Format","req","Please enter a name for the lab test format");
  frmvalidator.addValidation("Format","maxlen=500","Max length for lab test format is 500");  
<% } %>

</script>
<% } %>
	</table>

