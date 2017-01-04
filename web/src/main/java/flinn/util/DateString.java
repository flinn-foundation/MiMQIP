package flinn.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public final class DateString
{

	private final static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String now()
	{
		return format(new Date());
	}

	public static final String format(Date d)
	{
		DateFormat df = new SimpleDateFormat(DATE_PATTERN);
		return df.format(d);
	}

	public static final String format(Date d, String datePattern)
	{
		DateFormat df = new SimpleDateFormat(datePattern);
		if (d != null)
			return df.format(d);
		else
			return null;
	}

	public static final Date interpret(String d)
	{
		if (d == null)
		{
			return null;
		}
		if (d.equals("0000-00-00 00:00:00"))
		{
			return null;
		}
		DateFormat df = new SimpleDateFormat(DATE_PATTERN);
		try
		{
			return df.parse(d);
		}
		catch (Exception e)
		{
			// Date format issue.
		}
		return null;
	}

	public static Date parseRcopiaDate(String dateString, String datePattern)
	{
		try
		{
			if (dateString != null && dateString.length() > 0)
			{
				return DateUtils.parseDate(dateString, new String[] { datePattern });
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}


}
