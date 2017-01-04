<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<title>Flinn Foundation: Create New Patient</title>
	<link type="text/css" rel="Stylesheet" href="css/global.css" />
	<link type="text/css" rel="Stylesheet" href="css/patientSearch.css" />
	<link type="text/css" rel="Stylesheet" href="css/colorbox.css" />
	<!--<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script>-->
	<script type="text/javascript" src="js/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="js/globalUtils.js"></script>	
	<script type="text/javascript" src="js/patient.js"></script>
</head>
<!--[if IE 7 ]><body id="patientSelect" class="ie7"><![endif]-->
<!--[if IE 8 ]><body id="patientSelect" class="ie8"><![endif]-->
<!--[if IE 9 ]><body id="patientSelect" class="ie9"><![endif]--> 
<!--[if !IE]><!--><body id="patientSelect"><!--<![endif]-->
<div id="container">
	<div id="utilityBarWrapper">
		<div id="utilityBar">
			<h1></h1>
			<span class="welcome"></span>	
			<a href="#" id="logoutBtn" class="roundedBtn">Logout</a>
			<div class="buttons">
				<a href="/patient-search.jsp" id="patientSearchBtn" class="roundedBtn">Patient Search</a>							
				<a href="/admin/" id="adminBtn" class="roundedBtn">Administration</a>
			</div>
		</div>
	</div>
	<div id="patientSelectWrapper">
		<div id="topHeadingWrapper">
			<div id="topHeading">
				<h1>Create New Patient</h1>			
			</div>
		</div>
		<form id="newPatientForm" action="">
		<div id="createFields" class="patientForm">
			<jsp:include page="/inc/patientForm.jsp" />
			<p id="patientErrorMsg" class="errorMsg">Please complete all fields.</p>
			<div class="clear"></div>
			<input type="submit" value="Save" class="orangeBtn" id="savePatient" />
		</div>
		</form>
	</div>
</div>
</body>
</html>
