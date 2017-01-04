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

import flinn.beans.FacilityIPBean;
import flinn.beans.request.RequestFacilityBean;
import flinn.beans.response.ResponseFacilityBean;
import flinn.util.cache.EHCacheImpl;
import flinn.util.cache.ICache;

public abstract class FacilityDao {

	protected static final Logger LOG = Logger.getLogger(FacilityDao.class);

	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	static {
		LOG.debug("Log appender instantiated for " + FacilityDao.class);
	}

	protected ResponseFacilityBean getBeanFromRs(final ResultSet resultSet,
			Connection connection) throws SQLException {
		ICache<String, ResponseFacilityBean> facilityCache = EHCacheImpl
				.getDefaultInstance("facilityCache");

		ResponseFacilityBean bean = null;
		int id = resultSet.getInt("ID");

		// try to retrieve the object from the cache by id
		bean = facilityCache.get(String.valueOf(id));

		if (bean != null) {
			LOG.debug("findFacilityById --> ID: " + String.valueOf(id)
					+ " found in cache");
		}

		// if the returned object is null, it was not found in the cache
		if (bean == null) {
			bean = new ResponseFacilityBean();
			bean.setFacilityid(resultSet.getInt("ID"));
			bean.setFacilityname(resultSet.getString("FacilityName"));
			bean.setFacilityshortcut(resultSet.getString("FacilityShortcut"));
			bean.setFacilityemail(resultSet.getString("FacilityEmail"));
			bean.setAdministratorid(resultSet.getString("AdministratorID"));
			bean.setValid(resultSet.getBoolean("Valid"));
			bean.setLaunch(resultSet.getString("Launch"));
			bean.setExpiration(resultSet.getString("Expiration"));

			if (bean.getFacilityid() > 0) {
				bean.setSettings(getSettings(bean.getFacilityid(), connection));
				bean.setIp(getIps(bean.getFacilityid(), connection));
			}

			facilityCache.put(String.valueOf(bean.getFacilityid()), bean);
		}
		return bean;
	}

	protected List<ResponseFacilityBean> getListFromRs(
			final ResultSet resultSet, Connection connection)
			throws SQLException {
		final List<ResponseFacilityBean> results = new ArrayList<ResponseFacilityBean>();
		ResponseFacilityBean bean = null;
		while (resultSet.next()) {
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestFacilityBean bean, Connection connection)
			throws Exception {
		int facilityID = 0;
		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.debug("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into Facility (FacilityName, FacilityShortcut, FacilityEmail, AdministratorID, Valid, Launch, Expiration) "
					+ " values(" + "?, ?, ?, ?, ?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection
					.prepareStatement(command);

			// Insert into Facility
			insertStatement.setString(1, bean.getFacilityname());
			insertStatement.setString(2, bean.getFacilityshortcut());
			insertStatement.setString(3, bean.getFacilityemail());
			insertStatement.setString(4, bean.getAdministratorid());
			insertStatement.setBoolean(5, bean.getValid());
			insertStatement.setString(6, bean.getLaunch());
			insertStatement.setString(7, bean.getExpiration());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next()) {
				facilityID = rs.getInt(1);
			}

			if (facilityID > 0) {
				saveIps(facilityID, bean.getIp(), null, connection);
				saveSettings(facilityID, bean.getSettings(), null, connection);
			}

			insertStatement.close();
		}
		return facilityID;
	}

	public List<ResponseFacilityBean> find(final RequestFacilityBean bean,
			final String orderBy, Connection connection) throws Exception {
		List<ResponseFacilityBean> results = null;

		if (connection == null || connection.isClosed()) {
			LOG.debug("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String query = fillInColumnNames(bean);
		if (orderBy != null) {
			query += " ORDER BY " + orderBy;
		}

		LOG.debug("Going to run query = " + query);

		PreparedStatement preparedStatementQuery = connection
				.prepareStatement(query);
		ResultSet resultSet = null;

		try {
			preparedStatementQuery = connection.prepareStatement(query);

			fillInColumnValues(bean, preparedStatementQuery);

			resultSet = preparedStatementQuery.executeQuery();
			results = getListFromRs(resultSet, connection);
		} catch (Exception e) {
			LOG.error("Exception: ", e);
			throw e;
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}

		LOG.debug(results.get(0).getFacilityname());
		return results;
	}

	public List<ResponseFacilityBean> findAllFacilities(
			final RequestFacilityBean bean, final String orderBy,
			Connection connection) throws Exception {
		List<ResponseFacilityBean> results = null;

		if (connection == null || connection.isClosed()) {
			LOG.debug("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String query = "Select * from Facility";
		if (orderBy != null) {
			query += " ORDER BY " + orderBy;
		}

		LOG.debug("Going to run query = " + query);

		PreparedStatement preparedStatementQuery = null;
		ResultSet resultSet = null;
		try {
			preparedStatementQuery = connection.prepareStatement(query);
			resultSet = preparedStatementQuery.executeQuery();
			results = getListFromRs(resultSet, connection);
		} catch (SQLException e) {
			LOG.error("SQLException: ", e);
			throw e;
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}

		LOG.debug(results.get(0).getFacilityname());
		return results;
	}

	public List<ResponseFacilityBean> findValid(final RequestFacilityBean bean,
			final String orderBy, Connection connection, boolean isSuperAdmin)
			throws Exception {
		List<ResponseFacilityBean> results = null;

		if (connection == null || connection.isClosed()) {
			LOG.debug("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String facilityClause = "";
		if (!isSuperAdmin)
			facilityClause = " AND FacilityID = ?";

		String query = "Select * from Facility WHERE Valid='1' AND Launch < NOW() AND (Expiration > NOW() OR Expiration = '0000-00-00 00:00:00')"
				+ facilityClause;
		if (orderBy != null) {
			query += " ORDER BY " + orderBy;
		}

		LOG.debug("Going to run query = " + query);

		final PreparedStatement preparedStatementQuery = connection
				.prepareStatement(query);

		if (!isSuperAdmin)
			preparedStatementQuery.setInt(1, bean.getFacilityid());
		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			results = getListFromRs(resultSet, connection);
		} catch (SQLException e) {
			LOG.error("SQLException: ", e);
			throw e;
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		return results;
	}

	// Order must be the same as fillInColumnNames
	private void fillInColumnValues(final RequestFacilityBean bean,
			final PreparedStatement preparedStatementQuery) throws SQLException {
		int index = 1;

		if (bean.getFacilityid() > 0) {
			preparedStatementQuery.setInt(index, bean.getFacilityid());
			index++;
		}
		if (bean.getFacilityname() != null) {
			preparedStatementQuery.setString(index, bean.getFacilityname());
			index++;
		}
		if (bean.getFacilityshortcut() != null) {
			preparedStatementQuery.setString(index, bean.getFacilityshortcut());
			index++;
		}
		if (bean.getFacilityemail() != null) {
			preparedStatementQuery.setString(index, bean.getFacilityemail());
			index++;
		}
		if (bean.getAdministratorid() != null) {
			preparedStatementQuery.setString(index, bean.getAdministratorid());
			index++;
		}
		if (bean.getValid() != null) {
			preparedStatementQuery.setBoolean(index, bean.getValid());
			index++;
		}
		if (bean.getLaunch() != null) {
			preparedStatementQuery.setString(index, bean.getLaunch());
			index++;
		}
		if (bean.getExpiration() != null) {
			preparedStatementQuery.setString(index, bean.getExpiration());
			index++;
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestFacilityBean bean) {
		String query = "Select * from Facility ";
		boolean first = true;

		if (bean.getFacilityid() > 0) {
			query = query + " " + (first ? WHERE : AND) + "ID "
					+ EQUALS_QUESTION;

			first = false;
		}
		if (bean.getFacilityname() != null) {
			query = query + " " + (first ? WHERE : AND) + "FacilityName"
					+ EQUALS_QUESTION;

			first = false;
		}
		if (bean.getFacilityshortcut() != null) {
			query = query + " " + (first ? WHERE : AND) + "FacilityShortcut"
					+ EQUALS_QUESTION;

			first = false;
		}
		if (bean.getFacilityemail() != null) {
			query = query + " " + (first ? WHERE : AND) + "FacilityEmail"
					+ EQUALS_QUESTION;

			first = false;
		}
		if (bean.getAdministratorid() != null) {
			query = query + " " + (first ? WHERE : AND) + "AdministratorID"
					+ EQUALS_QUESTION;

			first = false;
		}
		if (bean.getValid() != null) {
			query = query + " " + (first ? WHERE : AND) + "Valid"
					+ EQUALS_QUESTION;

			first = false;
		}
		if (bean.getLaunch() != null) {
			query = query + " " + (first ? WHERE : AND) + "Launch"
					+ EQUALS_QUESTION;

			first = false;
		}
		if (bean.getExpiration() != null) {
			query = query + " " + (first ? WHERE : AND) + "Expiration"
					+ EQUALS_QUESTION;

			first = false;
		}

		return query;
	}

	public int update(final RequestFacilityBean bean,
			final ResponseFacilityBean original, Connection connection)
			throws Exception {
		int result = -1;

		ICache<String, ResponseFacilityBean> facilityCache = EHCacheImpl
				.getDefaultInstance("facilityCache");

		// remove from facility from cache if exist
		facilityCache.delete(String.valueOf(bean.getFacilityid()));

		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.debug("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			int fieldcount = 0;
			String command = "update Facility SET ";
			if (bean.getFacilityname() != null
					&& (original == null || !original.getFacilityname().equals(
							bean.getFacilityname()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "FacilityName = ?";
				fieldcount++;
			}
			if (bean.getFacilityshortcut() != null
					&& (original == null || !original.getFacilityshortcut()
							.equals(bean.getFacilityshortcut()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "FacilityShortcut = ?";
				fieldcount++;
			}
			if (bean.getFacilityemail() != null
					&& (original == null || !original.getFacilityemail()
							.equals(bean.getFacilityemail()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "FacilityEmail = ?";
				fieldcount++;
			}
			if (bean.getAdministratorid() != null
					&& (original == null || !original.getAdministratorid()
							.equals(bean.getAdministratorid()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "AdministratorID = ?";
				fieldcount++;
			}
			if (bean.getValid() != null
					&& (original == null || original.getValid() != bean
							.getValid())) {
				if (fieldcount > 0)
					command += ", ";
				command += "Valid = ?";
				fieldcount++;
			}
			if (bean.getLaunch() != null
					&& (original == null || !original.getLaunch().equals(
							bean.getLaunch()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "Launch = ?";
				fieldcount++;
			}
			if (bean.getExpiration() != null
					&& (original == null || !original.getExpiration().equals(
							bean.getExpiration()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "Expiration = ?";
				fieldcount++;
			}

			command += " WHERE ID = ?";

			if (fieldcount > 0) {
				LOG.debug("Running query: " + command);
				PreparedStatement updateStatement = connection
						.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getFacilityname() != null
						&& (original == null || !original.getFacilityname()
								.equals(bean.getFacilityname()))) {
					updateStatement.setString(fieldnum, bean.getFacilityname());
					;
					fieldnum++;
				}
				if (bean.getFacilityshortcut() != null
						&& (original == null || !original.getFacilityshortcut()
								.equals(bean.getFacilityshortcut()))) {
					updateStatement.setString(fieldnum,
							bean.getFacilityshortcut());
					;
					fieldnum++;
				}
				if (bean.getFacilityemail() != null
						&& (original == null || !original.getFacilityemail()
								.equals(bean.getFacilityemail()))) {
					updateStatement
							.setString(fieldnum, bean.getFacilityemail());
					;
					fieldnum++;
				}
				if (bean.getAdministratorid() != null
						&& (original == null || !original.getAdministratorid()
								.equals(bean.getAdministratorid()))) {
					updateStatement.setString(fieldnum,
							bean.getAdministratorid());
					;
					fieldnum++;
				}
				if (bean.getValid() != null
						&& (original == null || original.getValid() != bean
								.getValid())) {
					updateStatement.setBoolean(fieldnum, bean.getValid());
					;
					fieldnum++;
				}
				if (bean.getLaunch() != null
						&& (original == null || !original.getLaunch().equals(
								bean.getLaunch()))) {
					updateStatement.setString(fieldnum, bean.getLaunch());
					fieldnum++;
				}
				if (bean.getExpiration() != null
						&& (original == null || !original.getExpiration()
								.equals(bean.getExpiration()))) {
					updateStatement.setString(fieldnum, bean.getExpiration());
					fieldnum++;
				}

				if (original != null)
					updateStatement.setInt(fieldnum, original.getFacilityid());
				else
					throw new Exception(
							"Invalid or Missing FacilityID in update");

				result = updateStatement.executeUpdate();

				updateStatement.close();
			}
			if (original != null && original.getFacilityid() > 0) {
				saveSettings(original.getFacilityid(), bean.getSettings(),
						original.getSettings(), connection);
				saveIps(original.getFacilityid(), bean.getIp(),
						original.getIp(), connection);
			}
		}
		return result;
	}

	private HashMap<String, String> getSettings(int facilityid,
			Connection connection) throws SQLException {
		final String command = "select * from FacilitySetting WHERE FacilityID = ?";

		HashMap<String, String> results = new HashMap<String, String>();
		final PreparedStatement preparedStatementQuery = connection
				.prepareStatement(command);
		preparedStatementQuery.setInt(1, facilityid);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			while (resultSet.next()) {
				String key = resultSet.getString("FacilitySettingName");
				String value = resultSet.getString("FacilitySettingValue");
				results.put(key, value);
			}
		} catch (SQLException e) {
			LOG.error("SQLException: ", e);
			throw e;
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0) {
			return results;
		}
		return null;
	}

	private void saveSettings(int facilityID, HashMap<String, String> settings,
			HashMap<String, String> oldSettings, Connection connection)
			throws Exception {
		final String command = "insert into FacilitySetting (FacilityID, FacilitySettingName, FacilitySettingValue) values(?,?,?)";
		final String updcommand = "update FacilitySetting set FacilitySettingValue = ? WHERE FacilityID = ? and FacilitySettingName = ?";
		final String delcommand = "delete from FacilitySetting WHERE FacilityID = ? and FacilitySettingName = ?";

		// Insert into FacilitySetting
		if (settings != null) {
			Iterator<String> it = settings.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = settings.get(key);
				String oldValue = null;
				if (oldSettings != null) {
					oldValue = oldSettings.get(key);
				}
				if (value != null && oldValue == null) {
					PreparedStatement insertStatement = connection
							.prepareStatement(command);
					insertStatement.clearParameters();
					insertStatement.setInt(1, facilityID);
					insertStatement.setString(2, key);
					insertStatement.setString(3, value);
					insertStatement.executeUpdate();
					insertStatement.close();
				} else if (value != null && !value.equals(oldValue)) {
					PreparedStatement updateStatement = connection
							.prepareStatement(updcommand);
					updateStatement.clearParameters();
					updateStatement.setString(1, value);
					updateStatement.setInt(2, facilityID);
					updateStatement.setString(3, key);
					updateStatement.executeUpdate();
					updateStatement.close();
				}
			}
		}
		if (oldSettings != null) {
			Iterator<String> it = oldSettings.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = null;
				if (settings != null) {
					value = settings.get(key);
				}
				if (value == null) {
					PreparedStatement deleteStatement = connection
							.prepareStatement(delcommand);
					deleteStatement.clearParameters();
					deleteStatement.setInt(1, facilityID);
					deleteStatement.setString(2, key);
					deleteStatement.executeUpdate();
					deleteStatement.close();
				}
			}
		}

	}

	private FacilityIPBean[] getIps(int facilityid, Connection connection)
			throws SQLException {
		final String command = "select * from Facility_IP WHERE FacilityID = ?";

		ArrayList<FacilityIPBean> results = new ArrayList<FacilityIPBean>();
		final PreparedStatement preparedStatementQuery = connection
				.prepareStatement(command);
		preparedStatementQuery.setInt(1, facilityid);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			while (resultSet.next()) {
				FacilityIPBean fip = new FacilityIPBean();
				fip.setIpfrom(resultSet.getLong("ip_from"));
				fip.setIpto(resultSet.getLong("ip_to"));
				results.add(fip);
			}
		} catch (SQLException e) {
			LOG.error("SQLException: ", e);
			throw e;
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0) {
			FacilityIPBean[] ar = new FacilityIPBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}

	private void saveIps(int facilityID, FacilityIPBean[] ip,
			FacilityIPBean[] oldIp, Connection connection) throws Exception {
		final String command2 = "insert into Facility_IP (FacilityID, ip_from, ip_to) values(?, ?, ?)";
		final String delcommand = "delete from Facility_IP WHERE FacilityID = ? and ip_from = ?";

		// Insert into FacilityIP
		if (ip != null) {
			for (int i = 0; i < ip.length; i++) {
				FacilityIPBean fib = ip[i];
				if (fib != null) {
					boolean save = false;
					if (oldIp == null) {
						save = true;
					} else {
						boolean match = false;
						for (int j = 0; j < oldIp.length; j++) {
							if (fib.equals(oldIp[j]))
								match = true;
						}
						if (!match)
							save = true;
					}
					if (save) {
						PreparedStatement insertStatement = connection
								.prepareStatement(command2);
						insertStatement.clearParameters();
						insertStatement.setInt(1, facilityID);
						insertStatement.setLong(2, fib.getIpfrom());
						insertStatement.setLong(3, fib.getIpto());
						insertStatement.executeUpdate();
						insertStatement.close();
					}
				}
			}
		}
		// Delete from FacilityIP
		if (oldIp != null) {
			for (int i = 0; i < oldIp.length; i++) {
				FacilityIPBean fib = oldIp[i];
				if (fib != null) {
					boolean match = false;
					for (int j = 0; j < ip.length; j++) {
						if (fib.equals(ip[j]))
							match = true;
					}
					if (!match) {
						PreparedStatement delStatement = connection
								.prepareStatement(delcommand);
						delStatement.clearParameters();
						delStatement.setInt(1, facilityID);
						delStatement.setLong(2, fib.getIpfrom());
						delStatement.executeUpdate();
						delStatement.close();
					}
				}
			}
		}

	}

}
