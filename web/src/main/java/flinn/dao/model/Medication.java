package flinn.dao.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import flinn.rcopia.model.MedicationType;
import flinn.rcopia.service.DoseTimingUtils;
import flinn.util.DateString;
import flinn.util.NumberUtils;

public class Medication
{

	public Long medicationId;

	public Long rcopiaId;
	public String primaryFlag = "Y";
	public Date lastModifiedDate;

	public String deleted = "N";

	public Long patientId;
	public String providerId;
	public String preparerId;

	public String brandName;
	public String genericName;
	public String drugRoute;
	public String drugForm;
	public String drugStrength;
	public String action;
	public String doseAmount;
	public String doseUnit;
	public String doseRoute;
	public String doseTiming;
	public String doseOther;
	public String refills;
	public BigInteger duration;
	public BigDecimal quantity;
	public String quantityUnit;

	public Date startDate;
	public Date stopDate;

	public Medication()
	{
		super();
	}

	public Medication(Map<String, Object> row)
	{
		setMedicationId((Long) row.get("MEDICATIONID"));
		setRcopiaId((Long) row.get("RCOPIAID"));
		setPrimaryFlag((String) row.get("PRIMARYFLAG"));
		setLastModifiedDate((Date) row.get("LASTMODIFIEDDATE"));
		setDeleted((String) row.get("DELETED"));
		setPatientId((Long) row.get("PATIENTID"));
		setProviderId((String) row.get("PROVIDERID"));
		setPreparerId((String) row.get("PREPARERID"));
		setBrandName((String) row.get("BRANDNAME"));
		setGenericName((String) row.get("GENERICNAME"));
		setDrugRoute((String) row.get("DRUGROUTE"));
		setDrugForm((String) row.get("DRUGFORM"));
		setDrugStrength((String) row.get("DRUGSTRENGTH"));
		setAction((String) row.get("ACTION"));
		setDoseAmount((String) row.get("DOSEAMOUNT"));
		setDoseUnit((String) row.get("DOSEUNIT"));
		setDoseRoute((String) row.get("DOSEROUTE"));
		setDoseTiming((String) row.get("DOSETIMING"));
		setDoseOther((String) row.get("DOSEOTHER"));
		setRefills((String) row.get("REFILLS"));
		try {
			setDuration((BigInteger) row.get("DURATION"));
		} catch (ClassCastException e) {
			setDuration(new BigInteger(""+0));
		}
		try {
			setQuantity((BigDecimal) row.get("QUANTITY"));
		} catch (ClassCastException e) {
			setQuantity(new BigDecimal(""+0));
		}
		setQuantityUnit((String) row.get("QUANTITYUNIT"));
		setStartDate((Date) row.get("STARTDATE"));
		setStopDate((Date) row.get("STOPDATE"));
	}

	public Medication(MedicationType medicationType)
	{
		setMedicationId(null);
		setRcopiaId(new Long(medicationType.getRcopiaID()));
		setLastModifiedDate(DateString.parseRcopiaDate(medicationType.getLastModifiedDate(), "MM/dd/yyyy HH:mm:ss zzz"));
		setDeleted(medicationType.getDeleted());
		setPatientId(new Long(medicationType.getPatient().getExternalID()));
		setProviderId(medicationType.getProvider().getExternalID());
		setPreparerId(medicationType.getPreparer().getExternalID());
		setBrandName(medicationType.getSig().getDrug().getBrandName());
		setGenericName(medicationType.getSig().getDrug().getGenericName());
		setDrugRoute(medicationType.getSig().getDrug().getRoute());
		setDrugForm(medicationType.getSig().getDrug().getForm());
		setDrugStrength(medicationType.getSig().getDrug().getStrength());
		setAction(medicationType.getSig().getAction());
		setDoseAmount(medicationType.getSig().getDose());
		setDoseUnit(medicationType.getSig().getDoseUnit());
		setDoseRoute(medicationType.getSig().getRoute());
		setDoseTiming(medicationType.getSig().getDoseTiming());
		setDoseOther(medicationType.getSig().getDoseOther());
		setRefills(medicationType.getSig().getRefills());
		setDuration(medicationType.getSig().getDuration());
		if (getDuration() == null) { 
			setDuration(new BigInteger(""+0));
		}
		setQuantity(medicationType.getSig().getQuantity());
		setQuantityUnit(medicationType.getSig().getQuantityUnit());
		setStartDate(DateString.parseRcopiaDate(medicationType.getStartDate(), "MM/dd/yyyy HH:mm:ss zzz"));
		setStopDate(DateString.parseRcopiaDate(medicationType.getStopDate(), "MM/dd/yyyy HH:mm:ss zzz"));
	}

	public Long getMedicationId()
	{
		return medicationId;
	}

	public void setMedicationId(Long medicationId)
	{
		this.medicationId = medicationId;
	}

	public Long getRcopiaId()
	{
		return rcopiaId;
	}

	public void setRcopiaId(Long rcopiaId)
	{
		this.rcopiaId = rcopiaId;
	}

	public String getPrimaryFlag()
	{
		return primaryFlag;
	}

	public void setPrimaryFlag(String primaryFlag)
	{
		this.primaryFlag = primaryFlag;
	}

	public Date getLastModifiedDate()
	{
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate)
	{
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}

	public Long getPatientId()
	{
		return patientId;
	}

	public void setPatientId(Long patientId)
	{
		this.patientId = patientId;
	}

	public String getProviderId()
	{
		return providerId;
	}

	public void setProviderId(String providerId)
	{
		this.providerId = providerId;
	}

	public String getPreparerId()
	{
		return preparerId;
	}

	public void setPreparerId(String preparerId)
	{
		this.preparerId = preparerId;
	}

	public String getBrandName()
	{
		return brandName;
	}

	public void setBrandName(String brandName)
	{
		this.brandName = brandName;
	}

	public String getGenericName()
	{
		return genericName;
	}

	public void setGenericName(String genericName)
	{
		this.genericName = genericName;
	}

	public String getDrugRoute()
	{
		return drugRoute;
	}

	public void setDrugRoute(String drugRoute)
	{
		this.drugRoute = drugRoute;
	}

	public String getDrugForm()
	{
		return drugForm;
	}

	public void setDrugForm(String drugForm)
	{
		this.drugForm = drugForm;
	}

	public String getDrugStrength()
	{
		return drugStrength;
	}

	public void setDrugStrength(String drugStrength)
	{
		this.drugStrength = drugStrength;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getDoseAmount()
	{
		return doseAmount;
	}

	public void setDoseAmount(String doseAmount)
	{
		this.doseAmount = doseAmount;
	}

	public String getDoseUnit()
	{
		return doseUnit;
	}

	public void setDoseUnit(String doseUnit)
	{
		this.doseUnit = doseUnit;
	}

	public String getDoseRoute()
	{
		return doseRoute;
	}

	public void setDoseRoute(String doseRoute)
	{
		this.doseRoute = doseRoute;
	}

	public String getDoseTiming()
	{
		return doseTiming;
	}

	public void setDoseTiming(String doseTiming)
	{
		this.doseTiming = doseTiming;
	}

	public String getDoseOther()
	{
		return doseOther;
	}

	public void setDoseOther(String doseOther)
	{
		this.doseOther = doseOther;
	}

	public String getRefills()
	{
		return refills;
	}

	public void setRefills(String refills)
	{
		this.refills = refills;
	}

	public BigInteger getDuration()
	{
		return duration;
	}

	public void setDuration(BigInteger duration)
	{
		this.duration = duration;
	}

	public BigDecimal getQuantity()
	{
		return quantity;
	}

	public void setQuantity(BigDecimal quantity)
	{
		this.quantity = quantity;
	}

	public String getQuantityUnit()
	{
		return quantityUnit;
	}

	public void setQuantityUnit(String quantityUnit)
	{
		this.quantityUnit = quantityUnit;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getStopDate()
	{
		return stopDate;
	}

	public void setStopDate(Date stopDate)
	{
		this.stopDate = stopDate;
	}

	private static String [] getColumnArray()
	{
		return new String[] { "MedicationId", "RcopiaId", "PrimaryFlag", "LastModifiedDate", "DeletedFlag", "PatientId", "ProviderId", "PreparerId", "BrandName", "GenericName", "DrugRoute", "DrugForm",
		    "DrugStrength", "Action", "DoseAmount", "DoseUnit", "DoseRoute", "DoseTiming", "DoseOther", "Refills", "Duration", "Quantity", "QuantityUnit", "StartDate", "Stopdate" };
	}

	private Object [] getValueArray()
	{
		return new Object[] { getMedicationId(), getRcopiaId(), getPrimaryFlag(), getLastModifiedDate(), getDeleted(), getPatientId(), getProviderId(), getPreparerId(), getBrandName(), getGenericName(),
		    getDrugRoute(), getDrugForm(), getDrugStrength(), getAction(), getDoseAmount(), getDoseUnit(), getDoseRoute(), getDoseTiming(), getDoseOther(), getRefills(), getDuration(), getQuantity(),
		    getQuantityUnit(), getStartDate(), getStopDate() };
	}

	public static List<String> getColumns()
	{
		List<String> columns = new ArrayList<String>();
		String [] columnArray = getColumnArray();
		for (int i = 0; i < columnArray.length; i++)
		{
			columns.add(columnArray[i]);
		}
		return columns;
	}

	public List<Object> getValues()
	{
		List<Object> values = new ArrayList<Object>();
		Object [] valueArray = getValueArray();
		for (int i = 0; i < valueArray.length; i++)
		{
			values.add(valueArray[i]);
		}
		return values;
	}

	public boolean isDeleted()
	{
		return (deleted != null && deleted.equalsIgnoreCase("Y")) ? true : false;
	}

	public boolean isDiscontinued()
	{
		return (stopDate != null && stopDate.before(new Date())) ? true : false;
	}

	public boolean isPrimary()
	{
		return (primaryFlag != null && primaryFlag.equalsIgnoreCase("Y")) ? true : false;
	}

	public double getDrugStrengthAsNumber() throws ParseException
	{
		return NumberUtils.parseNumber(getDrugStrength(), "####.## mg");
	}

	public double getDoseAmountAsNumber() throws ParseException
	{
		double doseAmt = NumberUtils.parseNumber(getDoseAmount());
		if (drugRoute != null && doseRoute != null && (doseRoute.contains("intramuscularly") || drugRoute.equalsIgnoreCase("im") || drugRoute.equalsIgnoreCase("inj"))) {
			doseAmt = -doseAmt;
		}
		return doseAmt;
	}

	public double calculateDailyDoseAmount() throws ParseException
	{
		int timesPerDay = DoseTimingUtils.getDose(doseTiming);
		double dailyDoseAmount = 0;
		if (doseUnit.equalsIgnoreCase("mg"))
		{
			/* Dose form is already in mg */
			/* Take 300mg twice a day */
			dailyDoseAmount = getDoseAmountAsNumber() * timesPerDay;
		}
		else if (doseUnit.equalsIgnoreCase("ml"))
		{
			/* Dose form is  in ml */
			/* have to do fancy manipulation to get mg from xx mg/yy ml dose unit */
			String[] ratio = drugStrength.split("\\/");
			if (ratio == null || ratio.length < 2) {
				dailyDoseAmount = getDoseAmountAsNumber() * timesPerDay;
			} else {
				double mgRatio = NumberUtils.parseNumber(ratio[0], "####.## mg");
				double mlRatio = 1.0;
				if (!ratio[1].equals("mL")) {
					mlRatio = NumberUtils.parseNumber(ratio[1], "####.## mL");
				}
				dailyDoseAmount = getDoseAmountAsNumber() * mgRatio / mlRatio * timesPerDay;
			}
		}
		else
		{
			/* Dose form is in tablets or capsules */
			/* Take 2 300mg tablets twice a day */
			dailyDoseAmount = getDoseAmountAsNumber() * getDrugStrengthAsNumber() * timesPerDay;
		}
		return dailyDoseAmount;
	}

	public double calculateFirstDailyDoseAmount() throws ParseException
	{
		int timesPerDay = DoseTimingUtils.getDose(doseTiming);
		if (timesPerDay < 0) {
			// Long Term Drug
			return getDoseAmountAsNumber();
		}
		double dailyDoseAmount = 0;
		if (doseUnit.equalsIgnoreCase("mg"))
		{
			/* Dose form is already in mg */
			/* Take 300mg twice a day */
			dailyDoseAmount = getDoseAmountAsNumber() * timesPerDay;
		}
		else if (doseUnit.equalsIgnoreCase("ml"))
		{
			/* Dose form is  in ml */
			/* have to do fancy manipulation to get mg from xx mg/yy ml dose unit */
			String[] ratio = drugStrength.split("\\/");
			if (ratio == null || ratio.length < 2) {
				dailyDoseAmount = getDoseAmountAsNumber() * timesPerDay;
			} else {
				double mgRatio = NumberUtils.parseNumber(ratio[0], "####.## mg");
				double mlRatio = 1.0;
				if (!ratio[1].equals("mL")) {
					mlRatio = NumberUtils.parseNumber(ratio[1], "####.## mL");
				}
				dailyDoseAmount = getDoseAmountAsNumber() * mgRatio / mlRatio * timesPerDay;
			}
		}
		else
		{
			/* Dose form is in tablets or capsules */
			/* Take 2 6-20mg tablets twice a day */
			/* This function wants the '6' mg times number of doses. */
			if (getDrugStrength() != null) {
				String drugStr[] = getDrugStrength().split("-",2);
				if (drugStr == null || drugStr.length<1) {
					dailyDoseAmount = 0;
				} else {
					dailyDoseAmount = getDoseAmountAsNumber() * NumberUtils.parseNumber(drugStr[0], "###.##") * timesPerDay;
				}
			}
		}
		return dailyDoseAmount;
	}
	
	public double calculateSecondDailyDoseAmount() throws ParseException
	{
		int timesPerDay = DoseTimingUtils.getDose(doseTiming);
		if (timesPerDay < 0) {
			// Long Term Drug
			return getDoseAmountAsNumber();
		}
		double dailyDoseAmount = 0;
		if (doseUnit.equalsIgnoreCase("mg"))
		{
			/* Dose form is already in mg */
			/* Take 300mg twice a day */
			dailyDoseAmount = getDoseAmountAsNumber() * timesPerDay;
		}
		else
		{
			/* Dose form is in tablets or capsules */
			/* Take 2 6-20mg tablets twice a day */
			/* This function wants the '20' mg times number of doses. */
			if (getDrugStrength() != null) {
				String drugStr[] = getDrugStrength().split("-",2);
				if (drugStr == null || drugStr.length<2) {
					dailyDoseAmount = 0;
				} else {
					dailyDoseAmount = getDoseAmountAsNumber() * NumberUtils.parseNumber(drugStr[1], "###.## mg") * timesPerDay;
				}
			}
		}
		return dailyDoseAmount;
	}
	
	public String isValidForTreatment()
	{
		String errorMessage = "";
		try
		{
			// NumberUtils.parseNumber(getDrugStrength(), "####.## mg");
			NumberUtils.parseNumber(getDoseAmount());
			if (!DoseTimingUtils.isValid(getDoseTiming()))
			{
				errorMessage = "medication has invalid dose timing: " + getDoseTiming();
			}
			else if (getDuration() == null || !(getDuration().intValue() > 0))
			{
				errorMessage = ("medication has invalid duration: " + getDuration());
			}
			else if (getQuantity() == null || !(getQuantity().floatValue() > 0))
			{
				errorMessage = ("medication has invalid quantity: " + getQuantity());
			}
			else if (!(getDoseUnit().equalsIgnoreCase("mg") || getDoseUnit().equalsIgnoreCase("tablet") || getDoseUnit().equalsIgnoreCase("capsule") || getDoseUnit().equalsIgnoreCase("ml")))
			{
				errorMessage = ("medication has invalid dose unit: " + getDoseUnit() + " (should be mg, ml, tablet, or capsule)");
			}
		}
		catch (ParseException e)
		{
			errorMessage = "medication has invalid drug strength or dose amount";
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
		}
		return errorMessage;
	}

	public String shortDescription()
	{
		return genericName + " " + (brandName != null && brandName.length() > 0 && !brandName.equalsIgnoreCase(genericName) ? "(" + brandName + ") " : "") + (drugRoute + " ") + (drugStrength);
	}

	public String sigDescription()
	{
		return (action + " ") + (doseAmount + " ") + (doseUnit + " ") + (doseRoute + " ") + (doseTiming + " ") + (doseOther + " ") + "for " + getDuration() + " days times " + getRefills() + " refills";
	}

}
