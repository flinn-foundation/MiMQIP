<%@page import="flinn.util.AdminNavigation"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>

<%
String title = "";
String crumb = "";
String id = "";
String pre_final_element = "";
String post_final_element = "";
String admin_section = "";
String admin_subsection = "";
String sub_section = "";
Boolean hasID = false;

out.print("<p class=\"pageTitle\">");

if (request.getParameter("admin_section") != null){
	admin_section = request.getParameter("admin_section");
}

if (request.getParameter("admin_subsection") != null){
	admin_subsection = request.getParameter("admin_subsection");
}

if (request.getParameter("sub_section") != null){
	sub_section = request.getParameter("sub_section");
}

if (request.getParameter("id") != null){
	id = request.getParameter("id");
	hasID = true;
}
  
if (request.getParameter("reason") != null) {
  post_final_element = " > <span class='pageHeader'>"+request.getParameter("reason")+"</span>";
} else {
  pre_final_element = "<span class='pageHeader'>";
  post_final_element = "</span>";
}

if (admin_section.equals("home")) {
  crumb = pre_final_element+"administration tool"+post_final_element;
} else if (admin_section.equals("password")) {
  if (!sub_section.equals("")) {
    crumb = "change password > ";
    crumb += pre_final_element+post_final_element;
    //crumb += pre_final_element+get_userlogin(sub_section)+post_final_element;
  } else {
    crumb = pre_final_element+"change password"+post_final_element;
  }
} else { 
	HashMap<String, ArrayList<String>> navigation = AdminNavigation.getNavigation();
	HashMap<String, ArrayList<String>> navigation_url = AdminNavigation.getNavigationURL();
	java.util.Iterator<String> it = navigation.keySet().iterator();
	int navCount = 0;

	while(it.hasNext())
	{
		String navkey = (String)it.next();
		ArrayList<String> nav_section_al = (ArrayList<String>)navigation.get(navkey);
      if (nav_section_al.get(0).equals("id")) {
        if (hasID) {
		  ArrayList<String> nav_url_al = (ArrayList<String>)navigation_url.get(navkey);
          crumb = "<a href='"+nav_url_al.get(0)+"' class='pageTitle'>"+admin_section+"</a> > ";
          crumb += pre_final_element+title+post_final_element;
        } else {
          crumb = pre_final_element+admin_section+post_final_element;
        }
      }
	
		else {
		int alCount = 0;
		ArrayList<String> nav_url_al = (ArrayList<String>)navigation_url.get(navkey);        
		java.util.Iterator<String> ital = nav_section_al.iterator();		
		while(ital.hasNext())
		{
		  String subnav = ital.next();
		  if (admin_subsection.equals(subnav)) {
            if (hasID) {
              crumb = "<a href='"+nav_url_al.get(0)+"' class='pageTitle'>"+admin_section+"</a> > ";
              crumb += "<a href='"+nav_url_al.get(alCount)+"' class='pageTitle'>"+subnav+"</a> > ";
              crumb += pre_final_element+title+post_final_element;
            } else {
              crumb = pre_final_element+admin_section+post_final_element;
            }
          }
		  alCount++;
		}	
		navCount++;
		}
	}
      
}

if (crumb.equals("")) {
  crumb = pre_final_element+"Error: Unknown page"+post_final_element;
}

out.print(crumb);
out.print("</span></p>");
%>
