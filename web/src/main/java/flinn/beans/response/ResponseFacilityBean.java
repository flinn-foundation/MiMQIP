package flinn.beans.response;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.FacilityIPBean;
import flinn.beans.request.RequestFacilityBean;

@XmlRootElement(name = "facility")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseFacilityBean extends AbstractDataBean{
	@XmlElement(name = "facilityid")
	protected int facilityid;
	@XmlElement(name = "facilityname")
	protected String facilityname;
	@XmlElement(name = "facilityshortcut")
	protected String facilityshortcut;
	@XmlElement(name = "facilityemail")
	protected String facilityemail;
	@XmlElement(name = "administratorid")
	protected String administratorid;
	@XmlElement(name = "valid")
	protected Boolean valid;	
	@XmlElement(name = "launch")
	protected String launch;
	@XmlElement(name = "expiration")
	protected String expiration;
	@XmlElement(name = "ip")
	protected FacilityIPBean[] ip;
	@XmlElement(name = "settings")
	protected HashMap<String, String> settings;	
	
	public ResponseFacilityBean() {
		super();
	}

	public ResponseFacilityBean (RequestFacilityBean reqFacility) {
		setFacilityid(reqFacility.getFacilityid());
		setFacilityname(reqFacility.getFacilityname());
		setFacilityshortcut(reqFacility.getFacilityshortcut());
		setFacilityemail(reqFacility.getFacilityemail());
		setAdministratorid(reqFacility.getAdministratorid());		
		setValid(reqFacility.getValid());
		setLaunch(reqFacility.getLaunch());
		setExpiration(reqFacility.getExpiration());
		setSettings(reqFacility.getSettings());
		setIp(reqFacility.getIp());		
	}
	
	public int getFacilityid() {
		return facilityid;
	}
	public void setFacilityid(int facilityid) {
		this.facilityid = facilityid;
	}
	public String getFacilityname() {
		return facilityname;
	}
	public void setFacilityname(String facilityname) {
		this.facilityname = facilityname;
	}
	public String getFacilityshortcut() {
		return facilityshortcut;
	}
	public void setFacilityshortcut(String facilityshortcut) {
		this.facilityshortcut = facilityshortcut;
	}
	public String getFacilityemail() {
		return facilityemail;
	}
	public void setFacilityemail(String facilityemail) {
		this.facilityemail = facilityemail;
	}
	public String getAdministratorid() {
		return administratorid;
	}
	public void setAdministratorid(String administratorid) {
		this.administratorid = administratorid;
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
	public FacilityIPBean[] getIp() {
		return ip;
	}
	public void setIp(FacilityIPBean[] ip) {
		this.ip = ip;
	}
	public HashMap<String, String> getSettings() {
		return settings;
	}
	public void setSettings(HashMap<String, String> settings) {
		this.settings = settings;
	}
	
}