package flinn.dao.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import flinn.beans.request.RequestContainerBean;
import flinn.beans.request.RequestPrescriptionBean;
import flinn.beans.request.RequestPrescriptionSearchBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponsePatientBean;
import flinn.beans.response.ResponsePrescriptionBean;
import flinn.beans.response.ResponsePrescriptionContainerBean;
import flinn.beans.response.ResponsePrescriptionSearchContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.dao.DaoRequestManager;
import flinn.dao.PrescriptionDao;
import flinn.dao.model.Medication;
import flinn.rcopia.service.DoseTimingUtils;
import flinn.util.DateString;

public class PrescriptionDaoImp extends PrescriptionDao
{

	protected static final Logger LOG = Logger.getLogger(PrescriptionDaoImp.class);

	public String nullDate = "0000-00-00 00:00:00";

	static
	{
		LOG.debug("Log appender instantiated for " + FacilityDaoImp.class);
	}

	public ResponseContainerBean handlePrescriptionCreate(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		// sanity checks on incoming data.  Ensure no changes to aspects of the data we don't want changed.
		RequestPrescriptionBean bean = input.getPrescription();
		if (bean == null)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Prescription create submitted with no appropriate info", 41);
		}
		int newid = 0;
		List<ResponsePrescriptionBean> prescriptions = null;

		try
		{
			// Create new prescription record
			newid = create(bean, connection);
		}
		catch (Exception e)
		{
			LOG.error(e);
			try
			{
				connection.rollback();
			}
			catch (SQLException e1)
			{
				LOG.error("Error rolling back connection in handlePrescriptionCreate: " + e);
			}
			return DaoRequestManager.generateErrorBean(input.getAction(), "Prescription create failed (unknown error): " + e.getMessage(), 48);
		}

		try
		{
			connection.commit();
		}
		catch (Exception e)
		{
			try
			{
				connection.rollback();
			}
			catch (SQLException e1)
			{
				LOG.error("Error rolling back connection in handlePrescriptionCreate: " + e);
			}
			LOG.error("Prescription retrieval post-update failed (unknown error): " + e);
			return DaoRequestManager.generateErrorBean(input.getAction(), "Prescription retrieval post-create failed (unknown error): " + e.getMessage(), 49);
		}
		if (newid < 1)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Prescription create failed (unknown error - no returned ID)", 47);
		}
		bean = new RequestPrescriptionBean();
		bean.setPrescriptionid(newid);
		try
		{
			prescriptions = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Prescription retrieval post-create failed (unknown error): " + e.getMessage(), 47);
		}

		if (prescriptions == null || prescriptions.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Prescription retrieval post-create failed (unknown error) No Exception", 49);
		}

		ResponsePrescriptionContainerBean rcb = new ResponsePrescriptionContainerBean();
		rcb.setPrescription(prescriptions.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handlePrescriptionsCreate(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		if (input.getPrescriptions() == null)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Prescriptions create submitted with no appropriate info", 41);
		}
		String failureMessage = "";
		boolean failed = false;
		for (int i = 0; i < input.getPrescriptions().length; i++)
		{
			// sanity checks on incoming data.  Ensure no changes to aspects of the data we don't want changed.
			RequestPrescriptionBean bean = input.getPrescriptions()[i];
			if (bean == null)
			{
				failed = true;
				failureMessage = "Prescriptions create submitted with no appropriate info";
			}
			int newid = 0;
			List<ResponsePrescriptionBean> prescriptions = null;

			try
			{
				// Create new prescription record
				newid = create(bean, connection);
			}
			catch (Exception e)
			{
				LOG.error(e);
				try
				{
					connection.rollback();
				}
				catch (SQLException e1)
				{
					LOG.error("Error rolling back connection in handlePrescriptionsCreate: " + e);
				}
				failed = true;
				failureMessage = "Prescriptions create failed (unknown error): " + e.getMessage();
			}

			if (newid < 1)
			{
				failed = true;
				failureMessage = "Prescriptions create failed (unknown error - no returned ID)";
			}
			bean = new RequestPrescriptionBean();
			bean.setPrescriptionid(newid);
			try
			{
				prescriptions = find(bean, null, connection);
			}
			catch (Exception e)
			{
				failed = true;
				failureMessage = "Prescriptions retrieval post-create failed (unknown error): " + e.getMessage();
			}

			if (prescriptions == null || prescriptions.size() == 0)
			{
				failed = true;
				failureMessage = "Prescriptions retrieval post-create failed (unknown error) No Exception";
			}
		}
		if (failed)
		{
			try
			{
				connection.rollback();
			}
			catch (SQLException e1)
			{
				LOG.error("Error rolling back connection in handlePrescriptionsCreate: " + e1);
			}
			return DaoRequestManager.generateErrorBean(input.getAction(), failureMessage, 49);
		}
		else
		{
			try
			{
				connection.commit();
			}
			catch (Exception e)
			{
				try
				{
					connection.rollback();
				}
				catch (SQLException e1)
				{
					LOG.error("Error rolling back connection in handlePrescriptionsCreate: " + e);
				}
				LOG.error("Prescriptions retrieval post-update failed (unknown error): " + e);
				return DaoRequestManager.generateErrorBean(input.getAction(), "Prescriptions retrieval post-create failed (unknown error): " + e.getMessage(), 49);
			}
		}

		return session;
	}

	public ResponseContainerBean handlePrescriptionSearch(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection) throws Exception
	{
		RequestPrescriptionSearchBean search = input.getPrescriptionsearch();

		//Check patient facility to see if it = facility before returning labs
		ResponsePatientBean patient = null;
		try
		{
			patient = new PatientDaoImp().findPatientById(search.getPatientid(), connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Patient retrieval failed (Prescription) (unknown error): " + e.getMessage(), 47);
		}
		if (patient == null)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Patient retrieval failed (Prescription) (Incorrect patientid)", 49);
		}
		if (patient.getFacilityid() != session.getFacility().getFacilityid())
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Patient retrieval failed (Prescription) (Incorrect facility/patientid)", 49);
		}

		// Set up the search by prescriptions.
		RequestPrescriptionBean bean = new RequestPrescriptionBean();
		bean.setPatientid(search.getPatientid());

		HashMap<String, ArrayList<ResponsePrescriptionBean>> prescriptions = new HashMap<String, ArrayList<ResponsePrescriptionBean>>();
		HashMap<String, ResponsePrescriptionBean []> returnval = new HashMap<String, ResponsePrescriptionBean []>();
		List<ResponsePrescriptionBean> prescriptionList = null;

		try
		{
			prescriptionList = find(bean, "EntryDate DESC", connection);

			//Iterate through prescriptions
			Iterator<ResponsePrescriptionBean> it = prescriptionList.iterator();
			while (it.hasNext())
			{
				ResponsePrescriptionBean rpbean = it.next();
				String key = rpbean.getTreatment().getDetails().get("DisplayName");

				if (prescriptions.get(key) == null)
				{
					prescriptions.put(key, new ArrayList<ResponsePrescriptionBean>());
				}
				prescriptions.get(key).add(rpbean);
			}

			//Convert HashMap<String, ArrayList<ResponsePrescriptionBean>> to HashMap<String, ResponsePrescriptionBean[]>
			if (prescriptions.size() > 0)
			{
				Iterator<String> iterator = prescriptions.keySet().iterator();
				while (iterator.hasNext())
				{
					String key = iterator.next();
					ResponsePrescriptionBean [] values = new ResponsePrescriptionBean[prescriptions.get(key).size()];
					returnval.put(key, prescriptions.get(key).toArray(values));
				}
			}
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Prescriptions retrieval failed (unknown error): " + e.getMessage(), 47);
		}

		ResponsePrescriptionSearchContainerBean rcb = new ResponsePrescriptionSearchContainerBean();

		if (search.getShortform())
		{ //Get Shortform
			ResponsePrescriptionSearchContainerBean rpcb = new ResponsePrescriptionSearchContainerBean();
			rpcb = (ResponsePrescriptionSearchContainerBean) handlePrescriptionSearchShort(input, returnval);
			rpcb.setAction(new ResponseActionBean(input.getAction()));
			rcb = rpcb;
		}
		else
		{ //Get Standard Search
			ResponsePrescriptionSearchContainerBean rpcb = new ResponsePrescriptionSearchContainerBean();
			rpcb.setAction(new ResponseActionBean(input.getAction()));
			rpcb.setPrescriptions(returnval);
			rcb = rpcb;
		}

		return rcb;
	}

	public ResponseContainerBean handlePrescriptionSearchShort(RequestContainerBean input, HashMap<String, ResponsePrescriptionBean []> prescriptions) throws ParseException
	{
		HashMap<String, ResponsePrescriptionBean []> prescriptionsShort = prescriptions;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Iterate through prescriptions
		for (Iterator<String> it = prescriptionsShort.keySet().iterator(); it.hasNext();)
		{
			String key = it.next();
			// Remove discontinued prescriptions from short bean
			ResponsePrescriptionBean [] psdetails = prescriptionsShort.get(key);
			List<ResponsePrescriptionBean> psdetaillist = new ArrayList<ResponsePrescriptionBean>();
			psdetaillist.addAll(Arrays.asList(psdetails));

			boolean matches = false;
			//Get current date and entry to calc expiration based on duration
			Date now = df.parse(DateString.now());
			Date expdate = df.parse(psdetaillist.get(0).getEntrydate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(expdate);
			int doa = 0;
			if (psdetaillist.get(0).getTreatment().getDetails().get("DurationOfAction") != null) {
				doa = Integer.parseInt(psdetaillist.get(0).getTreatment().getDetails().get("DurationOfAction"));
			}
			cal.add(Calendar.DATE, psdetaillist.get(0).getDuration()+doa);
			expdate = cal.getTime();
			
			LOG.debug("Prescription: "+psdetaillist.get(0).getTreatment().getTreatmentname()+" Start: "+psdetaillist.get(0).getEntrydate()+" Expiration+doa: "+df.format(expdate));

			if (psdetaillist.get(0).getDiscontinue() || now.after(expdate)) //Check for discontinues or expired for first item
			{
				matches = true;
			}

			if (matches)
			{ //Remove key
				it.remove();
			}
		}

		ResponsePrescriptionSearchContainerBean rcb = new ResponsePrescriptionSearchContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setPrescriptions(prescriptionsShort);
		return rcb;
	}

	public ResponseContainerBean handlePrescriptionRead(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		RequestPrescriptionBean bean = input.getPrescription();
		List<ResponsePrescriptionBean> prescriptions = null;

		try
		{
			prescriptions = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Prescription retrieval failed (unknown error): " + e.getMessage(), 47);
		}

		if (prescriptions == null || prescriptions.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Prescription retrieval failed (unknown error) No Exception", 49);
		}

		ResponsePrescriptionContainerBean rcb = new ResponsePrescriptionContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setPrescription(prescriptions.get(0));

		return rcb;
	}

	public ResponsePrescriptionBean findPrescriptionById(int prescriptionid, Connection connection) throws Exception
	{
		RequestPrescriptionBean f = new RequestPrescriptionBean();
		f.setPrescriptionid(prescriptionid);

		List<ResponsePrescriptionBean> prescriptions = find(f, null, connection);
		if (prescriptions == null || prescriptions.size() == 0)
		{
			return null;
		}
		return prescriptions.get(0);
	}

	public void updatePrescriptionEvent(int treatmentId, Medication medication, Connection connection) throws Exception
	{
		updatePrescriptionEvent(treatmentId, medication, connection, false, false);
	}
	
	public void updatePrescriptionEvent(int treatmentId, Medication medication, Connection connection, boolean splitFlag, boolean firstMed) throws Exception
	{
		if (medication.isDeleted())
		{
			LOG.warn("Skipping medication marked as deleted");
			return;
		}

		RequestPrescriptionBean findBean = new RequestPrescriptionBean();
		findBean.setPatientid(medication.getPatientId().intValue());
		findBean.setTreatmentid(treatmentId);
		findBean.setRcopiaid(medication.getRcopiaId().intValue());
		findBean.setEntrydate(DateString.format(medication.getLastModifiedDate()));
		List<ResponsePrescriptionBean> responseList = find(findBean, "ID", connection);
		if (responseList != null && responseList.size() > 0)
		{
			LOG.warn("Skipping medication - already has prescription event");
			return;
		}

		String validationMessage = medication.isValidForTreatment();
		double dailyDoseAmount = -1;
		if (validationMessage.length() == 0)
		{
			try {
				if (splitFlag) {
					if (firstMed) {
						dailyDoseAmount = medication.calculateFirstDailyDoseAmount();
					} else {
						dailyDoseAmount = medication.calculateSecondDailyDoseAmount();
					}
				} else {
					dailyDoseAmount = medication.calculateDailyDoseAmount();
				}
			} catch (ParseException e) {
				LOG.warn("Prescription dosing parse exception - Dose set to '-1'/UNK");
			}
		}
		else
		{
			LOG.debug(validationMessage);
			LOG.warn("Prescription dosing unknown - Dose set to '-1'/UNK");
		}
		
		Date entryDate = medication.getLastModifiedDate();

		RequestPrescriptionBean rxRequest = new RequestPrescriptionBean();
		rxRequest.setPatientid(medication.getPatientId().intValue());
		rxRequest.setTreatmentid(treatmentId);
		rxRequest.setRcopiaid(medication.getRcopiaId().intValue());
		rxRequest.setEntrydate(DateString.format(entryDate));
		rxRequest.setDailydose(""+dailyDoseAmount);
		rxRequest.setDiscontinue(medication.isDiscontinued());

		//FIXME: Not sure if the stop date should come into play when calculating duration?
		int refills = 1;
		try {
			refills = 1 + Integer.parseInt(medication.getRefills());
		} catch (Exception e) {
			refills = 1;
		}
		if (refills == 0) refills = 1;
		if (medication.getDuration().intValue() > 0) {
			rxRequest.setDuration(medication.getDuration().intValue() * refills);
		} else if (dailyDoseAmount == -1) {
			rxRequest.setDuration(90);
			LOG.warn("Prescription dosing/duration unknown - Duration set to 90");
		} else {
			rxRequest.setDuration(0);
		}
		rxRequest.setDoctorname(medication.getPreparerId());
		create(rxRequest, connection);

		/*
		 * If the medication has a stop date in the past then set the medication as discontinued OR
		 * Write a process in the medicationdao to set all discontinued meds in the prescriptionevent table if the stopdate is in the past
		 *
		 */

	}

	/*
	 * No longer used
	 */
	@SuppressWarnings("unused")
	private List<Date> getTreatmentDates(Date startDate, Date stopDate, String doseTiming, int duration)
	{
		List<Date> treatmentDates = new ArrayList<Date>();
		int interval = DoseTimingUtils.getInterval(doseTiming);
		LOG.debug("Begin building treatment dates: startDate=" + DateString.format(startDate, "MM/dd/yyyy") + ", stopDate=" + DateString.format(stopDate, "MM/dd/yyyy") + ", duration=" + duration);
		if (interval > 0)
		{
			for (int i = 0; i < duration; i++)
			{
				if ((i % interval) == 0)
				{
					Date entryDate = DateUtils.addDays(startDate, i);
					if (stopDate != null && entryDate.compareTo(stopDate) >= 0)
					{
						LOG.debug("Quit processing medication: treatment date is on or after the medication stop date");
						break;
					}
					treatmentDates.add(entryDate);
				}
			}
		}

		return treatmentDates;
	}

}
