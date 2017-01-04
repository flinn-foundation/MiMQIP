<%
/* */
java.lang.String facidString = request.getParameter("facilityid");
// For this particular page, default to the MiniEHR admin facility.
// TODO: custom entry page URLs for facilities that set these IDs.
int facilityid = 1;
try {
        if (facidString != null)
                facilityid = Integer.decode(facidString).intValue();
} catch (NumberFormatException e) {
	// For this particular page, default to the MiniEHR admin facility.
	// TODO: custom entry page URLs for facilities that set these IDs.
        facilityid = 1;
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<title>Flinn Foundation: Login</title>
	<link type="text/css" rel="Stylesheet" href="/css/global.css" />
	<!--<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script>-->
	<script type="text/javascript" src="/js/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="/js/globalUtils.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			if($.cookie("authcode") != null){
				$("#openPopup").addClass("show")
				$(".login").hide();
			}
			$("#openApp").click(function(e){
				e.preventDefault();
				var theURl = $(this).attr("href");
				utils.openNewWindow(theURl);
			});
			$("#loginAgain").click(function(e){
				e.preventDefault();
				$("#openPopup").removeClass("show")
				$(".login").show();
			});			
			utils.setLoginEvents();
		});	
	</script>
</head>
<!--[if IE 7 ]><body id="login" class="ie7"><![endif]-->
<!--[if IE 8 ]><body id="login" class="ie8"><![endif]-->
<!--[if IE 9 ]><body id="login" class="ie9"><![endif]-->
<!--[if !IE]><!--><body id="login"><!--<![endif]-->
<div id="container">
	<jsp:include page="/inc/loginForm.jsp">
		<jsp:param name="facilityid" value="<%=facilityid%>" />
	</jsp:include>
	<div id="openPopup">
		<a href="/patient-search.jsp" id="openApp" class="orangeBtn">Return to Application</a>
		<a href="#" id="loginAgain">Login again</a>
	</div>
</div>
</body>
</html>

