package flinn.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tags")
@XmlAccessorType(XmlAccessType.FIELD)

public class ProgressNoteTagBean {
	@XmlElement(name = "progressnotetagid")
	protected int progressnotetagid = 0;
	@XmlElement(name = "progressnotetag")
	protected String progressnotetag;
	protected String progressnotetagdescription;
	@XmlElement(name = "valid")
	protected Boolean valid;

	public int getProgressnotetagid() {
		return progressnotetagid;
	}
	public void setProgressnotetagid(int progressnotetagid) {
		this.progressnotetagid = progressnotetagid;
	}
	public String getProgressnotetag() {
		return progressnotetag;
	}
	public void setProgressnotetag(String progressnotetag) {
		this.progressnotetag = progressnotetag;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getProgressnotetagdescription() {
		return progressnotetagdescription;
	}
	public void setProgressnotetagdescription(String progressnotetagdescription) {
		this.progressnotetagdescription = progressnotetagdescription;
	}	

	boolean equals (ProgressNoteTagBean b) {
		return (b.getProgressnotetagid() == progressnotetagid);
	}

}
