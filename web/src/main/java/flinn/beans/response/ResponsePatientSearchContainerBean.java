package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponsePatientSearchContainerBean extends ResponseContainerBean {
	@XmlElement(name = "patients")
	protected ResponsePatientShortBean patients[];
	@XmlElement(name = "page")
	protected int page;		
	@XmlElement(name = "total")
	protected int total;		
	@XmlElement(name = "pagecount")
	protected int pagecount;		

	public ResponsePatientShortBean[] getPatients() {
		return patients;
	}
	public void setPatients(ResponsePatientShortBean[] patients) {
		this.patients = patients;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPagecount() {
		return pagecount;
	}
	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}
}
