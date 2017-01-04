package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "initialstage")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseInitialStagingBean extends AbstractDataBean {
	@XmlElement(name = "stage")
	protected String stage;
	@XmlElement(name = "message")
	protected String message;
	
	public ResponseInitialStagingBean() {
		super();
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
