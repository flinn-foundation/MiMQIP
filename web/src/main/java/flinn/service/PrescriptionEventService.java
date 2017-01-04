package flinn.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.response.ResponseTreatmentBean;
import flinn.dao.dbconnection.DBConnectionPool;
import flinn.dao.imp.PrescriptionDaoImp;
import flinn.dao.imp.TreatmentDaoImp;
import flinn.dao.model.Medication;

public class PrescriptionEventService extends BaseService
{

	static final Logger LOG = Logger.getLogger(PrescriptionEventService.class);

	private final PrescriptionDaoImp prescriptionDao = new PrescriptionDaoImp();
	private final TreatmentDaoImp treatmentDao = new TreatmentDaoImp();

	public void updatePrescriptionEvents(List<Medication> medications) throws ServiceException
	{
		LOG.debug("Medication list size: " + medications.size());
		Connection connection = null;
		try
		{
			connection = DBConnectionPool.getConnection();
			connection.setAutoCommit(false);

			Medication medication;
			for (Iterator<Medication> iter = medications.iterator(); iter.hasNext();)
			{
				medication = iter.next();
				LOG.debug(medication.shortDescription() + " " + medication.sigDescription());
				ResponseTreatmentBean responseTreatment = treatmentDao.getTreatmentByGenericDrugNameAndForm(medication.getGenericName(),medication.getDrugForm(), connection);
				if (responseTreatment == null)
				{
					LOG.warn("Skipping medication since there is no treatment for generic drug " + medication.getGenericName() + ", Form: " + medication.getDrugForm());
					continue;
				}
				else
				{
					LOG.debug("Found treatment with Id " + responseTreatment.getTreatmentid() + " for generic drug " + responseTreatment.getDrfGenericDrugName());
				}
				if (responseTreatment.getGroup().getTreatmentgroupname().equalsIgnoreCase("combination")) {
					// TODO: Have to split the treatment into components, and add those.
					String genericNames[] = medication.getGenericName().split("-", 2);
					if (genericNames.length < 2) {
						LOG.warn("Skipping medication since combination drug doesn't split appropriately: " + medication.getGenericName() + ", Form: " + medication.getDrugForm());
					} else {
						responseTreatment = treatmentDao.getTreatmentByGenericDrugNameAndForm(genericNames[0],medication.getDrugForm(), connection);
						if (responseTreatment == null) {
							LOG.warn("Skipping medication since there is no treatment for generic drug " + genericNames[0] + ", Form: " + medication.getDrugForm());
						} else {
							prescriptionDao.updatePrescriptionEvent(responseTreatment.getTreatmentid(), medication, connection, true, true);
						}
						responseTreatment = treatmentDao.getTreatmentByGenericDrugNameAndForm(genericNames[1],medication.getDrugForm(), connection);
						if (responseTreatment == null) {
							LOG.warn("Skipping medication since there is no treatment for generic drug " + genericNames[1] + ", Form: " + medication.getDrugForm());
						} else {
							prescriptionDao.updatePrescriptionEvent(responseTreatment.getTreatmentid(), medication, connection, true, false);
						}
					}
				} else {
					prescriptionDao.updatePrescriptionEvent(responseTreatment.getTreatmentid(), medication, connection);
				}
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
	}

	/*
	private Map<Integer, List<Medication>> buildTreatmentMap(Connection connection, List<Medication> medications) throws Exception
	{
		Map<Integer, List<Medication>> treatmentMap = new HashMap<Integer, List<Medication>>();
		for (Iterator<Medication> iter = medications.iterator(); iter.hasNext();)
		{
			Medication medication = iter.next();
			LOG.debug(medication.shortDescription() + " " + medication.sigDescription());

			Integer treatmentId = new Integer(responseTreatment.getTreatmentid());
			if (!treatmentMap.containsKey(treatmentId))
			{
				List<Medication> mapList = new ArrayList<Medication>();
				treatmentMap.put(mapList);
				//compare med & medication and if medication is after med then continue.  otherwise skip this med.
			}
			List<Medication> medicationListFromMap = treatmentMap.get(treatmentId);
			LOG.debug("Putting medication in map for treatmentId: " + treatmentId);
			treatmentMap.put(treatmentId, medication);
		}
		return treatmentMap;
	}
	*/

}
