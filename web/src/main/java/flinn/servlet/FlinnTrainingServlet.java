package flinn.servlet;

import java.beans.XMLDecoder;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import flinn.beans.BeanInterface;
import flinn.beans.response.ErrorContainerBean;
import flinn.beans.request.RequestActionBean;
import flinn.beans.request.RequestContainerBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.dao.DaoRequestManager;

public class FlinnTrainingServlet extends HttpServlet {
	public static final String DOMAIN_NAME = "localhost";
	public static final String REQUEST_FORMAT = "format";

	public static final String JSON_RESPONSE_FORMAT = "json";
	public static final String XML_RESPONSE_FORMAT = "xml";
	public static final String JAVA_RESPONSE_FORMAT = "java";
	public static final String[] RESPONSE_CODES =
	{ JSON_RESPONSE_FORMAT, XML_RESPONSE_FORMAT, JAVA_RESPONSE_FORMAT };
	public static final String[] RESPONSE_CONTENT_TYPES =
	{ "text/x-json", "text/xml", "text/xml" };
	public static final String[] RESPONSE_CODES_HELP_HTML =
	{ "<a href=\"http://en.wikipedia.org/wiki/JSON\">JSON WIKI</a>", "XML with the use of JAXB", "Java Object to XML with the java.flinn.beans.encoder" };
	public static final int DEFAULT_CONTENT_TYPE_INDEX = 2;


	//protected static final String SERVLET_NAME = "Flinn";
	protected static final String SERVLET_PATH = "http://" + DOMAIN_NAME + "/";// + SERVLET_NAME;

	private static final long serialVersionUID = 1867147227L;
	protected static final Logger LOG = Logger.getLogger(FlinnServlet.class);

	static
	{
		LOG.debug("Log appender instantiated for " + FlinnServlet.class);
	}
	
	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
	{

		RequestContainerBean reqcont = null;
		RequestActionBean reqab = null;
		
		try
		{
			System.out.println("Request Start time: "+new Date().getTime());
			final String format = req.getParameter(REQUEST_FORMAT);
			int contentLength = req.getContentLength();
			int bytesRead = 0;
			final char[] buf = new char[contentLength];
			BufferedReader in = req.getReader();
			boolean endread = false;
			while (bytesRead < contentLength && !endread) {
				int thisread = in.read(buf,bytesRead,contentLength-bytesRead);
				if (thisread < 0) endread = true;
				else if (thisread == 0) Thread.sleep(50);
				else bytesRead += thisread;
				System.out.println("ContentLength: "+contentLength+" Received: "+bytesRead+" This Read: "+thisread);
			}	
			

			System.out.println("|"+new String(buf)+"|");
			reqcont = handleBeanInput(buf, bytesRead, format);
			if (reqcont != null) reqab = reqcont.getAction();
			
			if (reqcont == null || reqab == null || reqab.getCommand() == null || reqab.getCommand().equals("") || reqab.getType() == null || reqab.getType().equals("")) {
				try {
					sendError(reqab, "Inappropriate or missing level or type -- GENERIC", 3, req, resp);
				} catch (JAXBException e1) {
					LOG.warn(e1);
				}
			} else {
				try {
					DaoRequestManager reqman = new DaoRequestManager();
					ResponseContainerBean rcb = reqman.handleTrainingRequest(reqcont, req);
					rcb.getAction().setUrl(req.getRequestURL()+"?"+req.getQueryString());
					rcb.getAction().setTimestamp(new BigDecimal(new Date().getTime()));
					printBeanInterface(rcb, req, resp);

				} catch (Exception e1) {
					LOG.warn("Unknown Exception Thrown during Handling: "+e1);
					try {
						sendError(reqab, "Unknown Exception Thrown during Handling: "+e1.getMessage(), 4, req, resp);
					} catch (JAXBException e2) {
						LOG.warn(e2);
					}
				}
			}
			System.out.println("Request End time: "+new Date().getTime());
		}
		catch (Exception e)
		{
			LOG.warn("Exception Thrown (most likely parsing failure): "+e);
			try {
				sendError(reqab, "Exception Thrown (most likely parsing failure): "+e.getMessage(), 2, req, resp);
			} catch (JAXBException e1) {
				LOG.warn(e1);
			}
		}
	}

	@Override
	public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
	{
		try {
			sendError(null, "No Information Submitted (GET REQUEST INAPPROPRIATE)", 1, req, resp);
		} catch (JAXBException e1) {
			LOG.warn(e1);
			final PrintWriter out = resp.getWriter();
			out.print("ErrorPrintingError");
		}
	}

	public void sendError (final RequestActionBean reqab, final String message, final int number, final HttpServletRequest req, final HttpServletResponse resp) throws IOException, JAXBException {
		ErrorContainerBean rcb = DaoRequestManager.generateErrorBean(reqab, message, number);
		rcb.getAction().setUrl(req.getRequestURL()+"?"+req.getQueryString());
		printBeanInterface(rcb, req, resp);
	}
	
	public void printBeanInterface(final BeanInterface beanInterface, final HttpServletRequest req, final HttpServletResponse resp) throws IOException, JAXBException
	{
		final PrintWriter out = resp.getWriter();
		String returnType = req.getParameter(REQUEST_FORMAT);
		if (returnType == null) returnType = RESPONSE_CODES[DEFAULT_CONTENT_TYPE_INDEX];

		if (returnType.equalsIgnoreCase(JAVA_RESPONSE_FORMAT))
		{
			resp.setContentType(RESPONSE_CONTENT_TYPES[2]);
			out.println(beanInterface.toJavaXmlString(1.0f));
		}
		else if (returnType.equalsIgnoreCase(XML_RESPONSE_FORMAT))
		{
			resp.setContentType(RESPONSE_CONTENT_TYPES[1]);
			out.println(beanInterface.toStandardXmlString(1.0f));
		}
		else if (returnType.equalsIgnoreCase(JSON_RESPONSE_FORMAT))
		{
			resp.setContentType(RESPONSE_CONTENT_TYPES[0]);
			out.println(beanInterface.toJsonString(1.0f));
		}

	}

	private RequestContainerBean handleBeanInput(char[] buf, int buflen, String format) throws Exception {
		if (format == null) format = RESPONSE_CODES[DEFAULT_CONTENT_TYPE_INDEX];
		
		if (format.equalsIgnoreCase(JAVA_RESPONSE_FORMAT))
		{
			// String input="<?xml version=\"1.0\" encoding=\"UTF-8\"?><java version=\"1.6.0_13\" class=\"java.flinn.beans.XMLDecoder\"><object class=\"com.healthmedia.stepbystep.flinn.beans.RequestContainerBean\"><void property=\"action\"><object class=\"com.healthmedia.stepbystep.flinn.beans.RequestActionBean\"><void property=\"keycode\"><string>password</string></void><void property=\"level\"><string>profile</string></void><void property=\"type\"><string>read</string></void></object></void><void property=\"login\"><string>somevalueinlogin</string></void></object></java>";
			return processJavaintoRequestActionBean(new String(buf));
		}
		else if (format.equalsIgnoreCase(XML_RESPONSE_FORMAT))
		{
			// Added for testing ... Hard code to this string for a profile request.
			// String input = "<request><action><type>read</type><level>profile</level><keycode>asdf123asdf123</keycode><messageid>themessageid</messageid><correlationid>thecorrelationid</correlationid><timestamp>1257209293439</timestamp></action><profile><profileidentifier>15105</profileidentifier><pedometerinstanceid>5001</pedometerinstanceid><hmiuniqueidentifier>10</hmiuniqueidentifier><latestprofileid>1</latestprofileid><createdate>1257201825000</createdate><lastmodifiedate>1257201826000</lastmodifiedate><height>69</height><weight>140</weight><reason>something new</reason><heightchanged>0</heightchanged><weightchanged>1</weightchanged><reasonchanged>0</reasonchanged><waist>35</waist><pedometer>Omron HJ-720ITC</pedometer><pedometerchanged>0</pedometerchanged><activitieschanged>0</activitieschanged><waistchanged>0</waistchanged></profile></request>";
			//String input = "<request><action><type>read</type><level>profile</level><keycode>password</keycode><messageid>themessageid</messageid><correlationid>thecorrelationid</correlationid><timestamp>1257349203072</timestamp></action><profile></profile></request>";
			return processXmlintoRequestActionBean(new String(buf));
		}
		else if (format.equalsIgnoreCase(JSON_RESPONSE_FORMAT))
		{
			Gson gson = new Gson();
			return (gson.fromJson(new String(buf), RequestContainerBean.class));
		}
		return null;
	}

	public RequestContainerBean processXmlintoRequestActionBean(String xmlInput) throws JAXBException
	{
		RequestContainerBean myRCBean=null;
		try {
			ByteArrayInputStream is= new ByteArrayInputStream((xmlInput).getBytes());
			JAXBContext jaxbCtext = JAXBContext.newInstance(new RequestContainerBean().getClass());
			Unmarshaller uMarsh= jaxbCtext.createUnmarshaller();
			myRCBean = (RequestContainerBean) uMarsh.unmarshal(is);
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new JAXBException (e);
		}
	 return myRCBean;
	}
	
	
	public RequestContainerBean processJavaintoRequestActionBean(String xmlInput) throws Exception
	{
		RequestContainerBean myRCBean=null;
		try {
			ByteArrayInputStream is= new ByteArrayInputStream((xmlInput).getBytes());
			//ByteArrayInputStream is = new ByteArrayInputStream(xmlInput.getBytes("UTF8"));
			
			XMLDecoder decode = new XMLDecoder(is,new RequestContainerBean());
			//Object myObject = decode.readObject();
			myRCBean= (RequestContainerBean) decode.readObject();
			decode.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception (e);
		}
		return myRCBean;
	}

	public String getContentType(final HttpServletRequest req)
	{
		if (req != null)
		{
			final String returnType = req.getParameter(REQUEST_FORMAT);

			for (int i = 0; i < RESPONSE_CODES.length; i++)
			{
				if (RESPONSE_CODES[i].equals(returnType))
				{
					return RESPONSE_CONTENT_TYPES[i];
				}
			}
		}
		return RESPONSE_CONTENT_TYPES[DEFAULT_CONTENT_TYPE_INDEX];
	}
}
