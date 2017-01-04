<%@ page import="java.util.*" %>
<%@page import="flinn.beans.response.ResponseSessionContainerBean"%>

<%

  // determine role of authenticated user
  Boolean isAdmin = false, isSuperAdmin = false;
  Boolean isDoctor = false, isDrFirstAccessible = false;

  String authcode = flinn.util.CookieHandler.getCookie("authcode", request);
  flinn.dao.DaoAppManager dm = new flinn.dao.DaoAppManager();
  flinn.beans.response.ResponseSessionContainerBean userSession = dm.getSession(authcode, request);

  if (userSession != null) {
  	if(flinn.util.AdminRole.isFacilityAdmin(userSession))
  		isAdmin = true;
  	if (flinn.util.AdminRole.isAdmin(userSession))
  		isSuperAdmin = true;
  	if (flinn.util.AdminRole.isDoctor(userSession))
  		isDoctor = true;

  	isDrFirstAccessible = isAdmin || isSuperAdmin || isDoctor;
  }
  
%>


<%!
public String computeLi(int lower, int upper, boolean isYear){
	StringBuffer liOut = new StringBuffer();
	if (isYear){
		for(int i=upper; i>lower; i--){ 
			liOut.append(buildLi(i));													
		}	
	}	
	else{
		for(int i=lower; i<upper; i++){ 
			liOut.append(buildLi(i));													
		}
	}
	return liOut.toString();
}

public String buildLi(int i){
	StringBuffer liOut = new StringBuffer();
	liOut.append("<li");							
	if (i % 2 == 0) {
		liOut.append(" class=\"odd\""); 
	} 
	liOut.append("><a href=\"#");
	liOut.append(Integer.toString(i));
	liOut.append("\">");							
	liOut.append(Integer.toString(i));
	liOut.append("</a></li>");
	return liOut.toString();
}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<title>Flinn Foundation: Patient Search</title>
	<link type="text/css" rel="Stylesheet" href="css/global.css" />
	<link type="text/css" rel="Stylesheet" href="css/patientSearch.css" />
	<link type="text/css" rel="Stylesheet" href="css/colorbox.css" />
	<!--<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script>-->
	<script type="text/javascript" src="js/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="js/globalUtils.js"></script>	
	<script type="text/javascript" src="js/patientSearch.js"></script>
</head>
<!--[if IE 7 ]><body id="patientSelect" class="ie7"><![endif]-->
<!--[if IE 8 ]><body id="patientSelect" class="ie8"><![endif]-->
<!--[if IE 9 ]><body id="patientSelect" class="ie9"><![endif]-->
<!--[if !IE]><!--><body id="patientSelect"><!--<![endif]-->
<div id="container">
	<div id="utilityBarWrapper" class="tall">
		<div id="utilityBar">
			<h1></h1>
			<span class="welcome"></span>	
			<a href="#" id="logoutBtn" class="roundedBtn">Logout</a>
			<div class="buttons">
				<a href="/patient-search.jsp" id="patientSearchBtn" class="roundedBtn">Patient Search</a>
				<a href="/admin/" id="adminBtn" class="roundedBtn">Administration</a>
			</div>
<% if (isDrFirstAccessible == true) { %>			
			<div class="utilityLinks">
				<a href="#" id="drFirstMessageBtn">DrFirst Messages</a>	
				<a href="#" id="drFirstReportBtn">DrFirst Reports</a>	
			</div>
<% } %>				
		</div>
	</div>
	<div id="patientSelectWrapper">
		<div id="topHeadingWrapper">
			<div id="topHeading">
				<h1>Patient Search</h1>
				<a href="new-patient.jsp" class="orangeBtn" id="enterNewPatient">Enter New Patient</a>		
			</div>
		</div>
		<div id="searchFieldsWrapper">
			<form id="patientSearchForm" action="">
			<div id="searchFields" class="patientForm">
				<div class="fieldWrapper">
					<label for="fName">First Name:</label>
					<input type="text" class="greenInput" id="fName" />
				</div>
				<div class="fieldWrapper floatRight">
					<label for="sex">Sex:</label>
					<div class="dropdown" id="sexDropdown">
						<a href="#" class="pullDown">Select</a>
						<ul>
							<li class="odd"><a href="#" class="selected">Select</a></li>
							<li><a href="#M">Male</a></li>
							<li class="odd"><a href="#F">Female</a></li>
						</ul>
					</div>
				</div>
				<div class="fieldWrapper">
					<label for="lName">Last Name:</label>
					<input type="text" class="greenInput" id="lName" />
				</div>			
				<div class="fieldWrapper floatRight">
					<label for="dob">Date of Birth:</label>
					<div class="dropdown dob" id="mmDropdown">
						<a href="#" class="pullDown">MM</a>
						<ul>
							<li class="odd"><a href="#" class="selected">MM</a></li>
							<%out.print(computeLi(1,13,false));%>
						</ul>
					</div>
					<div class="dropdown dob" id="ddDropdown">
						<a href="#" class="pullDown">DD</a>
						<ul>
							<li class="odd"><a href="#" class="selected">DD</a></li>
							<%out.print(computeLi(1,32,false));%> 
						</ul>
					</div>
					<div class="dropdown dob" id="yyyyDropdown">
						<a href="#" class="pullDown">YYYY</a>
						<ul>
							<li class="odd"><a href="#" class="selected">YYYY</a></li>
							<% 						
							GregorianCalendar cal = new GregorianCalendar();
							Date date1 = new Date();
							cal.setTime(date1);
							int year = cal.get(GregorianCalendar.YEAR);
							out.print(computeLi(year-100,year,true));
							%>
						</ul>
					</div>
				</div>
				<div class="fieldWrapper">
					<label for="patientId">ID #:</label>
					<input type="text" class="greenInput" id="patientId" />
				</div>	
				<input type="submit" value="Search" class="orangeBtn" id="loginBtn" />
				<div class="clear"></div>
			</div>
			</form>
		</div>
		<div id="patientTable">
			<h2>Search Results:</h2>
			<div class="pagination" id="topPagination"></div>
			<table>
				<thead>
					<tr>
						<th class="lName">Last Name</th>
						<th class="fName">First Name</th>
						<th class="pID">ID #</th>
						<th class="dob">Date Of Birth</th>
						<th class="sex">Sex</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
			<div class="pagination" id="bottomPagination"></div>
			<div id="noSearchResults">No patient records match your search. Please try again.</div>
		</div>
	</div>
</div>
</body>
</html>
