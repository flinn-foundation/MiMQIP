package flinn.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import flinn.beans.response.ResponsePatientBean;
import flinn.dao.dbconnection.DBConnectionPool;
import flinn.dao.imp.PatientDaoImp;

public class PatientService extends BaseService
{

	static final Logger LOG = Logger.getLogger(PatientService.class);

	private final PatientDaoImp patientDao = new PatientDaoImp();

	public ResponsePatientBean getPatientResponseBean(int patientId) throws ServiceException
	{
		Connection connection = null;
		ResponsePatientBean patientBean = null;
		try
		{
			connection = DBConnectionPool.getConnection();
			patientBean = patientDao.findPatientById(patientId, connection);
			if (patientBean == null)
			{
				throw new IllegalArgumentException("Unable to find patient with id " + patientId);
			}
		}
		catch (SQLException e)
		{
			LOG.error(e);
			throw new ServiceException("SQLException when getting ResponsePatientBean from database", e);
		}
		catch (Exception e)
		{
			LOG.error(e);
			throw new ServiceException("Exception when getting ResponsePatientBean from databases", e);
		}
		finally
		{
			closeConnection(connection);
		}
		return patientBean;
	}

	public int updateRcopiaDate(int patientId, Date lastModifiedDate) throws ServiceException
	{
		LOG.debug("patientId=" + patientId + ", lastModifiedDate=" + lastModifiedDate);
		int status = -1;
		Connection connection = null;
		try
		{
			connection = DBConnectionPool.getConnection();
			connection.setAutoCommit(false);

			status = patientDao.updateRcopiaDate(patientId, lastModifiedDate, connection);

			commitConnection(connection);
		}
		catch (SQLException e)
		{
			LOG.error(e);
			rollbackConnection(connection);
			throw new ServiceException("SQLException in updateRcopiaDate", e);
		}
		catch (Exception e)
		{
			LOG.error(e);
			rollbackConnection(connection);
			throw new ServiceException("Exception in updateRcopiaDate", e);
		}
		finally
		{
			closeConnection(connection);
		}

		return status;
	}

}
