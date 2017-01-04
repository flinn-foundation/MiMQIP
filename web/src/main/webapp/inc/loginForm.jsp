<%
/* */
java.lang.String facidString = request.getParameter("facilityid");
int facilityid = 0;
try {
	if (facidString != null)
		facilityid = Integer.decode(facidString).intValue();
} catch (NumberFormatException e) {
	facilityid = 0;
}
flinn.beans.response.ResponseFacilityBean facility = null;

if (facilityid > 0) {
	java.sql.Connection connection = null;
	try {
		connection = flinn.dao.dbconnection.DBConnectionPool.getConnection();
		if (connection == null || connection.isClosed()) {
			// LOG.debug("Unable to get DB Connection");
		}
	} catch (java.lang.Exception e) {
		// LOG.debug("Unable to get DB Connection: "+e);
	}
	
	flinn.beans.request.RequestFacilityBean facreq = new flinn.beans.request.RequestFacilityBean();
	facreq.setFacilityid(facilityid);
	java.util.List<flinn.beans.response.ResponseFacilityBean> facilities = new flinn.dao.imp.FacilityDaoImp().find(facreq, null, connection);
	
	if (facilities != null && facilities.size() > 0) {
		facility = facilities.get(0);
	}
}
/* */
java.lang.String facilityName = "Unknown Facility Name";
/* */
if (facility != null) {
  facilityName = facility.getFacilityname();
}
/* */
%>
<div id="colorboxForm" class="login">
	<form action="" id="loginForm">
		<h1>Welcome</h1>
		<div class="fieldWrapper">
			<label for="uName">User Name:</label>
			<input type="text" class="greenInput" id="uName" /><span class="uName"></span>
		</div>
		<div class="fieldWrapper">
			<label for="password">Password:</label>
			<input type="password" class="greenInput" id="passwrd" />
		</div>
		<label>Facility:</label><span class="facilityName"><%=facilityName%></span>
		<div class="fieldWrapper">
			<label for="terms" style="padding-top:17px;">Terms:</label>
			<br/><input type="checkbox" id="terms" value="true" />
			<span style="padding-top:11px; font-size:10px;"> I have read and agree to the <a href="/MiMQIP-terms.pdf" target="_blank">terms and conditions</a>.</span>
		</div>
		<input type="submit" value="Login" class="orangeBtn" id="loginBtn"  />
		<input type="hidden" id="facilityID" value="<%=facilityid%>" />
		<p id="loginErrorMsg" class="errorMsg" style="padding-top:5px;"></p>
		<a href="#" id="newUser">Close window and sign in as a different user</a>
	</form>
</div>
