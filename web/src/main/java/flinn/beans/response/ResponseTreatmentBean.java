package flinn.beans.response;

import java.util.HashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.TreatmentGroupBean;

@XmlRootElement(name = "treatment")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseTreatmentBean extends AbstractDataBean
{

	@XmlElement(name = "treatmentid")
	protected int treatmentid;
	@XmlElement(name = "treatmentgroupid")
	protected int treatmentgroupid;
	@XmlElement(name = "treatmentname")
	protected String treatmentname;
	@XmlElement(name = "treatmentabbreviation")
	protected String treatmentabbreviation;
	@XmlElement(name = "drfgenericdrugname")
	protected String drfGenericDrugName;
	@XmlElement(name = "valid")
	protected Boolean valid;

	protected TreatmentGroupBean group;

	protected HashMap<String, String> details;

	public int getTreatmentid()
	{
		return treatmentid;
	}

	public void setTreatmentid(int treatmentid)
	{
		this.treatmentid = treatmentid;
	}

	public int getTreatmentgroupid()
	{
		return treatmentgroupid;
	}

	public void setTreatmentgroupid(int treatmentgroupid)
	{
		this.treatmentgroupid = treatmentgroupid;
	}

	public String getTreatmentname()
	{
		return treatmentname;
	}

	public void setTreatmentname(String treatmentname)
	{
		this.treatmentname = treatmentname;
	}

	public String getTreatmentabbreviation()
	{
		return treatmentabbreviation;
	}

	public void setTreatmentabbreviation(String treatmentabbreviation)
	{
		this.treatmentabbreviation = treatmentabbreviation;
	}

	public Boolean getValid()
	{
		return valid;
	}

	public void setValid(Boolean valid)
	{
		this.valid = valid;
	}

	public TreatmentGroupBean getGroup()
	{
		return group;
	}

	public void setGroup(TreatmentGroupBean group)
	{
		this.group = group;
	}

	public String getDrfGenericDrugName()
	{
		return drfGenericDrugName;
	}

	public void setDrfGenericDrugName(String drfGenericDrugName)
	{
		this.drfGenericDrugName = drfGenericDrugName;
	}

	public HashMap<String, String> getDetails()
	{
		return details;
	}

	public void setDetails(HashMap<String, String> details)
	{
		this.details = details;
	}
}
