package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.LabTestBean;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseLabTestsContainerBean extends ResponseContainerBean {

	@XmlElement(name = "labtests")
	protected LabTestBean[] labtests;

	public LabTestBean[] getLabtests() {
		return labtests;
	}
	public void setLabtests(LabTestBean[] labtests) {
		this.labtests = labtests;
	}
	
}
