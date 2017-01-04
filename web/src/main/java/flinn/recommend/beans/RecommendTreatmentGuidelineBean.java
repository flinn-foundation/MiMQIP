package flinn.recommend.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.response.ResponseTreatmentBean;

@XmlRootElement(name = "treatmentguideline")
@XmlAccessorType(XmlAccessType.FIELD)

public class RecommendTreatmentGuidelineBean extends AbstractDataBean {
	@XmlElement(name = "treatmentguidelineid")
	protected int treatmentguidelineid;
	@XmlElement(name = "treatmentid")
	protected int treatmentid;
	@XmlElement(name = "diagnosisid")
	protected int diagnosisid;
	@XmlElement(name = "row")
	protected int row;
	@XmlElement(name = "drug")
	protected int drug;
	@XmlElement(name = "treatment")
	protected ResponseTreatmentBean treatment;
	
	public RecommendTreatmentGuidelineBean() {
		super();
	}

	public int getTreatmentguidelineid() {
		return treatmentguidelineid;
	}

	public void setTreatmentguidelineid(int treatmentguidelineid) {
		this.treatmentguidelineid = treatmentguidelineid;
	}

	public int getTreatmentid() {
		return treatmentid;
	}

	public void setTreatmentid(int treatmentid) {
		this.treatmentid = treatmentid;
	}

	public int getDiagnosisid() {
		return diagnosisid;
	}

	public void setDiagnosisid(int diagnosisid) {
		this.diagnosisid = diagnosisid;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getDrug() {
		return drug;
	}

	public void setDrug(int drug) {
		this.drug = drug;
	}

	public ResponseTreatmentBean getTreatment() {
		return treatment;
	}

	public void setTreatment(ResponseTreatmentBean treatment) {
		this.treatment = treatment;
	}

}
