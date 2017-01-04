package flinn.beans.response;

import javax.xml.bind.annotation.XmlElement;

public class ResponsePrescriptionContainerBean extends ResponseContainerBean {
	@XmlElement(name = "prescription")
	protected ResponsePrescriptionBean prescription;

	public ResponsePrescriptionBean getPrescription() {
		return prescription;
	}
	public void setPrescription(ResponsePrescriptionBean prescription) {
		this.prescription = prescription;
	}
}
