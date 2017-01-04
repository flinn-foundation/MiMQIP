package flinn.persistence.delegate;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.sql.Date;

public class DatePersistenceDelegate extends PersistenceDelegate
{

	public Expression instantiate(final Object oldInstance, final Encoder out)
	{
		final Date date = (Date) oldInstance;
		final Long time = Long.valueOf(date.getTime());
		return new Expression(date, date.getClass(), "new", new Object[]
		{ time });
	}
}
