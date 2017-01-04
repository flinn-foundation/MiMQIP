package flinn.recommend.beans;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.response.ResponseGuidelineChart;
import flinn.beans.response.ResponseGuidelineChartRow;
import flinn.beans.response.ResponseTreatmentBean;

@XmlRootElement(name = "rule")
@XmlAccessorType(XmlAccessType.FIELD)

public class RecommendDiagnosisBean extends AbstractDataBean {
	@XmlElement(name = "diagnosisid")
	protected int diagnosisid;
	@XmlElement(name = "diagnosis")
	protected String diagnosis;	
	@XmlElement(name = "stage")
	protected String stage;
	@XmlElement(name = "notes")
	protected String notes;
	@XmlElement(name = "treatments")
	protected RecommendTreatmentGuidelineBean[] treatments;
	@XmlElement(name = "guidelinechart")
	protected ResponseGuidelineChart guidelinechart;

	public int getDiagnosisid() {
		return diagnosisid;
	}
	public void setDiagnosisid(int diagnosisid) {
		this.diagnosisid = diagnosisid;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public RecommendTreatmentGuidelineBean[] getTreatments() {
		return treatments;
	}
	public void setTreatments(RecommendTreatmentGuidelineBean[] treatments) {
		this.treatments = treatments;
	}
	public ResponseGuidelineChart getGuidelinechart() {
		return guidelinechart;
	}
	public void setGuidelinechart(ResponseGuidelineChart guidelinechart) {
		this.guidelinechart = guidelinechart;
	}
	public void setGuidelinechart(RecommendTreatmentGuidelineBean[] treatments2) {
		guidelinechart = new ResponseGuidelineChart();
		ArrayList<ResponseGuidelineChartRow> rows = new ArrayList<ResponseGuidelineChartRow>();
		int rownum = 0;
		int drugnum = 0;
		int groupnum = 0;
		ArrayList<String> treats = null;
		ArrayList<RecommendTreatmentGuidelineBean> cellTreats = null;
		String cell = null;
		for (int i=0; i<treatments2.length; i++) {
			RecommendTreatmentGuidelineBean t = treatments2[i];
			if (t.row > rownum) {
				if (treats != null) {
					ResponseGuidelineChartRow row = new ResponseGuidelineChartRow();
					row.setMedications(treats.toArray(new String[treats.size()]));
					rows.add(row);
				}
				drugnum = t.drug;
				rownum = t.row;
				treats = new ArrayList<String>();
				treats.add("");
				cell = null;
				cellTreats = new ArrayList<RecommendTreatmentGuidelineBean>();
			}
			if (t.drug > drugnum) {
				drugnum = t.drug;
				treats.add("");
				cell = null;
				cellTreats = new ArrayList<RecommendTreatmentGuidelineBean>();
			}
			if (containsTreatment(cellTreats,t.getTreatment())) {
				// cell already contains this treatment.
			} else {
				cellTreats.add(t);
			}
			if (i+1 >= treatments2.length || treatments2[i+1].row != rownum || treatments2[i+1].drug != drugnum) { 
				cell = treatmentsToGuidelineCell(cellTreats);
			}
			treats.set(drugnum-1, cell);
		}
		if (treats != null) {
			ResponseGuidelineChartRow row = new ResponseGuidelineChartRow();
			row.setMedications(treats.toArray(new String[treats.size()]));
			rows.add(row);
		}
		guidelinechart.setRows(rows.toArray(new ResponseGuidelineChartRow[rows.size()]));
		guidelinechart.setDiagnosis(diagnosis);
		guidelinechart.setNotes(notes);
		guidelinechart.setStage(stage);
	}
	
	private boolean containsTreatment(
			ArrayList<RecommendTreatmentGuidelineBean> cellTreats,
			ResponseTreatmentBean treatment) {
		boolean ret = false;
		for (int i=0;i<cellTreats.size();i++) {
			if (cellTreats.get(i).getTreatment().getDetails().get("GuidelineChartName").equalsIgnoreCase(treatment.getDetails().get("GuidelineChartName"))) {
				ret = true;
			}
		}
		return ret;
	}
	private String treatmentsToGuidelineCell(ArrayList<RecommendTreatmentGuidelineBean> cellTreats) {
		StringBuffer ret = null;
		int groupnum = 0;
		boolean first = true;
		if (cellTreats == null || cellTreats.size() == 0) return "";
		for (int i=0;i<cellTreats.size();i++) {
			RecommendTreatmentGuidelineBean t = cellTreats.get(i);
			if (groupnum != t.getTreatment().getTreatmentgroupid()) {
				groupnum = t.getTreatment().getTreatmentgroupid();
				if (ret == null) {
					ret = new StringBuffer(t.getTreatment().getGroup().getTreatmentgroupabbreviation() + "<br/> (");
					first = true;
				} else {
					ret.append(") (or)<br/></br> " + t.getTreatment().getGroup().getTreatmentgroupabbreviation() + "<br/> (");
					first = true;
				}
			}
			if (!first) ret.append(", ");
			ret.append(cellTreats.get(i).getTreatment().getDetails().get("GuidelineChartName"));
			first = false;
		}
		ret.append(")");
		return ret.toString();
	}
}
