package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseLabContainerBean extends ResponseContainerBean {
	@XmlElement(name = "lab")
	protected ResponseLabBean lab;

	public ResponseLabBean getLab() {
		return lab;
	}

	public void setLab(ResponseLabBean lab) {
		this.lab = lab;
	}

}
