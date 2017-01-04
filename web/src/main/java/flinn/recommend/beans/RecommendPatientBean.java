package flinn.recommend.beans;

import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.PatientDetailsBean;
import flinn.beans.PatientStatusBean;

@XmlRootElement(name = "patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecommendPatientBean extends AbstractDataBean
{

	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "facilityid")
	protected int facilityid;
	@XmlElement(name = "valid")
	protected Boolean valid;
	@XmlElement(name = "StartDate")
	protected String startdate;
	@XmlElement(name = "details")
	protected HashMap<String, PatientDetailsBean []> details;
	@XmlElement(name = "status")
	protected HashMap<String, PatientStatusBean []> status;
	@XmlElement(name = "properties")
	protected HashMap<String, String> properties;

	public RecommendPatientBean()
	{
		super();
	}

	public RecommendPatientBean(RecommendPatientBean bean, String statusPrefix)
	{
		setPatientid(bean.getPatientid());
		setFacilityid(bean.getFacilityid());
		setValid(bean.getValid());
		setStartdate(bean.getStartdate());
		setDetails(null);
		setStatus(bean.getStatus());
		setInitialProperties();
	}

	private void setInitialProperties() {
		properties = new HashMap<String,String>();
		if (details != null) {
			Iterator<String> it = details.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				String value = details.get(key)[0].getValue();
				this.properties.put(key, value);
			}
		}
		if (status != null) {
			Iterator<String> it = status.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				String value = status.get(key)[0].getValue();
				this.properties.put(key, value);
			}
		}
		// TODO Put in additional default properties (dates, older status, time deltas, etc.)
	}

	public int getPatientid()
	{
		return patientid;
	}

	public void setPatientid(int patientid)
	{
		this.patientid = patientid;
	}

	public int getFacilityid()
	{
		return facilityid;
	}

	public void setFacilityid(int facilityid)
	{
		this.facilityid = facilityid;
	}

	public Boolean getValid()
	{
		return valid;
	}

	public void setValid(Boolean valid)
	{
		this.valid = valid;
	}

	public String getStartdate()
	{
		return startdate;
	}

	public void setStartdate(String startdate)
	{
		this.startdate = startdate;
	}

	public HashMap<String, PatientDetailsBean []> getDetails()
	{
		return details;
	}

	public void setDetails(HashMap<String, PatientDetailsBean []> details)
	{
		this.details = details;
	}

	public HashMap<String, PatientStatusBean []> getStatus()
	{
		return status;
	}

	public void setStatus(HashMap<String, PatientStatusBean []> status)
	{
		this.status = status;
	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}
}
