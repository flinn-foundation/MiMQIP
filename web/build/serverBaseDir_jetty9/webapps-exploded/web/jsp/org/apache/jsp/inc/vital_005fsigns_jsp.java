package org.apache.jsp.inc;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class vital_005fsigns_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

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

      out.write("<form action=\"\" class=\"patientVitalSigns\">\n");
      out.write("<h1>Patient Vital Signs</h1>\n");
      out.write("<div class=\"evalNav\">\n");
      out.write("\t<a class=\"prevEval\" href=\"#\"></a>\n");
      out.write("\t<span class=\"evalDate\"></span>\n");
      out.write("\t<a class=\"nextEval\" href=\"#\"></a>\n");
      out.write("</div>\n");
      out.write("<ul class=\"vitalSigns\">\n");
      out.write("\t<li class=\"fieldWrapper symptom\" id=\"height\">\n");
      out.write("\t\t<label for=\"appearance\">Height:</label>\n");
      out.write("\t\t<div class=\"dropdown\" id=\"heightFt\">\n");
      out.write("\t\t\t<a href=\"#\" class=\"pullDown\">Select</a>\n");
      out.write("\t\t\t<ul>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#\" class=\"selected\">Select</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#1\">1</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#2\">2</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#3\">3</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#4\">4</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#5\">5</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#6\">6</a></li>\n");
      out.write("\t\t\t</ul>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t\t<span class=\"labelText marginRight\">ft</span>\n");
      out.write("\t\t<div class=\"dropdown\" id=\"heightIn\">\n");
      out.write("\t\t\t<a href=\"#0\" class=\"pullDown\">0</a>\n");
      out.write("\t\t\t<ul>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#0\" class=\"selected\">0</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#1\">1</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#2\">2</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#3\">3</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#4\">4</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#5\">5</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#6\">6</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#7\">7</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#8\">8</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#9\">9</a></li>\n");
      out.write("\t\t\t\t<li class=\"odd\"><a href=\"#10\">10</a></li>\n");
      out.write("\t\t\t\t<li><a href=\"#11\">11</a></li>\n");
      out.write("\t\t\t</ul>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<span class=\"inputVal heightIn\"></span>\n");
      out.write("\t\t<span class=\"labelText\">in</span>\t\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\" id=\"weight\">\n");
      out.write("\t\t<label for=\"motorActivity\">Weight:</label>\n");
      out.write("\t\t<input class=\"greenInput\" type=\"text\" maxlength=\"3\" />\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t\t<span class=\"labelText\">lbs</span>\n");
      out.write("\t\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\"id=\"bmi\">\n");
      out.write("\t\t<label for=\"motorActivity\">BMI:</label>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\" id=\"blood_pressure\">\n");
      out.write("\t\t<label for=\"motorActivity\">Blood Pressure:</label>\n");
      out.write("\t\t<input class=\"greenInput\" type=\"text\" maxlength=\"3\" /><span class=\"inputVal\"></span> \n");
      out.write("\t\t<span class=\"labelText\">/</span> \n");
      out.write("\t\t<input class=\"greenInput\" type=\"text\" maxlength=\"3\" /><span class=\"inputVal\"></span>\n");
      out.write("\t\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\" id=\"heart_rate\">\n");
      out.write("\t\t<label for=\"motorActivity\">Heart Rate:</label>\n");
      out.write("\t\t<input class=\"greenInput\" type=\"text\" maxlength=\"3\" />\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t\t<span class=\"labelText\">bpm</span> \t\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"clear\"></li>\n");
      out.write("</ul>\n");
      out.write("<div class=\"errorMsg\" style=\"display:none\">Please enter data in one or more of the fields.</div>\n");
      out.write("<div class=\"evaluatedBy\">Evaluated By: <span></span></div>\n");
      out.write("<div class=\"buttons bottom\">\n");
      out.write("\t<input type=\"submit\" class=\"orangeBtn\" value=\"Save\" />\n");
      out.write("</div>\n");
      out.write("</form>");
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
