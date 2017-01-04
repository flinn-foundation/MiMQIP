package flinn.recommend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.recommend.beans.request.RequestSettingBean;
import flinn.recommend.beans.response.ResponseSettingBean;

public abstract class SettingDao {

	protected static final Logger LOG = Logger.getLogger(SettingDao.class);

	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	static {
		LOG.debug("Log appender instantiated for " + SettingDao.class);
	}

	public ResponseSettingBean getBeanFromRs(final ResultSet resultSet,
			Connection connection) throws SQLException {
		final ResponseSettingBean bean = new ResponseSettingBean();

		bean.setSettingid(resultSet.getInt("ID"));
		bean.setSettingname(resultSet.getString("SettingName"));
		bean.setSettingvalue(resultSet.getString("SettingValue"));

		return bean;
	}

	public List<ResponseSettingBean> getListFromRs(final ResultSet resultSet,
			Connection connection) throws SQLException {
		final List<ResponseSettingBean> results = new ArrayList<ResponseSettingBean>();
		ResponseSettingBean bean = null;
		while (resultSet.next()) {
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestSettingBean bean, Connection connection)
			throws Exception {
		int messageID = 0;
		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into RecommendSetting (SettingName, SettingValue) "
					+ " values(" + "?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection
					.prepareStatement(command);

			// Insert into RecommendSetting
			insertStatement.setString(1, bean.getSettingname());
			insertStatement.setString(2, bean.getSettingvalue());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next()) {
				messageID = rs.getInt(1);
			}

			insertStatement.close();
		}
		return messageID;
	}

	public int update(final RequestSettingBean bean,
			final ResponseSettingBean original, Connection connection)
			throws Exception {
		int result = -1;
		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.debug("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			int fieldcount = 0;
			String command = "update RecommendSetting SET ";
			if (bean.getSettingname() != null
					&& (original == null || !original.getSettingname().equals(
							bean.getSettingname()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "SettingName = ?";
				fieldcount++;
			}

			if (bean.getSettingvalue() != null
					&& (original == null || !original.getSettingvalue().equals(
							bean.getSettingvalue()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "SettingValue = ?";
				fieldcount++;
			}

			command += " WHERE ID = ?";

			if (fieldcount > 0) {
				LOG.warn("Running query: " + command);
				final PreparedStatement updateStatement = connection
						.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getSettingname() != null
						&& (original == null || !original.getSettingname()
								.equals(bean.getSettingname()))) {
					updateStatement.setString(fieldnum, bean.getSettingname());
					fieldnum++;
				}
				if (bean.getSettingvalue() != null
						&& (original == null || !original.getSettingvalue()
								.equals(bean.getSettingvalue()))) {
					updateStatement.setString(fieldnum, bean.getSettingvalue());
					fieldnum++;
				}
				if (original != null)
					updateStatement.setInt(fieldnum, original.getSettingid());
				else
					throw new Exception(
							"Invalid or Missing RecommendSettingID in update");

				result = updateStatement.executeUpdate();
				updateStatement.close();
			}
		}
		return result;
	}

	public List<ResponseSettingBean> find(final RequestSettingBean bean,
			final String orderBy, Connection connection) throws Exception {
		List<ResponseSettingBean> results = null;

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

	public List<ResponseSettingBean> findAllSettings(
			final RequestSettingBean bean, final String orderBy,
			Connection connection) throws Exception {
		List<ResponseSettingBean> results = null;

		if (connection == null || connection.isClosed()) {
			LOG.debug("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String query = "Select * from RecommendSetting";
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
			LOG.debug(results.get(0).getSettingname());
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
	private void fillInColumnValues(final RequestSettingBean bean,
			final PreparedStatement preparedStatementQuery) throws SQLException {
		int index = 1;

		if (bean.getSettingid() != 0) {
			preparedStatementQuery.setInt(index, bean.getSettingid());
			index++;
		}
		if (bean.getSettingname() != null) {
			preparedStatementQuery.setString(index, bean.getSettingname());
			index++;
		}
		if (bean.getSettingvalue() != null) {
			preparedStatementQuery.setString(index, bean.getSettingvalue());
			index++;
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestSettingBean bean) {
		String query = "Select * from RecommendSetting ";
		boolean first = true;

		if (bean.getSettingid() != 0) {
			query = query + " " + (first ? WHERE : AND) + "ID "
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getSettingname() != null) {
			query = query + " " + (first ? WHERE : AND) + "SettingName"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getSettingvalue() != null) {
			query = query + " " + (first ? WHERE : AND) + "SettingValue"
					+ EQUALS_QUESTION;
			first = false;
		}

		return query;
	}

}
