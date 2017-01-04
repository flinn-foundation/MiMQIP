package flinn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.request.RequestPrescriptionBean;
import flinn.beans.response.ResponsePrescriptionBean;
import flinn.beans.response.ResponseTreatmentBean;
import flinn.dao.imp.TreatmentDaoImp;

public abstract class PrescriptionDao
{

	protected static final Logger LOG = Logger.getLogger(PrescriptionDao.class);

	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String EQUALS_QUESTION = " = ? ";

	static
	{
		LOG.debug("Log appender instantiated for " + PrescriptionDao.class);
	}

	protected ResponsePrescriptionBean getBeanFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final ResponsePrescriptionBean bean = new ResponsePrescriptionBean();
		ResponseTreatmentBean treatmentbean = new ResponseTreatmentBean();
		bean.setPrescriptionid(resultSet.getInt("ID"));
		bean.setTreatmentid(resultSet.getInt("TreatmentID"));
		bean.setPatientid(resultSet.getInt("PatientID"));
		bean.setRcopiaid(resultSet.getInt("RcopiaId"));
		bean.setEntrydate(resultSet.getString("EntryDate"));
		bean.setDiscontinue(resultSet.getBoolean("Discontinue"));
		bean.setDailydose(resultSet.getString("DailyDose"));
		bean.setDoctorname(resultSet.getString("DoctorName"));
		bean.setDuration(resultSet.getInt("Duration"));

		try
		{
			treatmentbean = (new TreatmentDaoImp()).getTreatmentByID(resultSet.getInt("TreatmentID"), connection);
			bean.setTreatment(treatmentbean);
		}
		catch (Exception e)
		{
			LOG.error("Unable to retrieve Treatment information");
		}

		return bean;
	}

	protected List<ResponsePrescriptionBean> getListFromRs(final ResultSet resultSet, Connection connection) throws SQLException
	{
		final List<ResponsePrescriptionBean> results = new ArrayList<ResponsePrescriptionBean>();
		ResponsePrescriptionBean bean = null;
		while (resultSet.next())
		{
			bean = getBeanFromRs(resultSet, connection);
			results.add(bean);
		}
		return results;
	}

	public int create(final RequestPrescriptionBean bean, Connection connection) throws Exception
	{
		int prescriptionID = 0;
		if (bean != null)
		{
			if (connection == null || connection.isClosed())
			{
				LOG.error("bad DB Connection");
				throw new Exception("bad DB Connection");
			}
			final String command = "insert into PrescriptionEvent (TreatmentID, PatientID, RcopiaId, EntryDate, Discontinue, DailyDose, DoctorName, Duration) " + " values(" + "?, ?, ?, ?, ?, ?, ?, ?)";

			//LOG.debug("Running query: " + command);
			PreparedStatement insertStatement = connection.prepareStatement(command);

			//Insert into Prescription
			insertStatement.setInt(1, bean.getTreatmentid());
			insertStatement.setInt(2, bean.getPatientid());
			insertStatement.setInt(3, bean.getRcopiaid());
			insertStatement.setString(4, bean.getEntrydate());
			insertStatement.setBoolean(5, bean.getDiscontinue());
			insertStatement.setString(6, bean.getDailydose());
			insertStatement.setString(7, bean.getDoctorname());
			insertStatement.setInt(8, bean.getDuration());
			insertStatement.executeUpdate();

			ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next())
			{
				prescriptionID = rs.getInt(1);
			}

			insertStatement.close();
		}
		return prescriptionID;
	}

	public int delete(int patientId, int treatmentId, int rcopiaId, Connection connection) throws Exception
	{
		if (connection == null || connection.isClosed())
		{
			LOG.error("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String sql = "delete from PrescriptionEvent where patientId = ? and treatmentid = ? and rcopiaid = ?";

		LOG.debug("Running query: " + sql);
		PreparedStatement deleteStatement = connection.prepareStatement(sql);

		deleteStatement.setInt(1, patientId);
		deleteStatement.setInt(2, treatmentId);
		deleteStatement.setInt(3, rcopiaId);
		int status = deleteStatement.executeUpdate();

		deleteStatement.close();

		return status;
	}

	public List<ResponsePrescriptionBean> find(final RequestPrescriptionBean bean, final String orderBy, Connection connection) throws Exception
	{
		List<ResponsePrescriptionBean> results = null;

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

	public List<ResponsePrescriptionBean> findAllPrescriptions(final RequestPrescriptionBean bean, final String orderBy, Connection connection) throws Exception
	{
		List<ResponsePrescriptionBean> results = null;

		if (connection == null || connection.isClosed())
		{
			LOG.error("bad DB Connection");
			throw new Exception("bad DB Connection");
		}

		String query = "Select * from PrescriptionEvent";
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
		}
		finally
		{
			resultSet.close();
			preparedStatementQuery.close();
		}
		return results;
	}

	// Order must be the same as fillInColumnNames
	private void fillInColumnValues(final RequestPrescriptionBean bean, final PreparedStatement preparedStatementQuery) throws SQLException
	{
		int index = 1;

		if (bean.getPrescriptionid() > 0)
		{
			preparedStatementQuery.setInt(index, bean.getPrescriptionid());
			index++;
		}
		if (bean.getTreatmentid() > 0)
		{
			preparedStatementQuery.setInt(index, bean.getTreatmentid());
			index++;
		}
		if (bean.getPatientid() > 0)
		{
			preparedStatementQuery.setInt(index, bean.getPatientid());
			index++;
		}
		if (bean.getRcopiaid() > 0)
		{
			preparedStatementQuery.setInt(index, bean.getRcopiaid());
			index++;
		}
		if (bean.getEntrydate() != null)
		{
			preparedStatementQuery.setString(index, bean.getEntrydate());
			index++;
		}
		if (bean.getDiscontinue() != null)
		{
			preparedStatementQuery.setBoolean(index, bean.getDiscontinue());
			index++;
		}
		if (bean.getDailydose() != null)
		{
			preparedStatementQuery.setString(index, bean.getDailydose());
			index++;
		}
		if (bean.getDoctorname() != null)
		{
			preparedStatementQuery.setString(index, bean.getDoctorname());
			index++;
		}
		if (bean.getDuration() > 0)
		{
			preparedStatementQuery.setInt(index, bean.getDuration());
			index++;
		}
	}

	// Order must be the same as fillInColumnValues
	private String fillInColumnNames(final RequestPrescriptionBean bean)
	{
		String query = "Select * from PrescriptionEvent ";
		boolean first = true;

		if (bean.getPrescriptionid() > 0)
		{
			query = query + " " + (first ? WHERE : AND) + "ID " + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getTreatmentid() > 0)
		{
			query = query + " " + (first ? WHERE : AND) + "TreatmentID" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getPatientid() > 0)
		{
			query = query + " " + (first ? WHERE : AND) + "PatientID" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getRcopiaid() > 0)
		{
			query = query + " " + (first ? WHERE : AND) + "RcopiaId" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getEntrydate() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "EntryDate" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getDiscontinue() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "Discontinue" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getDailydose() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "DailyDose" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getDoctorname() != null)
		{
			query = query + " " + (first ? WHERE : AND) + "DoctorName" + EQUALS_QUESTION;
			first = false;
		}
		if (bean.getDuration() > 0)
		{
			query = query + " " + (first ? WHERE : AND) + "Duration" + EQUALS_QUESTION;
			first = false;
		}
		return query;
	}
}
