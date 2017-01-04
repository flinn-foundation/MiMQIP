package flinn.dao.dbconnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBConnectionPool
{

	private static int debugFlag = 1; // DEFAULT SET TO NORMAL DEBUGGING
	private static boolean debugFlagSet = false;

	private static boolean ignorePropertiesFileCheck = true;

	private static javax.sql.DataSource staticDS;

	/** Initialize the database connection pool. */
	public static void init(javax.sql.DataSource ds)
	{
		if (ds == null)
		{
			throw new IllegalStateException("Database Connection Pool has not been initialized or created properly - null DataSource.");
		}
		else
		{

			// Initialize/Reinitialize the static DbConnectionPool
			staticDS = ds;
			ignorePropertiesFileCheck = true;
		}
	}

	/**
	 * Get a connection from the pool.
	 * <p>
	 * If MySQL.properties file in flinn.flinn.dao.dbconnection has "UseThisFileForDatasourceInfo" set to "true" then Connection Pool will be automatically
	 * initialized based on info in the properties file. Otherwise you need to initialize this static class using the init(Datasource ds) method
	 * before calling this getConnection() method.
	 * <p>
	 * Don't forget to close your connection!
	 */
	public static Connection getConnection() throws SQLException
	{
		if (staticDS == null)
		{
			if (checkInitFromMySqlProperties())
			{
				initFromMySqlProperties();
				if (staticDS == null)
					throw new IllegalStateException("Database Connection Pool has not been initialized or created.");
			}
			else
			{
				throw new IllegalStateException("Database Connection Pool has not been initialized or created.");
			}
		}
		return staticDS.getConnection();
	}

	private static boolean checkInitFromMySqlProperties()
	{
		if (ignorePropertiesFileCheck)
			return false;

		try
		{
			ResourceBundle p = ResourceBundle.getBundle("flinn.flinn.dao.dbconnection.MySQL");
			String usePropFileForDSInfo = p.getString("UseThisFileForDatasourceInfo");
			if (usePropFileForDSInfo != null && (usePropFileForDSInfo.equals("yes") || usePropFileForDSInfo.equals("true") || usePropFileForDSInfo.equals("1")))
			{
				return true;
			}
		}
		catch (Exception e)
		{
			// handleError("Exception in checkInitFromMySqlProperties() IGNORED.",e);
		}
		return false;
	}

	private static void initFromMySqlProperties()
	{
		try
		{
			if (staticDS == null)
			{
				// Get MySQL.properties file in flinn.flinn.dao.dbconnection
				ResourceBundle p = ResourceBundle.getBundle("flinn.flinn.dao.dbconnection.MySQL");
				String url = p.getString("URL");
				String user = p.getString("User");
				String password = p.getString("Password");

				com.mysql.jdbc.jdbc2.optional.MysqlDataSource mysqlds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
				mysqlds.setUrl(url);
				mysqlds.setUser(user);
				mysqlds.setPassword(password);

				staticDS = (javax.sql.DataSource) mysqlds;
			}
		}
		catch (Exception e)
		{
			// handleError("Exception in initFromMySqlProperties() IGNORED. getConnection() method will throw exception.",e);

		}

	}

	public static boolean getIgnorePropertiesFileCheck()
	{
		return ignorePropertiesFileCheck;
	}

	protected static int getDebugFlag()
	{
		if (!debugFlagSet)
		{
			try
			{
				debugFlagSet = true;

			}
			catch (NumberFormatException e)
			{
				// IGNORE ERROR
			}
		}
		return debugFlag;
	}

}
