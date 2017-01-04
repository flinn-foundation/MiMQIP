<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>
<%
String jspSelf = request.getRequestURL().toString();
String upassword = "-1", upassword2 = "-2";
int userid = -1;
int uid = -1;
Boolean changed = false;
Boolean match = false;


String authcode = flinn.util.CookieHandler.getCookie("authcode", request);
ResponseSessionContainerBean userSession = (new flinn.dao.DaoAppManager()).getSession(authcode, request);

userid = userSession.getUser().getAppuserid();

if (request.getParameter("id") != null){
  	userid = Integer.parseInt(request.getParameter("id"));
  }

String words = request.getParameter("reason");
if (request.getParameter("reason") != null){
  	if(request.getParameter("reason").contains("changed")) changed = true;
  }

if (request.getParameter("userpasswd1") != null){ upassword = request.getParameter("userpasswd1");}
if (request.getParameter("userpasswd2") != null){ upassword2 = request.getParameter("userpasswd2");}

if (upassword.equals(upassword2))match=true;

if (userid > 0 && changed && match){//Update user
	flinn.beans.request.RequestContainerBean rqcont = new flinn.beans.request.RequestContainerBean();
	flinn.beans.request.RequestAppUserBean input= new flinn.beans.request.RequestAppUserBean();
	flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();
	
	flinn.beans.response.ResponseAppUserContainerBean adminUser = null;
	input.setAppuserid(userid);
	rqcont.setUser(input);
	adminUser = (flinn.beans.response.ResponseAppUserContainerBean)dm.findUser(rqcont, userSession);
	input.setSettings(adminUser.getUser().getSettings());
	input.setRoles(adminUser.getUser().getRoles());
	input.setPassword(upassword);
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
}
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CAD8DF">
<script type="text/javascript" language="JavaScript" src="common/js/form_validate.js"></script>

<% if (changed){
 	out.print("<div class=\"pageHolder\">");
 	out.print("<p class=\"tableText\">");
 	out.print("Password ");
 	if (uid <= 0) out.print ("NOT "); 
 	out.print("Changed<br>");
	if (uid <= 0) out.print(uid);
	out.print("</p></div>");
}
%>

<FORM method="POST" action="<%=jspSelf %>" name="adminform">
<input type="hidden" name="id" value="<% out.print(userid); %>">
<input type="hidden" name="id" value="password changed">
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Password</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<input type='password' name='userpasswd1' value='' maxlength="40"></p></td>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
		<td colspan="2" background="/admin/images/row_separator.gif" style="background-repeat: repeat-x;"></td>
		<td><img src="/s.gif" width=1 height=1 alt="" border="0"></td>
	</tr>
	<tr>
		<td width="12" align="left"><img src="/s.gif" width=12 height=1 alt="" border="0"></td>
		<td nowrap><p class="formText">Password Again</p></td>
		<td align="right">
			<p class="formText" style="text-align:left;">
			<input type='password' name='userpasswd2' value='' maxlength="40"></p></td>
		<td>&nbsp;</td>
	</tr>	
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
                                        <div class="updateText"><a href="/admin/" class="updateText"><img src="/admin/images/update_arrow.gif" width=5 height=10 alt="" border="0">&nbsp;&nbsp;Cancel</a></div></td>
                        </tr>
                        </table>
                </td>
                <td>&nbsp;</td>
        </tr>
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
  frmvalidator.addValidation("userpasswd1","req","Please enter a user password");
  frmvalidator.addValidation("userpasswd1","maxlen=40","Max length for password is 40");
  frmvalidator.addValidation("userpasswd2","req","Please enter the password twice");
  frmvalidator.addValidation("userpasswd2","maxlen=40","Max length for password is 40");

</script>
	</table>
