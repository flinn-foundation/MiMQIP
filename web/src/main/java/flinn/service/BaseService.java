package flinn.service;

import java.sql.Connection;

import org.apache.log4j.Logger;

public abstract class BaseService
{

	static final Logger LOG = Logger.getLogger(BaseService.class);

	protected void commitConnection(Connection connection)
    {
    	try
    	{
    		if (connection != null) connection.commit();
    	}
    	catch (Exception e)
    	{
    		LOG.error(e);
    	}
    }

	protected void rollbackConnection(Connection connection)
    {
    	try
    	{
    		if (connection != null) connection.rollback();
    	}
    	catch (Exception e)
    	{
    		LOG.error(e);
    	}
    }

	protected void closeConnection(Connection connection)
    {
    	try
    	{
    		if (connection != null) connection.close();
    	}
    	catch (Exception e)
    	{
    		LOG.error(e);
    	}
    }

}