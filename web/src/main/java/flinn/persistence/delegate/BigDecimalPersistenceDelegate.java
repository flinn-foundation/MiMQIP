package flinn.persistence.delegate;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.math.BigDecimal;

public class BigDecimalPersistenceDelegate extends PersistenceDelegate
{

	public Expression instantiate(final Object oldInstance, final Encoder out)
	{
		final BigDecimal oldObj = (BigDecimal) oldInstance;
		final String newObj = oldObj.toString();
		return new Expression(oldObj, oldObj.getClass(), "new", new Object[]
		{ newObj });
	}
}
