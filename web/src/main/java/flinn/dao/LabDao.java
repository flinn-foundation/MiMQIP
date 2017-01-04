package flinn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mysql.jdbc.Statement;
import org.apache.log4j.Logger;

import flinn.beans.LabTestBean;
import flinn.beans.request.RequestLabBean;
import flinn.beans.response.ResponseLabBean;

public abstract class LabDao
{

	protected static final Logger LOG = Logger.getLogger(LabDao.class);

	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	static
	{
		LOG.debug("Log appender instantiated for " + LabDao.class);
	}

	public ResponseLabBean getBeanFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final ResponseLabBean bean = new ResponseLabBean();
		bean.setLabid(resultSet.getInt("Lab.ID"));
		bean.setPatientid(resultSet.getInt("Lab.PatientID"));
		bean.setLabdate(resultSet.getString("Lab.LabDate"));
		bean.setLabtext(resultSet.getString("Lab.LabText"));
		LabTestBean labtest = getTestBeanFromRs(resultSet, connection);
		bean.setLabtest(labtest);

		return bean;
	}

	public LabTestBean getTestBeanFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final LabTestBean bean = new LabTestBean();
		bean.setLabtestid(resultSet.getInt("LabTest.ID"));
		bean.setLabtestname(resultSet.getString("LabTest.LabTestName"));
		bean.setValid(resultSet.getBoolean("LabTest.Valid"));
		bean.setStartdate(resultSet.getString("LabTest.StartDate"));
		bean.setDiscontinuedate(resultSet.getString("LabTest.DiscontinueDate"));
		if (bean.getLabtestid() > 0)
		{
			bean.setDetails(getTestDetails(bean.getLabtestid(), connection));
		}
		return bean;
	}

	public List<ResponseLabBean> getListFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final List<ResponseLabBean> results = new ArrayList<ResponseLabBean>();
		ResponseLabBean bean = null;
		while (resultSet.next())
		{
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public List<LabTestBean> getTestListFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final List<LabTestBean> results = new ArrayList<LabTestBean>();
		LabTestBean bean = null;
		while (resultSet.next())
		{
			bean = getTestBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestLabBean bean, Connection connection) throws Exception
	{
		int labid = 0;
		if (bean != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into Lab (PatientID, LabTestID, LabDate, LabText) values(?, ?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection.prepareStatement(command,Statement.RETURN_GENERATED_KEYS);

			//Insert into AppUser
			insertStatement.setInt(1, bean.getPatientid());
			insertStatement.setInt(2, bean.getLabtest().getLabtestid());
			if (bean.getLabdate() != null)
				insertStatement.setString(3, bean.getLabdate());
			else
				insertStatement.setString(3, flinn.util.DateString.now());
			insertStatement.setString(4, bean.getLabtext());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next())
			{
				labid = rs.getInt(1);
			}

			insertStatement.close();
		}
		return labid;
	}

	public int createTest(final LabTestBean bean, Connection connection) throws Exception
	{
		int labtestid = 0;
		if (bean != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into LabTest (LabTestName, Valid, StartDate, DiscontinueDate) values(?, ?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection.prepareStatement(command);

			//Insert into AppUser
			insertStatement.setString(1, bean.getLabtestname());
			insertStatement.setBoolean(2, bean.getValid());
			if (bean.getStartdate() != null)
				insertStatement.setString(3, bean.getStartdate());
			else
				insertStatement.setString(3, flinn.util.DateString.now());
			if (bean.getDiscontinuedate() != null)
				insertStatement.setString(4, bean.getDiscontinuedate());
			else
				insertStatement.setString(4, "0000-00-00 00:00:00");
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next())
			{
				labtestid = rs.getInt(1);
			}

			if (labtestid > 0)
			{
				saveTestDetails(labtestid, bean.getDetails(), null, connection);
			}

			insertStatement.close();
		}
		return labtestid;
	}

	public List<ResponseLabBean> find(final RequestLabBean bean, final String orderBy, Connection connection) throws Exception
	{
		List<ResponseLabBean> results = null;

		if (connection == null || connection.isClosed())
		{
			LOG.error("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		final String query = fillInColumnNames(bean);

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
	private void fillInColumnValues(final RequestLabBean bean, final PreparedStatement preparedStatementQuery) throws SQLException
	{
		int index = 1;

		if (bean.getLabid() != 0)
		{
			preparedStatementQuery.setInt(index, bean.getLabid());
			index++;
		}
		if (bean.getPatientid() != 0)
		{
			preparedStatementQuery.setInt(index, bean.getPatientid());
			index++;
		}
		if (bean.getLabdate() != null)
		{
			preparedStatementQuery.setString(index, bean.getLabdate());
			index++;
		}
		if (bean.getLabtext() != null)
		{
			preparedStatementQuery.setString(index, bean.getLabtext());
			index++;
		}
		if (bean.getLabtest() != null && bean.getLabtest().getLabtestid() != 0)
		{
			preparedStatementQuery.setInt(index, bean.getLabtest().getLabtestid());
			index++;
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestLabBean bean)
	{
		String query = "Select * from Lab, LabTest WHERE Lab.LabTestID = LabTest.ID";
		boolean first = false;

		if (bean.getLabid() != 0)
		{
			query = query + " " + (first ? WHERE : AND) + "Lab.ID " + EQUALS_QUESTION;
			first = false;

		}
		if (bean.getPatientid() != 0)
		{
			query = query + " " + (first ? WHERE : AND) + "Lab.PatientID " + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getLabdate() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "Lab.LabDate " + EQUALS_QUESTION;
			first = false;

		}
		if (bean.getLabtext() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "Lab.LabText " + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getLabtest() != null && bean.getLabtest().getLabtestid() != 0)
		{
			query = query + " " + (first ? WHERE : AND) + "LabTest.ID " + EQUALS_QUESTION;
			first = false;
		}

		return query;
	}

	public List<LabTestBean> findTest(final LabTestBean bean, final String orderBy, Connection connection) throws Exception
	{
		List<LabTestBean> results = null;

		if (connection == null || connection.isClosed())
		{
			LOG.error("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String query = fillInTestColumnNames(bean);
		if (orderBy != null)
		{
			query += " ORDER BY " + orderBy;
		}

		LOG.debug("Going to run query = " + query);

		final PreparedStatement preparedStatementQuery = connection.prepareStatement(query);

		fillInTestColumnValues(bean, preparedStatementQuery);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
			results = getTestListFromRs(resultSet, connection);
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		return results;
	}

	// Order must be the same as fillInColumnNames
	private void fillInTestColumnValues(final LabTestBean bean, final PreparedStatement preparedStatementQuery) throws SQLException
	{
		int index = 1;

		if (bean != null)
		{
			if (bean.getLabtestid() != 0)
			{
				preparedStatementQuery.setInt(index, bean.getLabtestid());
				index++;
			}
			if (bean.getLabtestname() != null)
			{
				preparedStatementQuery.setString(index, bean.getLabtestname());
				index++;
			}
			if (bean.getStartdate() != null)
			{
				preparedStatementQuery.setString(index, bean.getStartdate());
				index++;
			}
			if (bean.getDiscontinuedate() != null)
			{
				preparedStatementQuery.setString(index, bean.getDiscontinuedate());
				index++;
			}
			if (bean.getValid() != null)
			{
				preparedStatementQuery.setBoolean(index, bean.getValid());
				index++;
			}
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInTestColumnNames(final LabTestBean bean)
	{
		String query = "Select * from LabTest ";
		boolean first = true;

		if (bean != null)
		{
			if (bean.getLabtestid() != 0)
			{
				query = query + " " + (first ? WHERE : AND) + "ID " + EQUALS_QUESTION;
				first = false;

			}
			if (bean.getLabtestname() != null)
			{
				query = query + " " + (first ? WHERE : AND) + "LabTestName " + EQUALS_QUESTION;
				first = false;
			}
			if (bean.getStartdate() != null)
			{
				query = query + " " + (first ? WHERE : AND) + "StartDate " + EQUALS_QUESTION;
				first = false;

			}
			if (bean.getDiscontinuedate() != null)
			{
				query = query + " " + (first ? WHERE : AND) + "DiscontinueDate " + EQUALS_QUESTION;
				first = false;
			}
			if (bean.getValid() != null)
			{
				query = query + " " + (first ? WHERE : AND) + "Valid" + EQUALS_QUESTION;

				first = false;

			}
		}

		return query;
	}

	public int update(final RequestLabBean bean, final ResponseLabBean original, Connection connection) throws Exception
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
			String command = "update Lab SET ";

			//Lab

			if (bean.getPatientid() != 0 && (original == null || !(original.getPatientid() == bean.getPatientid())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "PatientID = ?";
				fieldcount++;
			}
			if (bean.getLabtest() != null && bean.getLabtest().getLabtestid() != 0
			    && (original == null || original.getLabtest() == null || !(original.getLabtest().getLabtestid() == bean.getLabtest().getLabtestid())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "LabTestID = ?";
				fieldcount++;
			}
			if (bean.getLabdate() != null && (original == null || !original.getLabdate().equals(bean.getLabdate())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "LabDate = ?";
				fieldcount++;
			}
			if (bean.getLabtext() != null && (original == null || !original.getLabtext().equals(bean.getLabtext().trim())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "LabText = ?";
				fieldcount++;
			}

			command += " WHERE ID = ?";

			if (fieldcount > 0)
			{
				LOG.debug("Running query: " + command);
				PreparedStatement updateStatement = connection.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getPatientid() != 0 && (original == null || !(original.getPatientid() == bean.getPatientid())))
				{
					updateStatement.setInt(fieldnum, bean.getPatientid());
					;
					fieldnum++;
				}
				if (bean.getLabtest() != null && bean.getLabtest().getLabtestid() != 0
				    && (original == null || original.getLabtest() == null || !(original.getLabtest().getLabtestid() == bean.getLabtest().getLabtestid())))
				{
					updateStatement.setInt(fieldnum, bean.getLabtest().getLabtestid());
					;
					fieldnum++;
				}
				if (bean.getLabdate() != null && (original == null || !original.getLabdate().equals(bean.getLabdate())))
				{
					updateStatement.setString(fieldnum, bean.getLabdate());
					fieldnum++;
				}
				if (bean.getLabtext() != null && (original == null || !original.getLabtext().equals(bean.getLabtext().trim())))
				{
					updateStatement.setString(fieldnum, bean.getLabtext().trim());
					fieldnum++;
				}

				if (original != null)
					updateStatement.setInt(fieldnum, original.getLabid());
				else
					throw new Exception("Invalid or Missing LabID in update");

				result = updateStatement.executeUpdate();

				updateStatement.close();
			}
		}
		return result;
	}

	public int updateTest(final LabTestBean bean, final LabTestBean original, Connection connection) throws Exception
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
			String command = "update LabTest SET ";

			//LabTest

			if (bean.getLabtestname() != null && (original == null || !original.getLabtestname().equals(bean.getLabtestname())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "LabTestName = ?";
				fieldcount++;
			}
			if (bean.getValid() != null && (original == null || original.getValid() != bean.getValid()))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "Valid = ?";
				fieldcount++;
			}
			if (bean.getStartdate() != null && (original == null || !original.getStartdate().equals(bean.getStartdate())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "StartDate = ?";
				fieldcount++;
			}
			if (bean.getDiscontinuedate() != null && (original == null || !original.getDiscontinuedate().equals(bean.getDiscontinuedate())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "DiscontinueDate = ?";
				fieldcount++;
			}

			command += " WHERE ID = ?";

			if (fieldcount > 0)
			{
				LOG.debug("Running query: " + command);
				PreparedStatement updateStatement = connection.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getLabtestname() != null && (original == null || !original.getLabtestname().equals(bean.getLabtestname())))
				{
					updateStatement.setString(fieldnum, bean.getLabtestname());
					fieldnum++;
				}
				if (bean.getValid() != null && (original == null || original.getValid() != bean.getValid()))
				{
					updateStatement.setBoolean(fieldnum, bean.getValid());
					;
					fieldnum++;
				}
				if (bean.getStartdate() != null && (original == null || !original.getStartdate().equals(bean.getStartdate())))
				{
					updateStatement.setString(fieldnum, bean.getStartdate());
					fieldnum++;
				}
				if (bean.getDiscontinuedate() != null && (original == null || !original.getDiscontinuedate().equals(bean.getDiscontinuedate())))
				{
					updateStatement.setString(fieldnum, bean.getDiscontinuedate());
					fieldnum++;
				}

				if (original != null)
					updateStatement.setInt(fieldnum, original.getLabtestid());
				else
					throw new Exception("Invalid or Missing LabTestID in update");

				result = updateStatement.executeUpdate();

				updateStatement.close();
			}
			if (original != null && original.getLabtestid() > 0)
			{
				saveTestDetails(bean.getLabtestid(), bean.getDetails(), original.getDetails(), connection);
			}
		}
		return result;
	}

	private HashMap<String, String> getTestDetails(int LabTestID, Connection connection) throws SQLException
	{
		final String command = "select LabTestDetailName, LabTestDetailValue from LabTestDetail WHERE LabTestID = ? ORDER BY ID";

		HashMap<String, String> results = new HashMap<String, String>();
		final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);
		preparedStatementQuery.setInt(1, LabTestID);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
			while (resultSet.next())
			{
				String key = resultSet.getString("LabTestDetailName");
				String value = resultSet.getString("LabTestDetailValue");
				results.put(key, value);
			}
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

	private void saveTestDetails(int labtestID, HashMap<String, String> details, HashMap<String, String> oldDetails, Connection connection) throws Exception
	{
		final String inscommand = "insert into LabTestDetail (LabTestID, LabTestDetailName, LabTestDetailValue) values(?,?,?)";
		final String updcommand = "update LabTestDetail set LabTestDetailValue = ? where LabTestID = ? and LabTestDetailName = ?";

		//Insert into LabTestDetail
		if (details != null)
		{
			Iterator<String> it = details.keySet().iterator();
			while (it.hasNext())
			{
				String key = it.next();
				String value = details.get(key);
				String oldValue = null;
				if (oldDetails != null && oldDetails.get(key) != null)
				{
					oldValue = oldDetails.get(key);
				}
				if (value != null && oldValue == null)
				{
					PreparedStatement insertStatement = connection.prepareStatement(inscommand);
					insertStatement.clearParameters();
					insertStatement.setInt(1, labtestID);
					insertStatement.setString(2, key);
					insertStatement.setString(3, value.trim());
					insertStatement.executeUpdate();
					insertStatement.close();
				}
				else if (value != null && oldValue != null && !value.trim().equals(oldValue))
				{
					PreparedStatement updateStatement = connection.prepareStatement(updcommand);
					updateStatement.clearParameters();
					updateStatement.setString(1, value.trim());
					updateStatement.setInt(2, labtestID);
					updateStatement.setString(3, key);
					updateStatement.executeUpdate();
					updateStatement.close();
				}
			}
		}
	}

}
