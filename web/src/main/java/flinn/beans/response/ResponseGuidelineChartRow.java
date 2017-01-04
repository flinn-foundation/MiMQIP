package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseGuidelineChartRow extends AbstractDataBean {
	@XmlElement(name = "medications")
	protected String[] medications;

	public ResponseGuidelineChartRow() {
		super();
	}

	public String[] getMedications() {
		return medications;
	}

	public void setMedications(String[] medications) {
		this.medications = medications;
	}
	
}
