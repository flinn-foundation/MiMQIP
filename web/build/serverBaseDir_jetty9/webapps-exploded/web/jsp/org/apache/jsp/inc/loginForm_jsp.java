package org.apache.jsp.inc;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class loginForm_jsp extends org.apache.jasper.runtime.HttpJspBase
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
int facilityid = 0;
try {
	if (facidString != null)
		facilityid = Integer.decode(facidString).intValue();
} catch (NumberFormatException e) {
	facilityid = 0;
}
flinn.beans.response.ResponseFacilityBean facility = null;

if (facilityid > 0) {
	java.sql.Connection connection = null;
	try {
		connection = flinn.dao.dbconnection.DBConnectionPool.getConnection();
		if (connection == null || connection.isClosed()) {
			// LOG.debug("Unable to get DB Connection");
		}
	} catch (java.lang.Exception e) {
		// LOG.debug("Unable to get DB Connection: "+e);
	}
	
	flinn.beans.request.RequestFacilityBean facreq = new flinn.beans.request.RequestFacilityBean();
	facreq.setFacilityid(facilityid);
	java.util.List<flinn.beans.response.ResponseFacilityBean> facilities = new flinn.dao.imp.FacilityDaoImp().find(facreq, null, connection);
	
	if (facilities != null && facilities.size() > 0) {
		facility = facilities.get(0);
	}
}
/* */
java.lang.String facilityName = "Unknown Facility Name";
/* */
if (facility != null) {
  facilityName = facility.getFacilityname();
}
/* */

      out.write("\n");
      out.write("<div id=\"colorboxForm\" class=\"login\">\n");
      out.write("\t<form action=\"\" id=\"loginForm\">\n");
      out.write("\t\t<h1>Welcome</h1>\n");
      out.write("\t\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t\t<label for=\"uName\">User Name:</label>\n");
      out.write("\t\t\t<input type=\"text\" class=\"greenInput\" id=\"uName\" /><span class=\"uName\"></span>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t\t<label for=\"password\">Password:</label>\n");
      out.write("\t\t\t<input type=\"password\" class=\"greenInput\" id=\"passwrd\" />\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<label>Facility:</label><span class=\"facilityName\">");
      out.print(facilityName);
      out.write("</span>\n");
      out.write("\t\t<div class=\"fieldWrapper\">\n");
      out.write("\t\t\t<label for=\"terms\" style=\"padding-top:17px;\">Terms:</label>\n");
      out.write("\t\t\t<br/><input type=\"checkbox\" id=\"terms\" value=\"true\" />\n");
      out.write("\t\t\t<span style=\"padding-top:11px; font-size:10px;\"> I have read and agree to the <a href=\"/MiMQIP-terms.pdf\" target=\"_blank\">terms and conditions</a>.</span>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<input type=\"submit\" value=\"Login\" class=\"orangeBtn\" id=\"loginBtn\"  />\n");
      out.write("\t\t<input type=\"hidden\" id=\"facilityID\" value=\"");
      out.print(facilityid);
      out.write("\" />\n");
      out.write("\t\t<p id=\"loginErrorMsg\" class=\"errorMsg\" style=\"padding-top:5px;\"></p>\n");
      out.write("\t\t<a href=\"#\" id=\"newUser\">Close window and sign in as a different user</a>\n");
      out.write("\t</form>\n");
      out.write("</div>\n");
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
