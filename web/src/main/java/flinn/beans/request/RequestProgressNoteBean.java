package flinn.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.ProgressNoteTagBean;
import flinn.beans.response.ResponseProgressNoteBean;

@XmlRootElement(name = "progressnote")
@XmlAccessorType(XmlAccessType.FIELD)

public class RequestProgressNoteBean extends AbstractDataBean {
	@XmlElement(name = "progressnoteid")
	protected int progressnoteid;	
	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "notetext")
	protected String notetext;
	@XmlElement(name = "doctorname")
	protected String doctorname;
	@XmlElement(name = "authorid")
	protected String authorid;
	@XmlElement(name = "entrydate")
	protected String entrydate;
	@XmlElement(name = "tags")
	protected ProgressNoteTagBean[] tags;
	
	public RequestProgressNoteBean(ResponseProgressNoteBean bean) {
		setPatientid(bean.getPatientid());
		setNotetext(bean.getNotetext());
		setDoctorname(bean.getDoctorname());
		setAuthorid(bean.getAuthorid());
		setTags(bean.getTags());
		setEntrydate(bean.getEntrydate());
	}

	public int getProgressnoteid() {
		return progressnoteid;
	}
	public void setProgressnoteid(int progressnoteid) {
		this.progressnoteid = progressnoteid;
	}
	public int getPatientid() {
		return patientid;
	}
	public void setPatientid(int patientid) {
		this.patientid = patientid;
	}
	public String getNotetext() {
		return notetext;
	}
	public void setNotetext(String notetext) {
		this.notetext = notetext;
	}	
	public ProgressNoteTagBean[] getTags() {
		return tags;
	}
	public void setTags(ProgressNoteTagBean[] tags) {
		this.tags = tags;
	}
	public String getDoctorname() {
		return doctorname;
	}
	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}
	public String getAuthorid() {
		return authorid;
	}
	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	public String getEntrydate() {
		return entrydate;
	}
	public void setEntrydate(String entrydate) {
		this.entrydate = entrydate;
	}
	public RequestProgressNoteBean() {
		super();
	}
}

