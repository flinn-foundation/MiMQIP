package org.apache.jsp.inc;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class global_005frating_005fscale_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<form action=\"\" class=\"grsEval\">\n");
      out.write("<h1>Global Rating Scales</h1>\n");
      out.write("<p class=\"instructions\">Please note: MiMQIP requires a score on the Overall Side Effect Severity and Overall Symptom Severity scales to make a clinical recommendation.</p>\n");
      out.write("<div class=\"evalNav\">\n");
      out.write("\t<a class=\"prevEval\" href=\"#\"></a>\n");
      out.write("\t<span class=\"evalDate\"></span>\n");
      out.write("\t<a class=\"nextEval\" href=\"#\"></a>\n");
      out.write("</div>\n");
      out.write("<ul>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<div class=\"errorMsg\">Please select a response.</div>\n");
      out.write("\t\t<h2>Overall Side Effect Severity</h2>\n");
      out.write("\t\t<p>Please rate the severity of the patient's side effects over the last week on the scale below from<br /><span class=\"blue\">0 = No side effects</span> to <span class=\"blue\">10 = Very severe side effects</span></p>\n");
      out.write("\t\t<ul class=\"scale\">\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses0\">0</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses0\" value=\"0\" />\n");
      out.write("\t\t\t\t<span class=\"rating\">None</span>\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses1\">1</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses1\" value=\"1\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses2\">2</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses2\" value=\"2\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses3\">3</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses3\" value=\"3\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses4\">4</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses4\" value=\"4\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses5\">5</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses5\" value=\"5\" />\n");
      out.write("\t\t\t\t<span class=\"rating\">Moderate</span>\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses6\">6</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses6\" value=\"6\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses7\">7</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses7\" value=\"7\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses8\">8</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses8\" value=\"8\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses9\">9</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses9\" value=\"9\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oses10\">10</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oses\" id=\"oses10\" value=\"10\" />\n");
      out.write("\t\t\t\t<span class=\"rating\">Very Severe</span>\n");
      out.write("\t\t\t</li>\t\t\t\n");
      out.write("\t\t</ul>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<div class=\"errorMsg\">Please select a response.</div>\n");
      out.write("\t\t<h2>Overall Symptom Severity</h2>\n");
      out.write("\t\t<p>Please rate the severity of the patient's symptoms over the last week on the scale below from<br /><span class=\"blue\">0 = No symptoms</span> to <span class=\"blue\">10 = Very severe symptoms</span></p>\n");
      out.write("\t\t<ul class=\"scale\">\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss0\">0</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss0\" value=\"0\" />\n");
      out.write("\t\t\t\t<span class=\"rating\">None</span>\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss1\">1</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss1\" value=\"1\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss2\">2</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss2\" value=\"2\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss3\">3</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss3\" value=\"3\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss4\">4</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss4\" value=\"4\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss5\">5</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss5\" value=\"5\" />\n");
      out.write("\t\t\t\t<span class=\"rating\">Moderate</span>\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss6\">6</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss6\" value=\"6\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss7\">7</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss7\" value=\"7\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss8\">8</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss8\" value=\"8\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss9\">9</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss9\" value=\"9\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"oss10\">10</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"oss\" id=\"oss10\" value=\"10\" />\n");
      out.write("\t\t\t\t<span class=\"rating\">Very Severe</span>\n");
      out.write("\t\t\t</li>\t\t\t\n");
      out.write("\t\t</ul>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<div class=\"errorMsg\">Please select a response.</div>\n");
      out.write("\t\t<h2>Overall Functional Impairment</h2><span class=\"unableToAssess\"><input type=\"checkbox\" id=\"unableToAssess\" /> Unable to Assess</span>\n");
      out.write("\t\t<p>Please rate the severity of the patient's overall functional impariment over the last week on the scale below from <span class=\"blue\">0 = No functional impairment</span> to <span class=\"blue\">10 = Very severe functional impairment</span></p>\n");
      out.write("\t\t<ul class=\"scale\">\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi0\">0</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi0\" value=\"0\" />\n");
      out.write("\t\t\t\t<span class=\"rating\">None</span>\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi1\">1</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi1\" value=\"1\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi2\">2</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi2\" value=\"2\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi3\">3</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi3\" value=\"3\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi4\">4</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi4\" value=\"4\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi5\">5</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi5\" value=\"5\" />\n");
      out.write("\t\t\t\t<span class=\"rating\">Moderate</span>\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi6\">6</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi6\" value=\"6\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi7\">7</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi7\" value=\"7\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi8\">8</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi8\" value=\"8\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi9\">9</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi9\" value=\"9\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"ofi10\">10</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"ofi\" id=\"ofi10\" value=\"10\" />\n");
      out.write("\t\t\t\t<span class=\"rating\">Very Severe</span>\n");
      out.write("\t\t\t</li>\t\t\t\n");
      out.write("\t\t</ul>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<div class=\"errorMsg\">Please select a response.</div>\n");
      out.write("\t\t<h2>Patient Medication Compliance</h2>\n");
      out.write("\t\t<p>Do you think your patient is taking the medications as prescribed?</p>\n");
      out.write("\t\t<ul class=\"scale\">\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"pmc1\">Yes</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"pmc\" id=\"pmc1\" value=\"1\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"pmc0\">No</label> \n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"pmc\" id=\"pmc0\" value=\"0\" />\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t\t<li>\n");
      out.write("\t\t\t\t<label for=\"pmc-1\">Unknown</label>\n");
      out.write("\t\t\t\t<input type=\"radio\" name=\"pmc\" id=\"pmc-1\" value=\"-1\" />\n");
      out.write("\t\t\t\t<span class=\"rating\">I don't know</span>\n");
      out.write("\t\t\t</li>\n");
      out.write("\t\t</ul>\n");
      out.write("\t</li>\n");
      out.write("</ul>\n");
      out.write("<div class=\"evaluatedBy\">Evaluated By: <span></span></div>\n");
      out.write("<div class=\"buttons bottom\">\n");
      out.write("\t<input type=\"submit\" class=\"orangeBtn\" value=\"Save\" />\n");
      out.write("</div>\n");
      out.write("</form>\n");
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
