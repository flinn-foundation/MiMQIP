package flinn.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.ProgressNoteTagBean;

@XmlRootElement(name = "notesearch")
@XmlAccessorType(XmlAccessType.FIELD)

public class RequestProgressNoteSearchBean extends AbstractDataBean {
	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "tags")
	protected ProgressNoteTagBean[] tags;
	@XmlElement(name = "searchtext")
	protected String searchtext;	
	@XmlElement(name = "page")
	protected int page;		
	@XmlElement(name = "pagecount")
	protected int pagecount;
	
	
	public RequestProgressNoteSearchBean() {
		super();
	}
	
	public int getPatientid() {
		return patientid;
	}
	public void setPatientid(int patientid) {
		this.patientid = patientid;
	}
	public ProgressNoteTagBean[] getTags() {
		return tags;
	}
	public void setTags(ProgressNoteTagBean[] tags) {
		this.tags = tags;
	}
	public String getSearchtext() {
		return searchtext;
	}
	public void setSearchtext(String searchtext) {
		this.searchtext = searchtext;
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
