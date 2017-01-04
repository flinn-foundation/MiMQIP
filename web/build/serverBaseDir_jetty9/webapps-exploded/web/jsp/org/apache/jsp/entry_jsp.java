package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class entry_jsp extends org.apache.jasper.runtime.HttpJspBase
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


/* */
java.lang.String facidString = request.getParameter("facilityid");
// For this particular page, default to the MiniEHR admin facility.
// TODO: custom entry page URLs for facilities that set these IDs.
int facilityid = 1;
try {
        if (facidString != null)
                facilityid = Integer.decode(facidString).intValue();
} catch (NumberFormatException e) {
	// For this particular page, default to the MiniEHR admin facility.
	// TODO: custom entry page URLs for facilities that set these IDs.
        facilityid = 1;
}

      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n");
      out.write("<head>\n");
      out.write("\t<title>Flinn Foundation: Login</title>\n");
      out.write("\t<link type=\"text/css\" rel=\"Stylesheet\" href=\"/css/global.css\" />\n");
      out.write("\t<!--<script type=\"text/javascript\" src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js\"></script>-->\n");
      out.write("\t<script type=\"text/javascript\" src=\"/js/jquery-1.6.min.js\"></script>\n");
      out.write("\t<script type=\"text/javascript\" src=\"/js/globalUtils.js\"></script>\n");
      out.write("\t<script type=\"text/javascript\">\n");
      out.write("\t\t$(document).ready(function(){\n");
      out.write("\t\t\tif($.cookie(\"authcode\") != null){\n");
      out.write("\t\t\t\t$(\"#openPopup\").addClass(\"show\")\n");
      out.write("\t\t\t\t$(\".login\").hide();\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\t$(\"#openApp\").click(function(e){\n");
      out.write("\t\t\t\te.preventDefault();\n");
      out.write("\t\t\t\tvar theURl = $(this).attr(\"href\");\n");
      out.write("\t\t\t\tutils.openNewWindow(theURl);\n");
      out.write("\t\t\t});\n");
      out.write("\t\t\t$(\"#loginAgain\").click(function(e){\n");
      out.write("\t\t\t\te.preventDefault();\n");
      out.write("\t\t\t\t$(\"#openPopup\").removeClass(\"show\")\n");
      out.write("\t\t\t\t$(\".login\").show();\n");
      out.write("\t\t\t});\t\t\t\n");
      out.write("\t\t\tutils.setLoginEvents();\n");
      out.write("\t\t});\t\n");
      out.write("\t</script>\n");
      out.write("</head>\n");
      out.write("<!--[if IE 7 ]><body id=\"login\" class=\"ie7\"><![endif]-->\n");
      out.write("<!--[if IE 8 ]><body id=\"login\" class=\"ie8\"><![endif]-->\n");
      out.write("<!--[if IE 9 ]><body id=\"login\" class=\"ie9\"><![endif]-->\n");
      out.write("<!--[if !IE]><!--><body id=\"login\"><!--<![endif]-->\n");
      out.write("<div id=\"container\">\n");
      out.write("\t");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/inc/loginForm.jsp" + "?" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("facilityid", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode(String.valueOf(facilityid), request.getCharacterEncoding()), out, false);
      out.write("\n");
      out.write("\t<div id=\"openPopup\">\n");
      out.write("\t\t<a href=\"/patient-search.jsp\" id=\"openApp\" class=\"orangeBtn\">Return to Application</a>\n");
      out.write("\t\t<a href=\"#\" id=\"loginAgain\">Login again</a>\n");
      out.write("\t</div>\n");
      out.write("</div>\n");
      out.write("</body>\n");
      out.write("</html>\n");
      out.write("\n");
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
