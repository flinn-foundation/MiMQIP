package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.LabTestBean;

@XmlRootElement(name = "lab")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseLabBean extends AbstractDataBean {
	@XmlElement(name = "labid")
	protected int labid;
	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "labdate")
	protected String labdate;
	@XmlElement(name = "labtext")
	protected String labtext;
	@XmlElement(name = "labtest")
	protected LabTestBean labtest;

	public int getLabid() {
		return labid;
	}
	public void setLabid(int labid) {
		this.labid = labid;
	}
	public int getPatientid() {
		return patientid;
	}
	public void setPatientid(int patientid) {
		this.patientid = patientid;
	}
	public String getLabdate() {
		return labdate;
	}
	public void setLabdate(String labdate) {
		this.labdate = labdate;
	}
	public String getLabtext() {
		return labtext;
	}
	public void setLabtext(String labtext) {
		this.labtext = labtext;
	}
	public LabTestBean getLabtest() {
		return labtest;
	}
	public void setLabtest(LabTestBean labtest) {
		this.labtest = labtest;
	}

}
