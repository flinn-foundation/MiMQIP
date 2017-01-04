package flinn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import flinn.recommend.beans.RecommendDiagnosisBean;
import flinn.recommend.beans.RecommendTreatmentGuidelineBean;

public class RecommendUtils {

	public static HashMap<String, String> consistency(
			HashMap<String, String> info,
			HashMap<String, HashMap<String, String>> prescriptioninfo,
			RecommendDiagnosisBean diagnosis, RecommendDiagnosisBean[] diagnoses) {
		HashMap<String,String> ret = new HashMap<String,String>();
		
		if (isConsistent(prescriptioninfo,diagnosis)) {
			ret.put("consistent", "1");
		} else {
			ret.put("consistent", "0");

			if (isConsistentStageBelow(prescriptioninfo,diagnosis,diagnoses)) ret.put("consistent_stage_below", "1");
			else ret.put("consistent_stage_below", "0");

			if (isConsistentStageAbove(prescriptioninfo,diagnosis,diagnoses)) ret.put("consistent_stage_above", "1");
			else ret.put("consistent_stage_above", "0");

			if (isConsistentDrugAbove(prescriptioninfo,diagnosis,diagnoses)) ret.put("consistent_drug_above", "1");
			else ret.put("consistent_drug_above", "0");

			if (isConsistentAdditional(prescriptioninfo,diagnosis)) ret.put("consistent_additional", "1");
			else ret.put("consistent_additional", "0");
		}

		return ret;
	}
	
	public static boolean isConsistentAdditional(
			HashMap<String, HashMap<String, String>> prescriptioninfo,
			RecommendDiagnosisBean diagnosis) {

		int row=-1;
		int drug=-1;
		boolean rowworks = false;
		int drugs[] = new int[5];
		String scripts[] = new String[prescriptioninfo.size()];
		Iterator<String> it = prescriptioninfo.keySet().iterator();
		int j =0;
		while(it.hasNext()) {
			String key = it.next();
			scripts[j] = prescriptioninfo.get(key).get("medication");
			j++;
		}
		for (int i=0; i<diagnosis.getTreatments().length; i++) {
			RecommendTreatmentGuidelineBean bean = diagnosis.getTreatments()[i];
			if (bean.getRow() > row) {
				if (rowworks) return true;
				rowworks = true;
				row = bean.getRow();
				drug = -1;
				for (j=0; j<5; j++) {
					drugs[j] = 0;
				}
			}
			if (rowworks) {
				if (prescriptioninfo.containsKey(bean.getTreatment().getDetails().get("GuidelineChartName"))) {
					if (prescriptioninfo.containsKey(bean.getTreatment().getDetails().get("GuidelineChartName"))) {
						HashMap <String,String> pre = prescriptioninfo.get(bean.getTreatment().getDetails().get("GuidelineChartName"));
						if (pre.get("treatmentid").equals(""+bean.getTreatment().getTreatmentid())) {
							if (drugs[bean.getDrug()] > 0) {
								rowworks = false;
							}
							drugs[bean.getDrug()] = 1;
						}
					}
				}
				if (bean.getDrug() > drug) {
					if (drug < 0) {
						drug = bean.getDrug();
					} else {
						if (drugs[drug] == 0) {
							rowworks = false;
						} else {
							drug = bean.getDrug();
						}
					}
				}
				if (rowworks && diagnosis.getTreatments().length > i+1) {
					bean = diagnosis.getTreatments()[i+1];
					if (bean.getRow() > row){
						if (drugs[drug] == 0) rowworks = false;
					}
				} else {
					if (drugs[drug] == 0) rowworks = false;
				}
			}
		}
		return rowworks;
	}

	public static boolean isConsistentDrugAbove(
			HashMap<String, HashMap<String, String>> prescriptioninfo,
			RecommendDiagnosisBean diagnosis, RecommendDiagnosisBean[] diagnoses) {
		if (diagnosis == null) return false;
		HashMap<String,Integer> drugmap = new HashMap<String,Integer>();
		int stage = Integer.parseInt(diagnosis.getStage());
		for (int i=0; i<diagnoses.length; i++) {
			RecommendDiagnosisBean bean = diagnoses[i];
			if (diagnosis.getDiagnosis().equals(bean.getDiagnosis())) {
				int drugstage = Integer.parseInt(bean.getStage());
				for (int j=0; j<bean.getTreatments().length; j++) {
					if (!drugmap.containsKey(bean.getTreatments()[j].getTreatment().getDetails().get("GuidelineChartName"))) {
						drugmap.put(bean.getTreatments()[j].getTreatment().getTreatmentname(), new Integer(drugstage));
					}
				}
			}
		}
		Iterator<String> it = prescriptioninfo.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			if (drugmap.get(key) != null && drugmap.get(key).intValue() > stage) return true;
		}
		return false;
	}

	public static boolean isConsistentStageAbove(
			HashMap<String, HashMap<String, String>> prescriptioninfo,
			RecommendDiagnosisBean diagnosis, RecommendDiagnosisBean[] diagnoses) {
		if (diagnosis == null) return false;
		int stage = Integer.parseInt(diagnosis.getStage());
		for (int i=0; i<diagnoses.length; i++) {
			RecommendDiagnosisBean bean = diagnoses[i];
			if (diagnosis.getDiagnosis().equals(bean.getDiagnosis())) {
				if (Integer.parseInt(bean.getStage()) > stage) {
					if (isConsistent(prescriptioninfo, bean)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isConsistentStageBelow(
			HashMap<String, HashMap<String, String>> prescriptioninfo,
			RecommendDiagnosisBean diagnosis, RecommendDiagnosisBean[] diagnoses) {
		if (diagnosis == null) return false;
		int stage = Integer.parseInt(diagnosis.getStage());
		for (int i=0; i<diagnoses.length; i++) {
			RecommendDiagnosisBean bean = diagnoses[i];
			if (diagnosis.getDiagnosis().equals(bean.getDiagnosis())) {
				if (Integer.parseInt(bean.getStage()) < stage) {
					if (isConsistent(prescriptioninfo, bean)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isConsistent(
			HashMap<String, HashMap<String, String>> prescriptioninfo,
			RecommendDiagnosisBean diagnosis) {
		
		int row=-1;
		int drug=-1;
		boolean rowworks = false;
		int drugs[] = new int[5];
		String scripts[] = new String[prescriptioninfo.size()];
		Iterator<String> it = prescriptioninfo.keySet().iterator();
		int j =0;
		while(it.hasNext()) {
			String key = it.next();
			scripts[j] = prescriptioninfo.get(key).get("medication");
			j++;
		}
		for (int i=0; i<diagnosis.getTreatments().length; i++) {
			RecommendTreatmentGuidelineBean bean = diagnosis.getTreatments()[i];
			if (bean.getRow() > row) {
				// Last row did work.  Return true.
				if (rowworks) return true;
				// Last row didn't work, zero out test flags and start new row.
				rowworks = true;
				row = bean.getRow();
				drug = -1;
				for (j=0; j<5; j++) {
					drugs[j] = 0;
				}
			}
			if (rowworks) {
				if (prescriptioninfo.containsKey(bean.getTreatment().getDetails().get("GuidelineChartName"))) {
					HashMap <String,String> pre = prescriptioninfo.get(bean.getTreatment().getDetails().get("GuidelineChartName"));
					if (pre.get("treatmentid").equals(""+bean.getTreatment().getTreatmentid())) {
						if (drugs[bean.getDrug()] > 0) {
							rowworks = false;
						}
						drugs[bean.getDrug()] = 1;
					}
				}
				if (bean.getDrug() > drug) {
					if (drug < 0) {
						drug = bean.getDrug();
					} else {
						if (drugs[drug] == 0) {
							rowworks = false;
						} else {
							drug = bean.getDrug();
						}
					}
				}
				if (rowworks && diagnosis.getTreatments().length > i+1) {
					bean = diagnosis.getTreatments()[i+1];
					if (bean.getRow() > row){
						if (drugs[drug] == 0) rowworks = false;
						if (drug < prescriptioninfo.size()) rowworks = false;
					}
				} else {
					if (drugs[drug] == 0) rowworks = false;
					if (drug < prescriptioninfo.size()) rowworks = false;
				}
				if (!rowworks) System.out.println("  flinn.flinn.util.RecommendUtils.isConsistent  ---    rowworks false");
			}
		}
		return rowworks;
	}
	
	public static String formatDateFromDB(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ret = "";
		Date workingDate = null;
		try {
			workingDate = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ret = formatDateFromDB(workingDate);
		return ret;
	}
	
	public static String formatDateFromDB(Date date) {
		SimpleDateFormat dfout = new SimpleDateFormat("M/d/yyyy");
		String ret = "";
		ret = dfout.format(date);
		return ret;
	}
	
	public static String formatCacheKey(String value) {
		if(value == null)
			value = "all";
		return value;
	}

	public static int stageRecommendation(String diagnosis, String[] treatments, RecommendDiagnosisBean[] diagnoses) {
		int diagnosis_stage_base = 0;
		if (diagnosis != null) {
			if (diagnosis.toLowerCase().contains("bipolar")) {
				if (diagnosis.toLowerCase().contains("manic")) {
					diagnosis_stage_base = 9;
				} else {
					diagnosis_stage_base = 13;
				}
			} else if (diagnosis.toLowerCase().contains("schizophrenia")) {
				diagnosis_stage_base = 17;
			} else if (diagnosis.toLowerCase().contains("major depressive disorder")) {
				if (diagnosis.toLowerCase().contains("w/psychotic")) {
					diagnosis_stage_base = 5;
				} else {
					diagnosis_stage_base = 1;
				}
			}
		}
		// System.out.println("Found Diagnosis base: "+diagnosis_stage_base);
		// if (diagnosis_stage_base != 0) System.out.println("Found Diagnosis: "+diagnoses[diagnosis_stage_base-1].getDiagnosis()+" Stage: "+diagnoses[diagnosis_stage_base-1].getStage());
		if (diagnosis_stage_base != 0) return stageRecommendation(diagnoses[diagnosis_stage_base-1], treatments, diagnoses);
		return 0;
	}

	public static int stageRecommendation(RecommendDiagnosisBean diagnosis, String[] treatments, RecommendDiagnosisBean[] diagnoses) {
		int stage = 1;
		if (treatments != null && diagnoses != null) {
			for (int i=0; i<treatments.length; i++) {
				String treatment = treatments[i].toLowerCase();
				// System.out.println("Checking for treatment: "+treatment);
				boolean treatment_found = false;
				int treatment_stage = 0;
				for (int j=0; j<4; j++) {
					for (int k=0; k<diagnoses[diagnosis.getDiagnosisid()+j-1].getTreatments().length; k++) {
						RecommendTreatmentGuidelineBean bean = diagnoses[diagnosis.getDiagnosisid()+j-1].getTreatments()[k];
						// System.out.println("  Comparing treatment: \""+treatment+"\" to treatment: \""+bean.getTreatment().getTreatmentname()+"\" for Diagnosis: "+diagnoses[diagnosis.getDiagnosisid()+j-1].getDiagnosis()+" Stage: "+diagnoses[diagnosis.getDiagnosisid()+j-1].getStage());
						if (!treatment_found && bean.getTreatment().getTreatmentname().toLowerCase().equals(treatment)) {
							treatment_found = true;
							treatment_stage = j+1;
							// System.out.println("    Found Treatment at stage "+treatment_stage);
						}
					}
				}
				if (treatment_found && treatment_stage > stage) {
					stage = treatment_stage;
				}
			}
		}
		return stage;
	}

}
