package flinn.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class NumberUtils
{

	public static double parseNumber(String drugStrength) throws ParseException
	{
		NumberFormat nf = NumberFormat.getInstance();
		Number number = nf.parse(drugStrength);
		return number.doubleValue();
	}

	public static double parseNumber(String drugStrength, String format) throws ParseException
	{
		NumberFormat nf = new DecimalFormat(format);
		Number number = null;
		number = nf.parse(drugStrength);
		return number.doubleValue();
	}

	public static int parseNumber(String drugStrength, NumberFormat nf) throws ParseException
	{
		Number number = null;
		number = nf.parse(drugStrength);
		return number.intValue();
	}

}
