package flinn.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.response.ResponseAuthenticateBean;

@XmlRootElement(name = "authenticate")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestAuthenticateBean extends AbstractDataBean  {
	
	@XmlElement(name = "login")
	protected String login;
	@XmlElement(name = "password")
	protected String password;
	@XmlElement(name = "facilityid")
	protected int facilityid;		
	
	public RequestAuthenticateBean(ResponseAuthenticateBean bean) {
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getFacilityid() {
		return facilityid;
	}
	public void setFacilityid(int facilityid) {
		this.facilityid = facilityid;
	}		

	public RequestAuthenticateBean() {
		super();
	}
}
