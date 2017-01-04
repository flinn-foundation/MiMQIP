package flinn.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "group")
@XmlAccessorType(XmlAccessType.FIELD)

public class TreatmentGroupBean {
	
	@XmlElement(name = "treatmentgroupid")
	protected int treatmentgroupid = 0;
	@XmlElement(name = "treatmentgroupname")
	protected String treatmentgroupname;
	@XmlElement(name = "treatmentgroupabbreviation")
	protected String treatmentgroupabbreviation;
	@XmlElement(name = "valid")
	protected Boolean valid;
	
	
	public int getTreatmentgroupid() {
		return treatmentgroupid;
	}
	public void setTreatmentgroupid(int treatmentgroupid) {
		this.treatmentgroupid = treatmentgroupid;
	}
	public String getTreatmentgroupname() {
		return treatmentgroupname;
	}
	public void setTreatmentgroupname(String treatmentgroupname) {
		this.treatmentgroupname = treatmentgroupname;
	}
	public String getTreatmentgroupabbreviation() {
		return treatmentgroupabbreviation;
	}
	public void setTreatmentgroupabbreviation(String treatmentgroupabbreviation) {
		this.treatmentgroupabbreviation = treatmentgroupabbreviation;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}

}
