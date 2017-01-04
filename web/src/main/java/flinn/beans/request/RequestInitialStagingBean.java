package flinn.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "lab")
@XmlAccessorType(XmlAccessType.FIELD)

public class RequestInitialStagingBean extends AbstractDataBean {
	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "diagnosis")
	protected String diagnosis;
	@XmlElement(name = "treatments")
	protected String[] treatments;
	

	public int getPatientid() {
		return patientid;
	}
	public void setPatientid(int patientid) {
		this.patientid = patientid;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String[] getTreatments() {
		return treatments;
	}
	public void setTreatments(String[] treatments) {
		this.treatments = treatments;
	}

}
