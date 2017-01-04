package flinn.rcopia.service;

public class RcopiaSenderException extends Exception
{

	private static final long serialVersionUID = 1L;

	public RcopiaSenderException()
	{
		super();
	}

	public RcopiaSenderException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RcopiaSenderException(String message)
	{
		super(message);
	}

	public RcopiaSenderException(Throwable cause)
	{
		super(cause);
	}
}
