//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.29 at 03:02:40 PM EDT 
//


package flinn.rcopia.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for RequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Command" type="{http://www.drfirst.com/}CommandType"/>
 *         &lt;element name="LastUpdateDate" type="{http://www.drfirst.com/}MDYDateType" minOccurs="0"/>
 *         &lt;element name="Patient" type="{http://www.drfirst.com/}RcopiaOrExternalID" minOccurs="0"/>
 *         &lt;element name="PatientList" type="{http://www.drfirst.com/}PatientListTypeReq" minOccurs="0"/>
 *         &lt;element name="Provider" type="{http://www.drfirst.com/}ProviderType" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;group ref="{http://www.drfirst.com/}SendPatientRequest"/>
 *           &lt;group ref="{http://www.drfirst.com/}GetNotificationCountRequest"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestType", propOrder = {
    "command",
    "lastUpdateDate",
    "patient",
    "patientList",
    "provider",
    "synchronous",
    "checkEligibility",
    "type",
    "returnPrescriptionIDs"
})
public class RequestType {

    @XmlElement(name = "Command", required = true)
    protected CommandType command;
    @XmlElement(name = "LastUpdateDate")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String lastUpdateDate;
    @XmlElement(name = "Patient")
    protected RcopiaOrExternalID patient;
    @XmlElement(name = "PatientList")
    protected PatientListTypeReq patientList;
    @XmlElement(name = "Provider")
    protected ProviderType provider;
    @XmlElement(name = "Synchronous")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String synchronous;
    @XmlElement(name = "CheckEligibility")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String checkEligibility;
    @XmlElement(name = "Type")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;
    @XmlElement(name = "ReturnPrescriptionIDs")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String returnPrescriptionIDs;

    /**
     * Gets the value of the command property.
     * 
     * @return
     *     possible object is
     *     {@link CommandType }
     *     
     */
    public CommandType getCommand() {
        return command;
    }

    /**
     * Sets the value of the command property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommandType }
     *     
     */
    public void setCommand(CommandType value) {
        this.command = value;
    }

    /**
     * Gets the value of the lastUpdateDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Sets the value of the lastUpdateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastUpdateDate(String value) {
        this.lastUpdateDate = value;
    }

    /**
     * Gets the value of the patient property.
     * 
     * @return
     *     possible object is
     *     {@link RcopiaOrExternalID }
     *     
     */
    public RcopiaOrExternalID getPatient() {
        return patient;
    }

    /**
     * Sets the value of the patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link RcopiaOrExternalID }
     *     
     */
    public void setPatient(RcopiaOrExternalID value) {
        this.patient = value;
    }

    /**
     * Gets the value of the patientList property.
     * 
     * @return
     *     possible object is
     *     {@link PatientListTypeReq }
     *     
     */
    public PatientListTypeReq getPatientList() {
        return patientList;
    }

    /**
     * Sets the value of the patientList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PatientListTypeReq }
     *     
     */
    public void setPatientList(PatientListTypeReq value) {
        this.patientList = value;
    }

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link ProviderType }
     *     
     */
    public ProviderType getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProviderType }
     *     
     */
    public void setProvider(ProviderType value) {
        this.provider = value;
    }

    /**
     * Gets the value of the synchronous property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSynchronous() {
        return synchronous;
    }

    /**
     * Sets the value of the synchronous property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSynchronous(String value) {
        this.synchronous = value;
    }

    /**
     * Gets the value of the checkEligibility property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckEligibility() {
        return checkEligibility;
    }

    /**
     * Sets the value of the checkEligibility property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckEligibility(String value) {
        this.checkEligibility = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the returnPrescriptionIDs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnPrescriptionIDs() {
        return returnPrescriptionIDs;
    }

    /**
     * Sets the value of the returnPrescriptionIDs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnPrescriptionIDs(String value) {
        this.returnPrescriptionIDs = value;
    }

}