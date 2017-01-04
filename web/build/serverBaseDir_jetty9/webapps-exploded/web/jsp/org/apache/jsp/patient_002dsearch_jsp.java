package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import flinn.beans.response.ResponseSessionContainerBean;

public final class patient_002dsearch_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


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


  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");


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
  

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n");
      out.write("<head>\n");
      out.write("\t<title>Flinn Foundation: Patient Search</title>\n");
      out.write("\t<link type=\"text/css\" rel=\"Stylesheet\" href=\"css/global.css\" />\n");
      out.write("\t<link type=\"text/css\" rel=\"Stylesheet\" href=\"css/patientSearch.css\" />\n");
      out.write("\t<link type=\"text/css\" rel=\"Stylesheet\" href=\"css/colorbox.css\" />\n");
      out.write("\t<!--<script type=\"text/javascript\" src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js\"></script>-->\n");
      out.write("\t<script type=\"text/javascript\" src=\"js/jquery-1.6.min.js\"></script>\n");
      out.write("\t<script type=\"text/javascript\" src=\"js/globalUtils.js\"></script>\t\n");
      out.write("\t<script type=\"text/javascript\" src=\"js/patientSearch.js\"></script>\n");
      out.write("</head>\n");
      out.write("<!--[if IE 7 ]><body id=\"patientSelect\" class=\"ie7\"><![endif]-->\n");
      out.write("<!--[if IE 8 ]><body id=\"patientSelect\" class=\"ie8\"><![endif]-->\n");
      out.write("<!--[if IE 9 ]><body id=\"patientSelect\" class=\"ie9\"><![endif]-->\n");
      out.write("<!--[if !IE]><!--><body id=\"patientSelect\"><!--<![endif]-->\n");
      out.write("<div id=\"container\">\n");
      out.write("\t<div id=\"utilityBarWrapper\" class=\"tall\">\n");
      out.write("\t\t<div id=\"utilityBar\">\n");
      out.write("\t\t\t<h1></h1>\n");
      out.write("\t\t\t<span class=\"welcome\"></span>\t\n");
      out.write("\t\t\t<a href=\"#\" id=\"logoutBtn\" class=\"roundedBtn\">Logout</a>\n");
      out.write("\t\t\t<div class=\"buttons\">\n");
      out.write("\t\t\t\t<a href=\"/patient-search.jsp\" id=\"patientSearchBtn\" class=\"roundedBtn\">Patient Search</a>\n");
      out.write("\t\t\t\t<a href=\"/admin/\" id=\"adminBtn\" class=\"roundedBtn\">Administration</a>\n");
      out.write("\t\t\t</div>\n");
 if (isDrFirstAccessible == true) { 
      out.write("\t\t\t\n");
      out.write("\t\t\t<div class=\"utilityLinks\">\n");
      out.write("\t\t\t\t<a href=\"#\" id=\"drFirstMessageBtn\">DrFirst Messages</a>\t\n");
      out.write("\t\t\t\t<a href=\"#\" id=\"drFirstReportBtn\">DrFirst Reports</a>\t\n");
      out.write("\t\t\t</div>\n");
 } 
      out.write("\t\t\t\t\n");
      out.write("\t\t</div>\n");
      out.write("\t</div>\n");
      out.write("\t<div id=\"patientSelectWrapper\">\n");
      out.write("\t\t<div id=\"topHeadingWrapper\">\n");
      out.write("\t\t\t<div id=\"topHeading\">\n");
      out.write("\t\t\t\t<h1>Patient Search</h1>\n");
      out.write("\t\t\t\t<a href=\"new-patient.jsp\" class=\"orangeBtn\" id=\"enterNewPatient\">Enter New Patient</a>\t\t\n");
      out.write("\t\t\t</div>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<div id=\"searchFieldsWrapper\">\n");
      out.write("\t\t\t<form id=\"patientSearchForm\" action=\"\">\n");
      out.write("\t\t\t<div id=\"searchFields\" class=\"patientForm\">\n");
      out.write("\t\t\t\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t\t\t\t<label for=\"fName\">First Name:</label>\n");
      out.write("\t\t\t\t\t<input type=\"text\" class=\"greenInput\" id=\"fName\" />\n");
      out.write("\t\t\t\t</div>\n");
      out.write("\t\t\t\t<div class=\"fieldWrapper floatRight\">\n");
      out.write("\t\t\t\t\t<label for=\"sex\">Sex:</label>\n");
      out.write("\t\t\t\t\t<div class=\"dropdown\" id=\"sexDropdown\">\n");
      out.write("\t\t\t\t\t\t<a href=\"#\" class=\"pullDown\">Select</a>\n");
      out.write("\t\t\t\t\t\t<ul>\n");
      out.write("\t\t\t\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">Select</a></li>\n");
      out.write("\t\t\t\t\t\t\t<li><a href=\"#M\">Male</a></li>\n");
      out.write("\t\t\t\t\t\t\t<li class=\"odd\"><a href=\"#F\">Female</a></li>\n");
      out.write("\t\t\t\t\t\t</ul>\n");
      out.write("\t\t\t\t\t</div>\n");
      out.write("\t\t\t\t</div>\n");
      out.write("\t\t\t\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t\t\t\t<label for=\"lName\">Last Name:</label>\n");
      out.write("\t\t\t\t\t<input type=\"text\" class=\"greenInput\" id=\"lName\" />\n");
      out.write("\t\t\t\t</div>\t\t\t\n");
      out.write("\t\t\t\t<div class=\"fieldWrapper floatRight\">\n");
      out.write("\t\t\t\t\t<label for=\"dob\">Date of Birth:</label>\n");
      out.write("\t\t\t\t\t<div class=\"dropdown dob\" id=\"mmDropdown\">\n");
      out.write("\t\t\t\t\t\t<a href=\"#\" class=\"pullDown\">MM</a>\n");
      out.write("\t\t\t\t\t\t<ul>\n");
      out.write("\t\t\t\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">MM</a></li>\n");
      out.write("\t\t\t\t\t\t\t");
out.print(computeLi(1,13,false));
      out.write("\n");
      out.write("\t\t\t\t\t\t</ul>\n");
      out.write("\t\t\t\t\t</div>\n");
      out.write("\t\t\t\t\t<div class=\"dropdown dob\" id=\"ddDropdown\">\n");
      out.write("\t\t\t\t\t\t<a href=\"#\" class=\"pullDown\">DD</a>\n");
      out.write("\t\t\t\t\t\t<ul>\n");
      out.write("\t\t\t\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">DD</a></li>\n");
      out.write("\t\t\t\t\t\t\t");
out.print(computeLi(1,32,false));
      out.write(" \n");
      out.write("\t\t\t\t\t\t</ul>\n");
      out.write("\t\t\t\t\t</div>\n");
      out.write("\t\t\t\t\t<div class=\"dropdown dob\" id=\"yyyyDropdown\">\n");
      out.write("\t\t\t\t\t\t<a href=\"#\" class=\"pullDown\">YYYY</a>\n");
      out.write("\t\t\t\t\t\t<ul>\n");
      out.write("\t\t\t\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">YYYY</a></li>\n");
      out.write("\t\t\t\t\t\t\t");
 						
							GregorianCalendar cal = new GregorianCalendar();
							Date date1 = new Date();
							cal.setTime(date1);
							int year = cal.get(GregorianCalendar.YEAR);
							out.print(computeLi(year-100,year,true));
							
      out.write("\n");
      out.write("\t\t\t\t\t\t</ul>\n");
      out.write("\t\t\t\t\t</div>\n");
      out.write("\t\t\t\t</div>\n");
      out.write("\t\t\t\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t\t\t\t<label for=\"patientId\">ID #:</label>\n");
      out.write("\t\t\t\t\t<input type=\"text\" class=\"greenInput\" id=\"patientId\" />\n");
      out.write("\t\t\t\t</div>\t\n");
      out.write("\t\t\t\t<input type=\"submit\" value=\"Search\" class=\"orangeBtn\" id=\"loginBtn\" />\n");
      out.write("\t\t\t\t<div class=\"clear\"></div>\n");
      out.write("\t\t\t</div>\n");
      out.write("\t\t\t</form>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<div id=\"patientTable\">\n");
      out.write("\t\t\t<h2>Search Results:</h2>\n");
      out.write("\t\t\t<div class=\"pagination\" id=\"topPagination\"></div>\n");
      out.write("\t\t\t<table>\n");
      out.write("\t\t\t\t<thead>\n");
      out.write("\t\t\t\t\t<tr>\n");
      out.write("\t\t\t\t\t\t<th class=\"lName\">Last Name</th>\n");
      out.write("\t\t\t\t\t\t<th class=\"fName\">First Name</th>\n");
      out.write("\t\t\t\t\t\t<th class=\"pID\">ID #</th>\n");
      out.write("\t\t\t\t\t\t<th class=\"dob\">Date Of Birth</th>\n");
      out.write("\t\t\t\t\t\t<th class=\"sex\">Sex</th>\n");
      out.write("\t\t\t\t\t</tr>\n");
      out.write("\t\t\t\t</thead>\n");
      out.write("\t\t\t\t<tbody></tbody>\n");
      out.write("\t\t\t</table>\n");
      out.write("\t\t\t<div class=\"pagination\" id=\"bottomPagination\"></div>\n");
      out.write("\t\t\t<div id=\"noSearchResults\">No patient records match your search. Please try again.</div>\n");
      out.write("\t\t</div>\n");
      out.write("\t</div>\n");
      out.write("</div>\n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
