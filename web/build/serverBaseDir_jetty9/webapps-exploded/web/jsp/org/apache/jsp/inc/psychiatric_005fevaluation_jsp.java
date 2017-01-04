package org.apache.jsp.inc;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class psychiatric_005fevaluation_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<form action=\"\" class=\"psychiatricEval\">\n");
      out.write("<h1>Psychiatric Evaluation</h1>\n");
      out.write("<div class=\"buttons top\">\n");
      out.write("\t<input type=\"submit\" class=\"orangeBtn\" value=\"Save\" />\n");
      out.write("</div>\n");
      out.write("<div class=\"errorMsg\">Please enter text in one or more of the fields.</div>\n");
      out.write("<div class=\"evalNav\">\n");
      out.write("\t<a class=\"prevEval\" href=\"#\"></a>\n");
      out.write("\t<span class=\"evalDate\"></span>\n");
      out.write("\t<a class=\"nextEval\" href=\"#\"></a>\n");
      out.write("</div>\n");
      out.write("<ul class=\"questionList psychiatricEvaluation\">\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"evaluationsReasons\">Reasons for Evaluation</label>\n");
      out.write("\t\t<textarea id=\"evaluationsReasons\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"presentIllnessHistory\">History of Present Illness</label>\n");
      out.write("\t\t<textarea id=\"presentIllnessHistory\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"pastHistory\">Past Psychiatric History</label>\n");
      out.write("\t\t<textarea id=\"pastHistory\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"substanceAbuseHistory\">History of Alcohol and Other Substance Abuse</label>\n");
      out.write("\t\t<textarea id=\"substanceAbuseHistory\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"generalMedicalHistory\">General Medical history</label>\n");
      out.write("\t\t<textarea id=\"generalMedicalHistory\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"developmentalHistory\">Developmental, Psychosocial and Sociocultural History</label>\n");
      out.write("\t\t<textarea id=\"developmentalHistory\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"occupationalHistory\">Occupational and Military History</label>\n");
      out.write("\t\t<textarea id=\"occupationalHistory\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"legalHistory\">Legal History</label>\n");
      out.write("\t\t<textarea id=\"legalHistory\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"familyHistory\">Family History</label>\n");
      out.write("\t\t<textarea id=\"familyHistory\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"systemsReview\">Review of Systems</label>\n");
      out.write("\t\t<textarea id=\"systemsReview\">Enter text...</textarea>\n");
      out.write("\t\t<span class=\"inputVal\"></span>\n");
      out.write("\t</li>\n");
      out.write("\t<li class=\"fieldWrapper symptom\">\n");
      out.write("\t\t<label for=\"functionalAssessment\">Functional Assessment</label>\n");
      out.write("\t\t<textarea id=\"functionalAssessment\">Enter text...</textarea>\n");
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
