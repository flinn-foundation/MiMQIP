package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.request.RequestGuidelineReasonBean;
import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "guidelinereason")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseGuidelineReasonBean extends AbstractDataBean {
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
	
	public ResponseGuidelineReasonBean() {
		super();
	}
	
	public ResponseGuidelineReasonBean(RequestGuidelineReasonBean bean)
	{
		setReasonid(bean.getReasonid());
		setReason(bean.getReason());
		setReasondate(bean.getReasondate());
		setDoctorname(bean.getDoctorname());
		setPatientid(bean.getPatientid());
	}
	
	public int getReasonid() {
		return reasonid;
	}
	public void setReasonid(int reasonid) {
		this.reasonid = reasonid;
	}
	public String[] getReason() {
		return reason;
	}
	public void setReason(String[] reason) {
		this.reason = reason;
	}
	public String getReasondate() {
		return reasondate;
	}
	public void setReasondate(String reasondate) {
		this.reasondate = reasondate;
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
}
