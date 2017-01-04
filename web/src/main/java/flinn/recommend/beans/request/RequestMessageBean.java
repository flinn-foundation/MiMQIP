package flinn.recommend.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.recommend.beans.response.ResponseMessageBean;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestMessageBean  extends AbstractDataBean {
	@XmlElement(name = "messageid")
	protected int messageid;
	@XmlElement(name = "priority")
	protected int priority;
	@XmlElement(name = "messagetag")
	protected String messagetag;
	@XmlElement(name = "message")
	protected String message;

	public RequestMessageBean() {
		super();
	}
	
	public RequestMessageBean(ResponseMessageBean bean) {
		setPriority(bean.getPriority());
		setMessagetag(bean.getMessagetag());
		setMessage(bean.getMessage());
	}

	public int getMessageid() {
		return messageid;
	}

	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getMessagetag() {
		return messagetag;
	}

	public void setMessagetag(String messagetag) {
		this.messagetag = messagetag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
