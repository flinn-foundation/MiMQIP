package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponsePrescriptionShortContainerBean extends ResponseContainerBean {
	@XmlElement(name = "prescription")
	protected ResponsePrescriptionShortContainerBean prescription;

	public ResponsePrescriptionShortContainerBean getPrescription() {
		return prescription;
	}
	public void setPatient(ResponsePrescriptionShortContainerBean prescription) {
		this.prescription = prescription;
	}
}

