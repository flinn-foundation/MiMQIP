package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseGuidelineReasonContainerBean extends ResponseContainerBean {
	@XmlElement(name = "guidelinereason")
	protected ResponseGuidelineReasonBean guidelinereason;

	public ResponseGuidelineReasonBean getGuidelinereason() {
		return guidelinereason;
	}
	public void setGuidelinereason(ResponseGuidelineReasonBean guidelinereason) {
		this.guidelinereason = guidelinereason;
	}
}
