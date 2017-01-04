package flinn.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import flinn.beans.BeanInterface;

public abstract class AbstractBaseServlet extends HttpServlet implements BaseServletInterface
{
	private static final long serialVersionUID = -1622754340L;
	protected static final Logger LOG = Logger.getLogger(AbstractBaseServlet.class);
	static
	{
		LOG.debug("Log appender instantiated for " + AbstractBaseServlet.class);
	}

	public static final String DOMAIN_NAME = "localhost";
	public static final String HELP_FORM_NAME = "help";
	public static final String HELP_CONTENT_TYPE = "text/html";
	public static final String RESPONSE_FORM_NAME = "resp";
	public static final String LOCALE_FORM_NAME = "locale";
	public static final String VERSION_FORM_NAME = "ver";
	public static final String ACTION_FORM_NAME = "action";
	public static final String XML_RESPONSE_TYPE = "xml";
	public static final String JSON_RESPONSE_TYPE = "json";
	
	public static final String UN_ORDERED_LIST_START = "<ul>";
	public static final String UN_ORDERED_LIST_END = "</ul>";
	public static final String LIST_ENTRY_START = "<li>";
	public static final String LIST_ENTRY_END = "</li>";
	public static final String LINE_BRAKE = "<br />";
	public static final String DASH = " - ";

	public static final String[] RESPONSE_CODES =
	{ JSON_RESPONSE_TYPE, XML_RESPONSE_TYPE };

	public static final String[] RESPONSE_CODES_HELP_HTML =
	{ "<a href=\"http://en.wikipedia.org/wiki/JSON\">JSON WIKI</a>", "XML with the use of JAXB" };

	public static final String[] RESPONSE_CONTENT_TYPES =
	{ "text/x-json", "text/xml" };
	public static final int DEFAULT_CONTENT_TYPE_INDEX = 1;

	protected static final String[] BASE_REQUIRED_INPUTS =
	{ RESPONSE_FORM_NAME, LOCALE_FORM_NAME, VERSION_FORM_NAME, ACTION_FORM_NAME };

	protected static final String[] BASE_REQUIRED_INPUTS_HELP_HTML =
	{ "Code for how the response should be formatted", "The language code for the response", "The version of code you are expecting", "The action type that you are requesting" };

	protected static final String ACTION_CREATE_FORM_NAME = "create";
	protected static final String ACTION_READ_FORM_NAME = "read";
	protected static final String ACTION_UPDATE_FORM_NAME = "update";
	protected static final String ACTION_DELETE_FORM_NAME = "delete";

	protected static final String[] ACTION_POSSIBILITIES_VALUES =
	{ ACTION_CREATE_FORM_NAME, ACTION_READ_FORM_NAME, ACTION_UPDATE_FORM_NAME, ACTION_DELETE_FORM_NAME };

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
	{
		doGet(req, resp);
	}

	@Override
	public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
	{
		try
		{
			if (shouldPrintHelpScreen(req))
			{
				printHelpScreen(req, resp, null, null);
			}
			else
			{

				// Action has been supplied
				final String action = req.getParameter(ACTION_FORM_NAME);
				if (action.equalsIgnoreCase(ACTION_CREATE_FORM_NAME))
				{
					create(req, resp);
				}
				else if (action.equalsIgnoreCase(ACTION_READ_FORM_NAME))
				{
					read(req, resp);
				}
				else if (action.equalsIgnoreCase(ACTION_UPDATE_FORM_NAME))
				{
					update(req, resp);
				}
				else if (action.equalsIgnoreCase(ACTION_DELETE_FORM_NAME))
				{
					delete(req, resp);
				}

			}
		}
		catch (Exception e)
		{
			// TODO: Need to work on error handling
			LOG.warn(e);
		}
	}

	protected void printHelpScreen(final HttpServletRequest req, final HttpServletResponse resp, final Exception exception, final String message) throws IOException
	{
		resp.setContentType(HELP_CONTENT_TYPE);
		final PrintWriter out = resp.getWriter();
		printHelpHtmlStart(out);
		printHelpScreen(out, exception, message);
		printHelpHtmlEnd(out);
	}

	@Override
	public boolean shouldPrintHelpScreen(final HttpServletRequest req)
	{
		boolean shouldPrintHelp = false;
		if (req != null)
		{
			final String help = req.getParameter(HELP_FORM_NAME);
			if (help != null)
			{
				try
				{
					shouldPrintHelp = Boolean.parseBoolean(help);
				}
				catch (Exception e)
				{
					LOG.warn("User passed " + HELP_FORM_NAME + " in url, but did not have a boolean value.  Value passed was " +help);
				}
			}
			for (int i = 0; i < getRequiredInputs().length && !shouldPrintHelp; i++)
			{
				final String requiredVar = req.getParameter(getRequiredInputs()[i]);
				shouldPrintHelp = requiredVar == null;
				if (shouldPrintHelp)
				{
					LOG.warn("Could not find required value:" + getRequiredInputs()[i] + " in HttpServletRequest parameter.");
				}
			}
		}
		return shouldPrintHelp;
	}

	public String getContentType(final HttpServletRequest req)
	{
		if (req != null)
		{
			final String returnType = req.getParameter(RESPONSE_FORM_NAME);

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

	public void printHelpHtmlEnd(final PrintWriter out)
	{
		out.println("</body>");
		out.println("</html>");
	}

	public void printHelpHtmlStart(final PrintWriter out)
	{
		out.println("<html>");
		out.println("<head>");
		out.println("<title>" + getPageTitle() + "</title>");
		out.println("</head>");
		out.println("<body>");
	}

	public void printHelpMenu(final PrintWriter out)
	{
		printHelpMenu(out, null, null);
	}

	public void printHelpMenu(final PrintWriter out, final Exception exception, final String message)
	{

		
		
		out.println("<h1>Help Menu</h1>");
		out.println(LINE_BRAKE);
		if (message != null && !message.equals(""))
		{
			out.println("<h3>" + message + "</h3>");
			out.println(LINE_BRAKE);
		}
		out.println("<h3>Required Inputs</h3>");
		out.println(UN_ORDERED_LIST_START);

		for (int i = 0; i < getRequiredInputs().length; i++)
		{
			out.println(LIST_ENTRY_START + getRequiredInputs()[i] + DASH + getRequiredInputsHelpHtml()[i] + LIST_ENTRY_END);
		}

		out.println(UN_ORDERED_LIST_END);
		out.println("<h3>Actions</h3>");
		out.println(UN_ORDERED_LIST_START);

		for (int i = 0; i < getAvailableActions().length; i++)
		{
			out.println(LIST_ENTRY_START + getAvailableActions()[i] + DASH + getAvailableActionsHelpHtml()[i] + LIST_ENTRY_END);
		}

		out.println(UN_ORDERED_LIST_END);
		out.println("<h3>Read Actions</h3>");
		out.println(UN_ORDERED_LIST_START);

		for (int i = 0; i < getReadActions().length; i++)
		{
			out.println(LIST_ENTRY_START + getReadActions()[i] + DASH + getReadActionsHelpHtml()[i] + LIST_ENTRY_END);
		}

		out.println(UN_ORDERED_LIST_END);
		out.println("<h3>" + RESPONSE_FORM_NAME + " Values</h3>");
		out.println(UN_ORDERED_LIST_START);
		for (int i = 0; i < RESPONSE_CODES.length; i++)
		{
			out.println(LIST_ENTRY_START + RESPONSE_CODES[i] + DASH + RESPONSE_CODES_HELP_HTML[i] + LIST_ENTRY_END);
		}
		out.println(UN_ORDERED_LIST_END);
		out.println("<h3>" + LOCALE_FORM_NAME + "</h3>");
		out.println("<p>See <a href=\"http://java.sun.com/javase/6/docs/api/java/flinn.util/Locale.html\">http://java.sun.com/javase/6/docs/api/java/flinn.util/Locale.html</a> for more info</p>");
	}

	@Override
	public String[] getRequiredInputs()
	{
		return BASE_REQUIRED_INPUTS.clone();
	}

	@Override
	public String[] getRequiredInputsHelpHtml()
	{
		return BASE_REQUIRED_INPUTS_HELP_HTML.clone();
	}

	public void printHMIBeanInterface(final BeanInterface beanInterface, final HttpServletRequest req, final HttpServletResponse resp) throws IOException, JAXBException
	{
		final String contentType = getContentType(req);
		resp.setContentType(contentType);
		final PrintWriter out = resp.getWriter();
		final String returnType = req.getParameter(RESPONSE_FORM_NAME);

		if (returnType.equalsIgnoreCase(XML_RESPONSE_TYPE))
		{
			out.println(beanInterface.toStandardXmlString(1.0f));
		}
		else if (returnType.equalsIgnoreCase(JSON_RESPONSE_TYPE))
		{
			out.println(beanInterface.toJsonString(1.0f));
		}

	}

	@Override
	public void create(final HttpServletRequest req, final HttpServletResponse resp) throws IOException
	{
		// If you hit this then the child class does not support this method
		printHelpScreen(req, resp, null, "Create flinn.service is not implemented.");
	}

	@Override
	public void delete(final HttpServletRequest req, final HttpServletResponse resp) throws IOException
	{
		// If you hit this then the child class does not support this method
		printHelpScreen(req, resp, null, "Delete flinn.service is not implemented.");

	}

	@Override
	public void read(final HttpServletRequest req, final HttpServletResponse resp) throws IOException
	{
		// If you hit this then the child class does not support this method
		printHelpScreen(req, resp, null, "Read flinn.service is not implemented.");

	}

	@Override
	public void update(final HttpServletRequest req, final HttpServletResponse resp) throws IOException
	{
		// If you hit this then the child class does not support this method
		printHelpScreen(req, resp, null, "Update flinn.service is not implemented.");

	}

	protected String getInput(final HttpServletRequest req)
	{
		String input = req.getRequestURI() + "?";
		boolean first = true;
		if (req != null)
		{
			final Enumeration<?> parameterNames = req.getParameterNames();
			while (parameterNames.hasMoreElements())
			{
				final String parameterName = parameterNames.nextElement().toString();
				final String paramterValue = req.getParameter(parameterName);
				input = input + (first ? "" : "&") + parameterName + "=" + paramterValue;
				first = false;
			}
		}

		return input;
	}
}
