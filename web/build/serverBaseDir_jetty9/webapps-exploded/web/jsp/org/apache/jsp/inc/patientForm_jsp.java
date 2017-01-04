package org.apache.jsp.inc;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;

public final class patientForm_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write('\n');
      out.write("\n");
      out.write("<div class=\"floatLeft\">\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"fNameInput\">First Name:</label>\n");
      out.write("\t\t<input type=\"text\" class=\"greenInput\" id=\"fNameInput\" />\n");
      out.write("\t</div>\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"lNameInput\">Last Name:</label>\n");
      out.write("\t\t<input type=\"text\" class=\"greenInput\" id=\"lNameInput\" />\n");
      out.write("\t</div>\n");
      out.write("\t<div class=\"fieldWrapper\" id=\"raceField\">\n");
      out.write("\t\t<label for=\"raceWhite\">Race:</label>\n");
      out.write("\t\t<ul id=\"raceList\" class=\"inputList\">\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"White\" id=\"raceWhite\" /> <label for=\"raceWhite\">White</label></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"African-American, Black\" id=\"raceBlack\" /> <label for=\"raceBlack\">African-American, Black</label></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"American Indian\" id=\"raceIndian\" /> <label for=\"raceIndian\">American Indian</label></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"Asian (Indian, Chinese, Filipino, Japanese, Korean, or other)\" id=\"raceAsian\" /> <label for=\"raceAsian\">Asian <br /><span>(Indian, Chinese, Filipino, Japanese, Korean, or other)</span></label></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"Pacific Islander (Hawaiian, Samoan, or other)\" id=\"racePacific\" /> <label for=\"racePacific\">Pacific Islander <span>(Hawaiian, Samoan, or other)</span></label></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"No response\" id=\"raceNone\" /> <label for=\"raceNone\">No response</label></li>\n");
      out.write("\t\t</ul>\n");
      out.write("\t</div>\t\n");
      out.write("\t<div class=\"fieldWrapper\" id=\"ethnicityField\">\n");
      out.write("\t\t<label for=\"raceDropdown\">Ethnicity:</label>\n");
      out.write("\t\t<div class=\"floatLeft\">\n");
      out.write("\t\t\t<p>Hispanic, Latino or Spanish? </p>\n");
      out.write("\t\t\t<ul id=\"ethnicityList\" class=\"inputList\">\t\t\n");
      out.write("\t\t\t\t<li><input type=\"radio\" name=\"hispanic\" value=\"yes\" id=\"hispanicYes\" /> <label for=\"hispanicYes\">Yes</label></li>\n");
      out.write("\t\t\t\t<li><input type=\"radio\" name=\"hispanic\" value=\"no\" id=\"hispanicNo\" /> <label for=\"hispanicNo\">No</label></li>\n");
      out.write("\t\t\t\t<li><input type=\"radio\" name=\"hispanic\" value=\"no response\" id=\"hispanicNoResponse\" /> <label for=\"hispanicNoResponse\">No response</label></li>\n");
      out.write("\t\t\t</ul>\n");
      out.write("\t\t</div>\n");
      out.write("\t</div>\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"pcpnInput\" class=\"noPadding\">Primary Care Physician Name:</label>\n");
      out.write("\t\t<input type=\"text\" class=\"greenInput\" id=\"pcpnInput\" />\n");
      out.write("\t</div>\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"pcpeInput\" class=\"noPadding\">Primary Care Physician Email:</label>\n");
      out.write("\t\t<input type=\"text\" class=\"greenInput\" id=\"pcpeInput\" />\n");
      out.write("\t</div>\n");
      out.write("</div>\n");
      out.write("<div class=\"floatLeft right\">\t\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"patientIdInput\">ID #:</label>\n");
      out.write("\t\t<input type=\"text\" class=\"greenInput\" id=\"patientIdInput\" />\n");
      out.write("\t</div>\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"zipCodeInput\">ZIP Code:</label>\n");
      out.write("\t\t<input type=\"text\" maxlength=\"6\" class=\"greenInput\" id=\"zipCodeInput\" />\n");
      out.write("\t</div>\t\t\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"employmentDropdown\" class=\"noPadding\">Employment Status:</label>\n");
      out.write("\t\t<div class=\"dropdown\" id=\"employmentDropdown\">\n");
      out.write("\t\t\t<a href=\"#\" class=\"pullDown\">Select</a>\n");
      out.write("\t\t\t<ul>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">Select</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#Unemployed\">Unemployed</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#Full time salaried\">Full time salaried</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#Part time salaried\">Part time salaried</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#Full time non-salaried\">Full time non-salaried</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#Part time non-salaried\">Part time non-salaried</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#Self employed\">Self employed</a></li>\n");
      out.write("\t\t\t</ul>\n");
      out.write("\t\t</div>\n");
      out.write("\t</div>\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"sexDropdown\">Sex:</label>\n");
      out.write("\t\t<div class=\"dropdown\" id=\"sexDropdown\">\n");
      out.write("\t\t\t<a href=\"#\" class=\"pullDown\">Select</a>\n");
      out.write("\t\t\t<ul>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">Select</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#M\">Male</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#F\">Female</a></li>\n");
      out.write("\t\t\t</ul>\n");
      out.write("\t\t</div>\n");
      out.write("\t</div>\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"mmDropdown\">Date of Birth:</label>\n");
      out.write("\t\t<div class=\"dropdown dob\" id=\"mmDropdown\">\n");
      out.write("\t\t\t<a href=\"#\" class=\"pullDown\">MM</a>\n");
      out.write("\t\t\t<ul>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">MM</a></li>\n");
      out.write("\t\t\t\t");
out.print(computeLi(1,13,false));
      out.write("\n");
      out.write("\t\t\t</ul>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<div class=\"dropdown dob\" id=\"ddDropdown\">\n");
      out.write("\t\t\t<a href=\"#\" class=\"pullDown\">DD</a>\n");
      out.write("\t\t\t<ul>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">DD</a></li>\n");
      out.write("\t\t\t\t");
out.print(computeLi(1,32,false));
      out.write(" \n");
      out.write("\t\t\t</ul>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<div class=\"dropdown dob\" id=\"yyyyDropdown\">\n");
      out.write("\t\t\t<a href=\"#\" class=\"pullDown\">YYYY</a>\n");
      out.write("\t\t\t<ul>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">YYYY</a></li>\n");
      out.write("\t\t\t\t");
 						
				GregorianCalendar cal = new GregorianCalendar();
				Date date1 = new Date();
				cal.setTime(date1);
				int year = cal.get(GregorianCalendar.YEAR);
				out.print(computeLi(year-100,year,true));
				
      out.write("\n");
      out.write("\t\t\t</ul>\n");
      out.write("\t\t</div>\n");
      out.write("\t</div>\n");
      out.write("\n");
      out.write("\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"maritalDropdown\">Marital Status:</label>\n");
      out.write("\t\t<div class=\"dropdown\" id=\"maritalDropdown\">\n");
      out.write("\t\t\t<a href=\"#\" class=\"pullDown\">Select</a>\n");
      out.write("\t\t\t<ul>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">Select</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#Single\">Single</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#Married\">Married</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#Separated\">Separated</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#Divorced\">Divorced</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#Widowed\">Widowed</a></li>\n");
      out.write("\t\t\t</ul>\n");
      out.write("\t\t</div>\n");
      out.write("\t</div>\n");
      out.write("\n");
      out.write("\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t<label for=\"livingDropdown\" class=\"noPadding\">Living Arrangement:</label>\n");
      out.write("\t\t<div class=\"dropdown\" id=\"livingDropdown\">\n");
      out.write("\t\t\t<a href=\"#\" class=\"pullDown\">Select</a>\n");
      out.write("\t\t\t<ul>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">Select</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#Alone\">Alone</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#With roommates\">With roommates</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#With spouse\">With spouse</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#With spouse and children\">With spouse and children</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#With parents\">With parents</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#Group home\">Group home</a></li>\n");
      out.write("\t\t\t</ul>\n");
      out.write("\t\t</div>\n");
      out.write("\t</div>\t\n");
      out.write("</div>");
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
