package org.apache.jsp.inc;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class substance_005fabuse_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<form action=\"\" class=\"substanceAbuse\">\n");
      out.write("<h1>Substance Abuse Assessment</h1>\n");
      out.write("<p class=\"instructions\">In the table below check the appropriate box in the \"Abused Drug in the Past\" column for each drug the patient has previously used.</p>\n");
      out.write("<p class=\"instructions\">If the patient is currently abusing a drug place a check in the \"Currently Abuses Drug\" column.  Please enter additional relevant information about substance abuse in the progress notes.</p>\n");
      out.write("<div class=\"evalNav\">\n");
      out.write("\t<a class=\"prevEval\" href=\"#\"></a>\n");
      out.write("\t<span class=\"evalDate\"></span>\n");
      out.write("\t<a class=\"nextEval\" href=\"#\"></a>\n");
      out.write("</div>\n");
      out.write("<div class=\"scoreTitles\">\n");
      out.write("\t<ul>\n");
      out.write("\t\t<li>Abused Drug in the Past</li>\n");
      out.write("\t\t<li>Currently Abuses Drug</li>\n");
      out.write("\t</ul>\n");
      out.write("</div>\n");
      out.write("<ul class=\"substanceAbuseList\">\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label>Nicotine</label>\n");
      out.write("\t\t<ul class=\"checkboxlist\">\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"past\" /></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"current\" /></li>\n");
      out.write("\t\t</ul>\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label>Stimulants</label>\n");
      out.write("\t\t<ul class=\"checkboxlist\">\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"past\" /></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"current\" /></li>\n");
      out.write("\t\t</ul>\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label>Inhalents</label>\n");
      out.write("\t\t<ul class=\"checkboxlist\">\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"past\" /></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"current\" /></li>\n");
      out.write("\t\t</ul>\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label>Alcohol</label>\n");
      out.write("\t\t<ul class=\"checkboxlist\">\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"past\" /></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"current\" /></li>\n");
      out.write("\t\t</ul>\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label>Opioids</label>\n");
      out.write("\t\t<ul class=\"checkboxlist\">\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"past\" /></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"current\" /></li>\n");
      out.write("\t\t</ul>\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label>Cannabis</label>\n");
      out.write("\t\t<ul class=\"checkboxlist\">\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"past\" /></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"current\" /></li>\n");
      out.write("\t\t</ul>\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label>Cocaine</label>\n");
      out.write("\t\t<ul class=\"checkboxlist\">\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"past\" /></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"current\" /></li>\n");
      out.write("\t\t</ul>\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label>Hallucinogen</label>\n");
      out.write("\t\t<ul class=\"checkboxlist\">\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"past\" /></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"current\" /></li>\n");
      out.write("\t\t</ul>\t\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label>Other <em>Please enter detail in progress note.</em></label>\n");
      out.write("\t\t<ul class=\"checkboxlist\">\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"past\" /></li>\n");
      out.write("\t\t\t<li><input type=\"checkbox\" value=\"current\" /></li>\n");
      out.write("\t\t</ul>\t\n");
      out.write("\t</li>\n");
      out.write("</ul>\t\n");
      out.write("<div class=\"errorMsg\">Please select one or more options.</div>\n");
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
