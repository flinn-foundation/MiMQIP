<%@page import="flinn.util.AdminNavigation"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="flinn.util.AdminFunctions"%><table width="200" border="0" cellspacing="0" cellpadding="0" bgcolor="#E6E5E3">
<tr bgColor="#FFFFFF">
        <td width="100%" height="1"><img src="/admin/images/s.gif" width=1 height=1 alt="" border="0"></td>
</tr>
<%
String title = "";
String admin_section = "";
String admin_subsection = "";
String id = "";
String role = "";
Boolean hasID = false;
Boolean is_authenticated = true;

if (request.getParameter("admin_section") != null){
	admin_section = request.getParameter("admin_section");
}

if (request.getParameter("admin_subsection") != null){
	admin_subsection = request.getParameter("admin_subsection");
}

if (request.getParameter("id") != null){
	id = request.getParameter("id");
	hasID = true;
}

if (request.getParameter("role") != null){
	role = request.getParameter("role");
}

if (is_authenticated) {
//if (is_authenticated()) {
	HashMap<String, ArrayList<String>> navigation = AdminNavigation.getNavigation();
	HashMap<String, ArrayList<String>> navigation_url = AdminNavigation.getNavigationURL();
	HashMap<String, ArrayList<String>> navigation_role = AdminNavigation.getNavigationRole();	
	java.util.Iterator<String> it = navigation.keySet().iterator();
	int navCount = 0;
	while(it.hasNext())
	{
		String navkey = it.next();
		ArrayList<String> nav_section_al = (ArrayList<String>)navigation.get(navkey);
		ArrayList<String> nav_role = (ArrayList<String>)navigation_role.get(navkey);
		ArrayList<String> nav_url_al = (ArrayList<String>)navigation_url.get(navkey);
		
		//if ((has_role($navigation_role[$nav_section][0])) || (has_role($navigation_role[$nav_section][1]))) {
	    if (AdminFunctions.has_role(role, nav_role.get(0)) || AdminFunctions.has_role(role, nav_role.get(1))) {
	   		out.print("<tr valign='middle'><td><a href='"+nav_url_al.get(0)+"' class='navLink");
	   	    if (admin_section.equals(nav_section_al.get(0))) out.print("Selected");
	    	out.print("'><img src='/admin/images/s.gif' width=5 height=15 alt='' border='0'><img src='/admin/images/nav_boxed_arrow.gif' width=25 height=15 alt='' border='0'><img src='/admin/images/s.gif' width=8 height=15 alt='' border='0'>"+nav_section_al.get(0)+"</a></td></tr>\n");
	    	out.print("<tr bgColor='#FFFFFF'><td height='1'><img src='/admin/images/s.gif' width=1 height=1 alt='' border='0'></td></tr>\n");
	    	if (admin_section.equals(navkey) && nav_section_al.get(0).equals("id")) {
	    		if (hasID) {
	    	    	out.print("<tr valign='middle'><td><a href='"+nav_url_al.get(0)+"?id="+id+"' class='navLinkSelected");
	    	        out.print("'><img src='/admin/images/s.gif' width=25 height=15 alt='' border='0'><img src='/admin/images/nav_drop_arrow.gif' width=25 height=15 alt='' border='0'><img src='/admin/images/s.gif' width=8 height=15 alt='' border='0'>"+title+"</a></td></tr>\n");
	    	        out.print("<tr bgColor='#FFFFFF'><td height='1'><img src='/admin/images/s.gif' width=1 height=1 alt='' border='0'></td></tr>\n");
	    	    }
	    	} 
	    	else if (admin_section.equals(navkey)) {		
	    		int alCount = 0;
	    		java.util.Iterator<String> ital = nav_section_al.iterator();		
	    	 	while(ital.hasNext())
	    		{
	    			String subnav = ital.next();	    			  
	    	        out.print("<tr valign='middle'><td><a href='"+nav_url_al.get(alCount)+"' class='navLink");
	    	        if (admin_subsection.equals(subnav)) out.print("Selected");
	    	        out.print("'><img src='/admin/images/s.gif' width=25 height=15 alt='' border='0'><img src='/admin/images/nav_drop_arrow.gif' width=25 height=15 alt='' border='0'><img src='/admin/images/s.gif' width=8 height=15 alt='' border='0'>"+subnav+"</a></td></tr>\n");
	    	        out.print("<tr bgColor='#FFFFFF'><td height='1'><img src='/admin/images/s.gif' width=1 height=1 alt='' border='0'></td></tr>\n");
	    	        if (admin_subsection.equals(subnav) && hasID) {
	    	        	out.print("<tr valign='middle'><td><a href='"+nav_url_al.get(alCount)+"?id="+id+"' class='navLinkSelected");
	    	        	out.print("'><img src='/admin/images/s.gif' width=45 height=15 alt='' border='0'><img src='/admin/images/nav_drop_arrow.gif' width=25 height=15 alt='' border='0'><img src='/admin/images/s.gif' width=8 height=15 alt='' border='0'>"+title+"</a></td></tr>\n");
	    	        	out.print("<tr bgColor='#FFFFFF'><td height='1'><img src='/admin/images/s.gif' width=1 height=1 alt='' border='0'></td></tr>\n");
	    	        }
	    	        alCount++;
	    	    }	    	  			
	    	  }
	      	 }
	    navCount++;	 
	}  
  }

  String navLinkClass = "";
  if (admin_section.equals("password")) {
	  navLinkClass = "Selected";
  }
%>
<tr valign="middle">
        <td><span class="navLink"><img src="/admin/images/s.gif" width=5 height=50 alt="" border="0"></span></td>
</tr>
<tr bgColor="#FFFFFF">
        <td height="1"><img src="/admin/images/s.gif" width=1 height=1 alt="" border="0"></td>
</tr>
<tr valign="middle">
        <td><a href="/admin/logout.jsp" id="logoutBtn" class="navLink"><img src="/admin/images/s.gif" width=5 height=15 alt="" border="0"><img src="/admin/images/nav_boxed_arrow.gif" width=25 height=15 alt="" border="0"><img src="/admin/images/s.gif" width=8 height=15 alt="" border="0">Logout</a></td>
</tr>
<tr bgColor="#FFFFFF">
        <td height="1"><img src="/admin/images/s.gif" width=1 height=1 alt="" border="0"></td>
</tr>
<tr valign="middle">
        <td><a href="/admin/password.jsp" class="navLink<%=navLinkClass%>"><img src="/admin/images/s.gif" width=5 height=15 alt="" border="0"><img src="/admin/images/nav_boxed_arrow.gif" width=25 height=15 alt="" border="0"><img src="/admin/images/s.gif" width=8 height=15 alt="" border="0">Change Password</a></td>
</tr>
<tr bgColor="#FFFFFF">
        <td height="1"><img src="/admin/images/s.gif" width=1 height=1 alt="" border="0"></td>
</tr>
<tr valign="middle">
	<td><a href="/recommend/index.jsp" class="navLink"><img src="/admin/images/s.gif" width=5 height=15 alt="" border="0"><img src="/admin/images/nav_boxed_arrow.gif" width=25 height=15 alt="" border="0"><img src="/admin/images/s.gif" width=8 height=15 alt="" border="0">Recommend Home</a></td>
</tr>
<tr bgColor="#FFFFFF">
	<td height="1"><img src="/admin/images/s.gif" width=1 height=1 alt="" border="0"></td>
</tr>
</table>

