<%@page contentType="text/html" %> 
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<%
  int uid = -1;
  String serverName = request.getServerName();
  int userid = -1, facilityid = -1;
  Boolean isAdmin = false, isSuperAdmin = false;
  Boolean editable = false;
  Boolean cpasswd = false;
  Boolean postType = false;
  Boolean hasEdit = false;
  Boolean hasPwd = false;
  String date_include_date = "";
  String jspSelf = request.getRequestURI().toString();
  java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  String loginTailor = "", userFullName = "", userEmail = "";
  String userPracticeUserName = "", userUserExternalID = "";
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

  
  if (request.getParameter("FacilityID") != null){
	  facilityid = Integer.parseInt(request.getParameter("FacilityID"));
  }
  
  if (request.getParameter("id") != null){
  	userid = Integer.parseInt(request.getParameter("id"));
  }
  
  if(flinn.util.AdminRole.isFacilityAdmin(userSession))isAdmin = true;
  if (flinn.util.AdminRole.isAdmin(userSession))isSuperAdmin = true;
  
  if (request.getMethod() != null){
	  	if(request.getMethod().equals("POST")) postType = true;
  }
  
  if (request.getParameter("edit") != null){
	  	if(request.getParameter("edit").equals("y")) hasEdit = true;
  }
  
  if (request.getParameter("passwd") != null){
	  	if(request.getParameter("passwd").equals("y")) hasPwd = true;
  } 
  
  if (request.getParameter("FullName") != null){
	  	userFullName = request.getParameter("FullName");
  } 
  
  if (request.getParameter("Email") != null){
	  	userEmail = request.getParameter("Email");
  }   

  if (request.getParameter("PracticeUserName") != null){
	  	userPracticeUserName = request.getParameter("PracticeUserName");
  } 

  if (request.getParameter("UserExternalID") != null){
	  	userUserExternalID = request.getParameter("UserExternalID");
  }   


  if(isSuperAdmin == false){ //Force facilityid for facilityadmins
		facilityid = userSession.getUser().getFacilityid();
  }
  
  if (isAdmin && postType) {
	flinn.beans.request.RequestContainerBean rqcont = new flinn.beans.request.RequestContainerBean();
	flinn.beans.request.RequestAppUserBean input= new flinn.beans.request.RequestAppUserBean();

  if (request.getParameter("cpasswd") != null) {	
	flinn.beans.response.ResponseAppUserContainerBean adminUser = null;
	input.setAppuserid(userid);
	rqcont.setUser(input);
	adminUser = (flinn.beans.response.ResponseAppUserContainerBean)dm.findUser(rqcont, userSession);
	loginTailor = adminUser.getUser().getLogin();
	input.setSettings(adminUser.getUser().getSettings());
	input.setRoles(adminUser.getUser().getRoles());
	input.setPassword(request.getParameter("userpasswd1"));
	rqcont.setUser(input);

	//Change password
    flinn.beans.response.ResponseAppUserContainerBean rspUser = null;

	try{
	    rspUser = (flinn.beans.response.ResponseAppUserContainerBean)dm.updateUser(rqcont, userSession);
	    uid = rspUser.getUser().getAppuserid();
	}
	catch(Exception e) {
		dm.rollbackConnection("updateUser");
		dm.LOG.debug("Unable to commit changes to updateUser");
	}
	
    if (uid > 0) {      		response.sendRedirect("/admin/user_detail.jsp?id="+uid+"&reason=password+changed");
    } else {    		response.sendRedirect("/admin/error.jsp?reason="+uid);
    }
  } else {
		input.setFacilityid(facilityid);
		input.setLogin(request.getParameter("Login"));
		loginTailor = request.getParameter("Login");
		input.setPassword(request.getParameter("userpasswd1"));
		Boolean isValid = false;
		
		if (request.getParameter("Valid") != null){
			if (request.getParameter("Valid").equals("1")){isValid = true;}
		}
		
		//Do roles loop through checks and set to obj
		java.util.HashMap<String, String> userSetting = new java.util.HashMap<String,String>(); 
		userSetting.put("FullName", userFullName);
		if(!userEmail.equals(""))
			userSetting.put("Email", userEmail);
		if (!userPracticeUserName.equals(""))
			userSetting.put("PracticeUserName", userPracticeUserName); 
		if (!userUserExternalID.equals(""))		  
			userSetting.put("UserExternalID", userUserExternalID);
			
		input.setSettings(userSetting);
		input.setRoles(flinn.util.AdminFunctions.getRolesFromForm(request));
		input.setValid(isValid);
		input.setLaunch(flinn.util.AdminFunctions.parse_date_snippet("Launch",request));
		input.setExpiration(flinn.util.AdminFunctions.parse_date_snippet("Expiration",request));
		    	
    	if (userid > 0){//Update user
    		if(isSuperAdmin && facilityid == -1) input.setFacilityid(0);
    		input.setAppuserid(userid);
    		rqcont.setUser(input);
    		flinn.beans.response.ResponseAppUserContainerBean rspBean= new flinn.beans.response.ResponseAppUserContainerBean();

    		try{
        		rspBean = (flinn.beans.response.ResponseAppUserContainerBean)dm.updateUser(rqcont, userSession);
        		uid = rspBean.getUser().getAppuserid();
    		}
    		catch(Exception e) {
    			dm.rollbackConnection("updateUser");
    			dm.LOG.debug("Unable to commit changes to updateUser");
    		} 
    	}
    	else{ //Create user    		
    		try{
    			rqcont.setUser(input);
        		uid = dm.createUser(rqcont, userSession);
    		}
    		catch(Exception e) {
    			dm.rollbackConnection("createUser");
    			dm.LOG.debug("Unable to commit changes to createUser");
    		}    		
    	}
    	userid = uid;
        
      if (uid > 0) {
      if (userid > 0) {  		response.sendRedirect("/admin/user_detail.jsp?id="+uid+"&reason=user+changes+saved");    	  
      } else {  		response.sendRedirect("/admin/user_detail.jsp?id="+uid+"&reason=user+created"); 
      }
    } else {
		response.sendRedirect("/admin/error.jsp?reason="+uid);     	
    }
  }
}

flinn.beans.response.ResponseAppUserContainerBean adminUser = null;
flinn.beans.request.RequestContainerBean rqcont = new flinn.beans.request.RequestContainerBean();
flinn.beans.request.RequestAppUserBean input= new flinn.beans.request.RequestAppUserBean();
input.setAppuserid(userid);
rqcont.setUser(input);
try{
	adminUser = (flinn.beans.response.ResponseAppUserContainerBean)dm.findUser(rqcont, userSession);
}
catch(Exception e) {
	dm.LOG.debug("Unable to open connection findAllUsers" + e.toString());
}

String title = adminUser.getUser().getLogin();
if(isSuperAdmin)facilityid = adminUser.getUser().getFacilityid();
loginTailor = title;
java.util.Iterator<String> it = adminUser.getUser().getSettings().keySet().iterator();
while(it.hasNext())
{
	String key = it.next();
	String value = adminUser.getUser().getSettings().get(key);
	
	if (key.equals("FullName"))
		userFullName = value;
	if (key.equals("Email"))
		userEmail = value;
	if (key.equals("PracticeUserName"))
		userPracticeUserName = value;
	if (key.equals("UserExternalID"))
		userUserExternalID = value;

}

editable = false;   // Whether or not this is to edit the information
if (isAdmin && hasEdit) editable = true;
if (isAdmin && hasPwd) cpasswd = true;
%>

	<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CAD8DF">
<% if (editable || cpasswd) { %>
<script type="text/javascript" language="JavaScript" src="common/js/form_validate.js"></script>
<FORM method="POST" action="<% out.print(jspSelf); %>" name="adminform">
<input type="hidden" name="id" value="<% if (userid > 0) out.print(userid); else out.print(0); %>">
<input type="hidden" name="staff" value="<% out.print(request.getParameter("staff")); %>">
<% if (cpasswd) { %>
<input type="hidden" name="cpasswd" value="1">
<% } %>
<% } 

if(!cpasswd){%>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Facility ID</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (isSuperAdmin && editable){ out.print("<input type='text' name='FacilityID' value='"); out.print(facilityid); %><% if (editable) out.print("'>"); %>
			<% }else out.print(facilityid); %>
			</p>
			</td>
		<td>&nbsp;</td>
	</tr>
<%} %>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Login</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='Login' maxlength='20' value='"); %><% if (userid > 0)out.print(loginTailor.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>
<% if(!cpasswd){%>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/php/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>	
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Full Name</p></td>
		<td align="right">
		<% //print '<pre>'; print_r($row); print '</pre>'; %>
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='FullName' maxlength='80' value='"); %><% if (userid > 0)out.print(userFullName.replace("&","&amp;").replace("'","&#039;")); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/php/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>	
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Email Address</p></td>
		<td align="right">
		<% //print '<pre>'; print_r($row); print '</pre>'; %>
			<p class="formText" style="text-align:left;">
			<% if (editable) out.print("<input type='text' name='Email' maxlength='50' value='"); %><% if (userid > 0)out.print(userEmail); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>	
	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/php/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>	
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Practice User Name</p></td>
		<td align="right">
	<% //print '<pre>'; print_r($row); print '</pre>'; %>
		<p class="formText" style="text-align:left;">
		<% if (editable) out.print("<input type='text' name='PracticeUserName' maxlength='50' value='"); %><% if (userid > 0)out.print(userPracticeUserName); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>	

	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/php/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>	
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">User External ID</p></td>
		<td align="right">
		<% //print '<pre>'; print_r($row); print '</pre>'; %>
		<p class="formText" style="text-align:left;">
		<% if (editable) out.print("<input type='text' name='UserExternalID' maxlength='50' value='"); %><% if (userid > 0)out.print(userUserExternalID); %><% if (editable) out.print("'>"); %></p></td>
		<td>&nbsp;</td>
	</tr>	
	
<% }
if (cpasswd || userid == 0) { %>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/php/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Password</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<input type='password' name='userpasswd1' maxlength='40' value=''></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/php/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Password Again</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<input type='password' name='userpasswd2' maxlength='40' value=''></p></td>
		<td>&nbsp;</td>
	</tr>	
<% } %>
<% if (!cpasswd) { %>
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Roles</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
<%
if (editable) {
	//Pull all roles
  	flinn.beans.AppUserRoleBean[] ar = dm.getAllRolesUser();

  	//Pull user role
	flinn.beans.AppUserRoleBean[] urar = adminUser.getUser().getRoles();
  	
	for(int i = 0;i< ar.length; i++){ //Loop thru all roles
		if (!isSuperAdmin && i == 0){i++;} //Skip Admin if user isn't a superadmin
		flinn.beans.AppUserRoleBean role = (flinn.beans.AppUserRoleBean)ar[i];
				out.print("<input type='checkbox' name='userrole_"+role.getApprole()+"' value='"+role.getApproleid()+"'");
				
				for(int j=0; j < urar.length; j++){
					flinn.beans.AppUserRoleBean userRole = (flinn.beans.AppUserRoleBean)urar[j];
					String urole = userRole.getApprole();
					if (role.getApprole().equals(urole)) out.print(" CHECKED");
				}
				out.print(">"+role.getApprole()+"<br>\n");

	}
}
else
{
	flinn.beans.AppUserRoleBean[] ar = adminUser.getUser().getRoles();
	for (int i=0; i < ar.length; i++){
		flinn.beans.AppUserRoleBean role = (flinn.beans.AppUserRoleBean)ar[i];
		out.print(role.getApprole());
		if (i != ar.length - 1) out.print("<br/>");
	}
}
%></p></td>
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
		<td align="left"><%
if (editable) {
	out.print("<input type='radio' name='Valid' value='1'");
  if(userid == 0)adminUser.getUser().setValid(true);
  if (adminUser.getUser().getValid()) out.print(" CHECKED");
  out.print("> valid<br>\n");
  out.print("<input type='radio' name='Valid' value='0'");
  if (!adminUser.getUser().getValid()) out.print(" CHECKED");
  out.print("> invalid<br>\n");
} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (adminUser.getUser().getValid()) { out.print("valid"); } else { out.print("<span class=\"formTextRed\">invalid</span>"); } 
} if(userid == 0)adminUser.getUser().setValid(false);%></p></td>
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
  if (adminUser.getUser().getLaunch() != null) {
    date_include_date = adminUser.getUser().getLaunch();
  } else {
    date_include_date = flinn.util.DateString.now();
  }
  if(userid == 0)date_include_date = flinn.util.DateString.now();

  out.print(flinn.util.AdminFunctions.edit_date_snippet("Launch","formTextNarrow",date_include_date));

} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (flinn.util.DateString.interpret(adminUser.getUser().getLaunch()) != null) {
    if (df.parse(flinn.util.DateString.now()).compareTo(df.parse(adminUser.getUser().getLaunch())) < 0) {
    	out.print("<span class='formTextRed'>"+adminUser.getUser().getLaunch()+"</span>");
    } else {
    	out.print(flinn.util.AdminFunctions.formatNulls(adminUser.getUser().getLaunch())); 
    }
  } else {
	  out.print(flinn.util.AdminFunctions.formatNulls(adminUser.getUser().getLaunch())); 
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
  if (adminUser != null) {
    date_include_date = adminUser.getUser().getExpiration();
  } else {
    date_include_date = "0000-00-00 00:00:00";
  }
  if(userid == 0)date_include_date = "0000-00-00 00:00:00";

  out.print(flinn.util.AdminFunctions.edit_date_snippet("Expiration","formTextNarrow",date_include_date));

} else {
	out.print("<p class='formText' style='text-align:left;'>");
  if (flinn.util.DateString.interpret(adminUser.getUser().getExpiration()) != null) {

    if (df.parse(flinn.util.DateString.now()).compareTo(df.parse(adminUser.getUser().getExpiration())) > 0) {
    	out.print("<span class='formTextRed'>"+adminUser.getUser().getExpiration()+"</span>");
    } else {
    	out.print(flinn.util.AdminFunctions.formatNulls(adminUser.getUser().getExpiration()));
    }
  } else {
	  out.print(flinn.util.AdminFunctions.formatNulls(adminUser.getUser().getExpiration()));
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
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Last Login</p></td>
		<td align="right"><p class="formText" style="text-align:left;"><% out.print(flinn.util.AdminFunctions.formatNulls(adminUser.getUser().getLastloggedin())); %></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Last Remote Address</p></td>
		<td align="right"><p class="formText" style="text-align:left;"><% out.print(adminUser.getUser().getLastremoteaddress()); %></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Last Server Activity</p></td>
		<td align="right"><p class="formText" style="text-align:left;"><% out.print(flinn.util.AdminFunctions.formatNulls(adminUser.getUser().getLastactivity())); %></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
<%
  }
}
if (isAdmin && !editable && !cpasswd) {
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
					<div class="updateText"><a href="/admin/user_detail.jsp?id=<% out.print(userid); %>&passwd=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Change Password</a></div>
				</td>
				<td>
					<div class="updateText"><a href="/admin/user_detail.jsp?id=<% out.print(userid); %>&edit=y" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Edit</a></div>
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
if (userid > 0) {
  out.print(jspSelf); 
  out.print("?id="+userid); 
} else {
	out.print("/admin/user.jsp");
}

%>" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Cancel Updates</a></div></td>
			</tr>
			</table>
		</td>
		<td>&nbsp;</td>
	</tr>
  <%
} 
if (cpasswd) {
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
					<div class="updateText"><a href="javascript:formsubmit('adminform')" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Change Password</a></div>
                                </td>
                                <td>
                                        <div class="updateText"><a href="<%
if (adminUser != null) {
	  out.print(jspSelf); 
	  out.print("?id="+userid); 
	} else {
		out.print("/admin/user.jsp");
}

%>" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Cancel</a></div></td>
                        </tr>
                        </table>
                </td>
                <td>&nbsp;</td>
        </tr>
  <%
}
%>
<% 
dm.disposeConnection("user_detail");

if (editable || cpasswd) { %>
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
  frmvalidator.addValidation("Login","req","Please enter a user login");
  frmvalidator.addValidation("Login","maxlen=20","Max length for login is 20");
  frmvalidator.addValidation("Login","alphanumeric","Login must be alphanumeric with no spaces.");
  frmvalidator.addValidation("FullName","req","Please enter a full name for the user");
  frmvalidator.addValidation("FullName","maxlen=80","Max length for full name is 80");

<% } if (cpasswd || userid == 0) { %>
  frmvalidator.addValidation("userpasswd1","req","Please enter a user password");
  frmvalidator.addValidation("userpasswd1","maxlen=40","Max length for password is 40");
  frmvalidator.addValidation("userpasswd2","req","Please enter the password twice");
  frmvalidator.addValidation("userpasswd2","maxlen=40","Max length for password is 40");
  var theForm = document.forms["adminform"];
  /*if (theForm.userpasswd1 != theForm.userpasswd2){
	  alert("Please verify that the passwords match.");	  
	  }
	*/
<% } %>

</script>
<% } %>
	</table>

