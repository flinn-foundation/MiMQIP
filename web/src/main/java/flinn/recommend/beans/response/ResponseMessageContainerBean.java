package flinn.recommend.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.response.ResponseContainerBean;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseMessageContainerBean extends ResponseContainerBean {
	@XmlElement(name = "message")
	protected ResponseMessageBean message;

	public ResponseMessageBean getMessage() {
		return message;
	}

	public void setMessage(ResponseMessageBean bean) {
		this.message = bean;
	}
}
