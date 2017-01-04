package flinn.dao.imp;

import flinn.beans.PatientDetailsBean;
import flinn.beans.request.RequestContainerBean;
import flinn.beans.request.RequestPatientBean;
import flinn.beans.request.RequestPatientSearchBean;
import flinn.beans.request.RequestPrescriptionBean;
import flinn.beans.response.*;
import flinn.dao.DaoRequestManager;
import flinn.dao.PatientDao;
import flinn.recommend.beans.RecommendDiagnosisBean;
import flinn.recommend.dao.imp.RuleDaoImp;
import flinn.util.DateString;
import flinn.util.cache.EHCacheImpl;
import flinn.util.cache.ICache;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PatientDaoImp extends PatientDao {

	protected static final Logger LOG = Logger.getLogger(PatientDaoImp.class);

	static {
		LOG.debug("Log appender instantiated for " + PatientDaoImp.class);
	}

	public ResponseContainerBean handlePatientCreate(
            RequestContainerBean input, ResponseSessionContainerBean session,
            Connection connection) {
		// sanity checks on incoming data. Ensure no changes to aspects of the
		// data we don't want changed.
		RequestPatientBean bean = input.getPatient();
		if (bean == null)
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient create submitted with no appropriate info", 41);
		if (bean.getFacilityid() == 0) {
			bean.setFacilityid(session.getFacility().getFacilityid());
		}
		if (bean.getFacilityid() != session.getFacility().getFacilityid()) {
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Patient create failed (Attempt to create patient in wrong facility)",
							41);
		}
		int newid = 0;
		List<ResponsePatientBean> patients = null;

		try {
			// Create new Patient record
			newid = create(bean, connection);
		} catch (Exception e) {
			LOG.error(e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handlePatientCreate: "
						+ e);
			}
			LOG.error("Inappropriate Exception caught: " + e.getMessage(), e);
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient create failed (unknown error): " + e.getMessage(),
					48);
		}

		try {
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handlePatientCreate: "
						+ e);
			}
			LOG.error("Patient retrieval post-update failed (unknown error): "
					+ e);
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient retrieval post-create failed (unknown error): "
							+ e.getMessage(), 49);
		}
		if (newid < 1)
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient create failed (unknown error - no returned ID)",
					47);
		bean = new RequestPatientBean();
		bean.setPatientid(newid);
		try {
			patients = find(bean, null, connection);
		} catch (Exception e) {
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient retrieval post-create failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (patients == null || patients.size() == 0) {
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Patient retrieval post-create failed (unknown error) No Exception",
							49);
		}

		ResponsePatientContainerBean rcb = new ResponsePatientContainerBean();
		rcb.setPatient(patients.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handlePatientRead(RequestContainerBean input,
                                                   ResponseSessionContainerBean session, Connection connection) {
		RequestPatientBean bean = input.getPatient();
		List<ResponsePatientBean> patients = null;

		try {
			patients = find(bean, null, connection);
		} catch (Exception e) {
			LOG.error("Inappropriate Exception caught: " + e.getMessage(), e);
			return DaoRequestManager.generateErrorBean(
					input.getAction(),
					"Patient retrieval failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (patients == null || patients.size() == 0) {
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Patient retrieval failed (unknown error) No Exception",
							49);
		}

		ResponseContainerBean rcb = null;
		if (session.getFacility().getFacilityid() != patients.get(0)
				.getFacilityid()) {
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient retrieval failed (Incorrect Facility)", 49);
		}
		if (bean.getShortform()) {
			ResponsePatientShortContainerBean rpcb = new ResponsePatientShortContainerBean();
			rpcb.setAction(new ResponseActionBean(input.getAction()));
			rpcb.setPatient(new ResponsePatientShortBean(patients.get(0), false));
			rcb = rpcb;
		} else if (bean.getStatusprefix() != null
				&& !bean.getStatusprefix().equals("")) {
			ResponsePatientBean smallerPatient = new ResponsePatientBean(
					patients.get(0), bean.getStatusprefix());
			ResponsePatientContainerBean rpcb = new ResponsePatientContainerBean();
			rpcb.setAction(new ResponseActionBean(input.getAction()));
			rpcb.setPatient(smallerPatient);
			rcb = rpcb;
		} else {
			ResponsePatientContainerBean rpcb = new ResponsePatientContainerBean();
			rpcb.setAction(new ResponseActionBean(input.getAction()));
			rpcb.setPatient(patients.get(0));
			rcb = rpcb;
		}
		return rcb;
	}

	public ResponsePatientBean findPatientById(int id, Connection connection)
			throws Exception {
		ResponsePatientBean responsePatientBean;
		LOG.debug("findPatientById --> looking for ID: " + String.valueOf(id));

		ICache<String, ResponsePatientBean> patientCache = EHCacheImpl.getDefaultInstance("patientCache");

		// try to retrieve the object from the cache by id
		responsePatientBean = patientCache.get(String.valueOf(id));
		if (responsePatientBean != null) {
			LOG.debug("findPatientById --> ID: " + String.valueOf(id)
					+ " found in cache");
		}

		// if the returned object is null, it was not found in the cache
		if (responsePatientBean == null) {
			RequestPatientBean rpbean = new RequestPatientBean();
			rpbean.setPatientid(id);
			List<ResponsePatientBean> patients = null;
			patients = find(rpbean, null, connection);
			if (patients != null && patients.size() > 0) {
				// get the first item from the response and add to cache
				responsePatientBean = patients.get(0);
			}
		}
		return responsePatientBean;
	}

	public ResponseContainerBean handlePatientSearch(
            RequestContainerBean input, ResponseSessionContainerBean session,
            Connection connection) {
		RequestPatientSearchBean search = input.getPatientsearch();

		RequestPatientBean bean = new RequestPatientBean();
		bean.setFacilityid(search.getFacilityid());
		bean.setPatientid(search.getPatientid());
		bean.setValid(search.getValid());
		if (search.getFirstname() != null || search.getLastname() != null
				|| search.getSex() != null || search.getBirth() != null
				|| search.getPatientidentifier() != null) {
			bean.setDetails(new HashMap<String, String>());
			if (search.getFirstname() != null)
				bean.getDetails().put("firstname", search.getFirstname());
			if (search.getLastname() != null)
				bean.getDetails().put("lastname", search.getLastname());
			if (search.getSex() != null)
				bean.getDetails().put("sex", search.getSex());
			if (search.getBirth() != null)
				bean.getDetails().put("birth", search.getBirth());
			if (search.getPatientidentifier() != null)
				bean.getDetails().put("patientidentifier",
						search.getPatientidentifier());
		}

		List<ResponsePatientBean> patients = null;
		bean.setFacilityid(session.getFacility().getFacilityid());
		try {
			patients = find(bean, null, connection);
		} catch (Exception e) {
			LOG.error("Inappropriate Exception caught: " + e.getMessage(), e);
			return DaoRequestManager.generateErrorBean(
					input.getAction(),
					"Patient retrieval failed (unknown error): "
							+ e.getMessage(), 47);
		}

		HashMap<String, String> details = bean.getDetails();
		if (details != null && details.size() > 0) {
			// find will only check against direct patient properties. Not
			// detail or status elements.
			Iterator<String> it = details.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = details.get(key);
				if (value != null && !value.trim().equals("")) {
					for (int i = 0; i < patients.size(); i++) {
						PatientDetailsBean[] pdetails = patients.get(i)
								.getDetails().get(key);
						boolean matches = false;
						if (pdetails != null) {
							for (int j = 0; j < pdetails.length; j++) {
								if (pdetails[j].getValue().trim().toLowerCase()
										.contains(value.trim().toLowerCase())) {
									matches = true;
								}
							}
						}
						if (!matches) {
							patients.remove(i);
							i--;
						}
					}
				}
			}
		}
		int page = search.getPage();
		if (page < 1)
			page = 1;
		page--;
		int pagecount = search.getPagecount();
		if (pagecount < 1)
			pagecount = 10;

		// After this, thispagecount refers to the number of entries on this
		// page, rather than the number requested.
		int thispagecount = pagecount;
		if (page * pagecount + pagecount > patients.size())
			thispagecount = patients.size() - page * pagecount;
		if (thispagecount < 0)
			thispagecount = 0;

		ResponsePatientShortBean[] patientshorts = new ResponsePatientShortBean[thispagecount];
		for (int i = 0; i < thispagecount; i++) {
			patientshorts[i] = new ResponsePatientShortBean(
					patients.get((page * pagecount) + i), true);
		}

		ResponsePatientSearchContainerBean rcb = new ResponsePatientSearchContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setPatients(patientshorts);
		rcb.setPage(page + 1);
		rcb.setPagecount(pagecount);
		rcb.setTotal(patients.size());
		return rcb;
	}

	public HashMap<String,Float> handlePatientPrescriptionByDiagnosis(
			int diagnosisid,
			Connection connection) {
		
		if (diagnosisid == 0) return null;
		
		List<ResponsePatientBean> patients = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HashMap<String,Float> ret = null;
		HashMap<String,Integer> totals = new HashMap<String,Integer>();
		int total = 0;
		
		try {
			patients = find(new RequestPatientBean(), null, connection);
		} catch (Exception e) {
			LOG.error("Inappropriate Exception caught: " + e.getMessage(), e);
			return null;
		}

		RecommendDiagnosisBean[] allDiagnoses = null;
		try {
			allDiagnoses = new RuleDaoImp().getAllDiagnoses(connection);
		} catch (Exception e) {
			LOG.error("Inappropriate Exception caught: " + e.getMessage(), e);
		}

		for (int i = 0; i < patients.size(); i++) {
			RequestPrescriptionBean bean = new RequestPrescriptionBean();
			HashMap<String,String> treatments = new HashMap<String,String>();
			bean.setPatientid(patients.get(i).getPatientid());
			List<ResponsePrescriptionBean> prescriptionList = null;
			
			if (patientHasDiagnosis(patients.get(i),allDiagnoses, diagnosisid)) {
				try {
					prescriptionList = new PrescriptionDaoImp().find(bean, "EntryDate DESC", connection);
				} catch (Exception e) {
					LOG.error("Inappropriate Exception caught: " + e.getMessage(), e);
				}
				
				Iterator<ResponsePrescriptionBean> it = prescriptionList.iterator();
				while (it.hasNext())
				{
					ResponsePrescriptionBean rpbean = it.next();
					long distance = 181;
					try {
						distance =(new Date().getTime() - (df.parse(rpbean.getEntrydate()).getTime()+(1000*3600*12)))/(1000*3600*24);
					} catch (Exception e) {
						// nothing to do here.
					}
					LOG.debug("Prescription found: "+rpbean.getTreatment().getDetails().get("GuidelineChartName"));
					if (distance < 181 && patientDiagnosisIdAtDate(patients.get(i),rpbean.getEntrydate()) == diagnosisid) {
						LOG.debug("Prescription applies: "+rpbean.getEntrydate()+"  "+total);
						if (!treatments.containsKey(rpbean.getTreatment().getDetails().get("GuidelineChartName"))) {
							LOG.debug("Adding Prescription: "+rpbean.getTreatment().getDetails().get("GuidelineChartName"));
							treatments.put(rpbean.getTreatment().getDetails().get("GuidelineChartName"), 
									rpbean.getTreatment().getDetails().get("GuidelineChartName"));
							if (totals.containsKey(rpbean.getTreatment().getDetails().get("GuidelineChartName"))) {
								totals.put(rpbean.getTreatment().getDetails().get("GuidelineChartName"), 
										new Integer(totals.get(rpbean.getTreatment().getDetails().get("GuidelineChartName")).intValue()+1));
								total++;
							} else {
								totals.put(rpbean.getTreatment().getDetails().get("GuidelineChartName"), new Integer(1));
								total++;
							}
						} 
					}
				}
			}
		}
		if (totals.size() > 0) {
			HashMap<String,Float> tmpret = new HashMap<String,Float>();
			Iterator<String> keyit = totals.keySet().iterator();
			while (keyit.hasNext()) {
				String key = keyit.next();
				int thou = ((int)(((float)(totals.get(key) * 1000)) / total + 0.5));
				if (thou > 10) {
					float percent = ((float) thou) / 10;
					tmpret.put(key, new Float(percent));
				}
			}
			ArrayList<String> keys = new ArrayList<String>();
			keyit = tmpret.keySet().iterator();
			while (keyit.hasNext()) {
				String key = keyit.next();
				boolean done = false;
				for (int i=0; i<keys.size() && !done; i++) {
					if (tmpret.get(keys.get(i)).floatValue()-tmpret.get(key).floatValue() > 0) {
						keys.add(i, key);
						done = true;
					}
				}
				if (!done) keys.add(key);
			}
			ret = new HashMap<String,Float>();
			for (int i=0; i<keys.size(); i++) {
				ret.put(keys.get(i), tmpret.get(keys.get(i)));
			}
		}
		return ret;
	}

	private boolean patientHasDiagnosis(
            ResponsePatientBean patient,
            RecommendDiagnosisBean[] allDiagnoses, int diagnosisid) {
		boolean ret = false;
		RecommendDiagnosisBean diagnosis = null;
		for (int i = 0; i < allDiagnoses.length; i++) {
			if (allDiagnoses[i].getDiagnosisid() == diagnosisid) diagnosis = allDiagnoses[i];
		}
		if (diagnosis != null) {
			int stage = Integer.parseInt(diagnosis.getStage());
			int base = diagnosisid - ((diagnosisid-1)%4);
			LOG.debug("patientHasDiagnosis: stage,base: "+stage+","+base);
			if (patient.getStatus() != null && patient.getStatus().get("diagnosis_primary") != null) {
				for (int i = 0; i < patient.getStatus().get("diagnosis_primary").length; i++) {
					String diagPri = patient.getStatus().get("diagnosis_primary")[i].getValue();
					LOG.debug("patientHasDiagnosis: diagPri: "+diagPri);
					if (diagPri.contains("Bipolar")) {
						if (diagPri.toLowerCase().contains("manic")) {
							if (base == 9) ret = true;
						} else {
							if (base == 13) ret = true;
						}
					} else if (diagPri.contains("Schizophrenia")) {
						if (base == 17) ret = true;
					} else if (diagPri.contains("Major Depressive Disorder")) {
						if (diagPri.contains("w/Psychotic")) {
							if (base == 5) ret = true;
						} else {
							if (base == 1) ret = true;
						}
					}
				}
			}
			if (ret) LOG.debug("patientHasDiagnosis: Base found: "+base);
			if (ret == true && patient.getStatus() != null && patient.getStatus().get("diagnosis_stage") != null) {
				ret = false;
				for (int i = 0; i < patient.getStatus().get("diagnosis_stage").length; i++) {
					int diagStage = Integer.parseInt(patient.getStatus().get("diagnosis_stage")[i].getValue());
					if (diagStage == stage) {
						ret = true;
						LOG.debug("patientHasDiagnosis: Stage found: "+stage);
					}
				}
			}
		}
		return ret;
	}
	
	private int patientDiagnosisIdAtDate(ResponsePatientBean patient, String date) {
		int ret = 0;
		long time1 = DateString.interpret(date).getTime();
		int stage = 0;
		int base = 0;
		String primary = null;
		
		if (patient.getStatus() != null && patient.getStatus().get("diagnosis_primary") != null) {
			for (int i = 0; i < patient.getStatus().get("diagnosis_primary").length && primary == null; i++) {
				long time2 = DateString.interpret(patient.getStatus().get("diagnosis_primary")[i].getEntrydate()).getTime();
				LOG.debug("patientDiagnosisIdAtDate: check: "+date+" "+patient.getStatus().get("diagnosis_primary")[i].getEntrydate()+" "+patient.getStatus().get("diagnosis_primary")[i].getValue());
				if (primary == null && time2 < time1) primary = patient.getStatus().get("diagnosis_primary")[i].getValue();
			}
		}
		if (primary != null) {
			LOG.debug("patientDiagnosisIdAtDate: primary: "+primary);
			if (primary.contains("Bipolar")) {
				if (primary.toLowerCase().contains("manic")) {
					base = 9;
				} else {
					base = 13;
				}
			} else if (primary.contains("Schizophrenia")) {
				base = 17;
			} else if (primary.contains("Major Depressive Disorder")) {
				if (primary.contains("w/Psychotic")) {
					base = 5;
				} else {
					base = 1;
				}
			}
			LOG.debug("patientDiagnosisIdAtDate: base: "+base);
			for (int i = 0; i < patient.getStatus().get("diagnosis_stage").length && stage == 0; i++) {
				long time2 = DateString.interpret(patient.getStatus().get("diagnosis_stage")[i].getEntrydate()).getTime();
				if (stage == 0 && time2 < time1) stage = Integer.parseInt(patient.getStatus().get("diagnosis_stage")[i].getValue());
			}
			if (stage > 0) ret = base + stage - 1;
		}
		
		return ret;
	}

	public ResponseContainerBean handlePatientUpdate(
            RequestContainerBean input, ResponseSessionContainerBean session,
            Connection connection) {
		RequestPatientBean bean = input.getPatient();
		if (bean == null)
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient update submitted with no appropriate profile", 41);
		ResponsePatientBean patient = null;

		try {
			patient = findPatientById(bean.getPatientid(), connection);
		} catch (Exception e) {
			return DaoRequestManager.generateErrorBean(
					input.getAction(),
					"Patient retrieval failed in update (unknown error): "
							+ e.getMessage(), 47);
		}

		if (patient == null) {
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Patient retrieval failed in update (unknown error) No Exception",
							49);
		}
		try {
			// Update current patient record
			update(bean, patient, connection);

		} catch (Exception e) {
			LOG.error(e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handlePatientUpdate: "
						+ e);
			}
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient update failed (unknown error): " + e.getMessage(),
					48);
		}

		try {
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handlePatientUpdate: "
						+ e);
			}
			LOG.error("Patient retrieval post-update failed (unknown error): "
					+ e);
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient retrieval post-update failed (unknown error): "
							+ e.getMessage(), 49);
		}
		
		patient = null;
		try {
			patient = findPatientById(bean.getPatientid(), connection);
		} catch (Exception e) {
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient retrieval post-update failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (patient == null) {
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Patient retrieval post-update failed (unknown error) No Exception",
							49);
		}

		ResponsePatientShortContainerBean rpcb = new ResponsePatientShortContainerBean();
		rpcb.setAction(new ResponseActionBean(input.getAction()));
		rpcb.setPatient(new ResponsePatientShortBean(patient, false));
		return rpcb;
	}

	public ResponseContainerBean handlePatientAge(
            RequestContainerBean input, ResponseSessionContainerBean session,
            Connection connection) {
		RequestPatientBean bean = input.getPatient();
		if (bean == null || bean.getAgingfactor() <= 0)
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient age submitted with no appropriate patientid/agingfactor", 41);
		ResponsePatientBean patient = null;

		try {
			patient = findPatientById(bean.getPatientid(), connection);
		} catch (Exception e) {
			return DaoRequestManager.generateErrorBean(
					input.getAction(),
					"Patient retrieval failed in age (unknown error): "
							+ e.getMessage(), 47);
		}

		if (patient == null) {
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Patient retrieval failed in age (unknown error) No Exception",
							49);
		}
		try {
			// Update current patient record
			age(bean.getPatientid(), bean.getAgingfactor(), connection);

		} catch (Exception e) {
			LOG.error(e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handlePatientAge: "
						+ e);
			}
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient age failed (unknown error): " + e.getMessage(),
					48);
		}

		try {
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handlePatientAge: "
						+ e);
			}
			LOG.error("Patient retrieval post-age failed (unknown error): "
					+ e);
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient retrieval post-age failed (unknown error): "
							+ e.getMessage(), 49);
		}
		
		patient = null;
		try {
			patient = findPatientById(bean.getPatientid(), connection);
		} catch (Exception e) {
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Patient retrieval post-age failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (patient == null) {
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Patient retrieval post-age failed (unknown error) No Exception",
							49);
		}

		ResponsePatientShortContainerBean rpcb = new ResponsePatientShortContainerBean();
		rpcb.setAction(new ResponseActionBean(input.getAction()));
		rpcb.setPatient(new ResponsePatientShortBean(patient, false));
		return rpcb;
	}

	private void age (int patientid, int agingfactor, Connection connection) throws SQLException {
		// UPDATE Patient SET StartDate=StartDate - INTERVAL ? DAY WHERE ID=?
		// UPDATE PatientDetail SET EntryDate=EntryDate - INTERVAL ? DAY WHERE PatientID=?
		// UPDATE PatientStatus SET EntryDate=EntryDate - INTERVAL ? DAY WHERE PatientID=?
		// UPDATE PrescriptionEvent SET EntryDate=EntryDate - INTERVAL ? DAY WHERE PatientID=?
		// UPDATE ProgressNote SET EntryDate=EntryDate - INTERVAL ? DAY WHERE PatientID=?
		// UPDATE RecommendReason SET ReasonDate=ReasonDate - INTERVAL ? DAY WHERE PatientID=?
		final String command1 = "UPDATE Patient SET StartDate=StartDate - INTERVAL ? DAY WHERE ID=?";
		final String command2 = "UPDATE PatientDetail SET EntryDate=EntryDate - INTERVAL ? DAY WHERE PatientID=?";
		final String command3 = "UPDATE PatientStatus SET EntryDate=EntryDate - INTERVAL ? DAY WHERE PatientID=?";
		final String command4 = "UPDATE PrescriptionEvent SET EntryDate=EntryDate - INTERVAL ? DAY WHERE PatientID=?";
		final String command5 = "UPDATE ProgressNote SET EntryDate=EntryDate - INTERVAL ? DAY WHERE PatientID=?";
		final String command6 = "UPDATE RecommendReason SET ReasonDate=ReasonDate - INTERVAL ? DAY WHERE PatientID=?";
		
		ICache<String, ResponsePatientBean> patientCache = EHCacheImpl
		.getDefaultInstance("patientCache");

		// remove from patient from cache if exist
		patientCache.delete(String.valueOf(patientid));
		
		PreparedStatement stat1 = connection.prepareStatement(command1);
		stat1.clearParameters();
		stat1.setInt(1, agingfactor);
		stat1.setInt(2, patientid);
		stat1.executeUpdate();
		stat1.close();

		PreparedStatement stat2 = connection.prepareStatement(command2);
		stat2.clearParameters();
		stat2.setInt(1, agingfactor);
		stat2.setInt(2, patientid);
		stat2.executeUpdate();
		stat2.close();

		PreparedStatement stat3 = connection.prepareStatement(command3);
		stat3.clearParameters();
		stat3.setInt(1, agingfactor);
		stat3.setInt(2, patientid);
		stat3.executeUpdate();
		stat3.close();

		PreparedStatement stat4 = connection.prepareStatement(command4);
		stat4.clearParameters();
		stat4.setInt(1, agingfactor);
		stat4.setInt(2, patientid);
		stat4.executeUpdate();
		stat4.close();

		PreparedStatement stat5 = connection.prepareStatement(command5);
		stat5.clearParameters();
		stat5.setInt(1, agingfactor);
		stat5.setInt(2, patientid);
		stat5.executeUpdate();
		stat5.close();

		PreparedStatement stat6 = connection.prepareStatement(command6);
		stat6.clearParameters();
		stat6.setInt(1, agingfactor);
		stat6.setInt(2, patientid);
		stat6.executeUpdate();
		stat6.close();
		
		
	}
}
