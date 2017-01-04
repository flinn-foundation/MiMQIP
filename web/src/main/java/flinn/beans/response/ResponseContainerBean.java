package flinn.beans.response;
 
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseContainerBean extends AbstractDataBean {
	@XmlElement(name = "action")
	protected ResponseActionBean action;
	
	public ResponseActionBean getAction() {
		return action;
	}
	public void setAction(ResponseActionBean action) {
		this.action = action;
	}

}
