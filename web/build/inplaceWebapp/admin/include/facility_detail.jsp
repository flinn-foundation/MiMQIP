<%@page contentType="text/html" %> 
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<%
  int fid = -1;
  int adminid = -1;
  String serverName = request.getServerName();
  int facilityid = -1;
  Boolean isAdmin = false, isSuperAdmin = false;
  Boolean editable = false;
  Boolean postType = false;
  Boolean hasEdit = false;
  String date_include_date = "";
  String jspSelf = request.getRequestURI().toString();
  java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  String facName = "", facShortcut = "", facEmail = "", role = "";
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
	  facilityid = Integer.parseInt(request.getParameter("id"));
  }
  
  if(flinn.util.AdminRole.isFacilityAdmin(userSession))isAdmin = true;
  if (flinn.util.AdminRole.isAdmin(userSession))isSuperAdmin = true;
  
  if (request.getMethod() != null){
	  	if(request.getMethod().equals("POST")) postType = true;
  }
  
  if (request.getParameter("edit") != null){
	  	if(request.getParameter("edit").equals("y")) hasEdit = true;
  }
  
  if (request.getParameter("Email") != null){
	  	facEmail = request.getParameter("Email");
  }  
  
  if (request.getParameter("role") != null){
	  role = request.getParameter("role");
  }
  
  if (isAdmin && postType) {
		flinn.beans.request.RequestContainerBean rqcont = new flinn.beans.request.RequestContainerBean();
		flinn.beans.request.RequestFacilityBean input= new flinn.beans.request.RequestFacilityBean();
		
		if (request.getParameter("AdminID") != null) //Update adminid if input field differs 
		{
			if(adminid != Integer.parseInt(request.getParameter("AdminID"))) adminid = Integer.parseInt(request.getParameter("AdminID"));
		}
		
		input.setAdministratorid(Integer.toString(adminid));
		
		if(role.equals("Admin")){ //Only pull request data for super admin
			input.setFacilityid(facilityid);
		}
		else{ //Facility admins can only edit their facility
			input.setFacilityid(userSession.getUser().getFacilityid());
		}
		
		input.setFacilityname(request.getParameter("Name"));
		facName = request.getParameter("Name");
		input.setFacilityshortcut(request.getParameter("Shortcut"));
		facShortcut = request.getParameter("Shortcut");
		input.setFacilityemail(request.getParameter("Email"));
		
		Boolean isValid = false;
		
		if (request.getParameter("Valid") != null){
			if (request.getParameter("Valid").equals("1")){isValid = true;}
		}
		
		input.setLaunch(flinn.util.AdminFunctions.parse_date_snippet("Launch",request));
		input.setExpiration(flinn.util.AdminFunctions.parse_date_snippet("Expiration",request));

		//Do roles loop through checks and set to obj
		//java.util.HashMap<String, String> userSetting = new java.util.HashMap<String,String>(); 
		//userSetting.put("FullName", userFullName);
		//if(!userEmail.equals(""))userSetting.put("Email", userEmail);			
		//input.setSettings(userSetting);
		input.setValid(isValid);
		    	
    	if (facilityid > 0){//Update facility
	   		rqcont.setFacility(input);    		
    		flinn.beans.response.ResponseFacilityContainerBean rspBean= new flinn.beans.response.ResponseFacilityContainerBean();

    		try{
        		rspBean = (flinn.beans.response.ResponseFacilityContainerBean)dm.updateFacility(rqcont, userSession);
        		fid = rspBean.getFacility().getFacilityid();
    		}
    		catch(Exception e) {
    			dm.rollbackConnection("updateFacility");
    			dm.LOG.debug("Unable to commit changes to updateFacility");
    		}
    	}
    	else{ //Create facility
    		try{
        		rqcont.setFacility(input);
        		fid = dm.createFacility(rqcont, userSession);
    		}
    		catch(Exception e) {
    			dm.rollbackConnection("updateFacility");
    			dm.LOG.debug("Unable to commit changes to updateFacility");
    		}
    	}
    	facilityid = fid;
        
      if (fid > 0) {
	      if (facilityid > 0) {  		response.sendRedirect("/admin/facility_detail.jsp?id="+fid+"&reason=facility+changes+saved");    	  
	      } else {  		response.sendRedirect("/admin/facility_detail.jsp?id="+fid+"&reason=facility+created"); 
	      }
	    } else {
			response.sendRedirect("/admin/error.jsp?reason="+fid);     	
	    }
  }


flinn.beans.response.ResponseFacilityBean adminFacility = new flinn.beans.response.ResponseFacilityBean();
if (facilityid > 0){
try{
	adminFacility = (flinn.beans.response.ResponseFacilityBean)dm.getFacility(facilityid);
}
catch(Exception e) {
	dm.LOG.debug("Unable to open connection getFacility");
}

String title = adminFacility.getFacilityname();
facName = title;
adminid = Integer.parseInt(adminFacility.getAdministratorid());
facShortcut = adminFacility.getFacilityshortcut();
facEmail = adminFacility.getFacilityemail();
/*
java.util.Iterator<String> it = adminFacility.getSettings().keySet().iterator();

while(it.hasNext())
{
	String key = it.next();
	String value = adminFacility.getFacility().getSettings().get(key);
	
	if (key.equals("FullName"))userFullName = value;
	if (key.equals("Email"))userEmail = value;
}
*/
editable = false;   // Whether or not this is to edit the information
}
if (isAdmin && hasEdit) editable = true;
%>

	<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CAD8DF">
<% if (editable) { %>
<script type="text/javascript" language="JavaScript" src="common/js/form_validate.js"></script>
<FORM method="POST" action="<% out.print(jspSelf); %>" name="adminform">
<input type="hidden" name="id" value="<% if (facilityid > 0) out.print(facilityid); else out.print(0); %>">
<input type="hidden" name="staff" value="<% out.print(request.getParameter("staff")); %>">
<% } %>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Facility Name</p></td>
		<td align="right">
		<% //print '<pre>'; print_r($row); print '</pre>'; %>
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='Name' maxlength='80' value='"); %><% if (facilityid > 0)out.print(facName.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>	
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Facility Shortcut</p></td>
		<td align="right">
		<% //print '<pre>'; print_r($row); print '</pre>'; %>
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='Shortcut' maxlength='20' value='"); %><% if (facilityid > 0)out.print(facShortcut.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>	
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Email Address</p></td>
		<td align="right">
		<% //print '<pre>'; print_r($row); print '</pre>'; %>
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='Email' maxlength='50' value='"); %><% if (facilityid > 0)out.print(facEmail); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>	
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Administrator ID</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (isSuperAdmin){if (editable) out.print("<input type='text' name='AdminID' maxlength='8' value='"); out.print(adminid);  if (editable) out.print("'>"); %>
			<% } else {
					out.print(adminid);
					out.print("<input type=\"hidden\" name=\"AdminID\" value=\"" + adminid + "\">");
				}%></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Valid?</p></td>
		<td align="left"><%
if (editable) {
	out.print("<input type='radio' name='Valid' value='1'");
  if(facilityid == 0)adminFacility.setValid(true);
  if (adminFacility.getValid()) out.print(" CHECKED");
  out.print("> valid<br>\n");
  out.print("<input type='radio' name='Valid' value='0'");
  if (!adminFacility.getValid()) out.print(" CHECKED");
  out.print("> invalid<br>\n");
} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (adminFacility.getValid()) { out.print("valid"); } else { out.print("<span class=\"formTextRed\">invalid</span>"); } 
} if(facilityid == 0)adminFacility.setValid(false);%></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Live Date<br><em>when the account should become active</em></p></td>
		<td align="right"><% 
if (editable) {
  out.print("<p class='formText' style='text-align:left;'>");
  out.print("(YYYY-MM-DD hh:mm:ss)<br>");
  if (adminFacility.getLaunch() != null || facilityid != 0) {
    date_include_date = adminFacility.getLaunch();
  } else {
    date_include_date = flinn.util.DateString.now();
  }

  out.print(flinn.util.AdminFunctions.edit_date_snippet("Launch","formTextNarrow",date_include_date));

} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (flinn.util.DateString.interpret(adminFacility.getLaunch()) != null) {
    if (df.parse(flinn.util.DateString.now()).compareTo(df.parse(adminFacility.getLaunch())) < 0) {
    	out.print("<span class='formTextRed'>"+adminFacility.getLaunch()+"</span>");
    } else {
    	out.print(flinn.util.AdminFunctions.formatNulls(adminFacility.getLaunch())); 
    }
  } else {
	  out.print(flinn.util.AdminFunctions.formatNulls(adminFacility.getLaunch())); 
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
		<td nowrap><p class="formText">Expire Date<br><em>when the account should expire</em></p></td>
		<td><% 
if (editable) {
	out.print("<p class='formText' style='text-align:left;'>");
	out.print("(YYYY-MM-DD hh:mm:ss)<br>");
  if (adminFacility != null) {
    date_include_date = adminFacility.getExpiration();
  } else {
    date_include_date = "0000-00-00 00:00:00";
  }

  out.print(flinn.util.AdminFunctions.edit_date_snippet("Expiration","formTextNarrow",date_include_date));

} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (flinn.util.DateString.interpret(adminFacility.getExpiration()) != null) {

    if (df.parse(flinn.util.DateString.now()).compareTo(df.parse(adminFacility.getExpiration())) > 0) {
    	out.print("<span class='formTextRed'>"+adminFacility.getExpiration()+"</span>");
    } else {
    	out.print(flinn.util.AdminFunctions.formatNulls(adminFacility.getExpiration()));
    }
  } else {
	  out.print(flinn.util.AdminFunctions.formatNulls(adminFacility.getExpiration()));
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
					<div class="updateText"><a href="/admin/facility_detail.jsp?id=<% out.print(facilityid); %>&edit=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Edit</a></div>
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
if (facilityid > 0) {
  out.print(jspSelf); 
  out.print("?id="+facilityid); 
} else {
	out.print("/admin/facility.jsp");
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
dm.disposeConnection("facility_detail");
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
  frmvalidator.addValidation("Name","req","Please enter a name for the facility");
  frmvalidator.addValidation("Name","maxlen=80","Max length for facility name is 80");
  frmvalidator.addValidation("Shortcut","req","Please enter a shortcut for the facility");
  frmvalidator.addValidation("Shortcut","maxlen=20","Max length for facility shortcut is 20");
  frmvalidator.addValidation("Email","email","Please enter an Email address");
  frmvalidator.addValidation("Email","maxlen=50","Max length for Email address is 50");
  frmvalidator.addValidation("AdminID","numeric","Please enter an Administrator ID");
  frmvalidator.addValidation("AdminID","maxlen=8","Max length for Administrator ID is 8");  
<% } %>

</script>
<% } %>
	</table>

