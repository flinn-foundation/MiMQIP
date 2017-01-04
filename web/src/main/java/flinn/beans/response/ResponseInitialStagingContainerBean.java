package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseInitialStagingContainerBean extends ResponseContainerBean {
	@XmlElement(name = "initialstage")
	protected ResponseInitialStagingBean initialstage;

	public ResponseInitialStagingBean getInitialstage() {
		return initialstage;
	}

	public void setInitialstage(ResponseInitialStagingBean initialstage) {
		this.initialstage = initialstage;
	}
}
