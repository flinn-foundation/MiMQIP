package flinn.rcopia.service;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import flinn.util.ApplicationProperties;

public class RcopiaSenderService
{

	private static final Logger LOG = Logger.getLogger(RcopiaSenderService.class);

	protected String sendMessage(String xml) throws RcopiaSenderException
	{
		String responseBody = null;
		try
		{
			responseBody = sendAndReceive(xml);
		}
		catch (HttpException e)
		{
			LOG.error(e);
			throw new RcopiaSenderException("HttpException processing request: " + e.getMessage(), e);
		}
		catch (IOException e)
		{
			LOG.error(e);
			throw new RcopiaSenderException("IOException processing request: " + e.getMessage(), e);
		}
		catch (Exception e)
		{
			LOG.error(e);
			throw new RcopiaSenderException("Exception processing request: " + e.getMessage(), e);
		}
		return responseBody;
	}

	private String sendAndReceive(String xml) throws HttpException, IOException
	{
		String responseBody = null;
		LOG.debug("Sending to " + ApplicationProperties.getProperty("rcopiaapi.engineUrl"));
		PostMethod post = new PostMethod(ApplicationProperties.getProperty("rcopiaapi.engineUrl"));
		HttpClient httpclient = new HttpClient();

		NameValuePair param = new NameValuePair("xml", xml);
		post.setRequestBody(new NameValuePair[] { param });

		try
		{
			int statusCode = httpclient.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK)
			{
				throw new HttpException("HTTP status (" + statusCode + " " + HttpStatus.getStatusText(statusCode) + ") returned.");
			}
			responseBody = post.getResponseBodyAsString();
		}
		finally
		{
			if (post != null)
			{
				post.releaseConnection();
			}
		}

		return responseBody;
	}

	protected static class PreferredMapper extends NamespacePrefixMapper
	{
		@Override
		public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix)
		{
			return "ns1";
		}
	}

}
