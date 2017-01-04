package flinn.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.response.ResponseAppUserBean;
import flinn.beans.response.ResponseFacilityBean;

@XmlRootElement(name = "authcode")
@XmlAccessorType(XmlAccessType.FIELD)

public class AuthcodeBean extends AbstractDataBean {

	@XmlElement(name = "authcode")
	protected String authcode = null;
	@XmlElement(name = "address")
	protected String address = null;
	@XmlElement(name = "useragent")
	protected String useragent = null;
	@XmlElement(name = "expireTime")
	protected long expireTime;
	@XmlElement(name = "user")
	protected ResponseAppUserBean user;
	@XmlElement(name = "facility")
	protected ResponseFacilityBean facility;

	public String getAuthcode() {
		return authcode;
	}
	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUseragent() {
		return useragent;
	}
	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	public ResponseAppUserBean getUser() {
		return user;
	}
	public void setUser(ResponseAppUserBean user) {
		this.user = user;
	}
	public ResponseFacilityBean getFacility() {
		return facility;
	}
	public void setFacility(ResponseFacilityBean facility) {
		this.facility = facility;
	}

	public AuthcodeBean clone() {
		AuthcodeBean b = new AuthcodeBean();
		b.setAddress(address);
		b.setAuthcode(authcode);
		b.setExpireTime(expireTime);
		b.setUseragent(useragent);
		b.setFacility(facility);
		b.setUser(user);
		return b;
	}
	
}
