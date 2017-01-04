package flinn.recommend.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.response.ResponseContainerBean;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseRuleContainerBean extends ResponseContainerBean {
	@XmlElement(name = "rule")
	protected ResponseRuleBean rule;

	public ResponseRuleBean getRule() {
		return rule;
	}

	public void setRule(ResponseRuleBean rule) {
		this.rule = rule;
	}
}
