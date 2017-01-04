package flinn.beans.request;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "action")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestActionBean extends AbstractDataBean {

	@XmlElement(name = "type")
	protected String type;
	@XmlElement(name = "command")
	protected String command;
	@XmlElement(name = "token")
	protected String token;
	@XmlElement(name = "authcode")
	protected String authcode;
	@XmlElement(name = "messageid")
	protected String messageid;
	@XmlElement(name = "correlationid")
	protected String correlationid;
	@XmlElement(name = "timestamp")
	protected BigDecimal timestamp;
	
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAuthcode() {
		return authcode;
	}
	public void setAuthcode(String authcode) {
		this.authcode = authcode;
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
	public RequestActionBean() {
		super();
	}

}
