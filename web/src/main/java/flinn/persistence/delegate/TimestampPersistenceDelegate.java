package flinn.persistence.delegate;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.sql.Timestamp;

public class TimestampPersistenceDelegate extends PersistenceDelegate
{

	public Expression instantiate(final Object oldInstance, final Encoder out)
	{
		final Timestamp timestamp = (Timestamp) oldInstance;
		final Long time = Long.valueOf(timestamp.getTime());
		return new Expression(timestamp, timestamp.getClass(), "new", new Object[]
		{ time });
	}
}