package flinn.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.LabTestBean;

@XmlRootElement(name = "labsearch")
@XmlAccessorType(XmlAccessType.FIELD)

public class RequestLabSearchBean extends AbstractDataBean {
	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "labtest")
	protected LabTestBean labtest;
	@XmlElement(name = "page")
	protected int page;		
	@XmlElement(name = "pagecount")
	protected int pagecount;
	public int getPatientid() {
		return patientid;
	}
	public void setPatientid(int patientid) {
		this.patientid = patientid;
	}
	public LabTestBean getLabtest() {
		return labtest;
	}
	public void setLabtest(LabTestBean labtest) {
		this.labtest = labtest;
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
