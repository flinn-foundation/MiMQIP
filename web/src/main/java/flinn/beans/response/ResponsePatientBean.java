package flinn.beans.response;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.PatientDetailsBean;
import flinn.beans.PatientStatusBean;
import flinn.beans.request.RequestPatientBean;

@XmlRootElement(name = "patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponsePatientBean extends AbstractDataBean
{

	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "facilityid")
	protected int facilityid;
	@XmlElement(name = "valid")
	protected Boolean valid;
	@XmlElement(name = "StartDate")
	protected String startdate;
	@XmlElement(name = "rcopiaLastUpdatedDate")
	protected Date rcopiaLastUpdatedDate;
	@XmlElement(name = "details")
	protected HashMap<String, PatientDetailsBean []> details;
	@XmlElement(name = "status")
	protected HashMap<String, PatientStatusBean []> status;

	public ResponsePatientBean()
	{
		super();
	}

	public ResponsePatientBean(RequestPatientBean bean)
	{
		setPatientid(bean.getPatientid());
		setFacilityid(bean.getFacilityid());
		setValid(bean.getValid());
		setStartdate(bean.getStartdate());
	}

	public ResponsePatientBean(ResponsePatientBean bean, String statusPrefix)
	{
		setPatientid(bean.getPatientid());
		setFacilityid(bean.getFacilityid());
		setValid(bean.getValid());
		setStartdate(bean.getStartdate());
		setDetails(null);
		setStatus(bean.getStatus(), statusPrefix);
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

	public Date getRcopiaLastUpdatedDate()
	{
		return rcopiaLastUpdatedDate;
	}

	public void setRcopiaLastUpdatedDate(Date rcopiaLastUpdatedDate)
	{
		this.rcopiaLastUpdatedDate = rcopiaLastUpdatedDate;
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

	private void setStatus(HashMap<String, PatientStatusBean []> sourceStatus, String statusPrefix)
	{
		if (statusPrefix != null && !statusPrefix.equals(""))
		{
			String [] statuses = statusPrefix.split(",");
			this.status = new HashMap<String, PatientStatusBean []>();
			if (sourceStatus != null)
			{
				Iterator<String> it = sourceStatus.keySet().iterator();
				while (it.hasNext())
				{
					String key = it.next();
					for (int i = 0; i < statuses.length; i++)
					{
						if (key.startsWith(statuses[i]))
						{
							PatientStatusBean [] value = sourceStatus.get(key);
							this.status.put(key, value);
						}
					}
				}
			}
		}
		else
		{
			this.status = null;
		}
	}
}
