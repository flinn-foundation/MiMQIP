// javascript-rcopia \ html \ javascript \ rcopia_util.js
/**
 * @fileOverview Script containing utility variables and functions for Rcopia.
 *
 * @author DrFirst (various)
 * @author mkotelba
 */

var applicationName = "Rcopia";
var organizationName = "DrFirst";
var openWindow = true;
var poundsPerKilogram = 2.2046;
var debugIsOn = false;

var timerID = 0;
var secondsTotal = 600;
var msTotal = 0;
var closeWindow = false;
var currentUrl = null;

function myError(text)
{
	alert(text);
	throw text;
}

function isDefined(obj)
{
	var type = typeof (obj);
	return type != 'undefined' && type != 'unknown';
}

function getElementById(id, optional)
{
	return getElementByID(id, optional);
}

function getElementByID(id, optional)
{
	if (isDefined(document.getElementById))
	{
		return document.getElementById(id);
	}
	else if (isDefined(document.all))
	{
		return document.all[id];
	}
	else if (optional)
	{
		return null;
	}
	else
	{
		myError("Cannot find a method to locate element by ID: " + id);
	}
}

/* Return the first element in the given container object with the given name. */
function getElementByName(container, name, optional)
{

	var elements = getElementsByName(container, name, optional);
	if (elements && (elements.length > 0))
	{
		return elements[0];
	}

	return null;
}

function getForm(formName)
{
	if (!formName)
		myError("Cannot getForm with empty argument");

	if (!isDefined(document.forms))
		myError("Cannot find any forms in this document");

	for (var count = 0;count < document.forms.length;count++)
	{
		var oneForm = document.forms[count];
		if (formName == getElementName(oneForm))
			return oneForm;
	}

	myError("Cannot find form with name'" + formName + "'");
}



function getElementsByName(container, name, optional)
{

	if (container == null)
		container = document;

	/* Handle single-parameter calls. */
	if ((typeof (container) == "string") && !name)
	{
		name = container;
		return getElementsByName(document, name, optional);
	}

	if (isDefined(container.getElementsByName))
	{
		var found = container.getElementsByName(name);
		return found;
	}

	if (isDefined(container.elements))
	{
		var found = getElementsUsingArray(container.elements, name);
		if (found)
			return found;
	}

	if (isDefined(container.forms))
	{
		var found = getElementsUsingForms(container.forms, name);
		if (found)
			return found;
	}

	if (isDefined(container.all))
	{
		var found = getElementsUsingArray(container.all, name);
		if (found)
			return found;
	}

	if (optional)
		return null;

	myError("Cannot find a method to locate element by name: " + name);
}

function describeAlert(message, obj)
{
	alert(message + ' obj=' + obj);
	if (obj)
		alert(message + ' obj.length=' + obj.length);
}

function getElementsUsingTagName(container, name)
{
	name = new String(name);

	var myMatches = new Array();
	var allElements = container.getElementsByTagName("*");
	var numElements = allElements.length;

	for (var count = 0;count < numElements;count++)
	{
		var oneElement = allElements[count];
		if (!oneElement)
			continue;

		var oneName = oneElement.name;
		var anotherName = oneElement.getAttribute("name");

		if ((oneName == name) || (anotherName == name))
		{
			myMatches[myMatches.length] = oneElement;
		}
	}

	if (myMatches.length == 0)
		return null;

	return myMatches;
}

function getElementsUsingForms(forms, name)
{
	name = new String(name);

	var myMatches = new Array();
	var numForms = forms.length;

	for (var count = 0;count < numForms;count++)
	{
		var oneForm = forms[count];
		if (!oneForm)
			continue;

		var formMatches = getElementsByName(oneForm, name);

		if (formMatches && formMatches.length)
		{
			for (var match = 0;match < formMatches.length;match++)
			{
				myMatches[myMatches.length] = formMatches[match];
			}
		}
	}

	if (myMatches.length == 0)
		return null;

	return myMatches;
}

function getElementsUsingArray(allElements, name)
{
	name = new String(name);
	var myMatches = new Array();
	if (!allElements)
		return myMatches;

	var numElements = allElements.length;

	for (var count = 0;count < numElements;count++)
	{
		var oneElement = allElements[count];
		var oneElementName = getElementName(oneElement);

		if (oneElementName == name)
		{
			myMatches[myMatches.length] = oneElement;
		}
	}

	if (myMatches.length == 0)
		return null;

	return myMatches;
}

function getElementName(thisElement)
{
	if (isDefined(thisElement.name))
		return thisElement.name;

	if (isDefined(thisElement.getAttribute))
	{
		var attName = thisElement.getAttribute("name");
		if (attName)
			return attName;
	}

	if (isDefined(thisElement.tagName))
		return thisElement.tagName;

	//myError('Cannot find method to return element name');
	return null;
}

function setApplicationName(name)
{
	applicationName = name;
}

function setOrganizationName(name)
{
	organizationName = name;
}

function setOpenWindow(open)
{
	openWindow = ((open == "y") || (open == "Y"));
}

function openWindow()
{
	return openWindow;
}

function setDebug(debug)
{
	debugIsOn = debug;
}

function debugAlert(alertMessage)
{
	if (debugIsOn)
		alert(alertMessage);
}

function isArray(obj)
{
	return (isDefined(obj.length));
}

function getValue(formObject)
{
	return getValue(formObject, false);
}

function getValue(formObject)
{
	if (!formObject || (formObject == null) || (formObject == ''))
	{
		return '';
	}

	var objType = formObject.type;
	var value;

	if (objType == 'select-one')
	{
		var options = formObject.options;
		var index = formObject.selectedIndex;
		if (index < 0)
		{
			return '';
		}

		var option = options[index];

		if (!option || (option == null))
		{
			return '';
		}

		value = option.value;

		if (!value)
			return '';

		if (!value || (value == ''))
		{
			value = option.text;
		}
	}
	else if (formObject.length && (formObject.length > 0))
	{
		if (!formObject.length || (formObject.length == 0))
		{
			value = '';
		}
		else
		{
			for (count = 0;count < formObject.length;count++)
			{
				var one = formObject.item(count);

				if (one.checked)
					value = one.value;
			}
		}
	}
	else
	{
		value = formObject.value;
	}

	if (!value || (value == null))
	{
		return '';
	}

	return value;
}

function getSelectorOptionValue(option)
{
	if (!option || (option == null))
		return '';

	option.value;

	if (option.value && (option.value != ''))
		return option.value;

	return option.text;
}

function getSelectorText(selector)
{
	if (!selector || (selector == null) || (selector == ''))
	{
		return '';
	}

	var objType = selector.type;
	var value;

	if (objType != 'select-one')
		return '';

	var options = selector.options;
	var index = selector.selectedIndex;
	if (index < 0)
	{
		return '';
	}

	var option = options[index];

	if (!option || (option == null))
	{
		return '';
	}

	return option.text;
}

function setValue(formObject, value)
{
	if (!formObject || (formObject == ''))
	{
		return false;
	}

	if (!value)
		value = '';

	var objType = formObject.type;

	if (objType == 'select-one')
	{
		var options = formObject.options;
		var len = options.length;
		for (count = 0;count < len;count++)
		{
			var option = options[count];
			var optionValue = getSelectionValue(option);
			var optionText = getSelectionText(option);

			if (value == '')
			{
				if (optionValue == value)
				{
					if (options.selectedIndex)
						options.selectedIndex = count;
					else
						formObject.selectedIndex = count;
					return true;
				}
			}
			else
			{
				if ((optionText == value) || (optionValue == value))
				{
					if (options.selectedIndex)
						options.selectedIndex = count;
					else
						formObject.selectedIndex = count;
					return true;
				}
			}
		}
	}
	else
	{
		//debugAlert('object is not select, setting value ');
		formObject.value = value;
		return true;
	}

	return false;
}

function getSelectionValue(option)
{
	if (!option || (option == null) || (option == ''))
		return '';

	if (!option.value || (option.value == null))
		return '';

	return option.value;
}

function getSelectionText(option)
{
	if (!option || (option == null) || (option == ''))
		return '';

	if (!option.text || (option.text == null))
		return '';

	return option.text;
}

/* Set the values in thte given form as indicated by the values parameter.
The values parameter is in the syntax name=value&name=value, etc.
If an element is missing, and optional is true, it is ignored. Otherwise, an error is thrown.
*/
function setValues(formName, values, optional)
{

	var start, stop, len;
	var name, value;

	var thisForm = getForm(formName);

	start = 0;
	len = values.length;

	while (start < len)
	{
		while (values.charAt(start) == '&')
			start++;
		if (start >= len)
			break;

		stop = values.indexOf('=', start);
		if (stop ==  - 1)
			break;

		name = values.substring(start, stop);
		start = stop + 1;

		stop = values.indexOf('&', start);
		if (stop ==  - 1)
			value = values.substring(start);
		else
			value = values.substring(start, stop);

		//To avoid tangling with form action (i.e. URL)
		if (name == "action")
			name = "actionx";

		//  alert('getting object ' + name + ',optional=' + optional);
		var thisObject = getElementByName(thisForm, name, optional);

		if (isSettable(thisObject))
		{
			setValue(thisObject, value);
		}
		else
		{
			if (isDefined(document.createElement))
			{
				//        debugAlert('using document.createElement');
				var newObject = document.createElement("input");
				newObject.type = "hidden";
				newObject.name = name;
				newObject.value = value;
				thisForm.appendChild(newObject);
			}
			else
			{
				debugAlert('unable to add form element for ' + name + ', bailing');
			}
		}

		if (stop ==  - 1)
			break;

		start = stop + 1;
	}
}

function setValueByName(elementName, value)
{
	var element = getElementByName(document, elementName, true);
	if (!element)
		return;
	setValue(element, value);
}

function getValueByName(elementName)
{
	var element = getElementByName(document, elementName, true);
	if (!element)
		return '';
	return getValue(element);
}

function getValueById(elementID)
{
	return getValueByID(elementID);
}

function getValueByID(elementID)
{
	var element = getElementByID(elementID);
	if (!element)
		return "";
	return element.innerHTML;
}

function setValueById(elementID, value)
{
	setValueByID(elementID, value)
}

function setValueByID(elementID, value)
{
	var element = getElementByID(elementID);
	if (!element)
		return;
	element.innerHTML = value;
}

//For now, everything that exists is settable.
function isSettable(thisObject)
{
	if (!thisObject)
		return false;

	return true;

}

// values is in the syntax name=value&name=value, etc
function submitForm(formName, values)
{
	var thisForm = getForm(formName);

	debugAlert('submitform, form = ' + formName + ', action=' + getElementName(thisForm.action) + ', values=' + values);

	setValues(formName, values, true);
	thisForm.submit();
}

function selectAll(formName, checkBoxNameStart)
{
	setCheckboxes(formName, checkBoxNameStart, true);
}

function selectNone(formName, checkBoxNameStart)
{
	setCheckboxes(formName, checkBoxNameStart, false);
}

function setCheckboxes(formName, checkBoxNameStart, checked)
{

	var form = getForm(formName);

	if (!form)
		return;

	for (var count = 0;count < form.elements.length;count++)
	{
		var oneElement = form.elements[count];
		if (oneElement.type == 'checkbox' && (oneElement.name.indexOf(checkBoxNameStart) == 0))
		{
			oneElement.checked = checked;
		}
	}
}

function goTo(url)
{
	window.location.href = url;
}

function goToEmbedded(url)
{
	encodedUrl = encodeURIComponent(url);
	targetUrl = '/servlet/RcopiaWebServlet?screen=EmbedScreen&embed_url=' + encodedUrl;
	window.location.href = targetUrl;
}

function isEmpty(obj)
{
	if (!obj)
		return true;

	var len = obj.length;

	if (!len)
		return oneIsEmpty(obj);

	var count;
	for (count = 0;count < len;count++)
	{
		if (!oneIsEmpty(obj[count]))
			return false;
	}

	return true;
}

function oneIsEmpty(obj)
{
	if (obj == null)
		return true;

	var val = obj.value;
	if (val == null)
		return true;

	var len = val.length;
	for (index = 0;index < len;index++)
	{
		if (val.charAt(index) != ' ')
			return false;
	}

	return true;
}

function manualDrugWarning()
{
	alert('This drug was entered by hand rather than picked from a list, or was transferred with a code that ' + applicationName + ' did not recognize. ' + applicationName + ' may not be able to detect drug-drug interactions and drug-allergy reactions for this drug.  To correct the medication record, stop the medication, and use the Manage Medications link to create a new medication record for the correct drug.');
}

function manualInpatientDrugWarning()
{
	alert('This drug was entered by hand rather than picked from a list, or was transferred with a code that ' + applicationName + ' did not recognize. ' + applicationName + ' may not be able to detect drug-drug interactions and drug-allergy reactions for this drug.');
}

function detailsManualDrugWarning()
{
	alert('This drug was entered by hand rather than picked from a list, or was transferred with a code that ' + applicationName + ' did not recognize. ' + applicationName + ' may not be able to detect drug-drug interactions and drug-allergy reactions for this drug.');
}

function manualAllergyWarningMini()
{
	alert('This allergy was entered by hand rather than picked from a list, or was transferred with a code that ' + applicationName + ' did not recognize. ' + applicationName + ' may not be able to detect drug-allergy reactions for this allergy.');

}

function manualAllergyWarning()
{
	alert('This allergy was entered by hand rather than picked from a list, or was transferred with a code that ' + applicationName + ' did not recognize. ' + applicationName + ' may not be able to detect drug-allergy reactions for this allergy.  Click the "Find Match" link to find a listed drug or group for this record.');

}

function manualMedNoMatchHeader()
{
	alert('At least one drug in this list was entered by hand or transferred with a code that ' + applicationName + ' did not recognize. ' + applicationName + ' may not be able to detect drug-drug interactions and drug-allergy reactions for such drugs.');
}

function manualAllergyNoMatchHeader()
{
	alert('At least one drug in this list was entered by hand or transferred with a code that ' + applicationName + ' did not recognize. ' + applicationName + ' may not be able to detect drug-drug interactions and drug-allergy reactions for such drugs.');
}

function manualMedNearMatchHeader()
{
	alert('At least one drug in this list was transferred with a name or dose that is not exactly the same as the name or dose that appears in the medication list.  Please click on the yellow triangle below for details.');
}

function manualAllergyNearMatchHeader()
{
	alert('At least one drug in this list was transferred with a name that is not exactly the same as the name that appears in the Allergies/Adverse Reactions list.  Please click on the yellow triangle below for details.');
}

function manualAllergyWarning(singlePatient)
{
	alert('This allergy was entered by hand rather than picked from a list, or was transferred with a code that ' + applicationName + ' did not recognize. ' + applicationName + ' may not be able to detect drug-allergy reactions for this allergy.  Click the "Find Match" link to find a listed drug or group for this record.');
}

function nearMatchDrugWarning(srcDrungDrecription)
{
	alert('Transferred as "' + srcDrungDrecription + '". If this name and dose are equivalent to the name and dose shown in the medication list, then no action is needed. If this is not the same drug shown in the medication list, you should stop the medication, and use the Manage Meds link to create a new medication record for the correct drug.');
}

function nearMatchInpatientDrugWarning(srcDrungDrecription)
{
	alert('Transferred as "' + srcDrungDrecription + '". If this name and dose are equivalent to the name and dose shown in the medication list, then no action is needed. If this is not the same drug shown in the medication list and you wish to prescribe it, you should start over and select the drug from a drug search.');
}

function nearMatchAllergyWarning(srcDrungDrecription, singlePatient)
{
	alert('Transferred as "' + srcDrungDrecription + '". If this name is equivalent to the name shown in the Allergies/Adverse Reactions list, then no action is needed. If this is not the same drug shown in the Allergies/Adverse Reactions list, you should use the "Improve Match" link to replace the incorrect allergy with an appropriate coded drug or group.');
}

function explainElectronicDirectionsLimit()
{
	alert('Electronic prescriptions may only have 140 characters for patient directions ("Sig" dropdowns and boxes, plus "Directions to Patient" box). They may only have 210 characters for pharmacist directions ("Directions to Pharmacist" box). Prescriptions with longer directions will be faxed to the pharmacy, and renewal requests for those prescriptions will be faxed by the pharmacy. We urge you to trim prescription directions to fit those limits, both to gain the added security of electronic prescribing and to ensure that subsequent renewal requests are sent to Rcopia.');
}

function sharedNotice(what)
{
	alert('This ' + what + ' derives from data entered by another practice sharing this patient.');
}

function setDate(formName, prefix, year, month, day)
{
	var thisForm = getForm(formName);
	var yearObject = getElementByName(thisForm, prefix + "_year");
	setValue(yearObject, year);
	var monthObject = getElementByName(thisForm, prefix + "_month");
	setValue(monthObject, month);
	var dayObject = getElementByName(thisForm, prefix + "_day");
	setValue(dayObject, day);

/*  var command = "setValue(document." + form + "." + prefix
	+ "_year,'" + year + "')";
  eval(command);

  command = "setValue(document." + form + "." + prefix
	+ "_month,'" + month + "')";
  eval(command);

  command = "setValue(document." + form + "." + prefix
	+ "_day,'" + day + "')";
  eval(command);
*/
}

function checkRemove(formObject)
{
	var value = getValue(formObject);
	if (value != 'Remove')
		return;

	var message = "Removing a renewal request discards it without informing the pharmacy. If you have not communicated with the pharmacy by other means, it is likely you will receive more requests for the same renewal. \n If you wish to ignore the request for whatever reason, you should 'deny' it. \n Do you wish to remove this request?";

	if (!confirm(message))
		setValue(formObject, "");
}

function consentWarning(formObject)
{
	if (formObject)
	{
		var value = getValue(formObject);
		if (value != 'accept')
			return;
	}

	var message = "By selecting 'OK', I certify that consent to share protected health information with the provider listed has been obtained."

	if (!confirm(message))
	{
		if (formObject)
			setValue(formObject, "");

		return false;
	}

	return true;
}

function confirmUnshare()
{
	var message = "Are you certain that you wish to break this sharing agreement?"

	if (!confirm(message))
	{
		return false;
	}

	return true;
}

function submitIf(formName, buttonNames)
{
	if (!buttonNames)
	{
		return true;
	}

	var start, stop;
	var name;

	start = 0;
	len = buttonNames.length;
	var thisForm = getForm(formName);

	while (start < len)
	{
		stop = buttonNames.indexOf(',', start);
		if (stop ==  - 1)
			return false;

		name = buttonNames.substring(start, stop);
		start = stop + 1;

		var obj = getElementByName(thisForm, name);
		var val = getValue(obj);
		if (val && (val != ''))
		{
			return true;
		}
	}

	return false;
}

function whyNoMailOrder()
{
	alert('Many mail-order pharmacies, such as ExpressScripts, Medco, and CareMark, require that the patient be eligible for one of their plans before they will honor a prescription written to them. When ' + applicationName + ' finds eligibility for one of these payers, a mail-order option will automatically appear on the prescription details page. Sending a prescription without prior eligibility to these pharmacies would likely result in the prescription being rejected for reimbursement; thus these pharmacies listed in the Pharmacy Search only when the patient is eligible.');
}

function switchFormulary(selection, url)
{
	var val = getValue(selection);
	var urlAndParameters = url + "&action=switch_formulary&actionx=switch_formulary&master_id=" + val;
	goTo(urlAndParameters);
}

function switchPharmacy(selection, url)
{
	var val = getValue(selection);
	var urlAndParameters = url + "&action=pharmacy_selected&type=patient&pharmacy_id=" + val;
	goTo(urlAndParameters);
}

function viewDropdownPharmacy(dropdownElementID, url)
{
	var dropdownElement = getElementByID(dropdownElementID);
	var val = getValue(dropdownElement);
	var urlAndParameters = url + "&action=view_pharmacy&pharmacy_id=" + val;
	popup(urlAndParameters, { size: "small" });
}

function viewDropdownPharmacyNoPopup(dropdownElementID, url)
{
	var dropdownElement = getElementByID(dropdownElementID);
	var val = getValue(dropdownElement);
	var urlAndParameters = url + "&action=view_pharmacy&pharmacy_id=" + val;
	goTo(urlAndParameters);
}

function confirmNonRcopiaSite(url)
{
	var message = "You are about to view an external site not affiliated with " + applicationName + ". " + organizationName + " is not responsible for any content on this site.";

	if (!confirm(message))
		return;

	window.open(url, 'rcopia_drug_reference', 'scrollbars, resizable,height=550,width=750,location=no,menubar,toolbar');
}

function confirmNonRcopiaSiteEmbedded(url)
{
	var message = "You are about to view an external site not affiliated with " + applicationName + ". " + organizationName + " is not responsible for any content on this site.";

	if (!confirm(message))
		return;

	goToEmbedded(url);
}

function checkSearch(thisForm)
{
	if (!thisForm.patient)
		return false;

	return ((thisForm.patient.value != null) && (thisForm.patient.value != ''));

}

function convertWeight(thisForm, thisObj)
{
	if (!thisForm || !thisObj)
		return false;

	var weight = thisObj.value;
	if (weight == '')
		return;

	if (isNaN(weight))
	{
		alert('Weight must be a number.');
		thisObj.value = '';
		return;
	}

	if (thisObj == thisForm.this_patient_weight_lb)
		weight = weight / poundsPerKilogram;

	if (thisForm.this_patient_weight)
	{
		thisForm.this_patient_weight.value = weight;
		var kg = Math.round(weight * 100) / 100;
		thisForm.this_patient_weight_kg.value = kg;
		var lb = Math.round(weight * poundsPerKilogram * 100) / 100;
		thisForm.this_patient_weight_lb.value = lb;
	}
	else
	{
		thisForm.patient_weight.value = weight;
		var kg = Math.round(weight * 100) / 100;
		thisForm.patient_weight_kg.value = kg;
	}
}

function loadWeight(thisForm)
{
	//debugAlert('loadweight');
	if (!thisForm)
		return false;

	var weightObj = thisForm.this_patient_weight;
	if (!weightObj)
		weightObj = thisForm.patient_weight;

	if (!weightObj)
		return false;

	var weight = weightObj.value;
	if (weight == '')
		return;

	if (isNaN(weight))
		return;

	if (thisForm.this_patient_weight)
	{
		thisForm.this_patient_weight_kg.value = Math.round(weight * 100) / 100;
		thisForm.this_patient_weight_lb.value = Math.round(weight * poundsPerKilogram * 100) / 100;
	}
	else
	{
		thisForm.patient_weight_kg.value = Math.round(weight * 100) / 100;
	}
}

function truncateDecimal(num, numDecimalPlaces)
{
	var numString = "" + num;
	var period = numString.indexOf('.');
	if (period !=  - 1)
	{
		if (numDecimalPlaces <= 0)
			numString = numString.substring(0, period);
		else
			numString = numString.substring(0, period + numDecimalPlaces + 1);
	}

	return numString;
}

function setFemaleFormElements(thisForm)
{
	if (!thisForm)
		return false;

	if ("f" != thisForm.this_patient_sex.value)
	{
		thisForm.this_patient_pregnant.value = "n";
		thisForm.this_patient_breastfeeding.value = "n";
	}
}

function resetForSex(femaleElement, thisForm)
{
	if (!thisForm)
		return false;

	if ("f" != thisForm.this_patient_sex.value)
	{
		femaleElement.value = "n";
	}
}

function saveAllInterventions(formName)
{
	var form = getForm(formName);
	if (!form)
		return;

	var checkboxChecked = 0;

	for (var count = 0;count < form.elements.length;count++)
	{
		var oneElement = form.elements[count];
		if (oneElement.type == 'checkbox' && (oneElement.name == 'phoneCall4Med_id' || oneElement.name == 'verbalConsult4Med_id' || oneElement.name == 'sendLetter4Med_id' || oneElement.name == 'handout4Med_id') && oneElement.checked == true)
		{
			checkboxChecked = 1;
		}
	}

	if (checkboxChecked == 0)
	{
		alert('You must select one or more interventions before saving.');
	}
	else
	{
		submitForm(formName, 'actionx=record_compliance_interventions_for_all');
	}

}

function saveInterventionForOneMedication(formName, checkBoxValue)
{
	var form = getForm(formName);
	if (!form)
		return;

	var checkboxChecked = 0;

	for (var count = 0;count < form.elements.length;count++)
	{
		var oneElement = form.elements[count];
		if (oneElement.type == 'checkbox' && (oneElement.name == 'phoneCall4Med_id' || oneElement.name == 'verbalConsult4Med_id' || oneElement.name == 'sendLetter4Med_id' || oneElement.name == 'handout4Med_id') && oneElement.value == checkBoxValue && oneElement.checked == true)
		{
			checkboxChecked = 1;
		}
	}

	if (checkboxChecked == 0)
	{
		alert('You must select one or more interventions before saving.');
	}
	else
	{
		var specificAction = 'record_compliance_interventions_for_med_' + checkBoxValue;
		submitForm(formName, 'actionx=' + specificAction);
	}
}

function ignorePrescriptionPassword()
{
	var passwordObj = document.reportform.signature_password;
	if (!passwordObj)
		return;

	passwordObj[0].value = '_not_needed_';
}

function checkPrescriptionPassword()
{
	password = document.reportform.signature_password;
	if (!password)
		return true;

	if (isEmpty(password))
	{
		alert('You must provide a signature password.');
		return false;
	}

	return true;
}

function areYouSureRx(url, prescription_id, drug, patient_name)
{
	var message = "Are you sure that you want to delete this prescription for '" + drug + "' for '" + patient_name + "'?\n";
	if (confirm(message))
	{
		var params = "proxy=y&action=delete_prescription&actionx=delete_prescription&working_prescription_id=" + prescription_id;
		submitForm('reportform', params);
	}
}

function deleteAllPrescriptions()
{
	var message = "Are you sure that you want to delete ALL the selected prescriptions?\n";
	if (confirm(message))
	{
		submitForm('reportform', 'action=delete_prescription&actionx=delete_prescription');
	}
}

function changeDate()
{
	if (document.mainform && document.mainform.date_interval && document.mainform.date_interval[5])
	{
		document.mainform.date_interval[5].checked = true;
	}
}

function filledMessage()
{
	alert('Patient received dispensed medication.');
}

function partiallyFilledMessage()
{
	alert('Patient received dispensed medication.');
}

function notFilledMessage()
{
	alert('Patient did not receive dispensed medication.');
}

function policyNumberChanged(policyNumberObj)
{
	var box = document.mainform.insurances_use_policy_num_in_eligibility;
	if (!box)
		return;

	if (!policyNumberObj.value || (policyNumberObj.value == ''))
	{
		box.checked = false;
	}
}

function usePolicyNumClicked(checkboxObj)
{
	if (checkboxObj.checked && document.mainform.insurances_policy_number.value == '')
		checkboxObj.checked = false;
}

function appendSelectedCheckboxValuesToUrl(url, paramName, checkboxName)
{
	var appendString = paramName + "=";
	// if there's already a parameter, then add a "&" else, add a "?"
	if (url.match(/\?/))
	{
		appendString = "&" + appendString;
	}
	else
	{
		appendString = "?" + appendString;
	}
	var count = 0;
	var allInputs = document.getElementsByTagName("input");
	for (var i = 0;i < allInputs.length;i++)
	{
		var checkbox = allInputs[i];
		if (checkbox.type == "checkbox" && checkbox.name == checkboxName && checkbox.checked)
		{
			if (count++ > 0)
			{
				appendString = appendString + ",";
			}
			appendString = appendString + checkbox.value;
		}
	}
	return url + appendString;
}

var bt_secs;
var bt_timerID = null;
var bt_timerRunning = false;
var bt_delay = 1000;
var bt_popup

function initializeBlankTimer(seconds, popup)
{
	// Set the length of the timer, in seconds
	//bt_secs = seconds;
	//bt_popup = popup;
	//BTStopTheClock();
	//BTStartTheTimer();
}

function BTStopTheClock()
{
	if (bt_timerRunning)
		clearTimeout(bt_timerID);
	bt_timerRunning = false;
}

function BTStartTheTimer()
{
	if (bt_secs == 0)
	{
		BTStopTheClock();

		// Here's where you put something useful that's
		// supposed to happen after the allotted time.
		document.write("<html><body><h3>Session Timeout</h3> Your session has timed out.</body></html>");

	// close taken out 12/5/2006
	//if (bt_popup == 'y')
	//  {
	//    self.close();
	//  }
	//alert("You have just wasted 10 seconds of your life.")
	}
	else
	{
		self.status = bt_secs;
		bt_secs = bt_secs - 1;
		bt_timerRunning = true;
		bt_timerID = self.setTimeout("BTStartTheTimer()", bt_delay);
	}
}

function moveHistoryForward()
{
	while (window.history.forward(1) != null)
	{
		window.history.forward(1);
	}
}

function explainDAW()
{
	alert('This renewal request asks that the prescription have the indication "Dispense As Written" (generic substitution not allowed).');
}

function explainSP()
{
	alert('This renewal request asks that the prescription have the indication "Substitution Permitted" (generic substitution allowed where available).');
}

function explainAcknowledgedUndeliverableMessage()
{
	alert("This prescription could not be transmitted to the pharmacy by any means. Appropriate action has already been taken.");
}

function explainUndeliverableMessage()
{
	alert("This prescription could not be transmitted to the pharmacy by any means. Please take appropriate measures. When you have done so, click 'Acknowledge' to indicate that action has been taken.");
}

/*
If window.event or thisEvent is a keypress of the return key (ascii 13),submit the form with name = thisFormName,
adding the given parameters to the request
and return false. Otherwise, return true.

If thisFormName is null, use thisField's form name.

The parameters should be in name=value&name=value format, see submitForm().

If ignoreBlankField is true, the form will be submitted only
if the field has a value that includes non-whitespace
characters.


*/
function submitOnEnter(thisField, thisEvent, otherParameters, thisFormName, ignoreBlankField)
{
	if (!thisField)
	{
		alert('submitOnEnter: thisField cannot be null.');
		return true;
	}

	//thisEvent is permitted to be null.
	if (!otherParameters)
		otherParameters = '';

	if (!thisFormName)
	{
		thisFormObject = thisField.form;
		if (!thisFormObject)
		{
			alert('submitOnEnter: no form name specified, and thisField has no form object.');
			return true;
		}

		thisFormName = thisFormObject.name;
	}

	if (!ignoreBlankField)
		ignoreBlankField = false;

	var keycode;
	if (window.event)
	{
		keycode = window.event.keyCode;
	}
	else if (thisEvent)
	{
		keycode = thisEvent.which;
	}
	else
	{
		return true;
	}

	if (keycode != 13)
		return true;

	//myfield.form.submit();
	if (ignoreBlankField)
	{
		var thisFieldValue = getValue(field);
		if (oneIsEmpty(thisFieldValue))
			return true;
	}

	debugAlert('submitting ' + thisFormName + ', params=' + otherParameters);
	submitForm(thisFormName, otherParameters);
	return false;
}

/*
If window.event or thisEvent is a keypress of the return key (ascii 13),click the button whose ID is given and return false.
Otherwise, return true.
If ignoreBlankField is true, the form will be submitted only
if the field has a value that includes non-whitespace
characters.

If the button does not exist, return false.
*/
function clickOnEnter(thisField, thisEvent, buttonID, ignoreBlankField)
{
	if (!thisField)
	{
		alert('clickOnEnter: thisField cannot be null.');
		return true;
	}

	//thisEvent is permitted to be null.
	if (!ignoreBlankField)
		ignoreBlankField = false;

	var keycode;
	if (window.event)
	{
		keycode = window.event.keyCode;
	}
	else if (thisEvent)
	{
		keycode = thisEvent.which;
	}
	else
	{
		return true;
	}

	if (keycode != 13)
		return true;

	if (ignoreBlankField)
	{
		var thisFieldValue = getValue(field);
		if (oneIsEmpty(thisFieldValue))
			return true;
	}

	var button = getElementById(buttonID);
	if (!button)
	{
		//alert('clickOnEnter: cannot find button for id ' + buttonID);
		return false;
	}

	button.click();
	return false;
}

/*
If window.event or thisEvent is a keypress of the return key (ascii 13),ignore it and return false.
Otherwise, return true.
This is used to prevent a return from submitting a form.
*/
function ignoreEnter(thisField, thisEvent)
{
	if (!thisField)
	{
		alert('submitOnEnter: thisField cannot be null.');
		return true;
	}

	//thisEvent is permitted to be null.
	var keycode;
	if (window.event)
	{
		keycode = window.event.keyCode;
	}
	else if (thisEvent)
	{
		keycode = thisEvent.which;
	}
	else
	{
		return true;
	}

	return (keycode != 13);
}

function walkingDots(elem, fontQualifier, minDots, maxDots, intervalMsec)
{
	if (elem.walkingDotsInterval)
		window.clearInterval(elem.walkingDotsInterval);
	if (!elem.walkingDotsNumDots)
		elem.walkingDotsNumDots = 0;

	elem.walkingDotsInterval = window.setInterval(function ()
	{
		elem.walkingDotsNumDots++;
		if (elem.walkingDotsNumDots > maxDots)
			elem.walkingDotsNumDots = 1;

		var dotString = "";
		for (dot = 0;dot < elem.walkingDotsNumDots;dot++)
		{
			dotString += ".";
		}

		elem.innerHTML = "<font " + fontQualifier + ">" + dotString + "</font>";
	},
	intervalMsec)
}

function stopWalkingDots(elem)
{
	if (elem.walkingDotsInterval)
		window.clearInterval(elem.walkingDotsInterval);
}

function trim(stringToTrim)
{
	if (!stringToTrim)
		return "";
	return stringToTrim.replace(/^\s+|\s+$/g, "");
}

function ltrim(stringToTrim)
{
	if (!stringToTrim)
		return "";

	return stringToTrim.replace(/^\s+/, "");
}

function rtrim(stringToTrim)
{
	if (!stringToTrim)
		return "";

	return stringToTrim.replace(/\s+$/, "");
}

function rxNormNDCMismatchWarning(rcopiaDrugName, refillDrugName, ndcDrugName, rxNormDrugName)
{
	alert('This refill request contained two drug codes that do not agree with one another. The drugs indicated by the codes and text in the request are "'+ ndcDrugName + '", "' + rxNormDrugName + '", and "' + refillDrugName + '". Rcopia has tentatively chosen "' + rcopiaDrugName + '" as the most likely possibility. If this choice is correct, no action is needed. Otherwise, please change this request, or deny it and send a new prescription.');
}

/**
* Sets the value of the prescription history source field to that of the selector.
*/
function updateMedicationSource()
{
	var field = document.getElementById("prescriptions_history_source"), 
	selector = document.getElementById("prescriptions_history_source_selector");
		
	field.value = selector.value;
}

function cleanExit() 
{
	if(timerID) {
		clearTimeout(timerID);
		timerID  = 0;
	}
}