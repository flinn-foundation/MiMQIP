<%@ page import="flinn.rcopia.service.RcopiaTransformationService"%>
<%@ page import="flinn.rcopia.service.RcopiaService"%>
<%@ page import="flinn.rcopia.model.RcExtResponseType"%>

<%
	String patientIdString = request.getQueryString();
	int patientId = 0;
	try {
	  if (patientIdString != null)
	    patientId = Integer.decode(patientIdString).intValue();
	} catch (NumberFormatException e) {
	}

	RcopiaService service = new RcopiaService();
	RcopiaTransformationService transformation = new RcopiaTransformationService();
	boolean consistent = false;

	if (patientId > 0) {
		try {
			RcExtResponseType rcResponse = service.updateMedications(patientId);
			String xml = transformation.convertRcopiaResponseToXml(rcResponse);
			consistent = new flinn.dao.Recommendation().handleConsistencyCheck(patientId);
		} catch (Exception e) {
			e.printStackTrace();
			consistent = true;
		}
	}
	


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<script type="text/javascript" src="../js/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../js/globalUtils.js"></script>	
</head>
<body>
<%
if (consistent) {
%>
<script type="text/javascript">
	parent.utils.closeLightbox();
	//app.getMedicationData(); 
	//utils.getMedsData = true;
</script>
<!--There should be script here that will close the lightbox.-->
<%
} else {
%>
<script type="text/javascript">
	parent.utils.openColorbox("/inc/non-consistency.jsp","nonConsistency", false, 700);
</script>
<!--<jsp:include page="/inc/non-consistency.jsp" />-->
<%
}
%>
</body>
</html>
