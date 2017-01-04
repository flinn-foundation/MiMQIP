package flinn.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class ApplicationProperties
{

	private static final Logger LOG = Logger.getLogger(ApplicationProperties.class);

	private static final String RESOURCE_NAME = "application";

	private static ResourceBundle props;

	public static void initialize()
	{
		//FIXME: Add the ability to use vm arguments
		//FIXME: Add the ability to reload on the fly
		LOG.debug("*********************************************************");
		LOG.debug("Setting Properties File For Resource: " + RESOURCE_NAME);
		props = ResourceBundle.getBundle(RESOURCE_NAME);
		LOG.debug("*********************************************************");
	}

	public static String getProperty(String key)
	{
		if (props == null)
		{
			initialize();
		}
		String value = null;

		try
		{
			value = props.getString(key);
		}
		catch (Exception e)
		{
			LOG.error("Could not find value for key " + key + " in resource " + RESOURCE_NAME);
		}

		return (value == null) ? "" : value;
	}

	public static String [] getArray(String key)
	{
		if (props == null)
		{
			initialize();
		}
		List<String> arrayList = new ArrayList<String>();
		int i = 0;
		String value;
		while ((value = getProperty(key.toUpperCase() + "." + i++)).length() > 0)
		{
			arrayList.add(value);
		}
		return (String []) arrayList.toArray(new String[0]);
	}

}
