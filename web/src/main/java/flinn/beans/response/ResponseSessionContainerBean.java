package flinn.beans.response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseSessionContainerBean extends ResponseContainerBean {
	@XmlElement(name = "user")
	protected ResponseAppUserBean user;
	@XmlElement(name = "facility")
	protected ResponseFacilityBean facility;

	public ResponseAppUserBean getUser() {
		return user;
	}
	public void setUser(ResponseAppUserBean user) {
		this.user = user;
	}
	public ResponseFacilityBean getFacility() {
		return facility;
	}
	public void setFacility(ResponseFacilityBean facility) {
		this.facility = facility;
	}

}
