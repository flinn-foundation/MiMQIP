package flinn.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.dao.MedicationDao;
import flinn.dao.dbconnection.DBConnectionPool;
import flinn.dao.imp.PatientDaoImp;
import flinn.dao.model.Medication;

public class MedicationService extends BaseService
{

	static final Logger LOG = Logger.getLogger(MedicationService.class);

	private final MedicationDao medicationDao = new MedicationDao();
	private final PatientDaoImp patientDao = new PatientDaoImp();

	public List<Medication> mergeMedications(List<Medication> medications) throws ServiceException
	{
		LOG.debug("Medication list size: " + medications.size());
		int status = 0;
		Connection connection = null;
		List<Medication> modifiedMedications = new ArrayList<Medication>();
		try
		{
			connection = DBConnectionPool.getConnection();
			connection.setAutoCommit(false);

			for (Iterator<Medication> iter = medications.iterator(); iter.hasNext();)
			{
				Medication medication = iter.next();
				status = medicationDao.merge(connection, medication);
				//FIXME: Need to keep in case treatment transaction fails
				//if (status > 0)
				modifiedMedications.add(medication);

				status = patientDao.updateRcopiaDate(medication.getPatientId().intValue(), medication.getLastModifiedDate(), connection);
			}

			commitConnection(connection);
		}
		catch (SQLException e)
		{
			LOG.error(e);
			rollbackConnection(connection);
			throw new ServiceException("SQLException when updating prescription events", e);
		}
		catch (Exception e)
		{
			LOG.error(e);
			rollbackConnection(connection);
			throw new ServiceException("Exception in updateMedications", e);
		}
		finally
		{
			closeConnection(connection);
		}
		return modifiedMedications;
	}

	public void discontinueMedication()
	{
	}

}
