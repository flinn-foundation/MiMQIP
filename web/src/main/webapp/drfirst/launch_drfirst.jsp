<%@ page import="flinn.util.DrFirstUtils"%>
<%
	String drfUrl = DrFirstUtils.buildDrFirstUrl("et3439", "1", "ext_doc_123","http://"+request.getServerName()+"/inc/drFirst-logout.jsp");
%>

<html>

<head>
	<script>
		window.onbeforeunload =
			function()
			{
			};
	</script>
</head>

<body>

	<p>
		<a href="<%=drfUrl%>">Launch DrFirst</a>
	</p>

	<p>
		<a href="./process_drfirst_medications.jsp" target="_blank">Process Medication Updated in DrFirst</a>
	</p>

</body>

</html>
