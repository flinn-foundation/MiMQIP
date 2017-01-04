package flinn.beans.request;

import java.util.HashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.response.ResponsePatientBean;

@XmlRootElement(name = "patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestPatientBean extends AbstractDataBean {
	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "facilityid")
	protected int facilityid;
	@XmlElement(name = "valid")
	protected Boolean valid;	
	@XmlElement(name = "startdate")
	protected String startdate;
	@XmlElement(name = "details")
	protected HashMap<String, String> details;
	@XmlElement(name = "status")
	protected HashMap<String, String> status;	
	@XmlElement(name = "shortform")
	protected boolean shortform;
	@XmlElement(name = "statusprefix")
	protected String statusprefix;
	@XmlElement(name = "agingfactor")
	protected int agingfactor;

	
	public RequestPatientBean(ResponsePatientBean bean) {
		setPatientid(bean.getPatientid());
		setFacilityid(bean.getFacilityid());
		setValid(bean.getValid());
		setStartdate(bean.getStartdate());
	}
	
	public RequestPatientBean() {
		super();
	}

	public int getPatientid() {
		return patientid;
	}
	public void setPatientid(int patientid) {
		this.patientid = patientid;
	}
	public int getFacilityid() {
		return facilityid;
	}
	public void setFacilityid(int facilityid) {
		this.facilityid = facilityid;
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
	public HashMap<String, String> getDetails(){
		return details;
	}
	public void setDetails(HashMap<String, String> details){
		this.details = details;
	}	
	public HashMap<String, String> getStatus(){
		return status;
	}
	public void setStatus(HashMap<String, String> status){
		this.status = status;
	}
	public boolean getShortform() {
		return shortform;
	}
	public void setShortform(boolean shortform) {
		this.shortform = shortform;
	}

	public String getStatusprefix() {
		return statusprefix;
	}

	public void setStatusprefix(String statusprefix) {
		this.statusprefix = statusprefix;
	}
	public int getAgingfactor() {
		return agingfactor;
	}

	public void setAgingfactor(int agingfactor) {
		this.agingfactor = agingfactor;
	}
}
