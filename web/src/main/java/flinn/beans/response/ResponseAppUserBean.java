package flinn.beans.response;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.AppUserRoleBean;
import flinn.beans.request.RequestAppUserBean;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseAppUserBean extends AbstractDataBean {
	@XmlElement(name = "appuserid")
	protected int appuserid;
	@XmlElement(name = "facilityid")
	protected int facilityid;
	@XmlElement(name = "login")
	protected String login;
	@XmlElement(name = "valid")
	protected Boolean valid;	
	@XmlElement(name = "launch")
	protected String launch;
	@XmlElement(name = "expiration")
	protected String expiration;
	@XmlElement(name = "lastloggedin")
	protected String lastloggedin;
	@XmlElement(name = "lastactivity")
	protected String lastactivity;
	@XmlElement(name = "lastremoteaddress")
	protected String lastremoteaddress;
	@XmlElement(name = "roles")
	protected AppUserRoleBean[] roles;
	@XmlElement(name = "settings")
	protected HashMap<String, String> settings;	
	
	public ResponseAppUserBean() {
		super();
	}

	public ResponseAppUserBean (RequestAppUserBean reqAppUser) {
		setFacilityid(reqAppUser.getFacilityid());
		setLogin(reqAppUser.getLogin());
		setValid(reqAppUser.getValid());
		setLaunch(reqAppUser.getLaunch());
		setExpiration(reqAppUser.getExpiration());
		setLastloggedin(null);
		setLastactivity(null);
		setLastremoteaddress(null);
		setRoles(reqAppUser.getRoles());
		setSettings(reqAppUser.getSettings());
		setAppuserid(reqAppUser.getAppuserid());
	}

	public int getAppuserid() {
		return appuserid;
	}
	public void setAppuserid(int appuserid) {
		this.appuserid = appuserid;
	}	
	public int getFacilityid() {
		return facilityid;
	}
	public void setFacilityid(int facilityid) {
		this.facilityid = facilityid;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}	
	public String getLaunch() {
		return launch;
	}
	public void setLaunch(String launch) {
		this.launch = launch;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public String getLastloggedin() {
		return lastloggedin;
	}

	public void setLastloggedin(String lastloggedin) {
		this.lastloggedin = lastloggedin;
	}

	public String getLastactivity() {
		return lastactivity;
	}

	public void setLastactivity(String lastactivity) {
		this.lastactivity = lastactivity;
	}

	public String getLastremoteaddress() {
		return lastremoteaddress;
	}

	public void setLastremoteaddress(String lastremoteaddress) {
		this.lastremoteaddress = lastremoteaddress;
	}

	public AppUserRoleBean[] getRoles() {
		return roles;
	}
	public void setRoles(AppUserRoleBean[] roles) {
		this.roles = roles;
	}
	public HashMap<String, String> getSettings() {
		return settings;
	}
	public void setSettings(HashMap<String, String> settings) {
		this.settings = settings;
	}
}
