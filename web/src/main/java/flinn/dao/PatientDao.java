package flinn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.PatientDetailsBean;
import flinn.beans.PatientStatusBean;
import flinn.beans.request.RequestPatientBean;
import flinn.beans.response.ResponsePatientBean;
import flinn.util.cache.EHCacheImpl;
import flinn.util.cache.ICache;

public abstract class PatientDao {

	protected static final Logger LOG = Logger.getLogger(PatientDao.class);

	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	static {
		LOG.debug("Log appender instantiated for " + PatientDao.class);
	}

	public ResponsePatientBean getBeanFromRs(final ResultSet resultSet,
			Connection connection) throws SQLException {
		ICache<String, ResponsePatientBean> patientCache = EHCacheImpl
				.getDefaultInstance("patientCache");

		ResponsePatientBean bean = null;
		int id = resultSet.getInt("Patient.ID");

		// try to retrieve the object from the cache by id
		bean = patientCache.get(String.valueOf(id));

		if (bean != null) {
			LOG.debug("findPatientById --> ID: " + String.valueOf(id)
					+ " found in cache");
		}

		// if the returned object is null, it was not found in the cache
		if (bean == null) {

			bean = new ResponsePatientBean();
			bean.setPatientid(resultSet.getInt("Patient.ID"));
			bean.setFacilityid(resultSet.getInt("Patient.FacilityID"));
			bean.setValid(resultSet.getBoolean("Patient.Valid"));
			bean.setStartdate(resultSet.getString("Patient.StartDate"));
			bean.setRcopiaLastUpdatedDate(resultSet
					.getTimestamp("Patient.RcopiaLastUpdatedDate"));
			if (bean.getPatientid() > 0) {
				bean.setDetails(getDetails(bean.getPatientid(), connection));
				bean.setStatus(getStatus(bean.getPatientid(), connection));
			}

			patientCache.put(String.valueOf(bean.getPatientid()), bean);
		}
		return bean;
	}

	public List<ResponsePatientBean> getListFromRs(final ResultSet resultSet,
			Connection connection) throws SQLException {
		final List<ResponsePatientBean> results = new ArrayList<ResponsePatientBean>();
		ResponsePatientBean bean = null;
		while (resultSet.next()) {
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestPatientBean bean, Connection connection)
			throws Exception {
		int patientid = 0;
		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into Patient (FacilityID, Valid, StartDate) "
					+ " values(" + "?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection
					.prepareStatement(command);

			// Insert into AppUser
			insertStatement.setInt(1, bean.getFacilityid());
			insertStatement.setBoolean(2, bean.getValid());
			if (bean.getStartdate() != null)
				insertStatement.setString(3, bean.getStartdate());
			else
				insertStatement.setString(3, flinn.util.DateString.now());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next()) {
				patientid = rs.getInt(1);
			}

			if (patientid > 0) {
				saveStatus(patientid, bean.getStatus(), null, connection);
				saveDetails(patientid, bean.getDetails(), null, connection);
			}

			insertStatement.close();
		}
		return patientid;
	}

	public List<ResponsePatientBean> find(final RequestPatientBean bean,
			final String orderBy, Connection connection) throws Exception {
		List<ResponsePatientBean> results = null;

		if (connection == null || connection.isClosed()) {
			LOG.error("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		final String query = fillInColumnNames(bean);

		LOG.debug("Going to run query = " + query);

		final PreparedStatement preparedStatementQuery = connection
				.prepareStatement(query);

		fillInColumnValues(bean, preparedStatementQuery);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			results = getListFromRs(resultSet, connection);
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		return results;
	}

	// Order must be the same as fillInColumnNames
	private void fillInColumnValues(final RequestPatientBean bean,
			final PreparedStatement preparedStatementQuery) throws SQLException {
		int index = 1;

		if (bean.getPatientid() != 0) {
			preparedStatementQuery.setInt(index, bean.getPatientid());
			index++;
		}
		if (bean.getFacilityid() != 0) {
			preparedStatementQuery.setInt(index, bean.getFacilityid());
			index++;
		}
		if (bean.getValid() != null) {
			preparedStatementQuery.setBoolean(index, bean.getValid());
			index++;
		}
		if (bean.getStartdate() != null) {
			preparedStatementQuery.setString(index, bean.getStartdate());
			index++;
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestPatientBean bean) {
		String query = "Select * from Patient ";
		boolean first = true;

		if (bean.getPatientid() != 0) {
			query = query + " " + (first ? WHERE : AND) + "ID "
					+ EQUALS_QUESTION;
			first = false;

		}
		if (bean.getFacilityid() != 0) {
			query = query + " " + (first ? WHERE : AND) + "FacilityID "
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getValid() != null) {
			query = query + " " + (first ? WHERE : AND) + "Valid"
					+ EQUALS_QUESTION;
			first = false;

		}
		if (bean.getStartdate() != null) {
			query = query + " " + (first ? WHERE : AND) + "StartDate"
					+ EQUALS_QUESTION;
			first = false;
		}

		return query;
	}

	public int update(final RequestPatientBean bean,
			final ResponsePatientBean original, Connection connection)
			throws Exception {
		int result = -1;

		ICache<String, ResponsePatientBean> patientCache = EHCacheImpl
				.getDefaultInstance("patientCache");

		// remove from patient from cache if exist
		patientCache.delete(String.valueOf(bean.getPatientid()));

		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			int fieldcount = 0;
			String command = "update Patient SET ";

			// Patient

			if (bean.getFacilityid() != 0
					&& (original == null || !(original.getFacilityid() == bean
							.getFacilityid()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "FacilityID = ?";
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
			if (bean.getStartdate() != null
					&& (original == null || !original.getStartdate().equals(
							bean.getStartdate()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "StartDate = ?";
				fieldcount++;
			}

			command += " WHERE ID = ?";

			if (fieldcount > 0) {
				LOG.debug("Running query: " + command);
				PreparedStatement updateStatement = connection
						.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getFacilityid() != 0
						&& (original == null || !(original.getFacilityid() == bean
								.getFacilityid()))) {
					updateStatement.setInt(fieldnum, bean.getFacilityid());
					fieldnum++;
				}
				if (bean.getValid() != null
						&& (original == null || !original.getValid().equals(
								bean.getValid()))) {
					updateStatement.setBoolean(fieldnum, bean.getValid());
					fieldnum++;
				}
				if (bean.getStartdate() != null
						&& (original == null || !original.getStartdate()
								.equals(bean.getStartdate()))) {
					updateStatement.setString(fieldnum, bean.getStartdate());
					fieldnum++;
				}

				if (original != null)
					updateStatement.setInt(fieldnum, original.getPatientid());
				else
					throw new Exception(
							"Invalid or Missing PatientID in update");

				result = updateStatement.executeUpdate();

				updateStatement.close();
			}
			if (original != null && original.getPatientid() > 0) {
				saveDetails(bean.getPatientid(), bean.getDetails(),
						original.getDetails(), connection);
				saveStatus(bean.getPatientid(), bean.getStatus(),
						original.getStatus(), connection);
			}
		}
		return result;
	}

	public int updateRcopiaDate(int patientId, Date lastModifiedDate,
			Connection connection) throws Exception {
		int result = -1;
		String sql = "update Patient set RcopiaLastUpdatedDate = ? where ID = ? and (RcopiaLastUpdatedDate is null or RcopiaLastUpdatedDate < ?)";

		PreparedStatement ps = connection.prepareStatement(sql);
		Timestamp rcopiaLastUpdatedTime = new Timestamp(
				lastModifiedDate.getTime());
		ps.setTimestamp(1, rcopiaLastUpdatedTime);
		ps.setInt(2, patientId);
		ps.setTimestamp(3, rcopiaLastUpdatedTime);
		result = ps.executeUpdate();
		LOG.debug("result=" + result + ", lastModifiedDate=" + lastModifiedDate);
		ps.close();

		return result;
	}

	private HashMap<String, PatientStatusBean[]> getStatus(int patientID,
			Connection connection) throws SQLException {
		final String command = "select PatientStatusName, PatientStatusValue, EntryDate from PatientStatus WHERE PatientID = ? ORDER BY EntryDate DESC";

		HashMap<String, ArrayList<PatientStatusBean>> results = new HashMap<String, ArrayList<PatientStatusBean>>();
		final PreparedStatement preparedStatementQuery = connection
				.prepareStatement(command);
		preparedStatementQuery.setInt(1, patientID);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			while (resultSet.next()) {
				String key = resultSet.getString("PatientStatusName");
				PatientStatusBean value = new PatientStatusBean();
				value.setValue(resultSet.getString("PatientStatusValue"));
				value.setEntrydate(resultSet.getString("EntryDate"));
				if (results.get(key) == null) {
					results.put(key, new ArrayList<PatientStatusBean>());
				}
				results.get(key).add(value);
			}
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0) {
			HashMap<String, PatientStatusBean[]> returnval = new HashMap<String, PatientStatusBean[]>();
			Iterator<String> it = results.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				PatientStatusBean[] values = new PatientStatusBean[results.get(
						key).size()];
				returnval.put(key, results.get(key).toArray(values));
			}
			return returnval;
		}
		return null;
	}

	private void saveStatus(int patientID, HashMap<String, String> status,
			HashMap<String, PatientStatusBean[]> oldStatus,
			Connection connection) throws Exception {
		final String command = "insert into PatientStatus (PatientID, PatientStatusName, PatientStatusValue, EntryDate) values(?,?,?,NOW())";

		// Insert into PatientStatus
		if (status != null) {
			Iterator<String> it = status.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = status.get(key);
				String oldValue = null;

				// We want to save all new status records.
				// Unlike details, we don't "carry-over" values for status, we
				// re-save them,
				// because a value that isn't present has significance for
				// evaluations.

				// if (oldStatus != null && oldStatus.get(key) != null) {
				// oldValue = oldStatus.get(key)[0].getValue();
				// }
				if ((value != null && oldValue == null)
						|| (value != null && !value.equals(oldValue))) {
					PreparedStatement insertStatement = connection
							.prepareStatement(command);
					insertStatement.clearParameters();
					insertStatement.setInt(1, patientID);
					insertStatement.setString(2, key);
					insertStatement.setString(3, value);
					insertStatement.executeUpdate();
					insertStatement.close();
				}
			}
		}
	}

	private HashMap<String, PatientDetailsBean[]> getDetails(int PatientID,
			Connection connection) throws SQLException {
		final String command = "select PatientDetailName, PatientDetailValue, EntryDate from PatientDetail WHERE PatientID = ? ORDER BY EntryDate DESC";

		LOG.debug("Patient details query: " + command);

		HashMap<String, ArrayList<PatientDetailsBean>> results = new HashMap<String, ArrayList<PatientDetailsBean>>();
		final PreparedStatement preparedStatementQuery = connection
				.prepareStatement(command);
		preparedStatementQuery.setInt(1, PatientID);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try {
			while (resultSet.next()) {
				String key = resultSet.getString("PatientDetailName");
				PatientDetailsBean value = new PatientDetailsBean();
				value.setValue(resultSet.getString("PatientDetailValue"));
				value.setEntrydate(resultSet.getString("EntryDate").substring(
						0, 10));
				if (results.get(key) == null) {
					results.put(key, new ArrayList<PatientDetailsBean>());
				}
				results.get(key).add(value);
			}
		} finally {
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0) {
			HashMap<String, PatientDetailsBean[]> returnval = new HashMap<String, PatientDetailsBean[]>();
			Iterator<String> it = results.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				PatientDetailsBean[] values = new PatientDetailsBean[results
						.get(key).size()];
				returnval.put(key, results.get(key).toArray(values));
			}
			return returnval;
		}
		return null;
	}

	private void saveDetails(int patientID, HashMap<String, String> details,
			HashMap<String, PatientDetailsBean[]> oldDetails,
			Connection connection) throws Exception {
		final String command = "insert into PatientDetail (PatientID, PatientDetailName, PatientDetailValue, EntryDate) values(?,?,?,NOW())";

		// Insert into PatientDetail
		if (details != null) {
			Iterator<String> it = details.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = details.get(key);
				String oldValue = null;
				if (oldDetails != null && oldDetails.get(key) != null) {
					oldValue = oldDetails.get(key)[0].getValue();
				}
				if ((value != null && oldValue == null)
						|| (value != null && !value.equals(oldValue))) {
					PreparedStatement insertStatement = connection
							.prepareStatement(command);
					insertStatement.clearParameters();
					insertStatement.setInt(1, patientID);
					insertStatement.setString(2, key);
					insertStatement.setString(3, value);
					insertStatement.executeUpdate();
					insertStatement.close();
				}
			}
		}
	}

}
