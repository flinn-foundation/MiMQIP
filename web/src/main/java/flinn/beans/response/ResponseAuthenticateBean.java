package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "authenticate")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseAuthenticateBean extends AbstractDataBean {
	@XmlElement(name = "authcode")
	protected String authcode;
	@XmlElement(name = "message")
	protected String message;
	
	public ResponseAuthenticateBean() {
		super();
	}

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
