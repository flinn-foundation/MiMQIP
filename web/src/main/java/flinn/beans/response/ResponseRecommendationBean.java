package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "recommendation")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseRecommendationBean extends AbstractDataBean {
	@XmlElement(name = "consistent")
	protected String consistent;
	@XmlElement(name = "status")
	protected String[] status;
	@XmlElement(name = "medications")
	protected String[] medications;
	@XmlElement(name = "generalconsistency")
	protected String[] generalconsistency;
	@XmlElement(name = "additionalconsistency")
	protected String[] additionalconsistency;
	@XmlElement(name = "generalmessages")
	protected String[] generalmessages;
	@XmlElement(name = "clinicalresponse")
	protected String[] clinicalresponse;
	@XmlElement(name = "medicaltrial")
	protected String[] medicaltrial;
	@XmlElement(name = "treatmentmessages")
	protected String[] treatmentmessages;
	@XmlElement(name = "specialmessages")
	protected String[] specialmessages;
	@XmlElement(name = "othermessages")
	protected String[][] othermessages;
	@XmlElement(name = "guidelinechart")
	protected ResponseGuidelineChart guidelinechart;
	
	public ResponseRecommendationBean() {
		super();
	}

	public String getConsistent() {
		return consistent;
	}

	public void setConsistent(String consistent) {
		this.consistent = consistent;
	}

	public String[] getStatus() {
		return status;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

	public String[] getMedications() {
		return medications;
	}

	public void setMedications(String[] medications) {
		this.medications = medications;
	}

	public String[] getGeneralconsistency() {
		return generalconsistency;
	}

	public void setGeneralconsistency(String[] generalconsistency) {
		this.generalconsistency = generalconsistency;
	}

	public String[] getAdditionalconsistency() {
		return additionalconsistency;
	}

	public void setAdditionalconsistency(String[] additionalconsistency) {
		this.additionalconsistency = additionalconsistency;
	}

	public String[] getGeneralmessages() {
		return generalmessages;
	}

	public void setGeneralmessages(String[] generalmessages) {
		this.generalmessages = generalmessages;
	}

	public String[] getClinicalresponse() {
		return clinicalresponse;
	}

	public void setClinicalresponse(String[] clinicalresponse) {
		this.clinicalresponse = clinicalresponse;
	}

	public String[] getMedicaltrial() {
		return medicaltrial;
	}

	public void setMedicaltrial(String[] medicaltrial) {
		this.medicaltrial = medicaltrial;
	}

	public String[] getTreatmentmessages() {
		return treatmentmessages;
	}

	public void setTreatmentmessages(String[] treatmentmessages) {
		this.treatmentmessages = treatmentmessages;
	}

	public String[] getSpecialmessages() {
		return specialmessages;
	}

	public void setSpecialmessages(String[] specialmessages) {
		this.specialmessages = specialmessages;
	}

	public String[][] getOthermessages() {
		return othermessages;
	}

	public void setOthermessages(String[][] othermessages) {
		this.othermessages = othermessages;
	}

	public ResponseGuidelineChart getGuidelinechart() {
		return guidelinechart;
	}

	public void setGuidelinechart(ResponseGuidelineChart guidelinechart) {
		this.guidelinechart = guidelinechart;
	}

	
}
