package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.beans.request.RequestPrescriptionBean;

@XmlRootElement(name = "prescription")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponsePrescriptionBean extends AbstractDataBean
{

	@XmlElement(name = "prescriptionid")
	protected int prescriptionid;
	@XmlElement(name = "treatmentid")
	protected int treatmentid;
	@XmlElement(name = "patientid")
	protected int patientid;
	@XmlElement(name = "rcopiaid")
	protected int rcopiaid;
	@XmlElement(name = "discontinue")
	protected Boolean discontinue;
	@XmlElement(name = "entrydate")
	protected String entrydate;
	@XmlElement(name = "dailydose")
	protected String dailydose;
	@XmlElement(name = "doctorname")
	protected String doctorname;
	@XmlElement(name = "duration")
	protected int duration;
	protected ResponseTreatmentBean treatment;

	public ResponsePrescriptionBean(RequestPrescriptionBean reqPrescription)
	{
		setPrescriptionid(reqPrescription.getPrescriptionid());
		setTreatmentid(reqPrescription.getTreatmentid());
		setPatientid(reqPrescription.getPatientid());
		setRcopiaid(reqPrescription.getRcopiaid());
		setEntrydate(reqPrescription.getEntrydate());
		setDiscontinue(reqPrescription.getDiscontinue());
		setDailydose(reqPrescription.getDailydose());
		setDoctorname(reqPrescription.getDoctorname());
		setDuration(reqPrescription.getDuration());
		setTreatment(reqPrescription.getTreatment());
	}

	public ResponsePrescriptionBean(ResponsePrescriptionBean reqPrescription)
	{
		setPrescriptionid(reqPrescription.getPrescriptionid());
		setTreatmentid(reqPrescription.getTreatmentid());
		setPatientid(reqPrescription.getPatientid());
		setRcopiaid(reqPrescription.getRcopiaid());
		setEntrydate(reqPrescription.getEntrydate());
		setDiscontinue(reqPrescription.getDiscontinue());
		setDailydose(reqPrescription.getDailydose());
		setDoctorname(reqPrescription.getDoctorname());
		setDuration(reqPrescription.getDuration());
		setTreatment(reqPrescription.getTreatment());
	}

	public int getPrescriptionid()
	{
		return prescriptionid;
	}

	public void setPrescriptionid(int prescriptionid)
	{
		this.prescriptionid = prescriptionid;
	}

	public int getTreatmentid()
	{
		return treatmentid;
	}

	public void setTreatmentid(int treatmentid)
	{
		this.treatmentid = treatmentid;
	}

	public int getPatientid()
	{
		return patientid;
	}

	public void setPatientid(int patientid)
	{
		this.patientid = patientid;
	}

	public int getRcopiaid()
	{
		return rcopiaid;
	}

	public void setRcopiaid(int rcopiaid)
	{
		this.rcopiaid = rcopiaid;
	}

	public Boolean getDiscontinue()
	{
		return discontinue;
	}

	public void setDiscontinue(Boolean discontinue)
	{
		this.discontinue = discontinue;
	}

	public String getEntrydate()
	{
		return entrydate;
	}

	public void setEntrydate(String entrydate)
	{
		this.entrydate = entrydate;
	}

	public String getDailydose()
	{
		return dailydose;
	}

	public void setDailydose(String dailydose)
	{
		this.dailydose = dailydose;
	}

	public String getDoctorname()
	{
		return doctorname;
	}

	public void setDoctorname(String doctorname)
	{
		this.doctorname = doctorname;
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}

	public ResponseTreatmentBean getTreatment()
	{
		return treatment;
	}

	public void setTreatment(ResponseTreatmentBean treatment)
	{
		this.treatment = treatment;
	}

	public ResponsePrescriptionBean()
	{
		super();
	}
}
