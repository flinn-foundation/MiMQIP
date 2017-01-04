package flinn.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import flinn.dao.dbconnection.DBConnectionPool;

/* --------------------------------------------------------------- 
 @author Jeff Matthes, HealthMedia, A JNJ Company
 Date: 2009-5-12
 Project: Mobile Development
 --------------------------------------------------------------- */

public abstract class AbstractBaseDao implements DaoInterface
{

	protected static final Logger LOG = Logger.getLogger(AbstractBaseDao.class);

	static
	{
		LOG.debug("Log appender instantiated for " + AbstractBaseDao.class);
	}

	protected Connection connection;
	protected boolean autoCommit;

	public AbstractBaseDao()
	{
		super();
		autoCommit = false;
	}

	public void renewConnection() throws Exception
	{
		connection = DBConnectionPool.getConnection();
		connection.setAutoCommit(false);
	}

	public void setConnection(final Connection connectionIn)
	{
		connection = connectionIn;
	}

	public Connection getConnection()
	{
		return connection;
	}

	public void closeConnection() throws SQLException
	{
		connection.close();
	}

	/*
	 * Not sure why the jsp's are passing a string
	 */
	public void closeConnection(String dummy) throws SQLException
	{
		connection.close();
	}

	public void commitConnection() throws SQLException
	{
		connection.commit();
	}

	public void setAutoCommitConnection() throws SQLException
	{
		connection.setAutoCommit(autoCommit);
	}

	public void rollbackConnection() throws SQLException
	{
		connection.rollback();
	}

	public boolean isAutoCommit()
	{
		return autoCommit;
	}

	public void setAutoCommit(final boolean autoCommit)
	{
		this.autoCommit = autoCommit;
	}

}
