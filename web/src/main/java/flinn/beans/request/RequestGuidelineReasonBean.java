package flinn.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.response.ResponseGuidelineReasonBean;

@XmlRootElement(name = "guidelinereason")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestGuidelineReasonBean extends AbstractDataBean  {
	@XmlElement(name = "reasonid")
	protected int reasonid;	
	@XmlElement(name = "reason")
	protected String[] reason;
	@XmlElement(name = "reasondate")
	protected String reasondate;
	@XmlElement(name = "doctorname")
	protected String doctorname;
	@XmlElement(name = "patientid")
	protected int patientid;		
	
	public RequestGuidelineReasonBean(ResponseGuidelineReasonBean bean)
	{
		setReasonid(bean.getReasonid());
		setReason(bean.getReason());
		setReasondate(bean.getReasondate());
		setDoctorname(bean.getDoctorname());
		setPatientid(bean.getPatientid());
	}
	
	public String[] getReason() {
		return reason;
	}
	public void setReason(String[] reason) {
		this.reason = reason;
	}
	public int getReasonid() {
		return reasonid;
	}
	public String getReasondate() {
		return reasondate;
	}
	public void setReasondate(String reasondate) {
		this.reasondate = reasondate;
	}
	public void setReasonid(int reasonid) {
		this.reasonid = reasonid;
	}	
	public String getDoctorname() {
		return doctorname;
	}
	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}
	public int getPatientid() {
		return patientid;
	}
	public void setPatientid(int patientid) {
		this.patientid = patientid;
	}	
	public RequestGuidelineReasonBean() {
		super();
	}
}
