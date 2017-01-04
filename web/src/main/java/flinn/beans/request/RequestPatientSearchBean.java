package flinn.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestPatientSearchBean extends AbstractDataBean {
	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "facilityid")
	protected int facilityid;
	@XmlElement(name = "patientidentifier")
	protected String patientidentifier;		
	@XmlElement(name = "valid")
	protected Boolean valid;	
	@XmlElement(name = "firstname")
	protected String firstname;		
	@XmlElement(name = "lastname")
	protected String lastname;		
	@XmlElement(name = "sex")
	protected String sex;		
	@XmlElement(name = "birth")
	protected String birth;		
	@XmlElement(name = "page")
	protected int page;		
	@XmlElement(name = "pagecount")
	protected int pagecount;		

	
	public RequestPatientSearchBean() {
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
	public String getPatientidentifier() {
		return patientidentifier;
	}
	public void setPatientidentifier(String patientidentifier) {
		this.patientidentifier = patientidentifier;
	}	
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

}
