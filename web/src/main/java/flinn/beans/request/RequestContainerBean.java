package flinn.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.recommend.beans.request.RequestMessageBean;
import flinn.recommend.beans.request.RequestRuleBean;
import flinn.recommend.beans.request.RequestSettingBean;

@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestContainerBean extends AbstractDataBean {
	@XmlElement(name = "action")
	protected RequestActionBean action;
	@XmlElement(name = "user")
	protected RequestAppUserBean user;
	@XmlElement(name = "facility")
	protected RequestFacilityBean facility;
	@XmlElement(name = "patientsearch")
	protected RequestPatientSearchBean patientsearch;
	@XmlElement(name = "patient")
	protected RequestPatientBean patient;
	@XmlElement(name = "progressnote")
	protected RequestProgressNoteBean progressnote;
	@XmlElement(name = "progressnotesearch")
	protected RequestProgressNoteSearchBean progressnotesearch;
	@XmlElement(name = "lab")
	protected RequestLabBean lab;
	@XmlElement(name = "labsearch")
	protected RequestLabSearchBean labsearch;
	@XmlElement(name = "prescription")
	protected RequestPrescriptionBean prescription;
	@XmlElement(name = "prescriptions")
	protected RequestPrescriptionBean[] prescriptions;
	@XmlElement(name = "prescriptionsearch")
	protected RequestPrescriptionSearchBean prescriptionsearch;		
	@XmlElement(name = "treatment")
	protected RequestTreatmentBean treatment;	
	@XmlElement(name = "authenticate")
	protected RequestAuthenticateBean authenticate;
	@XmlElement(name = "initialstaging")
	protected RequestInitialStagingBean initialstaging;
	@XmlElement(name = "guidelinereason")
	protected RequestGuidelineReasonBean guidelinereason;

	// Recommend objects
	
	@XmlElement(name = "message")
	protected RequestMessageBean message;
	@XmlElement(name = "rule")
	protected RequestRuleBean rule;
	@XmlElement(name = "setting")
	protected RequestSettingBean setting;
	
	public RequestActionBean getAction() {
		return action;
	}
	public void setAction(RequestActionBean action) {
		this.action = action;
	}	
	public RequestAppUserBean getUser() {
		return user;
	}
	public void setUser(RequestAppUserBean user) {
		this.user = user;
	}
	public RequestPatientSearchBean getPatientsearch() {
		return patientsearch;
	}
	public void setPatientsearch(RequestPatientSearchBean patientsearch) {
		this.patientsearch = patientsearch;
	}
	public RequestPatientBean getPatient() {
		return patient;
	}
	public void setPatient(RequestPatientBean patient) {
		this.patient = patient;
	}		
	public RequestFacilityBean getFacility() {
		return facility;
	}
	public void setFacility(RequestFacilityBean facility) {
		this.facility = facility;
	}
	public RequestProgressNoteBean getProgressnote() {
		return progressnote;
	}
	public void setProgressnote(RequestProgressNoteBean progressnote) {
		this.progressnote = progressnote;
	}
	public RequestProgressNoteSearchBean getProgressnotesearch() {
		return progressnotesearch;
	}
	public void setProgressnotesearch(
			RequestProgressNoteSearchBean progressnotesearch) {
		this.progressnotesearch = progressnotesearch;
	}	
	public RequestLabBean getLab() {
		return lab;
	}
	public void setLab(RequestLabBean lab) {
		this.lab = lab;
	}
	public RequestLabSearchBean getLabsearch() {
		return labsearch;
	}
	public void setLabsearch(RequestLabSearchBean labsearch) {
		this.labsearch = labsearch;
	}
	public RequestPrescriptionBean getPrescription() {
		return prescription;
	}
	public void setPrescription(RequestPrescriptionBean prescription) {
		this.prescription = prescription;
	}
	public RequestPrescriptionBean[] getPrescriptions() {
		return prescriptions;
	}
	public void setPrescriptions(RequestPrescriptionBean[] prescriptions) {
		this.prescriptions = prescriptions;
	}
	public RequestPrescriptionSearchBean getPrescriptionsearch() {
		return prescriptionsearch;
	}
	public void setPrescriptionsearch(
			RequestPrescriptionSearchBean prescriptionsearch) {
		this.prescriptionsearch = prescriptionsearch;
	}		
	public RequestTreatmentBean getTreatment() {
		return treatment;
	}
	public void setTreatment(RequestTreatmentBean treatment) {
		this.treatment = treatment;
	}	
	public RequestAuthenticateBean getAuthenticate() {
		return authenticate;
	}
	public void setAuthenticate(RequestAuthenticateBean authenticate) {
		this.authenticate = authenticate;
	}
	public RequestInitialStagingBean getInitialstaging() {
		return initialstaging;
	}
	public void setInitialstaging(RequestInitialStagingBean initialstaging) {
		this.initialstaging = initialstaging;
	}
	public RequestGuidelineReasonBean getGuidelinereason() {
		return guidelinereason;
	}
	public void setGuidelinereason(RequestGuidelineReasonBean guidelinereason) {
		this.guidelinereason = guidelinereason;
	}

	public RequestContainerBean() {
		super();
	}

	// Recommend Objects
	
	public RequestMessageBean getMessage() {
		return message;
	}
	public void setMessage(RequestMessageBean message) {
		this.message = message;
	}
	public RequestRuleBean getRule() {
		return rule;
	}
	public void setRule(RequestRuleBean rule) {
		this.rule = rule;
	}
	public RequestSettingBean getSetting() {
		return setting;
	}
	public void setSetting(RequestSettingBean setting) {
		this.setting = setting;
	}

}
