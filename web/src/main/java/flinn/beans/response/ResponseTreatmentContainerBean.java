package flinn.beans.response;

import javax.xml.bind.annotation.XmlElement;

public class ResponseTreatmentContainerBean extends ResponseContainerBean {
	@XmlElement(name = "treatment")
	protected ResponseTreatmentBean treatment;

	public ResponseTreatmentBean getTreatment() {
		return treatment;
	}

	public void setTreatment(ResponseTreatmentBean treatment) {
		this.treatment = treatment;
	}
}
