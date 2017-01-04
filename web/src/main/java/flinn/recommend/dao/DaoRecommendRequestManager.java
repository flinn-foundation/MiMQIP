package flinn.recommend.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import flinn.beans.request.RequestContainerBean;
import flinn.beans.request.RequestPatientBean;
import flinn.beans.request.RequestPrescriptionBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseInitialStagingBean;
import flinn.beans.response.ResponseInitialStagingContainerBean;
import flinn.beans.response.ResponseLabBean;
import flinn.beans.response.ResponsePatientBean;
import flinn.beans.response.ResponsePrescriptionBean;
import flinn.beans.response.ResponseRecommendationBean;
import flinn.beans.response.ResponseRecommendationContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.dao.AbstractBaseDao;
import flinn.dao.DaoRequestManager;
import flinn.dao.imp.LabDaoImp;
import flinn.dao.imp.PatientDaoImp;
import flinn.dao.imp.PrescriptionDaoImp;
import flinn.recommend.beans.RecommendDiagnosisBean;
import flinn.recommend.beans.RecommendMessageBean;
import flinn.recommend.beans.RecommendPatientInfoBean;
import flinn.recommend.beans.RecommendRuleCriteriaBean;
import flinn.recommend.beans.response.ResponseRuleBean;
import flinn.recommend.dao.imp.GuidelineReasonDaoImp;
import flinn.recommend.dao.imp.RuleDaoImp;
import flinn.service.PatientService;
import flinn.service.ServiceException;

public class DaoRecommendRequestManager extends AbstractBaseDao {
	/* TODO: need to change this to handle web services (like DaoRequestManager) rather than handle calls directly via java method calls.  Phase 2 */
	
	public ResponseContainerBean handleInitialStaging(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection, boolean b)
	{
		if (input.getInitialstaging() != null)
		{
			if (input.getInitialstaging().getPatientid() <= 0)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Inappropriate arguments (inappropriate patientid)", 101);
			}
			if (input.getInitialstaging().getTreatments() == null || input.getInitialstaging().getTreatments().length == 0)
			{
				ResponseInitialStagingContainerBean rcb = new ResponseInitialStagingContainerBean();
				rcb.setAction(new ResponseActionBean(input.getAction()));
				ResponseInitialStagingBean risb = new ResponseInitialStagingBean();
				risb.setMessage("Stage");
				risb.setStage("1");
				rcb.setInitialstage(risb);
				return rcb;
			}
			else
			{
				try {
					RecommendDiagnosisBean[] diagnoses = new RuleDaoImp().getAllDiagnoses(connection);
					int stage = flinn.util.RecommendUtils.stageRecommendation(input.getInitialstaging().getDiagnosis(),input.getInitialstaging().getTreatments(), diagnoses);

					ResponseInitialStagingContainerBean rcb = new ResponseInitialStagingContainerBean();
					rcb.setAction(new ResponseActionBean(input.getAction()));
					ResponseInitialStagingBean risb = new ResponseInitialStagingBean();
					risb.setMessage("Stage");
					risb.setStage(""+stage);
					rcb.setInitialstage(risb);
					return rcb;
				} catch (Exception e) {
					return DaoRequestManager.generateErrorBean(input.getAction(), "Exception Thrown getting Initial Staging Info", 101);
				}
			}
		}
		return DaoRequestManager.generateErrorBean(input.getAction(), "Inappropriate arguments (no initialstaging submitted)", 101);
	}

	public ResponseContainerBean handleReason(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection, boolean b)
	{
		if (input.getGuidelinereason() != null)
		{
			if (input.getGuidelinereason().getReason().toString().length() < 1)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Inappropriate arguments (inappropriate reason)", 101);
			}
			else //Set reason bean
			{
				ResponseContainerBean rcb = (new GuidelineReasonDaoImp()).handleGuidelineReasonCreate(input, session, connection);
				
				return rcb;
			}
		}
		return DaoRequestManager.generateErrorBean(input.getAction(), "Inappropriate arguments (no guidelinereason submitted)", 101);
	}

	public boolean handleConsistencyCheck(int patientid) throws ServiceException {
		
		ResponsePatientBean patientBean = new PatientService().getPatientResponseBean(patientid);
		if (patientBean != null) {
			ResponseRecommendationContainerBean rcb = requestRecommendation(new RequestPatientBean(patientBean));
			if (rcb.getRecommendation().getConsistent() != null && (rcb.getRecommendation().getConsistent().equals("1") || rcb.getRecommendation().getConsistent().equals("true"))) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
	
	public ResponseContainerBean handleRequest(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection, boolean b)
	{
		if (input.getPatient() == null) {
			return DaoRequestManager.generateErrorBean(input.getAction(), "Inappropriate arguments (no patient submitted)", 101);
		}
		if (input.getPatient().getPatientid() <= 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Inappropriate arguments (inappropriate patientid)", 101);
		}
		// if (input.getPatient().getFacilityid() != session.getFacility().getFacilityid()) {
		//	return DaoRequestManager.generateErrorBean(input.getAction(), "Inappropriate arguments (inappropriate facilityid, number)", 101);
		// }

		ResponseRecommendationContainerBean rcb = requestRecommendation(input.getPatient());
		if (rcb == null) {
			return DaoRequestManager.generateErrorBean(input.getAction(), "Inappropriate arguments (no patient submitted)", 101);
		}
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}
	
	public ResponseRecommendationContainerBean requestRecommendation(RequestPatientBean patientreq)
	{
		if (patientreq != null)
		{
			// get prescriptions
			try
			{
				if (connection == null || connection.isClosed())
				{
					renewConnection();
					if (connection == null || connection.isClosed())
					{
						LOG.error("Unable to renew connection in requestRecommendation");
						return null;
					}
				}
			}
			catch (Exception e)
			{
				LOG.error("Unable to renew connection in requestRecommendation: " + e);
			}

			RequestPrescriptionBean bean = new RequestPrescriptionBean();
			bean.setPatientid(patientreq.getPatientid());
			
			HashMap<String, ArrayList<ResponsePrescriptionBean>> prescriptionInterim = new HashMap<String, ArrayList<ResponsePrescriptionBean>>();
			HashMap<String, ResponsePrescriptionBean []> prescriptions = new HashMap<String, ResponsePrescriptionBean []>();
			List<ResponsePrescriptionBean> prescriptionList = null;
			ResponsePatientBean patient = null;
			ResponseLabBean[] labs = null;
			List<ResponseRuleBean> rules = null;
			RecommendDiagnosisBean[] diagnoses = null;

			try
			{
				prescriptionList = new PrescriptionDaoImp().find(bean, "EntryDate DESC", connection);

				//Iterate through prescriptions
				Iterator<ResponsePrescriptionBean> it = prescriptionList.iterator();
				while (it.hasNext())
				{
					ResponsePrescriptionBean rpbean = it.next();
					String key = rpbean.getTreatment().getDetails().get("GuidelineChartName");

					if (prescriptionInterim.get(key) == null)
					{
						prescriptionInterim.put(key, new ArrayList<ResponsePrescriptionBean>());
					}
					prescriptionInterim.get(key).add(rpbean);
				}

				//Convert HashMap<String, ArrayList<ResponsePrescriptionBean>> to HashMap<String, ResponsePrescriptionBean[]>
				if (prescriptionInterim.size() > 0)
				{
					Iterator<String> iterator = prescriptionInterim.keySet().iterator();
					while (iterator.hasNext())
					{
						String key = iterator.next();
						ResponsePrescriptionBean [] values = new ResponsePrescriptionBean[prescriptionInterim.get(key).size()];
						prescriptions.put(key, prescriptionInterim.get(key).toArray(values));
					}
				}
				patient = new PatientDaoImp().findPatientById(patientreq.getPatientid(), connection);
				labs = new LabDaoImp().findByPatientId(patientreq.getPatientid(), connection);
				rules = new RuleDaoImp().findAllRules(null, null, connection);
				diagnoses = new RuleDaoImp().getAllDiagnoses(connection);
			}
			catch (Exception e)
			{
				LOG.error("Prescriptions/patient retrieval failed (unknown error): " + e.getMessage());
			}
			RecommendPatientInfoBean patientInfo = new RecommendPatientInfoBean(patient,prescriptions,labs, diagnoses);
			System.out.print(patientInfo.toExpandedString());
			// build recommendation
			ResponseRecommendationContainerBean rcb = new ResponseRecommendationContainerBean();
			ResponseRecommendationBean rrb = new ResponseRecommendationBean();

			String[] status = { "Patient Name: "+patient.getDetails().get("firstname")[0].getValue()+" "+patient.getDetails().get("lastname")[0].getValue(),
					"Patient Birthday: " + patient.getDetails().get("birth")[0].getValue(), 
					"Patient Diagnosis: " + patientInfo.getInfo().get("diagnosis_primary"), 
					"Patient Stage: " + patientInfo.getInfo().get("diagnosis_stage") };
			rrb.setStatus(status);
			String[] medications = new String[patientInfo.getPrescriptioninfo().size()];
			int i = 0;
			for (Iterator<String> it = patientInfo.getPrescriptioninfo().keySet().iterator(); it.hasNext();) {
				String key = it.next();
				if (patientInfo.getPrescriptioninfo().get(key) != null) {
					medications[i] = key + ": " + patientInfo.getPrescriptioninfo().get(key).get("dose") + patientInfo.getPrescriptioninfo().get(key).get("unit");
				} else {
					medications[i] = "Problem with medication " + key;
					LOG.error("Problem with medication " + key);
				}
				i++;
				
			}
			rrb.setMedications(medications);
			
			String[][] othermessages = null;
			String[] othersideeffects = getOthersideeffects(patientInfo, rules);
			String[] otherreports = getOtherreports(patientInfo, rules);
			
			int k = 0;
			// TODO: This should really handle "other messages" more flexibly than this.  Unfortunately, not at this time.
			if (othersideeffects != null) {
				othermessages = new String[2][];
				othermessages[k] = othersideeffects;
				k++;
			} else {
				othermessages = new String[1][];
			}
			if (otherreports != null) {
				othermessages[k] = otherreports;
				k++;
			}
			//String[][] othermessages = { { "Your Colleagues' Prescriptions", "Sufficient data for calculating the percent of clinicians who prescribed the specific guideline medications for patients with the same diagnosis are not yet available" },
			//		{ "Notes", "None at the present time" }
			//};
			rrb.setOthermessages(othermessages);
			
			rrb.setMedicaltrial(getMedicaltrial(patientInfo, rules));
			rrb.setGeneralconsistency(getGeneralConsistency(patientInfo, rules));
			rrb.setAdditionalconsistency(getAdditionalConsistency(patientInfo, rules));
			rrb.setGeneralmessages(getGeneralMessages(patientInfo, rules));
			rrb.setClinicalresponse(getClinicalResponse(patientInfo, rules));
			rrb.setTreatmentmessages(getTreatmentMessages(patientInfo, rules));
			rrb.setSpecialmessages(getSpecialMessages(patientInfo, rules));
			rrb.setConsistent(patientInfo.getInfo().get("consistent"));
			if (patientInfo.getDiagnosis() != null) {
				rrb.setGuidelinechart(patientInfo.getDiagnosis().getGuidelinechart());
			} else {
				rrb.setGuidelinechart(null);
			}
			rcb.setRecommendation(rrb);
			
			try
			{
				if (connection != null && !connection.isClosed())
				{
					connection.close();
					connection = null;
				}
			}
			catch (Exception e)
			{
				LOG.error("Unable to close connection in requestRecommendation: " + e);
			}

			return rcb;
		}
		return null;
	}

	private String[] getMedicaltrial(RecommendPatientInfoBean patientInfo,
			List<ResponseRuleBean> rules) {
		ArrayList<String> messages = new ArrayList<String>();
		if (patientInfo.getPrescriptioninfo() != null) {
			Iterator<String> it = patientInfo.getPrescriptioninfo().keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				HashMap<String,String> prescription = patientInfo.getPrescriptioninfo().get(key);
				ArrayList<String> message = evaluatePrescriptionRules(prescription,patientInfo,rules);
				if (message != null) {
					messages.addAll(message);
				}
			}
		}
		if (messages.size() > 0) {
			String[] ret = new String[messages.size()];
			ret = messages.toArray(ret);
			return ret;
		}
		return null;
	}

	private ArrayList<String> evaluatePrescriptionRules(
			HashMap<String, String> prescription,
			RecommendPatientInfoBean patientInfo, List<ResponseRuleBean> rules) {
		ArrayList<String> ret = new ArrayList<String>();
		for (int i=0; i<rules.size(); i++) {
			ResponseRuleBean rule = rules.get(i);
			if (rule.getRuletype().equals("medicaltrial") && rule.getValid()) {
				boolean ruleresult = true;
				if (rule.getCriteria() != null && rule.getCriteria().length > 0) {
					for (int j=0;j<rule.getCriteria().length; j++) {
						if (evaluateRuleCriterium(rule.getCriteria()[j],patientInfo.getInfo(),prescription)) {
							// Criteria satisfied, don't change the result.
						} else {
							// Criteria NOT satified, flag result as false, continue.
							ruleresult = false;
						}
					}
				}
				if (ruleresult && rule.getMessages() != null && rule.getMessages().length > 0) {
					ret.add(evaluateMessage(rule.getMessages()[0],patientInfo.getInfo(),prescription));
				} else if (!ruleresult && rule.getMessages() != null && rule.getMessages().length > 1) {
					ret.add(evaluateMessage(rule.getMessages()[1],patientInfo.getInfo(),prescription));
				}
			}
		}
		if (ret.size() == 0) ret = null;
		return ret;
	}

	private ArrayList<String> evaluateRules(String type,
			RecommendPatientInfoBean patientInfo, List<ResponseRuleBean> rules) {
		ArrayList<String> ret = new ArrayList<String>();
		for (int i=0; i<rules.size(); i++) {
			ResponseRuleBean rule = rules.get(i);
			if (rule.getRuletype().equals(type) && rule.getValid()) {
				boolean diagnosismatch = true;
				if (rule.getDiagnoses() != null) {
					diagnosismatch = false;
					for (int j=0; j<rule.getDiagnoses().length; j++) {
						if (rule.getDiagnoses()[j].getDiagnosisid() == patientInfo.getDiagnosis().getDiagnosisid()) diagnosismatch = true;
					}
				}
				if (diagnosismatch) {
					boolean ruleresult = true;
					if (ruleresult && rule.getCriteria() != null && rule.getCriteria().length > 0) {
						for (int j=0;j<rule.getCriteria().length; j++) {
							if (evaluateRuleCriterium(rule.getCriteria()[j],patientInfo.getInfo(),null)) {
								// Criteria satisfied, don't change the result.
							} else {
								// Criteria NOT satified, flag result as false, continue.
								ruleresult = false;
							}
						}
					}
					if (ruleresult && rule.getMessages() != null && rule.getMessages().length > 0) {
						ret.add(evaluateMessage(rule.getMessages()[0],patientInfo.getInfo(),null));
					} else if (!ruleresult && rule.getMessages() != null && rule.getMessages().length > 1) {
						ret.add(evaluateMessage(rule.getMessages()[1],patientInfo.getInfo(),null));
					}
				}
			}
		}
		if (ret.size() == 0) ret = null;
		return ret;
	}

	private String[] getGeneralConsistency(
			RecommendPatientInfoBean patientInfo, List<ResponseRuleBean> rules) {
		ArrayList<String> messages = evaluateRules("generalconsistency",patientInfo,rules);
		if (messages == null || messages.size() == 0) return null;
		String[] ret = new String[messages.size()];
		ret = messages.toArray(ret);
		return ret;
	}

	private String[] getAdditionalConsistency(
			RecommendPatientInfoBean patientInfo, List<ResponseRuleBean> rules) {
		ArrayList<String> messages = evaluateRules("additionalconsistency",patientInfo,rules);
		if (messages == null || messages.size() == 0) return null;
		String[] ret = new String[messages.size()];
		ret = messages.toArray(ret);
		return ret;
	}

	private String[] getGeneralMessages(RecommendPatientInfoBean patientInfo,
			List<ResponseRuleBean> rules) {
		ArrayList<String> messages = evaluateRules("generalmessages",patientInfo,rules);
		if (messages == null || messages.size() == 0) return null;
		String[] ret = new String[messages.size()];
		ret = messages.toArray(ret);
		return ret;
	}

	private String[] getClinicalResponse(RecommendPatientInfoBean patientInfo,
			List<ResponseRuleBean> rules) {
		ArrayList<String> messages = evaluateRules("clinicalresponse",patientInfo,rules);
		if (messages == null || messages.size() == 0) return null;
		String[] ret = new String[messages.size()];
		ret = messages.toArray(ret);
		return ret;
	}

	private String[] getTreatmentMessages(RecommendPatientInfoBean patientInfo,
			List<ResponseRuleBean> rules) {
		ArrayList<String> messages = evaluateRules("treatmentmessages",patientInfo,rules);
		if (messages == null || messages.size() == 0) return null;
		String[] ret = new String[messages.size()];
		ret = messages.toArray(ret);
		return ret;
	}

	private String[] getSpecialMessages(RecommendPatientInfoBean patientInfo,
			List<ResponseRuleBean> rules) {
		ArrayList<String> messages = evaluateRules("specialmessages",patientInfo,rules);
		if (messages == null || messages.size() == 0) return null;
		String[] ret = new String[messages.size()];
		ret = messages.toArray(ret);
		return ret;
	}
	
	private String[] getOthersideeffects(RecommendPatientInfoBean patientInfo,
			List<ResponseRuleBean> rules) {
		ArrayList<String> messages = evaluateRules("othersideeffects",patientInfo,rules);
		if (messages == null || messages.size() == 0) return null;
		String[] ret = new String[2];
		ret[0] = "Management of Side Effects";
		ret[1] = messages.get(0);
		return ret;
	}
	
	private String[] getOtherreports(RecommendPatientInfoBean patientInfo,
			List<ResponseRuleBean> rules) {
		ArrayList<String> messages = evaluateRules("otherreports",patientInfo,rules);
		if (messages == null || messages.size() == 0) return null;
		String[] ret = new String[2];
		ret[0] = "Your Colleagues' Prescriptions";
		ret[1] = messages.get(0);
		return ret;
	}
	
	private boolean evaluateRuleCriterium(
			RecommendRuleCriteriaBean bean,
			HashMap<String, String> info,
			HashMap<String, String> additionalInfo) {
		boolean ret = false;
		if (bean == null) {
			ret = true;
			System.out.println("Evaluated null criteria bean");
		} else {
			String operator = bean.getOperator();
			String type = bean.getType();
			String op1 = null;
			String op2 = null;
			if (type.equalsIgnoreCase("numeric")) {
				if (additionalInfo != null && additionalInfo.get(bean.getElement()) != null) {
					ret = evaluateNumeric(additionalInfo.get(bean.getElement()),operator,bean.getValue());
				} else if (info != null && info.get(bean.getElement()) != null) {
					ret = evaluateNumeric(info.get(bean.getElement()),operator,bean.getValue());
				} else {
					ret = evaluateNumeric("0",operator,bean.getValue());
				}
			} else if (type.equalsIgnoreCase("numericvalue")) {
				if (additionalInfo != null) {
					op1 = additionalInfo.get(bean.getElement());
					op2 = additionalInfo.get(bean.getValue());
				} else if (info != null) {
					if (op1 == null) op1 = info.get(bean.getElement());
					if (op2 == null) op2 = info.get(bean.getValue());
				}
				if (op1 == null) op1 = "0";
				if (op2 == null) op2 = "0";
				if (op1 != null && op2 != null) {
					ret = evaluateNumeric(op1,operator,op2);
				}
			} else if (type.equalsIgnoreCase("text")) {
				if (additionalInfo != null && additionalInfo.get(bean.getElement()) != null) {
					ret = evaluateText(additionalInfo.get(bean.getElement()),operator,bean.getValue());
				} else if (info != null && info.get(bean.getElement()) != null) {
					ret = evaluateText(info.get(bean.getElement()),operator,bean.getValue());
				} else {
					ret = evaluateText("",operator,bean.getValue());
				}
			} else if (type.equalsIgnoreCase("textvalue")) {
				if (additionalInfo != null) {
					op1 = additionalInfo.get(bean.getElement());
					op2 = additionalInfo.get(bean.getValue());
				} else if (info != null) {
					if (op1 == null) op1 = info.get(bean.getElement());
					if (op2 == null) op2 = info.get(bean.getValue());
				}
				if (op1 == null) op1 = "";
				if (op2 == null) op2 = "";
				if (op1 != null && op2 != null) {
					ret = evaluateText(op1,operator,op2);
				}
			}
			System.out.println("Evaluated "+bean.getElement()+bean.getOperator()+bean.getValue()+" op1="+op1+" op2="+op2+" result:"+ret);
		}
		return ret;
	}

	private boolean evaluateText(String string, String operator, String value) {
		if (operator.equals("=")) {
			return string.equals(value);
		} else if (operator.equals("!=") || operator.equals("<>")) {
			return !string.equals(value);
		}
		return false;
	}

	private boolean evaluateNumeric(String string, String operator, String value) {
		double op1 = 0;
		double op2 = 0;
		try {
			op1 = Double.parseDouble(string);
			op2 = Double.parseDouble(value);
		} catch (Exception e) {
			// Do we really want to track this?  Perhaps.
			e.printStackTrace();
			System.out.println("Number Conversion Error - DaoRecommendRequestManager.evaluateNumeric - "+string+operator+value);
		}
		if (operator.equals("=")) {
			return op1==op2;
		} else if (operator.equals("!=") || operator.equals("<>")) {
			return op1!=op2;
		} else if (operator.equals("<")) {
			return op1<op2;
		} else if (operator.equals("<=")) {
			return op1<=op2;
		} else if (operator.equals(">")) {
			return op1>op2;
		} else if (operator.equals(">=")) {
			return op1>=op2;
		} else if (operator.equals("<")) {
			return op1<op2;
		}
		return false;
	}

	private String evaluateMessage(RecommendMessageBean bean,
			HashMap<String, String> info,
			HashMap<String, String> additionalInfo) {
		String ret = null;
		if (bean != null && bean.getMessage() != null) {
			String rawMess = bean.getMessagetag()+": "+bean.getMessage();
			String[] messPieces = rawMess.split("\\[");
			if (messPieces.length > 1) {
				ret = messPieces[0];
				for (int i=1;i<messPieces.length;i++) {
					String[] fragments = messPieces[i].split("\\]",2);
					if (fragments.length == 1) {
						ret = ret.concat("["+fragments[0]);
					} else {
						if (additionalInfo != null && additionalInfo.get(fragments[0]) != null) {
							ret = ret.concat(additionalInfo.get(fragments[0]));
						} else if (info != null && info.get(fragments[0]) != null) {
							ret = ret.concat(info.get(fragments[0]));
						} else {
							ret = ret.concat("["+fragments[0]+"]");
						}
						ret = ret.concat(fragments[1]);
					}
				}
			} else ret = rawMess;
		}
		return ret;
	}

}
