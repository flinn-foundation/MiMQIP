// javascript-rcopia \ html \ javascript \ rcopia_sig.js
/**
 * @fileOverview Script containing variables and functions specific to prescription sigs in Rcopia.
 *
 * @author DrFirst (various)
 * @author mkotelba
 */

var useFreeTextDialog = false;

/* Return the number of times per day that the given frequency
string represents. The result can be fractional. */
function getFrequency(freq)
{
	freq = freq.replace(/^\s+|\s+$/g, '')
	if (freq == "once a day")
	{
		return 1;
	}
	if (freq == "every morning")
	{
		return 1;
	}
	if (freq == "every night")
	{
		return 1;
	}
	if (freq == "every two hours")
	{
		return 12;
	}
	if (freq == "every three hours")
	{
		return 8;
	}
	if (freq == "every four hours")
	{
		return 6;
	}
	if (freq == "every six hours")
	{
		return 4;
	}
	if (freq == "every eight hours")
	{
		return 3;
	}
	if (freq == "every twelve hours")
	{
		return 2;
	}
	if (freq == "every 24 hours")
	{
		return 1;
	}
	if (freq == "every 48 hours")
	{
		return 0.5;
	}
	if (freq == "every 72 hours")
	{
		return 0.333;
	}
	if (freq == "twice a day")
	{
		return 2;
	}
	if (freq == "three times a day")
	{
		return 3;
	}
	if (freq == "four times a day")
	{
		return 4;
	}
	if (freq == "five times a day")
	{
		return 5;
	}
	if (freq == "every three to four hours")
	{
		return 8;
	}
	if (freq == "every four to six hours")
	{
		return 6;
	}
	if (freq == "every eight to twelve hours")
	{
		return 3;
	}
	if (freq == "every six to eight hours")
	{
		return 3.42857;
	}
	if (freq == "single dose")
	{
		return 1;
	}
	if (freq == "once every other day")
	{
		return .5;
	}
	if (freq == "once a week")
	{
		return 1.0 / 7.0;
	}
	if (freq == "twice a week")
	{
		return 2.0 / 7.0;
	}
	if (freq == "three times a week")
	{
		return 3.0 / 7.0;
	}
	if (freq == "as directed")
	{
		return 0;
	}
	if (freq == "every two hours while awake")
	{
		return 8;
	}
	if (freq == "every three hours while awake")
	{
		return 6;
	}
	if (freq == "every four hours while awake")
	{
		return 5;
	}
	if (freq == "every three to four hours while awake")
	{
		return 6;
	}
	if (freq == "every four to six hours while awake")
	{
		return 5;
	}
	if (freq == "at bedtime")
	{
		return 1;
	}
	if (freq == "every two weeks")
	{
		return 1.0 / 14.0;
	}
	if (freq == "once every two weeks")
	{
		return 1.0 / 14.0;
	}
	if (freq == "once a month")
	{
		return 12.0 / 365.0;
	}
	if (freq == "every three months")
	{
		return 4.0 / 365.0;
	}
	if (freq == "every other day")
	{
		return 0.5;
	}
	if (freq == "every three days")
	{
		return 1.0 / 3.0;
	}
	return  - 1;
}

/* Return the number of units the given dose string represents.
*/
function getDoseValue(dose)
{
	if (dose == "1/4")
	{
		return 0.25;
	}
	if (dose == "1/2")
	{
		return 0.5;
	}
	if (dose == "3/4")
	{
		return 0.75;
	}
	if (dose == "1 1/4")
	{
		return 1.25;
	}
	if (dose == "1 1/2")
	{
		return 1.5;
	}
	if (dose == "2 1/2")
	{
		return 2.5;
	}
}

/* Return the quantity unit appropriate to the given dose unit. */
function getDoseToQuantityUnits(doseUnit)
{
	if (doseUnit == "teaspoon")
	{
		return "ml";
	}
	if (doseUnit == "tablespoon")
	{
		return "ml";
	}

	return doseUnit;
}

/* The last calculated quantity. */
var calculatedQuantity = 0;

/* The last dose unit. */
var lastDoseUnit = null;

function initializeDetailsScreen(doShowMailOrderQuantity, doPerformSigChoice)
{
	loadWeight(document.mainform);
	location.href.hash = "top";
	if (doShowMailOrderQuantity)
		showMailOrderQuantity();
	if (doPerformSigChoice)
		performSigChoice();
	resetPrescriptionDetails();
	resetDoseCalculator();
}

/* This is executed when the prescription details screen is loaded.
1. If the quantity unit is blank, instantiate it to match the dose
unit, if possible.
2. If the quantity unit was changed in (1) or the quantity is
blank, recalculate and set the quantity.
3. Set the last calculated quantity and last dose unit to the
present values.
*/
function resetPrescriptionDetails()
{
	var resetQuantity = false;
	var quantityUnit = getQuantityUnit();
	var doseUnit = getDoseUnit();

	if (quantityUnit == "")
	{
		if (doseUnit != "")
		{
			var newQuantityUnit = getDoseToQuantityUnits(doseUnit);
			var quantityUnitObject = getQuantityUnitObject();
			setValue(quantityUnitObject, newQuantityUnit);
			resetQuantity = true;
		}
	}

	if (getQuantity() == "")
	{
		resetQuantity = true;
	}

	if (resetQuantity)
	{
		setQuantity(true, false);
	}

	calculatedQuantity = calculateQuantity();
}

/* The user selected a dose from the pick list. Set the dose field
to the selected item, reset the pick list to a blank value, then
recalculate the quantity if possible.
*/
function changeDoseList()
{
	var doseListObject = document.mainform.prescriptions_dose_pick_list;
	var dose = getValue(doseListObject);

	if (dose == "")
		return;

	setValue(document.mainform.prescriptions_dose, dose);
	setValue(document.mainform.prescriptions_dose_pick_list, "");
	setSig();
	setQuantity(true, true);
}

/* The user changed the dose. If he changed it to a non-blank
value, reset the dose pick list to a blank value, then
recalculate the quantity if possible.
*/
function changeDose()
{
	var doseObject = document.mainform.prescriptions_dose;
	var dose = getValue(doseObject);
	if (dose == "")
		return;

	setValue(document.mainform.prescriptions_dose_pick_list, "");
	setSig();
	setQuantity(true, true);
}

/* The user changed the dose unit.
If the dose unit is now blank and keepQuantityIfDoseBlank is false,
blank the quantity.
If the dose unit is not blank, change the quantity unit to match,
or blank the quantity unit if there is no matching value.
Recalculate and reset the quantity after changing the units.
Set the last dose unit to the new value.
*/
function changeDoseUnit(keepQuantityIfDoseBlank)
{
	var doseUnit = getDoseUnit();
	if (doseUnit == "")
	{
		if (!keepQuantityIfDoseBlank)
		{
			blankQuantity();
			blankQuantityUnit();
		}

		lastDoseUnit = doseUnit;
		return;
	}

	var quantityUnitObject = document.mainform.prescriptions_quantity_unit;
	var quantityUnit = getValue(quantityUnitObject);

	var convertedUnit = getDoseToQuantityUnits(doseUnit);
	if ((quantityUnit == "") || (quantityUnit == lastDoseUnit))
	{
		setValue(quantityUnitObject, convertedUnit);
	}

	setSig();
	setQuantity(true, false);
	lastDoseUnit = doseUnit;
}

/* The user changed the dose timing (frequency). If he changed it
to a blank value or one that does not translate to a numerical
frequency, blank the quantity. Otherwise, recalculate the quantity.
*/
function changeDoseTiming()
{
	var timingObject = document.mainform.prescriptions_dose_timing;
	var timing = getValue(timingObject);
	var freq = getFrequency(timing);

	if (timing == "" || (freq == 0))
	{
		blankQuantity();
		return;
	}

	if (timing == "single dose")
	{
		var durationObject = document.mainform.prescriptions_duration;
		setValue(durationObject, "1");
	}

	setSig();

	if (freq == null)
		freq = 0;

	setQuantity((freq > 0), (freq > 0));

	setDoseCalculatorFrequency();
}

/* The user changed the duration. Recalculate the quantity. */
function changeDuration()
{
	setQuantity(false, true);
}

/* The user changed the quantity. No special action is needed. */
function changeQuantity()
{
//nothing at present
}

/* The user changed the quantity unit. If the quantity unit is
now empty, blank the quantity. Otherwise, if the quantity is blank,
calculate it.
*/
function changeQuantityUnit()
{
	var quantityUnitObject = document.mainform.prescriptions_quantity_unit;
	var quantityUnit = getValue(quantityUnitObject);

	/*If the quantity unit was blanked, blank the quantity. */
	if (quantityUnit == "")
	{
		blankQuantity();
		return;
	}

	/*If the quantity is not already set, try to calculate it.*/
	if (!getQuantity() || (getQuantity() == ""))
	{
		setQuantity(true, false);
	}
}

/** Calculate the quantity from the other field data. and return
the number. If the calculation cannot be made because the
ratio between the dose and quantity units is unknonw, return -1.
If the quantity cannot be calculated for other reasons,
e.g. duration, timing, or frequency is blank or set
to a value that does not resolve to a number, return 0.
*/
function calculateQuantity()
{
	var dose = getDose(document.mainform.prescriptions_dose);
	var durationIndex = document.mainform.prescriptions_duration.selectedIndex;
	var sigIndex = document.mainform.prescriptions_dose_timing.selectedIndex;

	var duration = document.mainform.prescriptions_duration.options[durationIndex].value;

	if (!duration || (duration == "") || (duration == 0))
		return 0;

	var sig = document.mainform.prescriptions_dose_timing.options[sigIndex].text;

	if (!sig || (sig == ""))
		return 0;

	var freq = getFrequency(sig);

	if (!freq || (freq == "") || (freq == "0"))
		return 0;

	var quantity = Math.round((dose * freq * duration) + 0.49);
	var factor = getQuantityFactor();
	if (factor == 0)
		return  - 1;

	if (factor > 0)
		quantity = quantity * factor;
	else
		quantity = quantity / ( - factor);

	return quantity;
}

/** Recalculate the quantity.
If the Factor (dose unit-quantity unit ratio) is unknown and
BlankIfZeroFactor is true, blank the quantity and return.
If the quantity cannot be calculated and
BlankIfZeroQuantity is true, blank the quantity and return.
Otherwise, set the quantity to the calculated value and return.
The last calculated quantity is stored in the global
calculatedQuantity.
*/
function setQuantity(blankIfZeroFactor, blankIfZeroQuantity)
{
	var newQuantity = calculateQuantity();
	var oldQuantity = getQuantity();

	// -1 returned when factor is zero
	if ((blankIfZeroFactor && (newQuantity ==  - 1)) || (blankIfZeroQuantity && (newQuantity == 0)))
	{
		if ((calculatedQuantity > 0) && (calculatedQuantity == oldQuantity))
		{
			blankQuantity();
		}
	}

	if (newQuantity > 0)
	{
		document.mainform.prescriptions_quantity.value = newQuantity;
		calculatedQuantity = newQuantity;
	}
}

/** Blank the quantity value. */
function blankQuantity()
{
	document.mainform.prescriptions_quantity.value = "";
	calculatedQuantity = 0;
}

/** Blank the quantity unit. */
function blankQuantityUnit()
{
	document.mainform.prescriptions_quantity_unit.value = "";
}

/** If the prescription action  was set to "no_sig", reset the dropdown to a blank value. */
function setSig()
{
	if (!noSig())
		return;

	var actionObject = document.mainform.prescriptions_action;
	var actionOptions = actionObject.options;
	actionOptions.selectedIndex = 0;
}

/* Return the value of the dose field as a number. */
function getDose(doseObject)
{
	var dose = getValue(doseObject);

	if (!isNaN(dose))
		return dose;

	if (dose == "")
		return 0;

	var convertedDose = getDoseValue(dose);
	if (convertedDose == null)
		return 0;

	return convertedDose;
}

/* Return the value of the dose unit field. */
function getDoseUnit()
{
	var doseUnitObject = document.mainform.prescriptions_dose_unit;
	return getValue(doseUnitObject);
}

/* Return the value of the quantity field. */
function getQuantity()
{
	var quantityObject = document.mainform.prescriptions_quantity;
	return getValue(quantityObject);
}

/* Return the value of the quantity unit field. */
function getQuantityUnit()
{
	return getValue(getQuantityUnitObject());
}

/* Return the quantity unit field object. */
function getQuantityUnitObject()
{
	var quantityUnitObject = document.mainform.prescriptions_quantity_unit;
	return quantityUnitObject;
}

/* Return the Quantity Factor, which is the ratio of the dose
unit in size to the quantity unit. For instace, if the dose unit
is "teaspoon" and the quantity unit is "ml", the factor is 5. If
the ratio is < 1, return its reciprocal as a negative number. For
instance, if the quantity unit is "ounce" and the dose unit is "ml",
return -30. If both units are empty, return 1.
*/
function getQuantityFactor()
{
	var doseUnitObject = document.mainform.prescriptions_dose_unit;
	var doseUnit = getValue(doseUnitObject);
	var quantityUnitObject = document.mainform.prescriptions_quantity_unit;
	var quantityUnit = getValue(quantityUnitObject);

	if ((doseUnit == "") && (quantityUnit = ""))
		return 1;

	if (doseUnit == quantityUnit)
		return 1;

	if ((doseUnit == "teaspoon") && (quantityUnit == "ml"))
		return 5;

	if ((doseUnit == "tablespoon") && (quantityUnit == "ml"))
		return 15;

	if ((doseUnit == "ml") && (quantityUnit == "ounce"))
		return  - 30;

	if ((doseUnit == "teaspoon") && (quantityUnit == "ounce"))
		return  - 6;

	if ((doseUnit == "tablespoon") && (quantityUnit == "ounce"))
		return  - 2;

	return 0;
}

/* Blank the given field. */
function blankItem(item)
{
	setValue(item, "");
}

/* If the action field is set to "no_sig", blank all the prescription details fields.
*/
function blankSig()
{
	if (noSig())
	{
		doBlankSig();
		blankItem(document.mainform.prescriptions_action);
	}
	else
	{
//TODO use the routes on the max daily dose
}
}

/* Blank the prescription details fields. */
function doBlankSig()
{
	blankItem(document.mainform.prescriptions_action);
	blankItem(document.mainform.prescriptions_dose);
	blankItem(document.mainform.prescriptions_dose_unit);
	blankItem(document.mainform.prescriptions_route);
	blankItem(document.mainform.prescriptions_dose_timing);
	blankItem(document.mainform.prescriptions_dose_other);
	blankItem(document.mainform.prescriptions_duration);
	blankItem(document.mainform.prescriptions_quantity);
	blankItem(document.mainform.prescriptions_quantity_unit);
	blankItem(document.mainform.prescriptions_refills);
}

/* Return true if the prescription action is "no_sig", else false.
*/
function noSig()
{
	return (getValue(document.mainform.prescriptions_action) == "no_sig");
}

function genericWarning(url)
{
	var note = getValue(document.mainform.prescriptions_pharmacist_notes);

	if ((note != "Dispense as written") && (note != "Brand name necessary"))
	{
		return;
	}

	if (!document.mainform.has_generic_warning)
		return;

	var parameters = 'actionx=display_generic_warning';

	if (openWindow)
	{
		var urlAndParameters = url + "&" + parameters;
		popup(urlAndParameters, { size: "small" });
	//submitForm("mainform",parameters,"generic_warning");
	}
	else
	{
		submitForm("mainform", parameters);
	}

//smallPopUp(urlAndParameters, { size: "small" });
}

/* The user selected a sig choice from the dropdown.
Set the sig values to those indicated in the choice. */
function performSigChoice()
{
	var sig = getValue(document.mainform.sig_choices);

	if (sig == "blank")
	{
		doBlankSig();
		blankItem(document.mainform.prescriptions_patient_notes);
		return;
	}

	setValues("mainform", sig, true);
}

function removePatientPharmacy()
{
	var val = getValue(document.mainform.prescriptions_pharmacy_id);
	if (!val)
		return;

	if (confirm('Are you certain that you wish to remove this pharmacy from the patient\'s list?'))
	{
		submitForm('mainform', 'action=delete_pharmacy&actionx=delete_pharmacy');
	}
}

function testDialog()
{
	var dialogHeight = 120;
	var dialogWidth = 350;

	var drugDescription = getValue(document.mainform.prescriptions_drug_name) + " " + getValue(document.mainform.prescriptons_drug_formula);

	document.getElementById('scheduled_drug_dialog_drug_name').innerHTML = drugDescription;

	showModalDialog('scheduled_drug_dialog', dialogWidth, dialogHeight);
}

function clickReviewButton()
{
	var button = document.getElementById('review_prescription_button');

	button.click();
}

function scheduledDrugDialogReply(status)
{
	hideModalDialog('scheduled_drug_dialog');
	if (status == 'cancel')
		return;

	document.getElementById('has_scheduled_drug_input').value = status;
	clickReviewButton();
}

var nameOfSubmitButton;

function setSubmitButton(button)
{
	nameOfSubmitButton = button.name;
}
/* Check to see if we need to display a free text drug dialog as a popup.
Return true to proceed on to the review prescription screen, else false. */
function reviewPrescription()
{
	if (!useFreeTextDialog)
		return true;
	if (nameOfSubmitButton != 'review_prescription')
		return true;

	var dialogHeight = 120;
	var dialogWidth = 350;

	if (document.mainform.drug_id)
	{
		return true;
	}

	if (document.mainform.prescriptions_has_scheduled_drug.value != "")
	{
		return true;
	}

	var drugDescription = getValue(document.mainform.prescriptions_drug_name) + " " + getValue(document.mainform.prescriptions_drug_formula);

	document.getElementById('scheduled_drug_dialog_drug_name').innerHTML = drugDescription;

	showModalDialog('scheduled_drug_dialog', dialogWidth, dialogHeight);
	return false;
}

function useMailOrderPharmacy(thisBox)
{
	if (thisBox.checked)
	{
		uncheckMailOrderBoxes();
		thisBox.checked = true;

		/* Only show mail order quantity if retail and mail
           order are both checked. */
		if (document.mainform.use_retail_pharmacy.checked)
		{
			//set the default quantity
			mailQuantity = getElementByName(document, "prescriptions_mail_order_quantity");

			if (!mailQuantity || (mailQuantity.value == "") || (mailQuantity.value == 0))
			{
				mailQuantity.value = getValue(getElementByName(document, "prescriptions_quantity"));
			}

			//set the default refills
			mailRefills = getElementByName(document, "prescriptions_mail_order_refills");
			if (!mailRefills || (mailRefills.value == "") || (mailRefills.value == 0))
			{
				mailRefills.value = getValue(getElementByName(document, "prescriptions_refills"));
			}

			showMailOrderQuantity();
			return;
		}
	}

	hideMailOrderQuantity();
}

function useRetailPharmacy(thisBox)
{
	if (thisBox.checked)
	{
		if (mailOrderBoxChecked())
		{
			showMailOrderQuantity();
			return;
		}
	}
	hideMailOrderQuantity();
}

function mailOrderBoxChecked()
{
	var boxes = getElementsByName(document, "use_mail_order");
	for (var count = 0;count < boxes.length;count++)
	{
		if (boxes[count].checked)
		{
			return true;
		}
	}
	return false;
}

function uncheckMailOrderBoxes()
{
	var boxes = getElementsByName(document, "use_mail_order");
	for (var count = 0;count < boxes.length;count++)
	{
		boxes[count].checked = false;
	}
}

function showMailOrderQuantity()
{
	var quantityObj = getElementById("mail_order_quantity_row");
	quantityObj.style.display = "";
	quantityObj.style.visibility = "visible";

	var refillObj = getElementById("mail_order_refills_row");
	refillObj.style.display = "";
	refillObj.style.visibility = "visible";
}

function hideMailOrderQuantity()
{
	var quantityObj = getElementById("mail_order_quantity_row");
	quantityObj.style.display = "none";
	quantityObj.style.visibility = "hidden";

	var refillObj = getElementById("mail_order_refills_row");
	refillObj.style.display = "none";
	refillObj.style.visibility = "hidden";
}

function changeDrugDescription()
{
	//getElementByName(document, "has_scheduled_drug") = "";
	var h=document.getElementsByName("prescriptions_drug_name");
	var t=document.getElementsByName("review_prescription");
	for(var i=0;i<h.length;i++)
	{
		if(h[i].value.length<1)
		{
			alert("drug name cannot be less than a character");
			for (var i = 0; i < t.length; i++)
			{
				t[i].disabled=true;
			}
		}
		else
		{
			for (var i = 0; i < t.length; i++)
			{
				t[i].disabled=false;
			}
		}
	}
}
