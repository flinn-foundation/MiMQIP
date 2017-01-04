<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>Flinn Foundation Recommend Administration Tool</title>
        <link rel="stylesheet" href="admin.css" type="text/css">
        <link rel="icon" href="/favicon.ico">
        <link rel="shortcut icon" href="/favicon.ico">
        <script type="text/javascript" language="JavaScript" src="js/admin_util.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.0/jquery.min.js"></script>
        <script type="text/javascript" src="/js/jquery.cookie.min.js"></script>
        <script type="text/javascript" src="/js/globalUtils.js"></script>
    </head>

    <body leftmargin=0 topmargin=0 bgcolor="#ffffff">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="670" align="left"><img src="images/admin_header.gif" width=670 height=96 alt="" border="0"></td>
                <td><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
                <td width="90" align="right"><img src="images/logo.png" width=200 height=96 alt="Flinn Foundation" border="0"></td>
            </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr valign="top" bgColor="#7F7F7F">
                <td width="199" valign="bottom" align="left"><p class="pageTitle">
                        <%
                                    String admin_section = "";
                                    if (request.getParameter("admin_section") != null) {
                                        admin_section = request.getParameter("admin_section");
                                    }
            if (!admin_section.equals("home")) {%>
                        <a href="/recommend" class="pageTitle">recommend home</a>
                        <% }%><br>
                        <img src="/recommend/images/s.gif" width=186 height=1 alt="" border="0"></p>
                </td>
                <td width=1 bgColor="#E6E5E3"><img src="/recommend/images/s.gif" width=1 height=1 alt="" border="0"></td>
                <td><jsp:include page="/recommend/include/crumb.jsp">
                        <jsp:param name="admin_section" value="<%=admin_section%>" />
                    </jsp:include></td>
            </tr>

