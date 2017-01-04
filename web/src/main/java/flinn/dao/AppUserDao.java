package flinn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.AppUserRoleBean;
import flinn.beans.request.RequestAppUserBean;
import flinn.beans.response.ResponseAppUserBean;
import flinn.beans.response.ResponseSessionContainerBean;

public abstract class AppUserDao
{

	protected static final Logger LOG = Logger.getLogger(AppUserDao.class);
	static
	{
		LOG.warn("Log appender instantiated for " + AppUserDao.class);
	}

	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	public ResponseAppUserBean getBeanFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final ResponseAppUserBean bean = new ResponseAppUserBean();
		bean.setFacilityid(resultSet.getInt("AU.FacilityID"));
		bean.setLogin(resultSet.getString("AU.Login"));
		bean.setValid(resultSet.getBoolean("AU.Valid"));
		bean.setLaunch(resultSet.getString("AU.Launch"));
		bean.setExpiration(resultSet.getString("AU.Expiration"));
		bean.setLastloggedin(resultSet.getString("AU.LastLoggedIn"));
		bean.setLastactivity(resultSet.getString("AU.LastActivity"));
		bean.setLastremoteaddress(resultSet.getString("AU.LastRemoteAddr"));
		bean.setAppuserid(resultSet.getInt("AU.ID"));
		if (bean.getAppuserid() > 0)
		{
			bean.setRoles(getRoles(bean.getAppuserid(), connection));
			bean.setSettings(getSettings(bean.getAppuserid(), connection));
		}
		return bean;
	}

	public List<ResponseAppUserBean> getListFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final List<ResponseAppUserBean> results = new ArrayList<ResponseAppUserBean>();
		ResponseAppUserBean bean = null;
		while (resultSet.next())
		{
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestAppUserBean bean, Connection connection) throws Exception
	{
		int appuserid = 0;
		if (bean != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into AppUser (FacilityID, Login, Password, Valid, Launch, Expiration, LastLoggedIn, LastActivity, LastRemoteAddr) " + " values("
			    + "?, ?, PASSWORD(?), ?, ?, ?, ?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection.prepareStatement(command);

			// Insert into AppUser
			insertStatement.setInt(1, bean.getFacilityid());
			insertStatement.setString(2, bean.getLogin());
			insertStatement.setString(3, bean.getPassword());
			insertStatement.setBoolean(4, bean.getValid());
			insertStatement.setString(5, bean.getLaunch());
			insertStatement.setString(6, bean.getExpiration());
			insertStatement.setString(7, "0000-00-00 00:00:00");
			insertStatement.setString(8, "0000-00-00 00:00:00");
			insertStatement.setString(9, "0.0.0.0");
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next())
			{
				appuserid = rs.getInt(1);
			}

			if (appuserid > 0)
			{
				saveRoles(appuserid, bean.getRoles(), null, connection);
				saveSettings(appuserid, bean.getSettings(), null, connection);
			}

			insertStatement.close();
		}
		return appuserid;
	}

	public List<ResponseAppUserBean> find(final RequestAppUserBean bean, final String orderBy, Connection connection) throws Exception
	{
		List<ResponseAppUserBean> results = null;

		if (connection == null || connection.isClosed())
		{
			LOG.error("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String query = fillInColumnNames(bean);
		if (orderBy != null)
		{
			query += " ORDER BY " + orderBy;
		}

		LOG.debug("Going to run query = " + query);

		final PreparedStatement preparedStatementQuery = connection.prepareStatement(query);

		LOG.debug("created PreparedStatement");

		fillInColumnValues(bean, preparedStatementQuery);

		LOG.debug("fillInColumnValues");

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		LOG.debug("Executed Query");
		try
		{
			results = getListFromRs(resultSet, connection);
		}
		catch (SQLException e)
		{
			LOG.error("SQLException: ", e);
			throw e;
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		LOG.debug("results = " + results.size());
		return results;
	}

	public List<ResponseAppUserBean> findValid(final RequestAppUserBean bean, final String orderBy, Connection connection, boolean isSuperAdmin) throws Exception
	{
		List<ResponseAppUserBean> results = null;

		if (connection == null || connection.isClosed())
		{
			LOG.error("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String facilityClause = "";

		if (!isSuperAdmin)
			facilityClause = " AND FacilityID = ?";

		String query = "Select * from AppUser AU WHERE Valid='1' AND Launch < NOW() AND (Expiration > NOW() OR Expiration = '0000-00-00 00:00:00')" + facilityClause;
		if (orderBy != null)
		{
			query += " ORDER BY " + orderBy;
		}

		LOG.debug("Going to run query = " + query);

		final PreparedStatement preparedStatementQuery = connection.prepareStatement(query);

		if (!isSuperAdmin)
			preparedStatementQuery.setInt(1, bean.getFacilityid());

		ResultSet resultSet = null;
		try
		{
			resultSet = preparedStatementQuery.executeQuery();
			results = getListFromRs(resultSet, connection);
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		return results;
	}

	// Order must be the same as fillInColumnNames
	private void fillInColumnValues(final RequestAppUserBean bean, final PreparedStatement preparedStatementQuery) throws SQLException
	{
		int index = 1;

		if (bean.getFacilityid() != 0)
		{
			preparedStatementQuery.setInt(index, bean.getFacilityid());
			index++;
		}
		if (bean.getLogin() != null)
		{
			preparedStatementQuery.setString(index, bean.getLogin());
			index++;
		}
		if (bean.getPassword() != null)
		{
			preparedStatementQuery.setString(index, bean.getPassword());
			index++;
		}
		if (bean.getValid() != null)
		{
			preparedStatementQuery.setBoolean(index, bean.getValid());
			index++;
		}
		if (bean.getLaunch() != null)
		{
			preparedStatementQuery.setString(index, bean.getLaunch());
			index++;
		}
		if (bean.getExpiration() != null)
		{
			preparedStatementQuery.setString(index, bean.getExpiration());
			index++;
		}
		if (bean.getAppuserid() != 0)
		{
			preparedStatementQuery.setInt(index, bean.getAppuserid());
			index++;
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestAppUserBean bean)
	{
		String query = "Select * from AppUser AU ";
		boolean first = true;

		if (bean.getFacilityid() != 0)
		{
			query = query + " " + (first ? WHERE : AND) + "AU.FacilityID " + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getLogin() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "AU.Login" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getPassword() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "AU.Password = PASSWORD(?)";
			first = false;
		}
		if (bean.getValid() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "AU.Valid" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getLaunch() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "AU.Launch" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getExpiration() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "AU.Expiration" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getAppuserid() != 0)
		{
			query = query + " " + (first ? WHERE : AND) + "AU.ID" + EQUALS_QUESTION;
			first = false;
		}
		return query;
	}

	public int update(final RequestAppUserBean bean, final ResponseAppUserBean original, Connection connection) throws Exception
	{
		int result = -1;
		if (bean != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			int fieldcount = 0;
			String command = "update AppUser SET ";
			if (bean.getFacilityid() != 0 && (original == null || !(original.getFacilityid() == bean.getFacilityid())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "FacilityID = ?";
				fieldcount++;
			}
			if (bean.getPassword() != null)
			{
				if (fieldcount > 0)
					command += ", ";
				command += "Password = PASSWORD(?)";
				fieldcount++;
			}
			if (bean.getValid() != null && (original == null || original.getValid() != bean.getValid()))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "Valid = ?";
				fieldcount++;
			}
			if (bean.getLaunch() != null && (original == null || !original.getLaunch().equals(bean.getLaunch())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "Launch = ?";
				fieldcount++;
			}
			if (bean.getExpiration() != null && (original == null || !original.getExpiration().equals(bean.getExpiration())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "Expiration = ?";
				fieldcount++;
			}
			if (bean.getLastloggedin() != null && (original == null || !original.getLastloggedin().equals(bean.getLastloggedin())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "LastLoggedIn = ?";
				fieldcount++;
			}
			if (bean.getLastactivity() != null && (original == null || !original.getLastactivity().equals(bean.getLastactivity())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "LastActivity = ?";
				fieldcount++;
			}
			if (bean.getLastremoteaddress() != null && (original == null || original.getLastremoteaddress() != bean.getLastremoteaddress()))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "LastRemoteAddress = ?";
				fieldcount++;
			}

			command += " WHERE ID = ?";

			if (fieldcount > 0)
			{
				LOG.debug("Running query: " + command);
				final PreparedStatement updateStatement = connection.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getFacilityid() != 0 && (original == null || !(original.getFacilityid() == bean.getFacilityid())))
				{
					updateStatement.setInt(fieldnum, bean.getFacilityid());
					;
					fieldnum++;
				}
				if (bean.getPassword() != null)
				{
					updateStatement.setString(fieldnum, bean.getPassword());
					;
					fieldnum++;
				}
				if (bean.getValid() != null && (original == null || original.getValid() != bean.getValid()))
				{
					updateStatement.setBoolean(fieldnum, bean.getValid());
					;
					fieldnum++;
				}
				if (bean.getLaunch() != null && (original == null || !original.getLaunch().equals(bean.getLaunch())))
				{
					updateStatement.setString(fieldnum, bean.getLaunch());
					fieldnum++;
				}
				if (bean.getExpiration() != null && (original == null || !original.getExpiration().equals(bean.getExpiration())))
				{
					updateStatement.setString(fieldnum, bean.getExpiration());
					fieldnum++;
				}
				if (bean.getLastloggedin() != null && (original == null || !original.getLastloggedin().equals(bean.getLastloggedin())))
				{
					updateStatement.setString(fieldnum, bean.getLastloggedin());
					fieldnum++;
				}
				if (bean.getLastactivity() != null && (original == null || !original.getLastactivity().equals(bean.getLastactivity())))
				{
					updateStatement.setString(fieldnum, bean.getLastactivity());
					fieldnum++;
				}
				if (bean.getLastremoteaddress() != null && (original == null || !original.getLastremoteaddress().equals(bean.getLastremoteaddress())))
				{
					updateStatement.setString(fieldnum, bean.getLastremoteaddress());
					;
					fieldnum++;
				}
				if (original != null)
					updateStatement.setInt(fieldnum, original.getAppuserid());
				else
					throw new Exception("Invalid or Missing AppUserID in update");

				result = updateStatement.executeUpdate();

				updateStatement.close();
			}
			if (original != null && original.getAppuserid() > 0)
			{
				saveRoles(bean.getAppuserid(), bean.getRoles(), original.getRoles(), connection);
				saveSettings(bean.getAppuserid(), bean.getSettings(), original.getSettings(), connection);
			}
		}
		return result;
	}

	public int updateLastLoggedIn(int appUserID, Connection connection) throws Exception
	{
		int result = -1;

		if (appUserID > 0)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}

			final String command = "UPDATE AppUser SET LastLoggedIn = ? WHERE ID = ?";
			final PreparedStatement updateStatement = connection.prepareStatement(command);
			updateStatement.setString(1, flinn.util.DateString.now());
			updateStatement.setInt(2, appUserID);
			result = updateStatement.executeUpdate();
			updateStatement.close();
		}
		return result;
	}

	public int updateLastActivity(ResponseSessionContainerBean session, Connection connection) throws Exception
	{
		int result = -1;

		if (session != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}

			String command = "UPDATE AppUser SET LastActivity = ? WHERE ID = ?";
			final PreparedStatement updateStatement = connection.prepareStatement(command);
			updateStatement.setString(1, flinn.util.DateString.now());
			updateStatement.setInt(2, session.getUser().getAppuserid());

			result = updateStatement.executeUpdate();
			updateStatement.close();
		}
		return result;
	}

	public AppUserRoleBean [] getAllRoles(Connection connection) throws SQLException
	{
		final String command = "SELECT ID, AdminName, Description FROM AppRole;";

		ArrayList<AppUserRoleBean> results = new ArrayList<AppUserRoleBean>();
		final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
			while (resultSet.next())
			{
				AppUserRoleBean role = new AppUserRoleBean();
				role.setApproleid(resultSet.getInt("ID"));
				role.setApprole(resultSet.getString("AdminName"));
				results.add(role);
			}
		}
		catch (SQLException e)
		{
			LOG.error("SQLException: ", e);
			throw e;
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0)
		{
			AppUserRoleBean [] ar = new AppUserRoleBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}

	private AppUserRoleBean [] getRoles(int appuserid, Connection connection) throws SQLException
	{
		final String command = "select R.ID, R.AdminName from AppRole_AppUser UR, AppRole R WHERE UR.AppUserId = ? AND UR.AppRoleId = R.ID";

		ArrayList<AppUserRoleBean> results = new ArrayList<AppUserRoleBean>();
		final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);
		preparedStatementQuery.setInt(1, appuserid);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
			while (resultSet.next())
			{
				AppUserRoleBean role = new AppUserRoleBean();
				role.setApproleid(resultSet.getInt("R.ID"));
				role.setApprole(resultSet.getString("R.AdminName"));
				results.add(role);
			}
		}
		catch (SQLException e)
		{
			LOG.error("SQLException: ", e);
			throw e;
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0)
		{
			AppUserRoleBean [] ar = new AppUserRoleBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}

	private void saveRoles(int appuserID, AppUserRoleBean [] roles, AppUserRoleBean [] oldRoles, Connection connection) throws Exception
	{
		final String command2 = "insert into AppRole_AppUser (AppUserId, AppRoleId) values(?, ?)";
		final String delcommand = "delete from AppRole_AppUser WHERE AppUserId = ? and AppRoleId = ?";

		// Insert into AppUser_AppRole
		if (roles != null)
		{
			for (int i = 0; i < roles.length; i++)
			{
				AppUserRoleBean role = roles[i];
				if (role != null)
				{
					boolean save = false;
					if (oldRoles == null)
					{
						save = true;
					}
					else
					{
						boolean match = false;
						for (int j = 0; j < oldRoles.length; j++)
						{
							if (role.equals(oldRoles[j]))
								match = true;
						}
						if (!match)
							save = true;
					}
					if (save)
					{
						PreparedStatement insertStatement = connection.prepareStatement(command2);
						insertStatement.clearParameters();
						insertStatement.setInt(1, appuserID);
						insertStatement.setInt(2, role.getApproleid());
						insertStatement.executeUpdate();
						insertStatement.close();
					}
				}
			}
		}
		// Delete from AppUser_AppRole
		if (oldRoles != null)
		{
			for (int i = 0; i < oldRoles.length; i++)
			{
				AppUserRoleBean role = oldRoles[i];
				if (role != null)
				{
					boolean match = false;
					for (int j = 0; j < roles.length; j++)
					{
						if (role.equals(roles[j]))
							match = true;
					}
					if (!match)
					{
						PreparedStatement delStatement = connection.prepareStatement(delcommand);
						delStatement.clearParameters();
						delStatement.setInt(1, appuserID);
						delStatement.setLong(2, role.getApproleid());
						delStatement.executeUpdate();
						delStatement.close();
					}
				}
			}
		}

	}

	private HashMap<String, String> getSettings(int appuserID, Connection connection) throws SQLException
	{
		final String command = "select * from AppUserSetting WHERE AppUserID = ?";

		HashMap<String, String> results = new HashMap<String, String>();
		final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);
		preparedStatementQuery.setInt(1, appuserID);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
			while (resultSet.next())
			{
				String key = resultSet.getString("AppUserSettingName");
				String value = resultSet.getString("AppUserSettingValue");
				results.put(key, value);
			}
		}
		catch (SQLException e)
		{
			LOG.error("SQLException: ", e);
			throw e;
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0)
		{
			return results;
		}
		return null;
	}

	private void saveSettings(int appuserID, HashMap<String, String> settings, HashMap<String, String> oldSettings, Connection connection) throws Exception
	{
		final String command = "insert into AppUserSetting (AppUserID, AppUserSettingName, AppUserSettingValue) values(?,?,?)";
		final String updcommand = "update AppUserSetting set AppUserSettingValue = ? WHERE AppUserID = ? and AppUserSettingName = ?";
		final String delcommand = "delete from AppUserSetting WHERE AppUserID = ? and AppUserSettingName = ?";

		// Insert into FacilitySetting
		if (settings != null)
		{
			Iterator<String> it = settings.keySet().iterator();
			while (it.hasNext())
			{
				String key = it.next();
				String value = settings.get(key);
				String oldValue = null;
				if (oldSettings != null)
				{
					oldValue = oldSettings.get(key);
				}
				if (value != null && oldValue == null)
				{
					PreparedStatement insertStatement = connection.prepareStatement(command);
					insertStatement.clearParameters();
					insertStatement.setInt(1, appuserID);
					insertStatement.setString(2, key);
					insertStatement.setString(3, value);
					insertStatement.executeUpdate();
					insertStatement.close();
				}
				else if (value != null && !value.equals(oldValue))
				{
					PreparedStatement updateStatement = connection.prepareStatement(updcommand);
					updateStatement.clearParameters();
					updateStatement.setString(1, value);
					updateStatement.setInt(2, appuserID);
					updateStatement.setString(3, key);
					updateStatement.executeUpdate();
					updateStatement.close();
				}
			}
		}
		if (oldSettings != null)
		{
			Iterator<String> it = oldSettings.keySet().iterator();
			while (it.hasNext())
			{
				String key = it.next();
				String value = null;
				if (settings != null)
				{
					value = settings.get(key);
				}
				if (value == null)
				{
					PreparedStatement deleteStatement = connection.prepareStatement(delcommand);
					deleteStatement.clearParameters();
					deleteStatement.setInt(1, appuserID);
					deleteStatement.setString(2, key);
					deleteStatement.executeUpdate();
					deleteStatement.close();
				}
			}
		}

	}
}
