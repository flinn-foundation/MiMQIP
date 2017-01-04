package flinn.beans.response;

import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.request.RequestActionBean;

@XmlRootElement(name = "action")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseActionBean extends AbstractDataBean {

	@XmlElement(name = "type")
	protected String type;
	@XmlElement(name = "command")
	protected String command;
	@XmlElement(name = "url")
	protected String url;
	@XmlElement(name = "messageid")
	protected String messageid;
	@XmlElement(name = "correlationid")
	protected String correlationid;
	@XmlElement(name = "timestamp")
	protected BigDecimal timestamp;
	
	public ResponseActionBean() {
		super();
	}
	public ResponseActionBean (RequestActionBean reqAction, HttpServletRequest req) {
		setType(reqAction.getType());
		setCommand(reqAction.getCommand());
		setMessageid(reqAction.getMessageid());
		setCorrelationid(reqAction.getCorrelationid());
		setTimestamp(new BigDecimal(new java.util.Date().getTime()));
		setUrl(req.getRequestURL()+"?"+req.getQueryString());
	}

	public ResponseActionBean (RequestActionBean reqAction) {
		setType(reqAction.getType());
		setCommand(reqAction.getCommand());
		setMessageid(reqAction.getMessageid());
		setCorrelationid(reqAction.getCorrelationid());
		setTimestamp(new BigDecimal(new java.util.Date().getTime()));
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMessageid() {
		return messageid;
	}
	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}
	public String getCorrelationid() {
		return correlationid;
	}
	public void setCorrelationid(String correlationid) {
		this.correlationid = correlationid;
	}
	public BigDecimal getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(BigDecimal timestamp) {
		this.timestamp = timestamp;
	}

}
