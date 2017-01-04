package flinn.beans.response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseAppUserContainerBean extends ResponseContainerBean {
	@XmlElement(name = "user")
	protected ResponseAppUserBean user;

	public ResponseAppUserBean getUser() {
		return user;
	}
	public void setUser(ResponseAppUserBean user) {
		this.user = user;
	}
}
