package flinn.beans.response;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponsePrescriptionSearchContainerBean extends ResponseContainerBean {
	@XmlElement(name = "prescriptions")
    protected HashMap<String, ResponsePrescriptionBean[]> prescriptions;

	public HashMap<String, ResponsePrescriptionBean[]> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(
			HashMap<String, ResponsePrescriptionBean[]> prescriptions) {
		this.prescriptions = prescriptions;
	}
}
