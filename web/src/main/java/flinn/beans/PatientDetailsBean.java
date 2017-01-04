package flinn.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "detail")
@XmlAccessorType(XmlAccessType.FIELD)

public class PatientDetailsBean extends AbstractDataBean {
	@XmlElement(name = "value")
	protected String value = null;
	@XmlElement(name = "entrydate")
	protected String entrydate = null;


	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getEntrydate() {
		return entrydate;
	}
	public void setEntrydate(String entrydate) {
		this.entrydate = entrydate;
	}
}
