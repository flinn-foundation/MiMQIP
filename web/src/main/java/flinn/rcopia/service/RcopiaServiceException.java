package flinn.rcopia.service;

public class RcopiaServiceException extends Exception
{

	private static final long serialVersionUID = 1L;

	public RcopiaServiceException()
	{
		super();
	}

	public RcopiaServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RcopiaServiceException(String message)
	{
		super(message);
	}

	public RcopiaServiceException(Throwable cause)
	{
		super(cause);
	}
}
