package flinn.recommend.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.PatientStatusBean;
import flinn.beans.response.ResponseLabBean;
import flinn.beans.response.ResponsePatientBean;
import flinn.beans.response.ResponsePrescriptionBean;
import flinn.util.DateString;
import flinn.util.RecommendUtils;

@XmlRootElement(name = "patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecommendPatientInfoBean extends AbstractDataBean
{

	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "facilityid")
	protected int facilityid;
	@XmlElement(name = "info")
	protected HashMap<String, String> info;
	@XmlElement(name = "prescriptioninfo")
	protected HashMap<String, HashMap<String,String>> prescriptioninfo;
	@XmlElement(name = "diagnosis")
	protected RecommendDiagnosisBean diagnosis;

	public RecommendPatientInfoBean()
	{
		super();
	}

	public RecommendPatientInfoBean(ResponsePatientBean bean, HashMap<String, ResponsePrescriptionBean[]> prescriptions, ResponseLabBean[] labs, RecommendDiagnosisBean[] diagnoses)
	{
		setPatientid(bean.getPatientid());
		setFacilityid(bean.getFacilityid());
		setInitialInfo(bean);
		setPrescriptionInfo(prescriptions);
		setLabInfo(labs);
		calculateAdditionalInfo(bean, diagnoses);
		calculateAdditionalAMTInfo(bean, diagnoses);
	}

	private void setInitialInfo(ResponsePatientBean bean) {
		info = new HashMap<String,String>();
		if (bean.getDetails() != null) {
			Iterator<String> it = bean.getDetails().keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				String value = bean.getDetails().get(key)[0].getValue();
				this.info.put(key, value);
			}
		}
		if (bean.getStatus() != null) {
			Iterator<String> it = bean.getStatus().keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				String value = bean.getStatus().get(key)[0].getValue();
				this.info.put(key, value);
			}
		}
	}

	private void setPrescriptionInfo(
			HashMap<String, ResponsePrescriptionBean[]> prescriptions2)  {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		prescriptioninfo = new HashMap<String,HashMap<String,String>>();
		//Iterate through prescriptions
		for (Iterator<String> it = prescriptions2.keySet().iterator(); it.hasNext();)
		{
			String key = it.next();
			// disregard discontinued prescriptions
			ResponsePrescriptionBean [] psdetails = prescriptions2.get(key);

			boolean matches = true;
			//Get current date and entry to calc expiration based on duration
			Date now = null;
			Date expdate = null;
			try {
				now = df.parse(DateString.now());
				expdate = df.parse(psdetails[0].getEntrydate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(expdate);
			cal.add(Calendar.DATE, psdetails[0].getDuration());
			if (psdetails[0].getTreatment().getDetails().get("DurationOfAction") != null) {
				int doa = Integer.parseInt(psdetails[0].getTreatment().getDetails().get("DurationOfAction"));
				if (doa > 1) cal.add(Calendar.DATE, doa);
			}
			expdate = cal.getTime();

			if (psdetails[0].getDiscontinue() || now.after(expdate)) //Check for discontinues or expired for first item
			{
				matches = false;
			}

			if (matches)
			{ //This is a current prescription, add it to the prescription list.
				prescriptioninfo.put(psdetails[0].getTreatment().getDetails().get("GuidelineChartName"), analyzePrescription(psdetails));
			}
		}
		
	}

	private HashMap <String,String> analyzePrescription(ResponsePrescriptionBean[] psdetails) {
		// prescription is valid, need to check length at dose...
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HashMap <String,String> resp = new HashMap <String,String>();
		resp.put("medication", psdetails[0].getTreatment().getDetails().get("DisplayName"));
		resp.put("dose", psdetails[0].getDailydose());
		resp.put("treatment_group", psdetails[0].getTreatment().getGroup().getTreatmentgroupabbreviation().toLowerCase());
		
		int response_time = 0;
		float amt_highdose = 0;
		float amt_lowdose = 0;
		int x_percent_of_usual_dose_range = 50;
		int y_percent_of_usual_response_time = 50;
		int grace = 0;
		int doa = 0;
		if (psdetails[0].getTreatment().getDetails() != null) {
			resp.put("display_name", psdetails[0].getTreatment().getDetails().get("DisplayName"));
			resp.put("chart_name", psdetails[0].getTreatment().getDetails().get("GuidelineChartName"));
			resp.put("treatmentid", ""+psdetails[0].getTreatment().getTreatmentid());
			resp.put("unit", psdetails[0].getTreatment().getDetails().get("Unit"));
			resp.put("treatment_function", psdetails[0].getTreatment().getDetails().get("DrugFunction"));
			if (psdetails[0].getTreatment().getDetails().get("LongActingFlag") != null) {
				resp.put("long_acting_flag", psdetails[0].getTreatment().getDetails().get("LongActingFlag"));
			}
			if (psdetails[0].getTreatment().getDetails().get("ResponseTime") != null) {
				response_time = Integer.parseInt(psdetails[0].getTreatment().getDetails().get("ResponseTime"));
			}
			if (psdetails[0].getTreatment().getDetails().get("DailyHighDose") != null && !psdetails[0].getTreatment().getDetails().get("DailyHighDose").equals("")) {
				amt_highdose = Float.parseFloat(psdetails[0].getTreatment().getDetails().get("DailyHighDose"));
			}
			if (psdetails[0].getTreatment().getDetails().get("DailyLowDose") != null && !psdetails[0].getTreatment().getDetails().get("DailyLowDose").equals("")) {
				amt_lowdose = Float.parseFloat(psdetails[0].getTreatment().getDetails().get("DailyLowDose"));
			}
			if (psdetails[0].getTreatment().getDetails().get("AMTPercentDoseRange") != null) {
				x_percent_of_usual_dose_range = Integer.parseInt(psdetails[0].getTreatment().getDetails().get("AMTPercentDoseRange"));
			}
			if (psdetails[0].getTreatment().getDetails().get("AMTPercentResponseTime") != null) {
				y_percent_of_usual_response_time = Integer.parseInt(psdetails[0].getTreatment().getDetails().get("AMTPercentResponseTime"));
			}
			if (psdetails[0].getTreatment().getDetails().get("AMTGracePeriod") != null) {
				grace = Integer.parseInt(psdetails[0].getTreatment().getDetails().get("AMTGracePeriod"));
			}
			if (psdetails[0].getTreatment().getDetails().get("DurationOfAction") != null) {
				doa = Integer.parseInt(psdetails[0].getTreatment().getDetails().get("DurationOfAction"));
			}
		}

		int amt_req_duration = response_time * y_percent_of_usual_response_time / 100;
		float amt_req_dose = amt_lowdose + (amt_highdose - amt_lowdose) * x_percent_of_usual_dose_range / 100;

		long dose_duration = 0;
		boolean same_dose = true;
		long medication_duration = 0;
		long amt_duration = 0;
		boolean amt_dose = false;
		if (amt_req_dose > 0) amt_dose = true;
		Date expdate = null;
		Date startdate = null;
		Date workingstartdate = null;
		float dose = 0;
		try {
			workingstartdate = df.parse(psdetails[0].getEntrydate());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		ResponsePrescriptionBean working = new ResponsePrescriptionBean(psdetails[0]);
		resp.put("dose_start_date", RecommendUtils.formatDateFromDB(workingstartdate));
		resp.put("medication_start_date", RecommendUtils.formatDateFromDB(workingstartdate));
		dose_duration = (new Date().getTime() - workingstartdate.getTime()+(1000*3600*12))/(1000*3600*24);
		medication_duration = (new Date().getTime() - workingstartdate.getTime()+(1000*3600*12))/(1000*3600*24);
		dose = Float.parseFloat(psdetails[0].getDailydose());
		if (amt_req_dose > 0 && dose >= amt_req_dose) {
			amt_duration = (new Date().getTime() - workingstartdate.getTime()+(1000*3600*12))/(1000*3600*24);
		}
		for (int i=1; i<psdetails.length; i++) {
			try {
				startdate = df.parse(psdetails[i].getEntrydate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(startdate);
			cal.add(Calendar.DATE, psdetails[i].getDuration()+doa+grace);
			expdate = cal.getTime();
			if (workingstartdate.before(expdate)) {
				workingstartdate = startdate;
				resp.put("medication_start_date", RecommendUtils.formatDateFromDB(startdate));
				medication_duration = (new Date().getTime() - workingstartdate.getTime()+(1000*3600*12))/(1000*3600*24);
				if (same_dose && working.getDailydose().equals(psdetails[i].getDailydose())) {
					resp.put("dose_start_date", RecommendUtils.formatDateFromDB(startdate));
					dose_duration = (new Date().getTime() - workingstartdate.getTime()+(1000*3600*12))/(1000*3600*24);
				} else {
					same_dose = false;
				}
				dose = Float.parseFloat(psdetails[i].getDailydose());
				if (amt_req_dose > 0 && dose >= amt_req_dose && amt_dose) {
					amt_duration = (new Date().getTime() - workingstartdate.getTime()+(1000*3600*12))/(1000*3600*24);
				} else {
					amt_dose = false;
				}
				
			} else { //There's a gap in this prescription, ignore earlier prescriptions of this drug.
				same_dose = false;
				i = psdetails.length;
			}
		}
		resp.put("medication_duration", ""+medication_duration);
		resp.put("medication_duration_weeks", ""+((int)(medication_duration/7)));
		resp.put("dose_duration", ""+dose_duration);
		resp.put("dose_duration_weeks", ""+((int)(dose_duration/7)));
		
		if (psdetails[0].getTreatment().getDetails().get("LongActingFlag") != null && psdetails[0].getTreatment().getDetails().get("LongActingFlag").equals("1")
				&& psdetails[0].getTreatment().getDetails().get("AdministrationMechanism") != null && psdetails[0].getTreatment().getDetails().get("AdministrationMechanism").equalsIgnoreCase("injection")) {
			resp.put("adequate_medication_trial", "0");
			resp.put("adequate_medication_trial_response", "0");
			if (amt_req_dose > 0 && medication_duration >= response_time) {
				if (analyzeLongActingAMTDoseDate(psdetails, new Date(), amt_req_dose)) {
					boolean amt = false;
					workingstartdate = null;
					try {
						workingstartdate = df.parse(psdetails[0].getEntrydate());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					for (int i=1; i<psdetails.length; i++) {
						// for each administration of the treatment, we have to determine if the patient 
						// was above the amt_req_dose level (within the grace period) before the administration.
						try {
							startdate = df.parse(psdetails[i].getEntrydate());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Calendar cal = Calendar.getInstance();
						cal.setTime(startdate);
						cal.add(Calendar.DATE, -1);
						if (analyzeLongActingAMTDoseDate(psdetails, cal.getTime(), amt_req_dose)) {
							workingstartdate = startdate;
							amt_duration = (new Date().getTime() - workingstartdate.getTime()+(1000*3600*12))/(1000*3600*24);
							if (amt_duration >= amt_req_duration) {
								amt = true;
								i = psdetails.length;
							}
						}
					}
					if (amt) {
						resp.put("adequate_medication_trial", "1");
					}
				}
			}
		} else {
			if (amt_req_dose > 0 && amt_duration >= amt_req_duration && medication_duration >= response_time) {
				resp.put("adequate_medication_trial", "1");
				resp.put("adequate_medication_trial_response", "0");
			} else {
				resp.put("adequate_medication_trial", "0");
				resp.put("adequate_medication_trial_response", "0");
			}
		}
		if (psdetails[0].getTreatment().getDetails().get("LabNamePattern") != null) {
			resp.put("serum_level_low", psdetails[0].getTreatment().getDetails().get("SerumLevelLow"));
			resp.put("serum_level_high", psdetails[0].getTreatment().getDetails().get("SerumLevelHigh"));
			resp.put("response_time", psdetails[0].getTreatment().getDetails().get("ResponseTime"));
			resp.put("serum_level_unit", psdetails[0].getTreatment().getDetails().get("SerumLevelUnit"));
			resp.put("lab_name", psdetails[0].getTreatment().getDetails().get("LabNamePattern"));
			resp.put("amt_response_time", ""+amt_req_duration);
		}
		return resp;
	}

	private boolean analyzeLongActingAMTDoseDate(
			ResponsePrescriptionBean[] psdetails, Date date, float amt_req_dose) {
		int doa = 1;
		if (psdetails[0].getTreatment().getDetails().get("DurationOfAction") != null) {
			doa = Integer.parseInt(psdetails[0].getTreatment().getDetails().get("DurationOfAction"));
		}
		int grace = 0;
		if (psdetails[0].getTreatment().getDetails().get("AMTGracePeriod") != null) {
			grace = Integer.parseInt(psdetails[0].getTreatment().getDetails().get("AMTGracePeriod"));
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		float effDose = 0;
		for (int i=1; i<psdetails.length; i++) {
			// At the given date, does this dose apply?  If so, add it to the effective dose.
			Date startdate = null;
			Calendar cal = null;
			Date expdate = null;
			try {
				startdate = df.parse(psdetails[i].getEntrydate());
				cal = Calendar.getInstance();
				cal.setTime(startdate);
				cal.add(Calendar.DATE, psdetails[i].getDuration()+doa+grace);
				expdate = cal.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (startdate.before(date) && expdate.after(date)) {
				effDose += Float.parseFloat(psdetails[i].getDailydose());
			}
			if (expdate.before(date)) {
				// short circuit the loop, we're done here.
				i = psdetails.length;
			}
		}
		return (effDose >= amt_req_dose);
	}

	private void setLabInfo(ResponseLabBean[] labs) {
		if (prescriptioninfo != null) {
			Iterator<String> it = prescriptioninfo.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				String value = prescriptioninfo.get(key).get("medication");
				if (value.equalsIgnoreCase("lithium")) {
					setLabInfoForMedication(prescriptioninfo.get(key),labs);
				} else if (value.equalsIgnoreCase("valproate")) {
					setLabInfoForMedication(prescriptioninfo.get(key),labs);
				} else if (value.equalsIgnoreCase("carbamazepine")) {
					setLabInfoForMedication(prescriptioninfo.get(key),labs);
				}
			}
		}
	}

	private void setLabInfoForMedication(HashMap<String, String> prescription,
			ResponseLabBean[] labs) {
		System.out.println("Checking Labs for "+prescription.get("medication"));
		long duration = 0;
		boolean done = false;
		boolean amt = false;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		double high = 0;
		double low = 0;
		int response = 0;
		try {
			high = Double.parseDouble(prescription.get("serum_level_high"));
			low = Double.parseDouble(prescription.get("serum_level_low"));
			response = Integer.parseInt(prescription.get("amt_response_time"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i=labs.length-1; i>=0; i--) {
			ResponseLabBean bean = labs[i];
			if (bean.getLabtest().getLabtestname().toLowerCase().contains(prescription.get("lab_name").toLowerCase())) {
				String[] strarr = bean.getLabtext().split("\\|",2);
				if (!done) {
					System.out.println("    Found lab for "+prescription.get("medication")+": "+prescription.get("lab_name")+": "+bean.getLabtext());
					if (strarr.length > 1) {
						prescription.put("blood_level", strarr[1]);
						prescription.put("blood_level_date", RecommendUtils.formatDateFromDB(bean.getLabdate()));
						done = true;
					}
				}
				if (!amt) {
					System.out.println("    Checking serum AMT for "+prescription.get("medication")+": "+bean.getLabtext() + " at " + bean.getLabdate());
					if (strarr.length > 1) {
						double level = 0;
						Date testdate = null;
						try {
							testdate = df.parse(bean.getLabdate());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						try {
							level = Double.parseDouble(strarr[1]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (testdate != null && level > low && level < high) {
							duration = (new Date().getTime() - testdate.getTime()+(1000*3600*12))/(1000*3600*24);
							if (duration >= response) {
								amt = true;
							}
						}
					}
				}
			}
		}
		if (done) {
			if (amt) {
				prescription.put("adequate_medication_trial", "1");
				prescription.put("adequate_medication_trial_response", "0");
			} else {
				prescription.put("adequate_medication_trial", "0");
				prescription.put("adequate_medication_trial_response", "0");
			}
		}
	}

	public int getPatientid()
	{
		return patientid;
	}

	public void setPatientid(int patientid)
	{
		this.patientid = patientid;
	}

	public int getFacilityid()
	{
		return facilityid;
	}

	public void setFacilityid(int facilityid)
	{
		this.facilityid = facilityid;
	}

	public HashMap<String, String> getInfo() {
		return info;
	}

	public void setInfo(HashMap<String, String> info) {
		this.info = info;
	}

	public HashMap<String, HashMap<String, String>> getPrescriptioninfo() {
		return prescriptioninfo;
	}

	public void setPrescriptioninfo(
			HashMap<String, HashMap<String, String>> prescriptioninfo) {
		this.prescriptioninfo = prescriptioninfo;
	}


	public RecommendDiagnosisBean getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(RecommendDiagnosisBean diagnosis) {
		this.diagnosis = diagnosis;
	}

	private void calculateAdditionalInfo(ResponsePatientBean bean, RecommendDiagnosisBean[] diagnoses) {
		setGenderInfo();
		setDiagnosisInfo(bean, diagnoses);
		if (info.get("diagnosis") != null && info.get("diagnosis").contains("Bipolar")) {
			setBipolarInfo(bean);
		}
		if (info.get("diagnosis") != null && info.get("diagnosis").contains("Major Depressive Disorder")) {
			setDepressInfo(bean);
		}
		if (info.get("diagnosis") != null && info.get("diagnosis").contains("Schizophrenia")) {
			setSchizInfo(bean);
		}
		setGlobalRatingInfo(bean);
		setConsistentInfo(diagnoses);
	}

	private void calculateAdditionalAMTInfo(ResponsePatientBean bean, RecommendDiagnosisBean[] diagnoses) {
		if (info.get("diagnosis") != null && info.get("diagnosis").contains("Bipolar")) {
			if (info.get("bdss_score_max") !=null && Integer.parseInt(info.get("bdss_score_max")) <= 2) {
				setresponseAMT(bean);
			}
		}
		if (info.get("diagnosis") != null && info.get("diagnosis").contains("Major Depressive Disorder")) {
			if (info.get("crs_depression_total") !=null && Integer.parseInt(info.get("crs_depression_total")) <= 5) {
				setresponseAMT(bean);
			}
		}
		if (info.get("diagnosis") != null && info.get("diagnosis").contains("Schizophrenia")) {
			if (info.get("diagnosis_stage") != null) {
				if (Integer.parseInt(info.get("diagnosis_stage")) <= 2) {
					if (info.get("psrs_score") != null && Integer.parseInt(info.get("psrs_score")) <= 5) {
						setresponseAMT(bean);
					}
				} else if (Integer.parseInt(info.get("diagnosis_stage")) <= 4) {
					if (info.get("psrs_score_6month_decrease_percent") != null && Integer.parseInt(info.get("psrs_score_6month_decrease_percent")) >= 20) {
						setresponseAMT(bean);
					}
				}
			}
		}
	}
	
	private void setresponseAMT(ResponsePatientBean bean) {
		if (prescriptioninfo != null) {
			Iterator<String> it = prescriptioninfo.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				prescriptioninfo.get(key).put("adequate_medication_trial", "1");
				prescriptioninfo.get(key).put("adequate_medication_trial_response", "1");
			}
		}
	}

	private void setGlobalRatingInfo(ResponsePatientBean bean) {
		info.put("grs_side_effect_score", info.get("global_rating_scale_overall_side_effect_severity"));
		if (bean.getStatus().get("global_rating_scale_overall_functional_impairment") != null && bean.getStatus().get("global_rating_scale_overall_functional_impairment").length > 0) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			info.put("grs_date", RecommendUtils.formatDateFromDB(bean.getStatus().get("global_rating_scale_overall_functional_impairment")[0].getEntrydate()));
			long duration = 0;
			try {
				Date startdate = df.parse(bean.getStatus().get("global_rating_scale_overall_functional_impairment")[0].getEntrydate());
				duration = (new Date().getTime() - startdate.getTime()+(1000*3600*12))/(1000*3600*24);
				info.put("grs_days_since_last",""+duration);
			} catch (Exception e) {
				// If there's an exception calculating the date difference, the difference is zero (default case.)
				duration = 1000;
				info.remove("grs_days_since_last");
			}
			info.put("ofi_score", info.get("global_rating_scale_overall_functional_impairment"));
			int maxscore = 0;
			int maxscore6 = 0;
			int currscore = 0;
			try {
				currscore = Integer.parseInt(info.get("global_rating_scale_overall_functional_impairment"));
			} catch (Exception e) {
				currscore = 0;
			}
			info.put("ofi_score_overall_max", "0");
			info.put("ofi_score_6month_max", "0");
			info.put("ofi_score_6month_difference", "0");
			info.put("ofi_score_6month_difference_percent", "0");
			info.put("ofi_score_6month_decrease", "0");
			info.put("ofi_score_6month_decrease_percent", "0");
			for (int i=1; i<bean.getStatus().get("global_rating_scale_overall_functional_impairment").length; i++) {
				PatientStatusBean crs_info = bean.getStatus().get("global_rating_scale_overall_functional_impairment")[i];
				int score = 0;
				try {
					score = Integer.parseInt(crs_info.getValue());
				} catch (Exception e) {
					score = 0;
				}
				try {
					Date startdate = df.parse(crs_info.getEntrydate());
					duration = (new Date().getTime() - startdate.getTime()+(1000*3600*12))/(1000*3600*24);
				} catch (Exception e) {
					// If there's an exception calculating the date difference, the difference is zero (default case.)
					duration = 1000;
				}
				if (score > maxscore) {
					info.put("ofi_score_overall_max", crs_info.getValue());
					maxscore = score;
					info.put("ofi_score_overall_max_dates", RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
					info.put("ofi_score_overall_max_date_recent", RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
				} else if (score == maxscore) {
					info.put("ofi_score_overall_max_dates", info.get("ofi_score_overall_max_dates") + ", " + RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
				}
				if (duration < 180) {
					if (score > maxscore6) {
						info.put("ofi_score_6month_max", crs_info.getValue());
						maxscore6 = score;
						info.put("ofi_score_6month_max_dates", RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
						info.put("ofi_score_6month_max_date_recent", RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
						info.put("ofi_score_6month_difference", ""+Math.abs(score - currscore));
						info.put("ofi_score_6month_difference_percent", ""+(int)(0.5+Math.abs((0.0 + score - currscore)/score*100)));
						info.put("ofi_score_6month_decrease", ""+(score - currscore));
						info.put("ofi_score_6month_decrease_percent", ""+(int)(0.5+(0.0 + score - currscore)/score*100));
					} else if (score == maxscore6) {
						info.put("ofi_score_6month_max_dates", info.get("ofi_score_6month_max_dates") + ", " + RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
					}
				}
			}
		}
	}

	private void setConsistentInfo(RecommendDiagnosisBean[] diagnoses) {
		int sga = 0;
		int fga = 0;
		int ssri = 0;
		int snri = 0;
		int maoi = 0;
		int tca = 0;
		int lithium = 0;
		int lamotrigine = 0;
		int other = 0;
		int antidepress = 0;
		String antipsych = null;
		int dose_duration = 36500;
		int dose_duration_weeks = 36500;
		int medication_duration = 36500;
		int medication_duration_weeks = 36500;
		
		for (Iterator<String> it = prescriptioninfo.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			HashMap<String,String> pres = prescriptioninfo.get(key);
			if (pres.get("treatment_group").equals("ssri")) ssri++;
			else if (pres.get("treatment_group").equals("snri")) snri++;
			else if (pres.get("treatment_group").equals("sga")) sga++;
			else if (pres.get("treatment_group").equals("fga")) fga++;
			else if (pres.get("treatment_group").equals("maoi")) maoi++;
			else if (pres.get("treatment_group").equals("tca")) tca++;
			else if (pres.get("chart_name").equalsIgnoreCase("lithium")) lithium++;
			else if (pres.get("chart_name").equalsIgnoreCase("lamotrigine")) lamotrigine++;
			else other++;
			if (pres.get("treatment_function").equals("antidepressant")) antidepress++;
			else if (pres.get("treatment_function").equals("antipsychotic")) antipsych = pres.get("medication");
			if (dose_duration < 0 || (dose_duration > 0 && Integer.parseInt(pres.get("dose_duration")) < dose_duration)) {
				dose_duration = Integer.parseInt(pres.get("dose_duration"));
			}
			if (dose_duration_weeks < 0 || (dose_duration_weeks > 0 && Integer.parseInt(pres.get("dose_duration_weeks")) < dose_duration_weeks)) {
				dose_duration_weeks = Integer.parseInt(pres.get("dose_duration_weeks"));
			}
			if (medication_duration < 0 || (medication_duration > 0 && Integer.parseInt(pres.get("medication_duration")) < medication_duration)) {
				medication_duration = Integer.parseInt(pres.get("medication_duration"));
			}
			if (medication_duration_weeks < 0 || (medication_duration_weeks > 0 && Integer.parseInt(pres.get("medication_duration_weeks")) < medication_duration_weeks)) {
				medication_duration_weeks = Integer.parseInt(pres.get("medication_duration_weeks"));
			}
		}
		info.put("sga_count", ""+sga);
		info.put("fga_count", ""+fga);
		info.put("ssri_count", ""+ssri);
		info.put("snri_count", ""+snri);
		info.put("maoi_count", ""+maoi);
		info.put("tca_count", ""+tca);
		info.put("lithium_count", ""+lithium);
		info.put("lamotrigine_count", ""+lamotrigine);
		info.put("antidepressant_count", ""+antidepress);
		if (dose_duration == 36500) dose_duration = 0;
		if (dose_duration_weeks == 36500) dose_duration_weeks = 0;
		if (dose_duration >= 0) info.put("dose_duration", ""+dose_duration);
		if (dose_duration_weeks >= 0) info.put("dose_duration_weeks", ""+dose_duration_weeks);
		if (medication_duration == 36500) medication_duration = 0;
		if (medication_duration_weeks == 36500) medication_duration_weeks = 0;
		if (medication_duration >= 0) info.put("medication_duration", ""+medication_duration);
		if (medication_duration_weeks >= 0) info.put("medication_duration_weeks", ""+medication_duration_weeks);
		if (antipsych != null) info.put("antipsychotic_treatment", antipsych);
		HashMap<String,String> consistencymap = flinn.util.RecommendUtils.consistency(info,prescriptioninfo,diagnosis,diagnoses);
		if (consistencymap != null) {
			Iterator<String> it = consistencymap.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				String value = consistencymap.get(key);
				this.info.put(key, value);
			}
		}

	}

	private void setSchizInfo(ResponsePatientBean bean) {
		info.put("drs_name", "Positive Symptom Rating Scale and Brief Negative Symptom Assessment Scale");
		if (bean.getStatus().get("crs_schizophrenia_positive_total") != null && bean.getStatus().get("crs_schizophrenia_positive_total").length > 0) {
			int drs6MonthCount = 0;
			boolean cont = true;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i=0; i< bean.getStatus().get("crs_schizophrenia_positive_total").length && cont; i++) {
				try {
					Date drsdate = df.parse(bean.getStatus().get("crs_schizophrenia_positive_total")[0].getEntrydate());
					if ((new Date().getTime() - drsdate.getTime()+(1000*3600*12))/(1000*3600*24) < 180) {
						drs6MonthCount++;
					} else {
						cont = false;
					}
				} catch (Exception e) {
					cont = false;
				}
			}
			info.put("drs_6month_count", ""+drs6MonthCount);
			info.put("drs_date", RecommendUtils.formatDateFromDB(bean.getStatus().get("crs_schizophrenia_positive_total")[0].getEntrydate()));
			long duration = 0;
			try {
				Date startdate = df.parse(bean.getStatus().get("crs_schizophrenia_positive_total")[0].getEntrydate());
				duration = (new Date().getTime() - startdate.getTime()+(1000*3600*12))/(1000*3600*24);
				info.put("drs_days_since_last",""+duration);
			} catch (Exception e) {
				// If there's an exception calculating the date difference, the difference is zero (default case.)
				duration = 1000;
				info.remove("drs_days_since_last");
			}
			info.put("psrs_score", info.get("crs_schizophrenia_positive_total"));
			// Note that maximum scores are PREVIOUS to the current score, not inclusive of the current score.
			int maxscore = 0;
			int maxscore6 = 0;
			int currscore = Integer.parseInt(info.get("crs_schizophrenia_positive_total"));
			info.put("psrs_score_overall_max", "0");
			info.put("psrs_score_6month_difference", "0");
			info.put("psrs_score_6month_difference_percent", "0");
			info.put("psrs_score_6month_decrease", "0");
			info.put("psrs_score_6month_decrease_percent", "0");
			for (int i=1; i<bean.getStatus().get("crs_schizophrenia_positive_total").length; i++) {
				PatientStatusBean crs_info = bean.getStatus().get("crs_schizophrenia_positive_total")[i];
				int score = Integer.parseInt(crs_info.getValue());
				try {
					Date startdate = df.parse(crs_info.getEntrydate());
					duration = (new Date().getTime() - startdate.getTime()+(1000*3600*12))/(1000*3600*24);
				} catch (Exception e) {
					// If there's an exception calculating the date difference, the difference is zero (default case.)
					duration = 1000;
				}
				if (score > maxscore) {
					info.put("psrs_score_overall_max", crs_info.getValue());
					maxscore = score;
					info.put("psrs_score_overall_max_dates", RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
					info.put("psrs_score_overall_max_date_recent", RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
				} else if (score == maxscore) {
					info.put("psrs_score_overall_max_dates", info.get("psrs_score_overall_max_dates") + ", " + RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
				}
				if (duration < 180) {
					if (score > maxscore6) {
						info.put("psrs_score_6month_max", crs_info.getValue());
						maxscore6 = score;
						info.put("psrs_score_6month_max_dates", RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
						info.put("psrs_score_6month_max_date_recent", RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
						info.put("psrs_score_6month_difference", ""+Math.abs(score - currscore));
						info.put("psrs_score_6month_difference_percent", ""+(int)(0.5+Math.abs((0.0 + score - currscore)/score*100)));
						info.put("psrs_score_6month_decrease", ""+(score - currscore));
						info.put("psrs_score_6month_decrease_percent", ""+(int)(0.5+(0.0 + score - currscore)/score*100));
					} else if (score == maxscore6) {
						info.put("psrs_score_6month_max_dates", info.get("psrs_score_6month_max_dates") + ", " + RecommendUtils.formatDateFromDB(crs_info.getEntrydate()));
					}
				}
			}
		}
	}

	private void setDepressInfo(ResponsePatientBean bean) {
		info.put("drs_name", "PHQ9 for Major Depressive Disorder");
		if (bean.getStatus().get("crs_depression_total") != null && bean.getStatus().get("crs_depression_total").length > 0) {
			int drs6MonthCount = 0;
			boolean cont = true;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i=0; i< bean.getStatus().get("crs_depression_total").length && cont; i++) {
				try {
					Date drsdate = df.parse(bean.getStatus().get("crs_depression_total")[0].getEntrydate());
					if ((new Date().getTime() - drsdate.getTime()+(1000*3600*12))/(1000*3600*24) < 180) {
						drs6MonthCount++;
					} else {
						cont = false;
					}
				} catch (Exception e) {
					cont = false;
				}
			}
			info.put("drs_6month_count", ""+drs6MonthCount);
			info.put("drs_date", RecommendUtils.formatDateFromDB(bean.getStatus().get("crs_depression_total")[0].getEntrydate()));
			try {
				Date startdate = df.parse(bean.getStatus().get("crs_depression_total")[0].getEntrydate());
				long duration = (new Date().getTime() - startdate.getTime()+(1000*3600*12))/(1000*3600*24);
				info.put("drs_days_since_last",""+duration);
			} catch (Exception e) {
				// If there's an exception calculating the date difference, the difference is zero (default case.)
				info.remove("drs_days_since_last");
			}
			info.put("phq9_score", info.get("crs_depression_total"));
		}
	}

	private void setBipolarInfo(ResponsePatientBean bean) {
		info.put("drs_name", "Brief Bipolar Disorder Symptom Scale");
		int drs6MonthCount = 0;
		boolean cont = true;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i=0; i< bean.getStatus().get("crs_bipolar_total").length && cont; i++) {
			try {
				Date drsdate = df.parse(bean.getStatus().get("crs_bipolar_total")[0].getEntrydate());
				if ((new Date().getTime() - drsdate.getTime()+(1000*3600*12))/(1000*3600*24) < 180) {
					drs6MonthCount++;
				} else {
					cont = false;
				}
			} catch (Exception e) {
				cont = false;
			}
		}
		info.put("drs_6month_count", ""+drs6MonthCount);
		if (bean.getStatus().get("crs_bipolar_total") != null && bean.getStatus().get("crs_bipolar_total").length > 0) {
			info.put("drs_date", RecommendUtils.formatDateFromDB(bean.getStatus().get("crs_bipolar_total")[0].getEntrydate()));
			try {
				Date startdate = df.parse(bean.getStatus().get("crs_bipolar_total")[0].getEntrydate());
				long duration = (new Date().getTime() - startdate.getTime()+(1000*3600*12))/(1000*3600*24);
				info.put("drs_days_since_last",""+duration);
			} catch (Exception e) {
				// If there's an exception calculating the date difference, the difference is zero (default case.)
				info.remove("drs_days_since_last");
			}
			int q1 = Integer.parseInt(info.get("crs_bipolar_q1"));
			int q2 = Integer.parseInt(info.get("crs_bipolar_q2"));
			int q3 = Integer.parseInt(info.get("crs_bipolar_q3"));
			int q4 = Integer.parseInt(info.get("crs_bipolar_q4"));
			int q5 = Integer.parseInt(info.get("crs_bipolar_q5"));
			int q6 = Integer.parseInt(info.get("crs_bipolar_q6"));
			int q7 = Integer.parseInt(info.get("crs_bipolar_q7"));
			int q8 = Integer.parseInt(info.get("crs_bipolar_q8"));
			int q9 = Integer.parseInt(info.get("crs_bipolar_q9"));
			int q10 = Integer.parseInt(info.get("crs_bipolar_q10"));
			int total = (q1+q2+q3+q4+q5+q6+q7+q8+q9+q10);
			
			info.put("bdss_score", ""+total);
			int manictotal = q1+q2+q3+q7+q8;
			int manicmax = Math.max(q1,Math.max(q2, Math.max(q3, Math.max(q7, q8))));
			double manicmean = (0.0+manictotal)/5;
			info.put("bdss_score_manic_symptoms_by_level", "");
			info.put("bdss_score_manic_symptoms_severe", "");
			info.put("bdss_score_manic_total", ""+manictotal);
			info.put("bdss_score_manic_max", ""+manicmax);
			info.put("bdss_score_manic_mean", ""+manicmean);
			int depresstotal = q4+q5+q9+q10;
			int depressmax = Math.max(q4, Math.max(q5, Math.max(q9,q10)));
			double depressmean = (0.0+depresstotal)/4;
			info.put("bdss_score_depress_symptoms_by_level", "");
			info.put("bdss_score_depress_symptoms_severe", "");
			info.put("bdss_score_depress_total", ""+depresstotal);
			info.put("bdss_score_depress_max", ""+depressmax);
			info.put("bdss_score_depress_mean", ""+depressmean);
			info.put("bdss_score_psych_mean", info.get("crs_bipolar_q6"));
			info.put("bdss_score_psych_total", info.get("crs_bipolar_q6"));
			info.put("bdss_score_psych_max", ""+info.get("crs_bipolar_q6"));
			info.put("bdss_score_max", ""+Math.max(q6, Math.max(manicmax, depressmax)));
			boolean firstdep = true;
			boolean firstman = true;
			for (int i=6; i>=0; i--) {
				if (q1 == i) {
					if (i > 3) {
						if (!firstman) info.put("bdss_score_manic_symptoms_severe", info.get("bdss_score_manic_symptoms_severe")+",");
						info.put("bdss_score_manic_symptoms_severe", info.get("bdss_score_manic_symptoms_severe")+" hostility-"+i);
					}
					if (!firstman) info.put("bdss_score_manic_symptoms_by_level", info.get("bdss_score_manic_symptoms_by_level")+",");
					info.put("bdss_score_manic_symptoms_by_level", info.get("bdss_score_manic_symptoms_by_level")+" hostility-"+i);
					firstman = false;
				}
				if (q2 == i) {
					if (i > 3) {
						if (!firstman) info.put("bdss_score_manic_symptoms_severe", info.get("bdss_score_manic_symptoms_severe")+",");
						info.put("bdss_score_manic_symptoms_severe", info.get("bdss_score_manic_symptoms_severe")+" elevated mood-"+i);
					}
					if (!firstman) info.put("bdss_score_manic_symptoms_by_level", info.get("bdss_score_manic_symptoms_by_level")+",");
					info.put("bdss_score_manic_symptoms_by_level", info.get("bdss_score_manic_symptoms_by_level")+" elevated mood-"+i);
					firstman = false;
				}
				if (q3 == i) {
					if (i > 3) {
						if (!firstman) info.put("bdss_score_manic_symptoms_severe", info.get("bdss_score_manic_symptoms_severe")+",");
						info.put("bdss_score_manic_symptoms_severe", info.get("bdss_score_manic_symptoms_severe")+" grandiosity-"+i);
					}
					if (!firstman) info.put("bdss_score_manic_symptoms_by_level", info.get("bdss_score_manic_symptoms_by_level")+",");
					info.put("bdss_score_manic_symptoms_by_level", info.get("bdss_score_manic_symptoms_by_level")+" grandiosity-"+i);
					firstman = false;
				}
				if (q4 == i) {
					if (i > 3) {
						if (!firstdep) info.put("bdss_score_depress_symptoms_severe", info.get("bdss_score_depress_symptoms_severe")+",");
						info.put("bdss_score_depress_symptoms_severe", info.get("bdss_score_depress_symptoms_severe")+" depression-"+i);
					}
					if (!firstdep) info.put("bdss_score_depress_symptoms_by_level", info.get("bdss_score_depress_symptoms_by_level")+",");
					info.put("bdss_score_depress_symptoms_by_level", info.get("bdss_score_depress_symptoms_by_level")+" depression-"+i);
					firstdep = false;
				}
				if (q5 == i) {
					if (i > 3) {
						if (!firstdep) info.put("bdss_score_depress_symptoms_severe", info.get("bdss_score_depress_symptoms_severe")+",");
						info.put("bdss_score_depress_symptoms_severe", info.get("bdss_score_depress_symptoms_severe")+" anxiety-"+i);
					}
					if (!firstdep) info.put("bdss_score_depress_symptoms_by_level", info.get("bdss_score_depress_symptoms_by_level")+",");
					info.put("bdss_score_depress_symptoms_by_level", info.get("bdss_score_depress_symptoms_by_level")+" anxiety-"+i);
					firstdep = false;
				}
				// Question 6 is psychotic, it is its own group.
				// if (q6 == i) {
				//	if (i > 3) info.put("bdss_score_psych_symptoms_severe", info.get("bdss_score_psych_symptoms_severe")+", unusual thought content");
				//	info.put("bdss_score_psych_symptoms_by_level", info.get("bdss_score_psych_symptoms_by_level")+", unusual thought content");
				//}
				if (q7 == i) {
					if (i > 3) {
						if (!firstman) info.put("bdss_score_manic_symptoms_severe", info.get("bdss_score_manic_symptoms_severe")+",");
						info.put("bdss_score_manic_symptoms_severe", info.get("bdss_score_manic_symptoms_severe")+" excitement-"+i);
					}
					if (!firstman) info.put("bdss_score_manic_symptoms_by_level", info.get("bdss_score_manic_symptoms_by_level")+",");
					info.put("bdss_score_manic_symptoms_by_level", info.get("bdss_score_manic_symptoms_by_level")+" excitement-"+i);
					firstman = false;
				}
				if (q8 == i) {
					if (i > 3) {
						if (!firstman) info.put("bdss_score_manic_symptoms_severe", info.get("bdss_score_manic_symptoms_severe")+",");
						info.put("bdss_score_manic_symptoms_severe", info.get("bdss_score_manic_symptoms_severe")+" motor hyperactivity-"+i);
					}
					if (!firstman) info.put("bdss_score_manic_symptoms_by_level", info.get("bdss_score_manic_symptoms_by_level")+",");
					info.put("bdss_score_manic_symptoms_by_level", info.get("bdss_score_manic_symptoms_by_level")+" motor hyperactivity-"+i);
					firstman = false;
				}
				if (q9 == i) {
					if (i > 3) {
						if (!firstdep) info.put("bdss_score_depress_symptoms_severe", info.get("bdss_score_depress_symptoms_severe")+",");
						info.put("bdss_score_depress_symptoms_severe", info.get("bdss_score_depress_symptoms_severe")+" emotional withdrawal-"+i);
					}
					if (!firstdep) info.put("bdss_score_depress_symptoms_by_level", info.get("bdss_score_depress_symptoms_by_level")+",");
					info.put("bdss_score_depress_symptoms_by_level", info.get("bdss_score_depress_symptoms_by_level")+" emotional withdrawal-"+i);
					firstdep = false;
				}
				if (q10 == i) {
					if (i > 3) {
						if (!firstdep) info.put("bdss_score_depress_symptoms_severe", info.get("bdss_score_depress_symptoms_severe")+",");
						info.put("bdss_score_depress_symptoms_severe", info.get("bdss_score_depress_symptoms_severe")+" blunted affect-"+i);
					}
					if (!firstdep) info.put("bdss_score_depress_symptoms_by_level", info.get("bdss_score_depress_symptoms_by_level")+",");
					info.put("bdss_score_depress_symptoms_by_level", info.get("bdss_score_depress_symptoms_by_level")+" blunted affect-"+i);
					firstdep = false;
				}
			}
			if (bean.getStatus().get("crs_bipolar_total").length > 1) {
				int pq1 = Integer.parseInt(bean.getStatus().get("crs_bipolar_q1")[1].getValue());
				int pq2 = Integer.parseInt(bean.getStatus().get("crs_bipolar_q2")[1].getValue());
				int pq3 = Integer.parseInt(bean.getStatus().get("crs_bipolar_q3")[1].getValue());
				int pq4 = Integer.parseInt(bean.getStatus().get("crs_bipolar_q4")[1].getValue());
				int pq5 = Integer.parseInt(bean.getStatus().get("crs_bipolar_q5")[1].getValue());
				int pq6 = Integer.parseInt(bean.getStatus().get("crs_bipolar_q6")[1].getValue());
				int pq7 = Integer.parseInt(bean.getStatus().get("crs_bipolar_q7")[1].getValue());
				int pq8 = Integer.parseInt(bean.getStatus().get("crs_bipolar_q8")[1].getValue());
				int pq9 = Integer.parseInt(bean.getStatus().get("crs_bipolar_q9")[1].getValue());
				int pq10 = Integer.parseInt(bean.getStatus().get("crs_bipolar_q10")[1].getValue());
				int ptotal = (pq1+pq2+pq3+pq4+pq5+pq6+pq7+pq8+pq9+pq10);
				int pmanictotal = pq1+pq2+pq3+pq7+pq8;
				int pdepresstotal = pq4+pq5+pq9+pq10;
				info.put("bdss_score_previous", ""+ptotal);
				info.put("bdss_score_difference", ""+Math.abs(ptotal-total));
				info.put("bdss_score_difference_percent", ""+Math.abs((int)(0.5+(0.0+ptotal-total)/ptotal*100)));
				double pmanicmean = (0.0+pmanictotal)/5;
				info.put("bdss_score_manic_total_previous", ""+pmanictotal);
				info.put("bdss_score_manic_total_difference", ""+Math.abs(0.0+pmanictotal-manictotal));
				info.put("bdss_score_manic_mean_previous", ""+pmanicmean);
				info.put("bdss_score_manic_mean_difference", ""+Math.abs((0.0+pmanictotal-manictotal)/5));
				info.put("bdss_score_manic_mean_difference_percent", ""+Math.abs((int)(0.5+(0.0+pmanictotal-manictotal)/pmanictotal*100)));
				info.put("bdss_score_manic_total_difference_percent", ""+Math.abs((int)(0.5+(0.0+pmanictotal-manictotal)/pmanictotal*100)));
				double pdepressmean = (0.0+pdepresstotal)/4;
				info.put("bdss_score_depress_total_previous", ""+pdepresstotal);
				info.put("bdss_score_depress_total_difference", ""+Math.abs(0.0+pdepresstotal-depresstotal));
				info.put("bdss_score_depress_mean_previous", ""+pdepressmean);
				info.put("bdss_score_depress_mean_difference", ""+Math.abs((0.0+pdepresstotal-depresstotal)/5));
				info.put("bdss_score_depress_mean_difference_percent", ""+Math.abs((int)(0.5+(0.0+pdepresstotal-depresstotal)/pdepresstotal*100)));
				info.put("bdss_score_depress_total_difference_percent", ""+Math.abs((int)(0.5+(0.0+pdepresstotal-depresstotal)/pdepresstotal*100)));
				info.put("bdss_score_psych_total_previous", bean.getStatus().get("crs_bipolar_q6")[1].getValue());
				info.put("bdss_score_psych_total_difference", ""+Math.abs(pq6-q6));
				info.put("bdss_score_psych_mean_previous", bean.getStatus().get("crs_bipolar_q6")[1].getValue());
				info.put("bdss_score_psych_mean_difference", ""+Math.abs(pq6-q6));
				info.put("bdss_score_psych_mean_difference_percent", ""+Math.abs((int)(0.5+(0.0+pq6-q6)/pq6*100)));
				info.put("bdss_score_psych_total_difference_percent", ""+Math.abs((int)(0.5+(0.0+pq6-q6)/pq6*100)));
			}
		}
	}

	private void setDiagnosisInfo(ResponsePatientBean bean,
			RecommendDiagnosisBean[] diagnoses) {
		int diagnosis_stage_base = 0;
		if (info.get("diagnosis_primary").contains("Bipolar")) {
			if (info.get("diagnosis_primary").toLowerCase().contains("manic")) {
				info.put("diagnosis","Bipolar (Manic)");
				diagnosis_stage_base = 9;
			} else {
				info.put("diagnosis","Bipolar (Depressed)");
				diagnosis_stage_base = 13;
			}
		} else if (info.get("diagnosis_primary").contains("Schizophrenia")) {
			info.put("diagnosis","Schizophrenia");
			diagnosis_stage_base = 17;
		} else if (info.get("diagnosis_primary").contains("Major Depressive Disorder")) {
			if (info.get("diagnosis_primary").contains("w/Psychotic")) {
				info.put("diagnosis","Major Depressive Disorder (Psychotic)");
				diagnosis_stage_base = 5;
			} else {
				info.put("diagnosis","Major Depressive Disorder (Nonpsychotic)");
				diagnosis_stage_base = 1;
			}
		} else {
			info.put("diagnosis","Other");
		}
		if (diagnosis_stage_base > 0) {
			int i = 0;
			if (info.get("diagnosis_stage") != null) {
				i = Integer.parseInt(info.get("diagnosis_stage"));
			}
			if (i > 0 && i <= 4) {
				// Need to subtract two from the index because the array index is zero-based, 
				// but both the diagnosis_stage_base (databaseID based) and i are one-based.
				diagnosis = diagnoses[diagnosis_stage_base+i-2];
				System.out.println("Setting diagnosis to: "+diagnosis.getDiagnosis());
				System.out.println("  diagnosis_primary: "+info.get("diagnosis_primary"));
				System.out.println("  diagnosis: "+info.get("diagnosis"));
				System.out.println("  diagnosis_stage_base: "+diagnosis_stage_base);
				System.out.println("  i: "+i);
				System.out.println("  diagnosis_primary: "+info.get("diagnosis_primary"));
			} else {
				diagnosis = null;
			}
		} else {
			diagnosis = null;
		}

		if (bean.getStatus().get("diagnosis_primary") != null && bean.getStatus().get("diagnosis_primary").length > 0) {
			String currdiag = bean.getStatus().get("diagnosis_primary")[0].getValue();
			String changedate = null;
			info.put("diagnosis_bipolar_depressed_flag", "0");
			info.put("diagnosis_bipolar_manic_flag", "0");
			int i=0;
			while (i < bean.getStatus().get("diagnosis_primary").length) {
				if (!currdiag.equals(bean.getStatus().get("diagnosis_primary")[i].getValue())) {
					changedate = bean.getStatus().get("diagnosis_primary")[i-1].getEntrydate();
				}
				if (bean.getStatus().get("diagnosis_primary")[i].getValue().contains("Bipolar")) {
					if (bean.getStatus().get("diagnosis_primary")[i].getValue().toLowerCase().contains("manic")) {
						info.put("diagnosis_bipolar_manic_flag", "1");
					} else {
						info.put("diagnosis_bipolar_depressed_flag", "1");
					}
				}
				
				i++;
			}
			if (changedate != null) {
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date startdate = df.parse(changedate);
					long duration = (new Date().getTime() - startdate.getTime()+(1000*3600*12))/(1000*3600*24);
					info.put("diagnosis_since_last",""+duration);
				} catch (Exception e) {
					// If there's an exception calculating the date difference, the difference is zero (default case.)
				}
			}
		}
		if (bean.getStatus().get("diagnosis_primary") != null) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startdate = df.parse(bean.getStatus().get("diagnosis_primary")[(bean.getStatus().get("diagnosis_primary").length)-1].getEntrydate());
				long duration = (new Date().getTime() - startdate.getTime()+(1000*3600*12))/(1000*3600*24);
				long duration_weeks = duration / 7;
				info.put("patient_duration",""+duration);
				info.put("patient_duration_weeks",""+duration_weeks);
			} catch (Exception e) {
				// If there's an exception calculating the date, the duration is zero (default case.)
			}
		}
		if (info.get("diagnosis_since_last") == null) {
			info.put("diagnosis_since_last", "0");
		}
		if (info.get("patient_duration") == null) {
			info.put("patient_duration", "0");
		}
		if (info.get("patient_duration_weeks") == null) {
			info.put("patient_duration_weeks", "0");
		}
	}

	private void setGenderInfo() {
		if (info.get("sex").equalsIgnoreCase("F")) {
			info.put("gender_pronoun", "she");
			info.put("gender_possessive", "her");
			info.put("Gender_pronoun", "She");
			info.put("Gender_possessive", "Her");
		} else {
			info.put("gender_pronoun", "he");
			info.put("gender_possessive", "his");
			info.put("Gender_pronoun", "He");
			info.put("Gender_possessive", "His");
		}
	}
	
	public String toExpandedString() {
		StringBuffer ret = new StringBuffer();
		ret.append("PatientInfo:\n");
		ret.append("  PatientID: "+patientid+"\n");
		ret.append("  FacilityID: "+facilityid+"\n");
		ret.append("  Info:\n");
		TreeSet<String> keys = new TreeSet<String>();
		keys.addAll(info.keySet());
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			String key = it.next();
			String value = info.get(key);
			ret.append("    "+key+": "+value+"\n");
		}
		ret.append("  PrescriptionInfo:\n");
		keys = new TreeSet<String>();
		keys.addAll(prescriptioninfo.keySet());
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			String key = it.next();
			ret.append("    Prescription: "+key+"\n");
			HashMap<String,String> pres = prescriptioninfo.get(key);
			for (Iterator<String> it2 = pres.keySet().iterator(); it2.hasNext();) {
				String key2 = it2.next();
				String value2 = pres.get(key2);
				ret.append("      "+key2+": "+value2+"\n");
			}
		}
		if (diagnosis != null) {
			ret.append("  Diagnosis:\n");
			ret.append("      "+diagnosis.getDiagnosisid()+": "+diagnosis.getDiagnosis()+"\n");
			if (diagnosis.getTreatments() != null) {
				ret.append("      Treatments: "+diagnosis.getTreatments().length+"\n");
				for (int i = 0; i < diagnosis.getTreatments().length; i++) {
					String key = diagnosis.getTreatments()[i].getRow() + " " + diagnosis.getTreatments()[i].getDrug();
					String value = diagnosis.getTreatments()[i].treatment.getTreatmentid() + " " + diagnosis.getTreatments()[i].treatment.getTreatmentname();
					ret.append("        "+key+": "+value+"\n");
				}
			} else {
				ret.append("      No Treatments Available\n");
			}
			ret.append("      JSON:/n");
			if (diagnosis.getGuidelinechart() != null) {
				ret.append("        "+diagnosis.getGuidelinechart().toJsonString()+"\n");
			} else {
				ret.append("        No Guideline Chart Available\n");
			}
		} else {
			ret.append("        No Diagnosis Available\n");
		}
	
		return ret.toString();
	}

}
