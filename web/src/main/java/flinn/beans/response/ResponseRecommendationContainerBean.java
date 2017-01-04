package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseRecommendationContainerBean extends ResponseContainerBean {
	@XmlElement(name = "recommendation")
	protected ResponseRecommendationBean recommendation;

	public ResponseRecommendationBean getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(ResponseRecommendationBean recommendation) {
		this.recommendation = recommendation;
	}
}
