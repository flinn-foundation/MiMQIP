package flinn.beans.response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseFacilityContainerBean extends ResponseContainerBean {
	@XmlElement(name = "facility")
	protected ResponseFacilityBean facility;

	public ResponseFacilityBean getFacility() {
		return facility;
	}
	public void setFacility(ResponseFacilityBean facility) {
		this.facility = facility;
	}
}
