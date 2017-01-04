//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.29 at 03:02:40 PM EDT 
//


package flinn.rcopia.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PatientListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PatientListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumberOfPatients" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="Patient" type="{http://www.drfirst.com/}PatientType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PatientListType", propOrder = {
    "numberOfPatients",
    "patient"
})
public class PatientListType {

    @XmlElement(name = "NumberOfPatients")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfPatients;
    @XmlElement(name = "Patient")
    protected List<PatientType> patient;

    /**
     * Gets the value of the numberOfPatients property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfPatients() {
        return numberOfPatients;
    }

    /**
     * Sets the value of the numberOfPatients property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfPatients(BigInteger value) {
        this.numberOfPatients = value;
    }

    /**
     * Gets the value of the patient property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the patient property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPatient().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PatientType }
     * 
     * 
     */
    public List<PatientType> getPatient() {
        if (patient == null) {
            patient = new ArrayList<PatientType>();
        }
        return this.patient;
    }

}