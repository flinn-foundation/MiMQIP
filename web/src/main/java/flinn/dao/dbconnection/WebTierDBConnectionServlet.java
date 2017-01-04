package flinn.dao.dbconnection;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.sql.DataSource;
import java.sql.*;
import javax.naming.*;

/**
 * @author
 * @version
 */
public class WebTierDBConnectionServlet extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -847882461616688872L;

	/**
	 * Initializes the flinn.servlet.
	 */
	public void init(ServletConfig config) throws ServletException
	{
		System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - init started");
		super.init(config);

		String dsJndiLookupName = "";
		DataSource ds = null;
		String useFlag = config.getInitParameter("useflag");
		String mysqlurl = "";
		String mysqlusername = "";
		String mysqlpassword = "";
		if (useFlag != null && useFlag.equals("datasourcejndiname"))
		{
			System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - useFlag.equals( datasourcejndiname )");
			try
			{
				dsJndiLookupName = config.getInitParameter("datasourcejndiname"); // e.g. datasourcejndiname = "java:comp/env/jdbc/myDataSource"
				Context jndiCntx = new InitialContext();
				System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - Looking up datasourcejndiname: " + dsJndiLookupName);
				ds = (DataSource) jndiCntx.lookup(dsJndiLookupName);
				System.out.println("Found. Connecting to " + dsJndiLookupName + ds.toString());
				DBConnectionPool.init(ds);
				System.out.println("Calling flinn.flinn.dao.dbconnection.DbConnectionPool.init(ds)");
			}
			catch (Exception ex)
			{
				if (dsJndiLookupName == null)
					dsJndiLookupName = "NULL";
				System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - error - dsJndiLookupName = " + dsJndiLookupName + " " + ex.toString());
				System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - dsJndiLookupName = " + dsJndiLookupName);
			}
		}
		else if (useFlag != null && useFlag.equals("mysql"))
		{
			System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - useFlag.equals( mysql )");
			try
			{
				mysqlurl = config.getInitParameter("jdbc:mysql://bashful.enlighten.com/flinn?zeroDateTimeBehavior=convertToNull");
				mysqlusername = config.getInitParameter("flinn");
				mysqlpassword = config.getInitParameter("flinn");

				com.mysql.jdbc.jdbc2.optional.MysqlDataSource mysqlds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
				mysqlds.setUrl(mysqlurl);
				mysqlds.setUser(mysqlusername);
				mysqlds.setPassword(mysqlpassword);

				DBConnectionPool.init(mysqlds);
				System.out.println("Calling flinn.flinn.dao.dbconnection.DBConnectionPool.init(mysqlds)");
			}
			catch (Exception ex)
			{
				System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - error - mysql");
				System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - mysql");
			}
		}

		// try get DB connection to see if there is an error or not
		Connection conn = null;
		try
		{
			System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - getConnection test start");

			System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - getConnection " );

			conn = DBConnectionPool.getConnection();
			if (null != conn)
			{
				System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - getConnection test successful!");
			}
			else
			{
				System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - getConnection returned null connection.");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - getConnection failed. " + ex.toString());
		}
		finally
		{
			// close the Connection
			try
			{
				if (conn != null)
					conn.close();
			}
			catch (SQLException ex)
			{
				conn = null;
				System.out.println("flinn.flinn.dao.dbconnection.WebTierDBConnectionServlet - connection close failed.  " + ex.toString());
			}
		}

	}

	/**
	 * Destroys the flinn.servlet.
	 */
	public void destroy()
	{

	}

	/**
	 * Returns a short description of the flinn.servlet.
	 */
	public String getServletInfo()
	{
		return "Short description";
	}

}
