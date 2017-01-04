package flinn.rcopia.service;

import java.util.HashMap;
import java.util.Map;

public class DoseTimingUtils
{

	//private static final Logger LOG = Logger.getLogger(DoseTiming.class);

	private static final Map<String, Integer> doseMap = new HashMap<String, Integer>();
	private static final Map<String, Integer> intervalMap = new HashMap<String, Integer>();

	public static int getDose(String doseTiming)
	{
		if (doseMap.containsKey(doseTiming))
			return doseMap.get(doseTiming).intValue();
		else
			return -1;
	}

	public static int getInterval(String doseTiming)
	{
		if (intervalMap.containsKey(doseTiming))
			return intervalMap.get(doseTiming).intValue();
		else
			return 0;
	}

	public static boolean isValid(String doseTiming)
	{
		if (doseMap.containsKey(doseTiming) && intervalMap.containsKey(doseTiming))
			return true;
		else
			return false;
	}

	static
	{
		doseMap.put("single dose", new Integer(1));
		doseMap.put("once a day", new Integer(1));
		doseMap.put("twice a day", new Integer(2));
		doseMap.put("three times a day", new Integer(3));
		doseMap.put("four times a day", new Integer(4));
		doseMap.put("five times a day", new Integer(5));
		doseMap.put("every morning", new Integer(1));
		doseMap.put("every night", new Integer(1));
		doseMap.put("at bedtime", new Integer(1));
		doseMap.put("every other day", new Integer(1));
		doseMap.put("every three days", new Integer(1));
		doseMap.put("every two hours", new Integer(12));
		doseMap.put("every two hours while awake", new Integer(8));
		doseMap.put("every three hours", new Integer(8));
		doseMap.put("every three hours while awake", new Integer(6));
		doseMap.put("every four hours", new Integer(6));
		doseMap.put("every four hours while awake", new Integer(5));
		doseMap.put("every six hours", new Integer(4));
		doseMap.put("every six to eight hours", new Integer(4));
		doseMap.put("every eight hours", new Integer(3));
		doseMap.put("every twelve hours", new Integer(2));
		doseMap.put("every 24 hours", new Integer(1));
		doseMap.put("every 48 hours", new Integer(1));
		doseMap.put("every 72 hours", new Integer(1));
		doseMap.put("every three to four hours", new Integer(8));
		doseMap.put("every three to four hours while awake", new Integer(6));
		doseMap.put("every four to six hours", new Integer(6));
		doseMap.put("every four to six hours while awake", new Integer(5));
		doseMap.put("every eight to twelve hours", new Integer(3));
		doseMap.put("once a week", new Integer(-1));
		doseMap.put("twice a week", new Integer(-1));
		doseMap.put("three times a week", new Integer(-1));
		doseMap.put("once every two weeks", new Integer(-2));
		doseMap.put("every two weeks", new Integer(-2));
		doseMap.put("every four weeks", new Integer(-4));
		doseMap.put("once a month", new Integer(-4));
		doseMap.put("every three months", new Integer(-12));

		intervalMap.put("single dose", new Integer(1));
		intervalMap.put("once a day", new Integer(1));
		intervalMap.put("twice a day", new Integer(1));
		intervalMap.put("three times a day", new Integer(1));
		intervalMap.put("four times a day", new Integer(1));
		intervalMap.put("five times a day", new Integer(1));
		intervalMap.put("every morning", new Integer(1));
		intervalMap.put("every night", new Integer(1));
		intervalMap.put("at bedtime", new Integer(1));
		intervalMap.put("every other day", new Integer(2));
		intervalMap.put("every three days", new Integer(3));
		intervalMap.put("every two hours", new Integer(1));
		intervalMap.put("every two hours while awake", new Integer(1));
		intervalMap.put("every three hours", new Integer(1));
		intervalMap.put("every three hours while awake", new Integer(1));
		intervalMap.put("every four hours", new Integer(1));
		intervalMap.put("every four hours while awake", new Integer(1));
		intervalMap.put("every six hours", new Integer(1));
		intervalMap.put("every six to eight hours", new Integer(1));
		intervalMap.put("every eight hours", new Integer(1));
		intervalMap.put("every twelve hours", new Integer(1));
		intervalMap.put("every 24 hours", new Integer(1));
		intervalMap.put("every 48 hours", new Integer(2));
		intervalMap.put("every 72 hours", new Integer(3));
		intervalMap.put("every three to four hours", new Integer(1));
		intervalMap.put("every three to four hours while awake", new Integer(1));
		intervalMap.put("every four to six hours", new Integer(1));
		intervalMap.put("every four to six hours while awake", new Integer(1));
		intervalMap.put("every eight to twelve hours", new Integer(1));
		intervalMap.put("once a week", new Integer(7));
		intervalMap.put("twice a week", new Integer(3)); //FIXME: Unsupported Dose Timing "twice a week"
		intervalMap.put("three times a week", new Integer(2)); //FIXME: Unsupported Dose Timing "three times a week
		intervalMap.put("once every two weeks", new Integer(14));
		intervalMap.put("every two weeks", new Integer(14));
		intervalMap.put("every four weeks", new Integer(28));
		intervalMap.put("once a month", new Integer(30)); //FIXME: Unsupported Dose Timing "once a month"
		intervalMap.put("every three months", new Integer(90)); //FIXME: Unsupported Dose Timing "every three months"
	}

}
