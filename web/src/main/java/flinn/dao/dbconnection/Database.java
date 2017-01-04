package flinn.dao.dbconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class Database
{

	protected static final Logger LOG = Logger.getLogger(Database.class);

	public static Map<String, Object> getRow(Connection conn, String sql, Object [] params) throws SQLException
	{
		List<Map<String, Object>> results = getRows(conn, sql, params);
		if (results.size() == 0)
		{
			//return new HashMap<String, Object>();
			return null;
		}
		else
		{
			return (Map<String, Object>) results.get(0);
		}
	}

	public static List<Map<String, Object>> getRows(Connection conn, String sql, Object [] params) throws SQLException
	{
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		PreparedStatement ps = null;
		ps = conn.prepareStatement(sql);

		for (int i = 0; i < params.length; i++)
		{
			/*
			if (params[i] instanceof java.flinn.util.Date)
			{
				java.sql.Date paramDate = new java.sql.Date(((Date) params[i]).getTime());
				ps.setDate(i + 1, paramDate);
			}
			else
			{
				ps.setObject(i + 1, params[i]);
			}
			*/
			ps.setObject(i + 1, params[i]);
		}

		results = executeQuery(ps);

		return results;
	}

	public static Long insertRow(Connection conn, String sql, Object [] params) throws SQLException
	{
		Long generatedKey = null;

		PreparedStatement ps = null;

		ps = conn.prepareStatement(sql);
		updateRows(ps, params);

		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next())
		{
			generatedKey = new Long(rs.getInt(1));
		}

		ps.close();

		return generatedKey;
	}

	public static int updateRows(Connection conn, String sql, Object [] params) throws SQLException
	{
		int status = -1;

		PreparedStatement ps = null;
		ps = conn.prepareStatement(sql);
		status = updateRows(ps, params);
		ps.close();

		return status;
	}

	public static int updateRows(PreparedStatement ps, Object [] params) throws SQLException
	{
		int status = -1;

		for (int i = 0; i < params.length; i++)
		{
			if (params[i] instanceof Date)
			{
				java.sql.Timestamp paramDate = new java.sql.Timestamp(((Date) params[i]).getTime());
				ps.setTimestamp(i + 1, paramDate);
			}
			else
			{
				ps.setObject(i + 1, params[i]);
			}
		}
		status = ps.executeUpdate();

		return status;
	}

	private static List<Map<String, Object>> executeQuery(PreparedStatement ps) throws SQLException
	{
		ResultSet rs = null;
		rs = ps.executeQuery();
		return processResultSet(rs);
	}

	private static List<Map<String, Object>> processResultSet(ResultSet rs) throws SQLException
	{
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		ResultSetMetaData rsmd = rs.getMetaData();
		String [] columnNames = getColumnNames(rsmd);
		String [] columnTypes = getColumnTypes(rsmd);

		while (rs.next())
		{
			Map<String, Object> row = new HashMap<String, Object>();

			for (int i = 0; i < columnNames.length; i++)
			{
				//LOG.debug(columnTypes[i] + "," + rs.getTimestamp(i + 1));
				Object columnValue = rs.getObject(i + 1);
				if (columnValue != null)
				{
					if (columnTypes[i].equals("java.sql.Timestamp"))
					{
						row.put(columnNames[i].toUpperCase(), columnValue);
						//row.put(columnNames[i].toUpperCase(), toCalendar(rs.getTimestamp(i + 1)));
					}
					else
					{
						row.put(columnNames[i].toUpperCase(), columnValue);
					}
				}
				else
				{
					if (columnTypes[i].equals("java.sql.Timestamp"))
					{
						Date nullDate = null;
						row.put(columnNames[i].toUpperCase(), nullDate);
					}
					else
					{
						row.put(columnNames[i].toUpperCase(), "");
					}
				}
			}
			results.add(row);
		}
		return results;
	}

	private static String [] getColumnNames(ResultSetMetaData rsmd) throws SQLException
	{
		int numCols = rsmd.getColumnCount();
		String [] columnNames = new String[numCols];
		for (int i = 0; i < numCols; i++)
		{
			columnNames[i] = rsmd.getColumnName(i + 1);
		}
		return columnNames;
	}

	private static String [] getColumnTypes(ResultSetMetaData rsmd) throws SQLException
	{
		int numCols = rsmd.getColumnCount();
		String [] columnNames = new String[numCols];
		for (int i = 0; i < numCols; i++)
		{
			columnNames[i] = rsmd.getColumnClassName(i + 1);
		}
		return columnNames;
	}

	@SuppressWarnings("unused")
	private static Calendar toCalendar(java.sql.Timestamp t)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return c;
	}

}
