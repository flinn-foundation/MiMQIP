package flinn.recommend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.recommend.beans.request.RequestMessageBean;
import flinn.recommend.beans.response.ResponseMessageBean;

public abstract class MessageDao {

	protected static final Logger LOG = Logger.getLogger(MessageDao.class);

	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	static {
		LOG.debug("Log appender instantiated for " + MessageDao.class);
	}

	public ResponseMessageBean getBeanFromRs(final ResultSet resultSet,
			Connection connection) throws SQLException {
		final ResponseMessageBean bean = new ResponseMessageBean();

		bean.setMessageid(resultSet.getInt("MessageID"));
		bean.setPriority(resultSet.getInt("Priority"));
		bean.setMessage(resultSet.getString("Message"));
		bean.setMessagetag(resultSet.getString("MessageTag"));

		return bean;
	}

	public List<ResponseMessageBean> getListFromRs(final ResultSet resultSet,
			Connection connection) throws SQLException {
		final List<ResponseMessageBean> results = new ArrayList<ResponseMessageBean>();
		ResponseMessageBean bean = null;
		while (resultSet.next()) {
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestMessageBean bean, Connection connection)
			throws Exception {
		int messageID = 0;
		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into RecommendMessage (Priority, Message, MessageTag) "
					+ " values(" + "?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection
					.prepareStatement(command);

			// Insert into RecommendMessage
			insertStatement.setInt(1, bean.getPriority());
			insertStatement.setString(2, bean.getMessage());
			insertStatement.setString(3, bean.getMessagetag());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next()) {
				messageID = rs.getInt(1);
			}

			insertStatement.close();
		}
		return messageID;
	}

	public int update(final RequestMessageBean bean,
			final ResponseMessageBean original, Connection connection)
			throws Exception {
		int result = -1;
		if (bean != null) {
			if (connection == null || connection.isClosed()) {
				LOG.debug("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			int fieldcount = 0;
			String command = "update RecommendMessage SET ";
			if (bean.getPriority() > 0
					&& (original == null || original.getPriority() != bean
							.getPriority())) {
				if (fieldcount > 0)
					command += ", ";
				command += "Priority = ?";
				fieldcount++;
			}

			if (bean.getMessage() != null
					&& (original == null || !original.getMessage().equals(
							bean.getMessage()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "Message = ?";
				fieldcount++;
			}

			if (bean.getMessagetag() != null
					&& (original == null || !original.getMessagetag().equals(
							bean.getMessagetag()))) {
				if (fieldcount > 0)
					command += ", ";
				command += "MessageTag = ?";
				fieldcount++;
			}

			command += " WHERE MessageID = ?";

			if (fieldcount > 0) {
				LOG.warn("Running query: " + command);
				final PreparedStatement updateStatement = connection
						.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getPriority() > 0
						&& (original == null || original.getPriority() != bean
								.getPriority())) {
					updateStatement.setInt(fieldnum, bean.getPriority());
					;
					fieldnum++;
				}
				if (bean.getMessage() != null
						&& (original == null || !original.getMessage().equals(
								bean.getMessage()))) {
					updateStatement.setString(fieldnum, bean.getMessage());
					fieldnum++;
				}
				if (bean.getMessagetag() != null
						&& (original == null || !original.getMessagetag()
								.equals(bean.getMessagetag()))) {
					updateStatement.setString(fieldnum, bean.getMessagetag());
					fieldnum++;
				}
				if (original != null)
					updateStatement.setInt(fieldnum, original.getMessageid());
				else
					throw new Exception(
							"Invalid or Missing RecommendMessageID in update");

				result = updateStatement.executeUpdate();
				updateStatement.close();
			}
		}
		return result;
	}

	public List<ResponseMessageBean> find(final RequestMessageBean bean,
			final String orderBy, Connection connection) throws Exception {
		List<ResponseMessageBean> results = null;

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

	public List<ResponseMessageBean> findAllMessages(
			final RequestMessageBean bean, final String orderBy,
			Connection connection) throws Exception {
		List<ResponseMessageBean> results = null;

		if (connection == null || connection.isClosed()) {
			LOG.debug("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String query = "Select * from RecommendMessage";
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
			LOG.debug(results.get(0).getMessagetag());
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
	private void fillInColumnValues(final RequestMessageBean bean,
			final PreparedStatement preparedStatementQuery) throws SQLException {
		int index = 1;

		if (bean.getMessageid() != 0) {
			preparedStatementQuery.setInt(index, bean.getMessageid());
			index++;
		}
		if (bean.getPriority() != 0) {
			preparedStatementQuery.setInt(index, bean.getPriority());
			index++;
		}
		if (bean.getMessage() != null) {
			preparedStatementQuery.setString(index, bean.getMessage());
			index++;
		}
		if (bean.getMessagetag() != null) {
			preparedStatementQuery.setString(index, bean.getMessagetag());
			index++;
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestMessageBean bean) {
		String query = "Select * from RecommendMessage ";
		boolean first = true;

		if (bean.getMessageid() != 0) {
			query = query + " " + (first ? WHERE : AND) + "MessageID "
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getPriority() != 0) {
			query = query + " " + (first ? WHERE : AND) + "Priority"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getMessage() != null) {
			query = query + " " + (first ? WHERE : AND) + "Message"
					+ EQUALS_QUESTION;
			first = false;
		}
		if (bean.getMessagetag() != null) {
			query = query + " " + (first ? WHERE : AND) + "MessageTag"
					+ EQUALS_QUESTION;
			first = false;
		}

		return query;
	}

}
