package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseAuthenticateContainerBean extends ResponseContainerBean {

	@XmlElement(name = "authenticate")
	protected ResponseAuthenticateBean authenticate;

	public ResponseAuthenticateBean getAuthenticate() {
		return authenticate;
	}
	public void setAuthenticate(ResponseAuthenticateBean authenticate) {
		this.authenticate = authenticate;
	}

}
