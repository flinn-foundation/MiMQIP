package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponsePatientShortContainerBean extends ResponseContainerBean {
	@XmlElement(name = "patient")
	protected ResponsePatientShortBean patient;

	public ResponsePatientShortBean getPatient() {
		return patient;
	}
	public void setPatient(ResponsePatientShortBean patient) {
		this.patient = patient;
	}
}
