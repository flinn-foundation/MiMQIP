package flinn.recommend.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.recommend.beans.RecommendMessageBean;
import flinn.recommend.beans.request.RequestMessageBean;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseMessageBean extends AbstractDataBean{
	@XmlElement(name = "messageid")
	protected int messageid;
	@XmlElement(name = "priority")
	protected int priority;
	@XmlElement(name = "messagetag")
	protected String messagetag;
	@XmlElement(name = "message")
	protected String message;
	
	public ResponseMessageBean() {
		super();
	}

	public ResponseMessageBean (RequestMessageBean reqMessage) {
		setMessageid(reqMessage.getMessageid());
		setPriority(reqMessage.getPriority());
		setMessagetag(reqMessage.getMessagetag());
		setMessage(reqMessage.getMessage());
	}

	public RecommendMessageBean getRecommendMessageBean () {
		RecommendMessageBean mb = new RecommendMessageBean();
		mb.setMessageid(this.getMessageid());
		mb.setPriority(this.getPriority());
		mb.setMessagetag(this.getMessagetag());
		mb.setMessage(this.getMessage());
		return mb;
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