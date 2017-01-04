package flinn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;
import org.apache.log4j.Logger;

import flinn.beans.ProgressNoteTagBean;
import flinn.beans.request.RequestProgressNoteBean;
import flinn.beans.response.ResponseProgressNoteBean;
import flinn.beans.response.ResponseSessionContainerBean;

public abstract class ProgressNoteDao
{

	protected static final Logger LOG = Logger.getLogger(ProgressNoteDao.class);

	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	static
	{
		LOG.debug("Log appender instantiated for " + ProgressNoteDao.class);
	}

	public ResponseProgressNoteBean getBeanFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final ResponseProgressNoteBean bean = new ResponseProgressNoteBean();
		bean.setPatientid(resultSet.getInt("PatientID"));
		bean.setNotetext(resultSet.getString("NoteText"));
		bean.setDoctorname(resultSet.getString("DoctorName"));
		bean.setAuthorid(resultSet.getString("AppUserID"));
		bean.setEntrydate(resultSet.getString("EntryDate"));
		bean.setProgressnoteid(resultSet.getInt("ID"));

		if (bean.getProgressnoteid() > 0)
		{
			bean.setTags(getTags(bean.getProgressnoteid(), connection));
		}
		return bean;
	}

	public List<ResponseProgressNoteBean> getListFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final List<ResponseProgressNoteBean> results = new ArrayList<ResponseProgressNoteBean>();
		ResponseProgressNoteBean bean = null;
		while (resultSet.next())
		{
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestProgressNoteBean bean, ResponseSessionContainerBean session, Connection connection) throws Exception
	{
		int progressnoteid = 0;
		if (bean != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into ProgressNote (PatientID, AppUserID, EntryDate, DoctorName, Notetext) " + " values(" + "?, ?, ?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);

			//Insert into AppUser
			insertStatement.setInt(1, bean.getPatientid());
			insertStatement.setInt(2, session.getUser().getAppuserid());
			insertStatement.setString(3, flinn.util.DateString.now());
			insertStatement.setString(4, session.getUser().getSettings().get("FullName"));
			insertStatement.setString(5, bean.getNotetext());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next())
			{
				progressnoteid = rs.getInt(1);
			}

			if (progressnoteid > 0)
			{
				saveTags(progressnoteid, bean.getTags(), null, connection);
			}

			insertStatement.close();
		}
		return progressnoteid;
	}

	public int createTag(final ProgressNoteTagBean bean, ResponseSessionContainerBean session, Connection connection) throws Exception
	{
		int progressnotetagid = 0;
		if (bean != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into ProgressNoteTag (Valid, TagName, TagDescription) " + " values(" + "?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection.prepareStatement(command);

			//Insert into ProgressNoteTag
			insertStatement.setBoolean(1, bean.getValid());
			insertStatement.setString(2, bean.getProgressnotetag());
			insertStatement.setString(3, bean.getProgressnotetagdescription());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next())
			{
				progressnotetagid = rs.getInt(1);
			}

			insertStatement.close();
		}
		return progressnotetagid;
	}

	public List<ResponseProgressNoteBean> find(final RequestProgressNoteBean bean, final String orderBy, Connection connection) throws Exception
	{
		List<ResponseProgressNoteBean> results = null;

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

		fillInColumnValues(bean, preparedStatementQuery);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
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
	private void fillInColumnValues(final RequestProgressNoteBean bean, final PreparedStatement preparedStatementQuery) throws SQLException
	{
		int index = 1;

		if (bean.getPatientid() != 0)
		{
			preparedStatementQuery.setInt(index, bean.getPatientid());
			index++;
		}
		if (bean.getNotetext() != null)
		{
			preparedStatementQuery.setString(index, bean.getNotetext());
			index++;
		}
		if (bean.getDoctorname() != null)
		{
			preparedStatementQuery.setString(index, bean.getDoctorname());
			index++;
		}
		if (bean.getAuthorid() != null)
		{
			preparedStatementQuery.setString(index, bean.getAuthorid());
			index++;
		}
		if (bean.getEntrydate() != null)
		{
			preparedStatementQuery.setString(index, bean.getEntrydate());
			index++;
		}
		if (bean.getProgressnoteid() != 0)
		{
			preparedStatementQuery.setInt(index, bean.getProgressnoteid());
			index++;
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestProgressNoteBean bean)
	{
		String query = "Select * from ProgressNote ";
		boolean first = true;

		if (bean.getPatientid() != 0)
		{
			query = query + " " + (first ? WHERE : AND) + "PatientID " + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getNotetext() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "NoteText" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getDoctorname() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "DoctorName" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getAuthorid() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "AppUserID" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getEntrydate() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "EntryDate" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getProgressnoteid() != 0)
		{
			query = query + " " + (first ? WHERE : AND) + "ID" + EQUALS_QUESTION;
			first = false;
		}

		return query;
	}

	private ProgressNoteTagBean [] getTags(int progressnoteid, Connection connection) throws SQLException
	{
		final String command = "select PT.ID, PT.TagName from ProgressNote_ProgressNoteTag PNPT, ProgressNoteTag PT " + "where PNPT.ProgressNoteId = ? AND PNPT.ProgressNoteTagId = PT.ID";

		ArrayList<ProgressNoteTagBean> results = new ArrayList<ProgressNoteTagBean>();
		final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);
		preparedStatementQuery.setInt(1, progressnoteid);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
			while (resultSet.next())
			{
				ProgressNoteTagBean tag = new ProgressNoteTagBean();
				tag.setProgressnotetagid(resultSet.getInt("PT.ID"));
				tag.setProgressnotetag(resultSet.getString("PT.TagName"));
				results.add(tag);
			}
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0)
		{
			ProgressNoteTagBean [] ar = new ProgressNoteTagBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}

	private void saveTags(int progressnoteid, ProgressNoteTagBean [] tags, ProgressNoteTagBean [] oldTags, Connection connection) throws Exception
	{
		final String command2 = "insert into ProgressNote_ProgressNoteTag (ProgressNoteId, ProgressNoteTagId) values(?,?)";

		//Insert into ProgressNote_ProgressNoteTag
		if (tags != null)
		{
			for (int i = 0; i < tags.length; i++)
			{
				ProgressNoteTagBean tag = tags[i];
				if (tag != null && tag.getProgressnotetagid() > 0)
				{
					boolean save = false;
					if (oldTags == null)
					{
						save = true;
					}
					else
					{
						boolean match = false;
						for (int j = 0; j < oldTags.length; j++)
						{
							if (tag.equals(oldTags[j]))
								match = true;
						}
						if (!match)
							save = true;
					}
					if (save)
					{
						PreparedStatement insertStatement = connection.prepareStatement(command2);
						insertStatement.clearParameters();
						insertStatement.setInt(1, progressnoteid);
						insertStatement.setInt(2, tag.getProgressnotetagid());
						insertStatement.executeUpdate();
						insertStatement.close();
					}
				}
			}
		}
	}

	public ProgressNoteTagBean [] getAllTags(Connection connection, boolean valid) throws SQLException
	{
		String command = "select ID, TagName, Valid FROM ProgressNoteTag ";

		if (valid)
			command += "where valid = 1";

		ArrayList<ProgressNoteTagBean> results = new ArrayList<ProgressNoteTagBean>();
		final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
			while (resultSet.next())
			{
				ProgressNoteTagBean tag = new ProgressNoteTagBean();
				tag.setProgressnotetagid(resultSet.getInt("ID"));
				tag.setProgressnotetag(resultSet.getString("TagName"));
				tag.setValid(resultSet.getBoolean("Valid"));
				results.add(tag);
			}
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0)
		{
			ProgressNoteTagBean [] ar = new ProgressNoteTagBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}

	public ProgressNoteTagBean [] getTag(int notetagid, Connection connection) throws SQLException
	{
		final String command = "select ID, TagName, TagDescription, Valid FROM ProgressNoteTag WHERE ID = ?";

		ArrayList<ProgressNoteTagBean> results = new ArrayList<ProgressNoteTagBean>();
		final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);
		preparedStatementQuery.setInt(1, notetagid);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
			while (resultSet.next())
			{
				ProgressNoteTagBean tag = new ProgressNoteTagBean();
				tag.setProgressnotetagid(resultSet.getInt("ID"));
				tag.setProgressnotetag(resultSet.getString("TagName"));
				tag.setProgressnotetagdescription(resultSet.getString("TagDescription"));
				tag.setValid(resultSet.getBoolean("Valid"));
				results.add(tag);
			}
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		if (results.size() > 0)
		{
			ProgressNoteTagBean [] ar = new ProgressNoteTagBean[results.size()];
			return results.toArray(ar);
		}
		return null;
	}

	public int updateTag(final ProgressNoteTagBean bean, final ProgressNoteTagBean original, Connection connection) throws Exception
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
			String command = "update ProgressNoteTag SET ";

			//LabTest

			if (bean.getProgressnotetag() != null && (original == null || !original.getProgressnotetag().equals(bean.getProgressnotetag())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "TagName = ?";
				fieldcount++;
			}
			if (bean.getValid() != null && (original == null || original.getValid() != bean.getValid()))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "Valid = ?";
				fieldcount++;
			}
			if (bean.getProgressnotetagdescription() != null && (original == null || !original.getProgressnotetagdescription().equals(bean.getProgressnotetagdescription())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "TagDescription = ?";
				fieldcount++;
			}

			command += " WHERE ID = ?";

			if (fieldcount > 0)
			{
				LOG.debug("Running query: " + command);
				PreparedStatement updateStatement = connection.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getProgressnotetag() != null && (original == null || !original.getProgressnotetag().equals(bean.getProgressnotetag())))
				{
					updateStatement.setString(fieldnum, bean.getProgressnotetag());
					fieldnum++;
				}
				if (bean.getValid() != null && (original == null || original.getValid() != bean.getValid()))
				{
					updateStatement.setBoolean(fieldnum, bean.getValid());
					;
					fieldnum++;
				}
				if (bean.getProgressnotetagdescription() != null && (original == null || !original.getProgressnotetagdescription().equals(bean.getProgressnotetagdescription())))
				{
					updateStatement.setString(fieldnum, bean.getProgressnotetagdescription());
					fieldnum++;
				}

				if (original != null)
					updateStatement.setInt(fieldnum, original.getProgressnotetagid());
				else
					throw new Exception("Invalid or Missing LabTestID in update");

				result = updateStatement.executeUpdate();

				updateStatement.close();
			}
		}
		return result;
	}

}
