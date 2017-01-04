package org.apache.jsp.inc;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class mental_005fstatus_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<form action=\"\" class=\"mentalStatus\">\n");
      out.write("<h1>Mental Status</h1>\n");
      out.write("<div class=\"buttons top\">\n");
      out.write("\t<input type=\"submit\" class=\"orangeBtn\" value=\"Save\" />\n");
      out.write("</div>\n");
      out.write("<div class=\"errorMsg\">Please enter text in one or more of the fields.</div>\n");
      out.write("<div class=\"evalNav\">\n");
      out.write("\t<a class=\"prevEval\" href=\"#\"></a>\n");
      out.write("\t<span class=\"evalDate\"></span>\n");
      out.write("\t<a class=\"nextEval\" href=\"#\"></a>\n");
      out.write("</div>\n");
      out.write("<ul class=\"questionList\">\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"appearance\">Appearance and General Behavior</label>\n");
      out.write("\t\t<textarea id=\"appearance\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"motorActivity\">Motor Activity</label>\n");
      out.write("\t\t<textarea id=\"motorActivity\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"speech\">Speech</label>\n");
      out.write("\t\t<textarea id=\"speech\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"moodAffect\">Mood and Affect</label>\n");
      out.write("\t\t<textarea id=\"moodAffect\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"thoughtProcess\">Thought Process</label>\n");
      out.write("\t\t<textarea id=\"thoughtProcess\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"thoughtContent\">Thought Content</label>\n");
      out.write("\t\t<textarea id=\"thoughtContent\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"perceptualDisturbances\">Perceptual Disturbances</label>\n");
      out.write("\t\t<textarea id=\"perceptualDisturbances\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"sensoriumCognition\">Sensorium and Cognition</label>\n");
      out.write("\t\t<textarea id=\"sensoriumCognition\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"insight\">Insight</label>\n");
      out.write("\t\t<textarea id=\"insight\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"judgement\">Judgement</label>\n");
      out.write("\t\t<textarea id=\"judgement\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("</ul>\n");
      out.write("<div class=\"errorMsg\">Please enter text in one or more of the fields.</div>\n");
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
