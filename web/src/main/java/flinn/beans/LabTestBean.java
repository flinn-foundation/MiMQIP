package flinn.beans;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "labtest")
@XmlAccessorType(XmlAccessType.FIELD)

public class LabTestBean extends AbstractDataBean {
	@XmlElement(name = "labtestid")
	protected int labtestid;
	@XmlElement(name = "labtestname")
	protected String labtestname;
	@XmlElement(name = "valid")
	protected Boolean valid;	
	@XmlElement(name = "startdate")
	protected String startdate;
	@XmlElement(name = "discontinuedate")
	protected String discontinuedate;
	@XmlElement(name = "details")
	protected HashMap<String, String> details;
	
	public int getLabtestid() {
		return labtestid;
	}
	public void setLabtestid(int labtestid) {
		this.labtestid = labtestid;
	}
	public String getLabtestname() {
		return labtestname;
	}
	public void setLabtestname(String labtestname) {
		this.labtestname = labtestname;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getDiscontinuedate() {
		return discontinuedate;
	}
	public void setDiscontinuedate(String discontinuedate) {
		this.discontinuedate = discontinuedate;
	}
	public HashMap<String, String> getDetails() {
		return details;
	}
	public void setDetails(HashMap<String, String> details) {
		this.details = details;
	}	

}
