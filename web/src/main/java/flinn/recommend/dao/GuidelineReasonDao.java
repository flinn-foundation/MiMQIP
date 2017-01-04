package flinn.recommend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.request.RequestGuidelineReasonBean;
import flinn.beans.response.ResponseGuidelineReasonBean;

public abstract class GuidelineReasonDao {
	protected static final Logger LOG = Logger.getLogger(GuidelineReasonDao.class);

	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	static {
		LOG.debug("Log appender instantiated for " + GuidelineReasonDao.class);
	}

	public ResponseGuidelineReasonBean getBeanFromRs(final ResultSet resultSet,
			Connection connection) throws SQLException {
		final ResponseGuidelineReasonBean bean = new ResponseGuidelineReasonBean();

		bean.setReasonid(resultSet.getInt("ID"));
		bean.setPatientid(resultSet.getInt("PatientID"));
		bean.setReasondate(resultSet.getString("ReasonDate"));
 		//Pull DB record into reason array.
		String[] reason = new String [1];
 		reason[0] = resultSet.getString("Reason");
		bean.setReason(reason);
		bean.setDoctorname(resultSet.getString("DoctorName"));

		return bean;
	}

	public List<ResponseGuidelineReasonBean> getListFromRs(final ResultSet resultSet,
			Connection connection) throws SQLException {
		final List<ResponseGuidelineReasonBean> results = new ArrayList<ResponseGuidelineReasonBean>();
		ResponseGuidelineReasonBean bean = null;
		while (resultSet.next()) {
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestGuidelineReasonBean bean, Connection connection)
			throws Exception {
		int reasonID = 0;
		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into RecommendReason (PatientID, ReasonDate, Reason, DoctorName) "
					+ " values(" + "?, ?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection
					.prepareStatement(command);

			// Insert into RecommendReason
			insertStatement.setInt(1, bean.getPatientid());
			insertStatement.setString(2, flinn.util.DateString.now());
			insertStatement.setString(3, arrayToString(bean.getReason(), ","));
			insertStatement.setString(4, bean.getDoctorname());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next()) {
				reasonID = rs.getInt(1);
			}

			insertStatement.close();
		}
		return reasonID;
	}

	public List<ResponseGuidelineReasonBean> find(final RequestGuidelineReasonBean bean,
			final String orderBy, Connection connection) throws Exception {
		List<ResponseGuidelineReasonBean> results = null;

		if (connection == null || connection.isClosed()) {
			LOG.error("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String query = fillInColumnNames(bean);
		if (orderBy != null) {
			query += " ORDER BY " + orderBy;
		}

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
	private void fillInColumnValues(final RequestGuidelineReasonBean bean,
			final PreparedStatement preparedStatementQuery) throws SQLException {
		int index = 1;

		if (bean.getReasonid() != 0) {
			preparedStatementQuery.setInt(index, bean.getReasonid());
			index++;
		}
		if (bean.getPatientid() != 0) {
			preparedStatementQuery.setInt(index, bean.getPatientid());
			index++;
		}
		if (bean.getReasondate() != null) {
			preparedStatementQuery.setString(index, bean.getReasondate());
			index++;
		}
		if (bean.getReason() != null) {
			preparedStatementQuery.setString(index, Arrays.toString(bean.getReason()));
			index++;
		}
		if (bean.getDoctorname() != null) {
			preparedStatementQuery.setString(index, bean.getDoctorname());
			index++;
		}		
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestGuidelineReasonBean bean) {
		String query = "Select * from RecommendReason ";
		boolean first = true;

		if (bean.getReasonid() != 0) {
			query = query + " " + (first ? WHERE : AND) + "ID"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getPatientid() != 0) {
			query = query + " " + (first ? WHERE : AND) + "PatientID"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getReasondate() != null) {
			query = query + " " + (first ? WHERE : AND) + "ReasonDate"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getReason() != null) {
			query = query + " " + (first ? WHERE : AND) + "Reason"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getDoctorname() != null) {
			query = query + " " + (first ? WHERE : AND) + "DoctorName"
					+ EQUALS_QUESTION;
			first = false;
		}
		return query;
	}
	
	public String arrayToString(String[] array, String delimiter) {
	    StringBuilder arTostr = new StringBuilder();
	    if (array.length > 0) {
	        arTostr.append(array[0]);
	        for (int i=1; i<array.length; i++) {
	            arTostr.append(delimiter);
	            arTostr.append(array[i]);
	        }
	    }
	    return arTostr.toString();
	}
}
