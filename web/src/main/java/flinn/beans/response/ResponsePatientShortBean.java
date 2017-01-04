package flinn.beans.response;

import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.PatientDetailsBean;
import flinn.beans.PatientStatusBean;
import flinn.beans.request.RequestPatientBean;

@XmlRootElement(name = "patient")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponsePatientShortBean extends AbstractDataBean {
	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "facilityid")
	protected int facilityid;
	@XmlElement(name = "valid")
	protected Boolean valid;	
	@XmlElement(name = "StartDate")
	protected String startdate;
	@XmlElement(name = "details")
	protected HashMap<String, String> details;
	@XmlElement(name = "status")
	protected HashMap<String, String> status;	
	
	public ResponsePatientShortBean() {
		super();
	}

	public ResponsePatientShortBean (RequestPatientBean bean) {
		setPatientid(bean.getPatientid());
		setFacilityid(bean.getFacilityid());
		setValid(bean.getValid());
		setStartdate(bean.getStartdate());
	}
	
	public ResponsePatientShortBean (ResponsePatientBean bean, boolean search) {
		setPatientid(bean.getPatientid());
		setFacilityid(bean.getFacilityid());
		setValid(bean.getValid());
		setStartdate(bean.getStartdate());
		setDetails(bean.getDetails(), search);
		setStatus(bean.getStatus(), search);
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

	private void setDetails(HashMap<String, PatientDetailsBean[]> details2, boolean search) {
		if (details2 != null) {
			this.details = new HashMap<String,String>();
			Iterator<String> it = details2.keySet().iterator();
			while(it.hasNext())
			{
				String key = it.next();
				if (!search || key.equals("firstname") || key.equals("lastname") || key.equals("sex") || key.equals("birth") || key.equals("patientidentifier")) {
					String value = details2.get(key)[0].getValue();
					this.details.put(key, value);
				}
			}
		} else {
			this.details = null;
		}
	}

	private void setStatus(HashMap<String, PatientStatusBean[]> status2, boolean search) {
		if (status2 != null) {
			this.status = new HashMap<String,String>();
			if (!search) {
				Iterator<String> it = status2.keySet().iterator();
				while(it.hasNext())
				{
					String key = it.next();
					String value = status2.get(key)[0].getValue();
					this.status.put(key, value);
				}
			}
		} else {
			this.status = null;
		}
	}

}
