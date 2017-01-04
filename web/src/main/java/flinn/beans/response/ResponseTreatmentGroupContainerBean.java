package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.TreatmentGroupBean;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseTreatmentGroupContainerBean extends ResponseContainerBean {
	@XmlElement(name = "group")
	protected TreatmentGroupBean[] group;

	public TreatmentGroupBean[] getGroups() {
		return group;
	}
	public void setGroups(TreatmentGroupBean[] group) {
		this.group = group;
	}
}
