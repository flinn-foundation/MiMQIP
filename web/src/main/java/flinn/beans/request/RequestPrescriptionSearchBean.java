package flinn.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "prescriptionsearch")
@XmlAccessorType(XmlAccessType.FIELD)

public class RequestPrescriptionSearchBean extends AbstractDataBean {
	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "treatmentid")
	protected int treatmentid;
	@XmlElement(name = "entrydate")
	protected String entrydate;
	@XmlElement(name = "discontinue")
	protected boolean discontinue;
	@XmlElement(name = "dailydose")
	protected String dailydose;
	@XmlElement(name = "doctorname")
	protected String doctorname;
	@XmlElement(name = "shortform")
	protected boolean shortform;	
	
	
	public RequestPrescriptionSearchBean() {
		super();
	}

	public int getPatientid() {
		return patientid;
	}
	public void setPatientid(int patientid) {
		this.patientid = patientid;
	}
	public int getTreatmentid() {
		return treatmentid;
	}

	public void setTreatmentid(int treatmentid) {
		this.treatmentid = treatmentid;
	}

	public String getEntrydate() {
		return entrydate;
	}

	public void setEntrydate(String entrydate) {
		this.entrydate = entrydate;
	}

	public boolean isDiscontinue() {
		return discontinue;
	}

	public void setDiscontinue(boolean discontinue) {
		this.discontinue = discontinue;
	}

	public String getDailydose() {
		return dailydose;
	}

	public void setDailydose(String dailydose) {
		this.dailydose = dailydose;
	}

	public String getDoctorname() {
		return doctorname;
	}

	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}
	public boolean getShortform() {
		return shortform;
	}
	public void setShortform(boolean shortform) {
		this.shortform = shortform;
	}	
}
