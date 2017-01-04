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

import flinn.beans.TreatmentGroupBean;
import flinn.beans.request.RequestTreatmentBean;
import flinn.beans.response.ResponseTreatmentBean;
import flinn.dao.imp.TreatmentDaoImp;
import flinn.util.RecommendUtils;
import flinn.util.cache.EHCacheImpl;
import flinn.util.cache.ICache;

public abstract class TreatmentDao
{

	protected static final Logger LOG = Logger.getLogger(TreatmentDao.class);

	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	static
	{
		LOG.debug("Log appender instantiated for " + TreatmentDao.class);
	}

	private ResponseTreatmentBean getBeanFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final ResponseTreatmentBean bean = new ResponseTreatmentBean();
		bean.setTreatmentid(resultSet.getInt("T.ID"));
		bean.setTreatmentgroupid(resultSet.getInt("T.TreatmentGroupID"));
		bean.setTreatmentname(resultSet.getString("T.TreatmentName"));
		bean.setTreatmentabbreviation(resultSet.getString("T.TreatmentAbbr"));
		bean.setValid(resultSet.getBoolean("T.Valid"));
		bean.setDrfGenericDrugName(resultSet.getString("T.drfGenericDrugName"));
		TreatmentGroupBean group = new TreatmentGroupBean();
		group.setTreatmentgroupid(resultSet.getInt("T.TreatmentGroupID"));
		List<TreatmentGroupBean> groups;
		try
		{
			groups = (new TreatmentDaoImp().findGroup(group, null, connection));
			if (groups != null && groups.size() > 0)
			{
				group = groups.get(0);
			}
			bean.setGroup(group);
		}
		catch (Exception e)
		{
			LOG.error(e);
		}

		if (bean.getTreatmentid() > 0)
		{
			bean.setDetails(getTreatmentDetails(bean.getTreatmentid(), connection));
		}

		return bean;
	}

	public TreatmentGroupBean getGroupBeanFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final TreatmentGroupBean bean = new TreatmentGroupBean();
		bean.setTreatmentgroupid(resultSet.getInt("TG.ID"));
		bean.setTreatmentgroupname(resultSet.getString("TG.TreatmentGroupName"));
		bean.setTreatmentgroupabbreviation(resultSet.getString("TG.TreatmentGroupAbbr"));
		bean.setValid(resultSet.getBoolean("TG.Valid"));

		return bean;
	}

	public List<ResponseTreatmentBean> getListFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final List<ResponseTreatmentBean> results = new ArrayList<ResponseTreatmentBean>();
		ResponseTreatmentBean bean = null;
		while (resultSet.next())
		{
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public List<TreatmentGroupBean> getGroupListFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final List<TreatmentGroupBean> results = new ArrayList<TreatmentGroupBean>();
		TreatmentGroupBean bean = null;
		while (resultSet.next())
		{
			bean = getGroupBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestTreatmentBean bean, Connection connection) throws Exception
	{
		int treatmentid = 0;
		
		ICache<String, ResponseTreatmentBean> treatmentCache = EHCacheImpl
		.getDefaultInstance("treatmentCache");

		// remove from treatment from cache if exist
		treatmentCache.deleteAll();
		
		if (bean != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into Treatment (TreatmentGroupID, TreatmentName, TreatmentAbbr, Valid) values(?, ?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection.prepareStatement(command);

			// Insert into Treatment
			insertStatement.setInt(1, bean.getTreatmentgroupid());
			insertStatement.setString(2, bean.getTreatmentname());
			insertStatement.setString(3, bean.getTreatmentabbreviation());
			insertStatement.setBoolean(4, bean.getValid());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next())
			{
				treatmentid = rs.getInt(1);
			}

			if (treatmentid > 0)
			{
				saveTreatmentDetails(treatmentid, bean.getDetails(), null, connection);
			}

			insertStatement.close();
		}
		return treatmentid;
	}

	public int createGroup(final TreatmentGroupBean bean, Connection connection) throws Exception
	{
		int groupid = 0;
		if (bean != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into TreatmentGroup (TreatmentGroupName, TreatmentGroupAbbr, Valid) values(?, ?, ?)";

			LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection.prepareStatement(command);

			// Insert into TreatmentGroup
			insertStatement.setString(1, bean.getTreatmentgroupname());
			insertStatement.setString(2, bean.getTreatmentgroupabbreviation());
			insertStatement.setBoolean(3, bean.getValid());

			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next())
			{
				groupid = rs.getInt(1);
			}

			insertStatement.close();
		}
		return groupid;
	}

	public List<ResponseTreatmentBean> find(final RequestTreatmentBean bean, final String orderBy, Connection connection) throws Exception
	{
		List<ResponseTreatmentBean> results = null;

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

	public List<ResponseTreatmentBean> findAllTreatments(final String orderBy, Connection connection) throws Exception
	{
		List<ResponseTreatmentBean> results;

		ICache<String, List<ResponseTreatmentBean>> treatmentCache = EHCacheImpl.getDefaultInstance("treatmentCache");
		
		String cacheKey = RecommendUtils.formatCacheKey(orderBy);
		
		// try to retrieve the object from the cache by orderBy
		results = treatmentCache.get(String.valueOf(cacheKey));
		if (results != null) {
			LOG.debug("findAllTreatments --> orderBy: " + String.valueOf(cacheKey)
					+ " found in cache");
		}
		// if the returned object is null, it was not found in the cache
		if (results == null) {
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
	
			String query = "Select * from Treatment T, TreatmentGroup TG WHERE T.TreatmentGroupID = TG.ID";
			if (orderBy != null)
			{
				query += " ORDER BY " + orderBy;
			}
	
			LOG.debug("Going to run query = " + query);
	
			final PreparedStatement preparedStatementQuery = connection.prepareStatement(query);
	
			final ResultSet resultSet = preparedStatementQuery.executeQuery();
			try
			{
				results = getListFromRs(resultSet, connection);
				treatmentCache.put(String.valueOf(orderBy), results);
			}
			finally
			{
				resultSet.close();
				preparedStatementQuery.close();
			}
		}
		return results;
	}

	public List<ResponseTreatmentBean> findValid(final String orderBy, Connection connection, boolean isSuperAdmin) throws Exception
	{
		List<ResponseTreatmentBean> results = null;
		
		ICache<String, List<ResponseTreatmentBean>> treatmentCache = EHCacheImpl.getDefaultInstance("treatmentCache");
		
		String cacheKey = RecommendUtils.formatCacheKey(orderBy);
		
		// try to retrieve the object from the cache by orderBy
		results = treatmentCache.get(String.valueOf(cacheKey));
		if (results != null) {
			LOG.debug("findValid --> orderBy: " + String.valueOf(cacheKey)
					+ " found in cache");
		}
		// if the returned object is null, it was not found in the cache
		if (results == null) {
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
	
			String query = "Select * from Treatment T, TreatmentGroup TG WHERE T.TreatmentGroupID = TG.ID AND T.Valid='1' AND TG.Valid='1'";
			if (orderBy != null) {
				query += " ORDER BY " + orderBy;
			} else {
				query += " ORDER BY TG.ID,T.ID";
			}
	
			LOG.debug("Going to run query = " + query);
	
			final PreparedStatement preparedStatementQuery = connection.prepareStatement(query);
	
			final ResultSet resultSet = preparedStatementQuery.executeQuery();
			try
			{
				results = getListFromRs(resultSet, connection);
				treatmentCache.put(String.valueOf(orderBy), results);
			}
			finally
			{
				resultSet.close();
				preparedStatementQuery.close();
			}
		}
		return results;
	}

	// Order must be the same as fillInColumnNames
	private void fillInColumnValues(final RequestTreatmentBean bean, final PreparedStatement preparedStatementQuery) throws SQLException
	{
		int index = 1;

		if (bean.getTreatmentid() != 0)
		{
			preparedStatementQuery.setInt(index, bean.getTreatmentid());
			index++;
		}
		if (bean.getTreatmentname() != null)
		{
			preparedStatementQuery.setString(index, bean.getTreatmentname());
			index++;
		}
		if (bean.getTreatmentabbreviation() != null)
		{
			preparedStatementQuery.setString(index, bean.getTreatmentabbreviation());
			index++;
		}
		if (bean.getDrfGenericDrugName() != null)
		{
			preparedStatementQuery.setString(index, bean.getDrfGenericDrugName().toUpperCase());
			index++;
		}
		if (bean.getValid() != null)
		{
			preparedStatementQuery.setBoolean(index, bean.getValid());
			index++;
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestTreatmentBean bean)
	{
		String query = "Select * from Treatment T, TreatmentGroup TG";
		boolean first = false;

		query += " WHERE T.TreatmentGroupID = TG.ID";

		if (bean.getTreatmentid() != 0)
		{
			query = query + " " + (first ? WHERE : AND) + "T.ID " + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getTreatmentgroupid() != 0)
		{
			query = query + " " + (first ? WHERE : AND) + "T.TreatmentGroupID " + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getTreatmentname() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "T.TreatmentName " + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getTreatmentabbreviation() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "T.TreatmentAbbr " + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getDrfGenericDrugName() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "T.DrfGenericDrugName " + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getValid() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "T.Valid" + EQUALS_QUESTION;
			first = false;
		}

		return query;
	}

	public List<TreatmentGroupBean> findGroup(final TreatmentGroupBean bean, final String orderBy, Connection connection) throws Exception
	{
		List<TreatmentGroupBean> results = null;

		if (connection == null || connection.isClosed())
		{
			LOG.error("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		final String query = fillInGroupColumnNames(bean);

		LOG.debug("Going to run query = " + query);

		final PreparedStatement preparedStatementQuery = connection.prepareStatement(query);

		fillInGroupColumnValues(bean, preparedStatementQuery);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
			results = getGroupListFromRs(resultSet, connection);
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		return results;
	}

	// Order must be the same as fillInColumnNames
	private void fillInGroupColumnValues(final TreatmentGroupBean bean, final PreparedStatement preparedStatementQuery) throws SQLException
	{
		int index = 1;

		if (bean != null)
		{
			if (bean.getTreatmentgroupid() != 0)
			{
				preparedStatementQuery.setInt(index, bean.getTreatmentgroupid());
				index++;
			}
			if (bean.getTreatmentgroupname() != null)
			{
				preparedStatementQuery.setString(index, bean.getTreatmentgroupname());
				index++;
			}
			if (bean.getTreatmentgroupabbreviation() != null)
			{
				preparedStatementQuery.setString(index, bean.getTreatmentgroupabbreviation());
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
	private String fillInGroupColumnNames(final TreatmentGroupBean bean)
	{
		String query = "Select * from TreatmentGroup TG";
		boolean first = true;

		if (bean != null)
		{
			if (bean.getTreatmentgroupid() != 0)
			{
				query = query + " " + (first ? WHERE : AND) + "TG.ID " + EQUALS_QUESTION;
				first = false;

			}
			if (bean.getTreatmentgroupname() != null)
			{
				query = query + " " + (first ? WHERE : AND) + "TG.TreatmentGroupName " + EQUALS_QUESTION;
				first = false;
			}
			if (bean.getTreatmentgroupabbreviation() != null)
			{
				query = query + " " + (first ? WHERE : AND) + "TG.TreatmentGroupAbbr " + EQUALS_QUESTION;
				first = false;

			}
			if (bean.getValid() != null)
			{
				query = query + " " + (first ? WHERE : AND) + "TG.Valid" + EQUALS_QUESTION;

				first = false;

			}
		}

		return query;
	}

	public int update(final RequestTreatmentBean bean, final ResponseTreatmentBean original, Connection connection) throws Exception
	{
		int result = -1;
		
		ICache<String, ResponseTreatmentBean> treatmentCache = EHCacheImpl
		.getDefaultInstance("treatmentCache");

		// remove from treatment from cache if exist
		treatmentCache.deleteAll();

		if (bean != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			int fieldcount = 0;
			String command = "update Treatment SET ";

			// Treatment
			if (bean.getTreatmentgroupid() > 0 && bean.getTreatmentgroupid() != 0 && (original == null || !(original.getTreatmentgroupid() == bean.getTreatmentgroupid())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "TreatmentGroupID = ?";
				fieldcount++;
			}
			if (bean.getTreatmentname() != null && (original == null || !original.getTreatmentname().equals(bean.getTreatmentname())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "TreatmentName = ?";
				fieldcount++;
			}
			if (bean.getTreatmentabbreviation() != null && (original == null || !original.getTreatmentabbreviation().equals(bean.getTreatmentabbreviation())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "TreatmentAbbr = ?";
				fieldcount++;
			}
			if (bean.getValid() != null && (original == null || original.getValid() != bean.getValid()))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "Valid = ?";
				fieldcount++;
			}

			command += " WHERE ID = ?";

			if (fieldcount > 0)
			{
				LOG.debug("Running query: " + command);
				PreparedStatement updateStatement = connection.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getTreatmentgroupid() > 0 && bean.getTreatmentgroupid() != 0 && (original == null || !(original.getTreatmentgroupid() == bean.getTreatmentgroupid())))
				{
					updateStatement.setInt(fieldnum, bean.getTreatmentgroupid());
					fieldnum++;
				}
				if (bean.getTreatmentname() != null && (original == null || !original.getTreatmentname().equals(bean.getTreatmentname())))
				{
					updateStatement.setString(fieldnum, bean.getTreatmentname());
					fieldnum++;
				}
				if (bean.getTreatmentabbreviation() != null && (original == null || !original.getTreatmentabbreviation().equals(bean.getTreatmentabbreviation())))
				{
					updateStatement.setString(fieldnum, bean.getTreatmentabbreviation());
					fieldnum++;
				}
				if (bean.getValid() != null && (original == null || original.getValid() != bean.getValid()))
				{
					updateStatement.setBoolean(fieldnum, bean.getValid());
					fieldnum++;
				}

				if (original != null)
					updateStatement.setInt(fieldnum, original.getTreatmentid());
				else
					throw new Exception("Invalid or Missing TreatmentID in update");

				result = updateStatement.executeUpdate();

				updateStatement.close();
			}
		}

		if (original != null && original.getTreatmentid() > 0)
		{
			saveTreatmentDetails(bean.getTreatmentid(), bean.getDetails(), original.getDetails(), connection);
		}

		return result;
	}

	public int updateGroup(final TreatmentGroupBean bean, final TreatmentGroupBean original, Connection connection) throws Exception
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
			String command = "update TreatmentGroup SET ";

			// TreatmentGroup

			if (bean.getTreatmentgroupname() != null && (original == null || !original.getTreatmentgroupname().equals(bean.getTreatmentgroupname())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "TreatmentGroupName = ?";
				fieldcount++;
			}
			if (bean.getTreatmentgroupabbreviation() != null && (original == null || !original.getTreatmentgroupabbreviation().equals(bean.getTreatmentgroupabbreviation())))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "TreatmentGroupAbbr = ?";
				fieldcount++;
			}
			if (bean.getValid() != null && (original == null || original.getValid() != bean.getValid()))
			{
				if (fieldcount > 0)
					command += ", ";
				command += "Valid = ?";
				fieldcount++;
			}

			command += " WHERE ID = ?";

			if (fieldcount > 0)
			{
				LOG.debug("Running query: " + command);
				PreparedStatement updateStatement = connection.prepareStatement(command);
				int fieldnum = 1;

				if (bean.getTreatmentgroupname() != null && (original == null || !original.getTreatmentgroupname().equals(bean.getTreatmentgroupname())))
				{
					updateStatement.setString(fieldnum, bean.getTreatmentgroupname());
					fieldnum++;
				}
				if (bean.getTreatmentgroupabbreviation() != null && (original == null || !original.getTreatmentgroupabbreviation().equals(bean.getTreatmentgroupabbreviation())))
				{
					updateStatement.setString(fieldnum, bean.getTreatmentgroupabbreviation());
					fieldnum++;
				}
				if (bean.getValid() != null && (original == null || original.getValid() != bean.getValid()))
				{
					updateStatement.setBoolean(fieldnum, bean.getValid());
					;
					fieldnum++;
				}

				if (original != null)
					updateStatement.setInt(fieldnum, original.getTreatmentgroupid());
				else
					throw new Exception("Invalid or Missing TreatmentGroupID in update");

				result = updateStatement.executeUpdate();

				updateStatement.close();
			}
		}
		return result;
	}

	private HashMap<String, String> getTreatmentDetails(int TreatmentID, Connection connection) throws SQLException
	{
		final String command = "select TreatmentDetailName, TreatmentDetailValue from TreatmentDetail WHERE TreatmentID = ? ORDER BY ID";

		HashMap<String, String> results = new HashMap<String, String>();
		final PreparedStatement preparedStatementQuery = connection.prepareStatement(command);
		preparedStatementQuery.setInt(1, TreatmentID);

		final ResultSet resultSet = preparedStatementQuery.executeQuery();
		try
		{
			while (resultSet.next())
			{
				String key = resultSet.getString("TreatmentDetailName");
				String value = resultSet.getString("TreatmentDetailValue");
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

	private void saveTreatmentDetails(int TreatmentID, HashMap<String, String> details, HashMap<String, String> oldDetails, Connection connection) throws Exception
	{
		final String inscommand = "insert into TreatmentDetail (TreatmentID, TreatmentDetailName, TreatmentDetailValue) values(?,?,?)";
		final String updcommand = "update TreatmentDetail set TreatmentDetailValue = ? where TreatmentID = ? and TreatmentDetailName = ?";

		// Insert into TreatmentDetail
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
					insertStatement.setInt(1, TreatmentID);
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
					updateStatement.setInt(2, TreatmentID);
					updateStatement.setString(3, key);
					updateStatement.executeUpdate();
					updateStatement.close();
				}
			}
		}
	}
}
