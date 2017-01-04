package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseTreatmentSearchContainerBean extends ResponseContainerBean {
	@XmlElement(name = "medications")
	protected ResponseTreatmentBean[] treatments;

	public ResponseTreatmentBean[] getTreatments() {
		return treatments;
	}
	public void setTreatments(ResponseTreatmentBean[] treatments) {
		this.treatments = treatments;
	}
}
