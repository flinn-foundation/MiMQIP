// javascript-rcopia \ html \ javascript \ rcopia_dose_calculator.js
/**
 * @fileOverview Script containing variables and functions specific to the dose calculator in Rcopia.
 *
 * @author DrFirst (various)
 * @author mkotelba
 */

var digits = "0123456789.";

var dcDebugOn = false;

function dcDebugAlert(message)
{
	if (dcDebugOn)
		alert(message);
}

function doseCalcChangeCalculation(selectObj)
{
	var calc = getValue(selectObj);
	var values = calc.split("/");
	var doseCalcDoseUnitObject = getElementByID("dosecalc_dose_unit");
	doseCalcDoseUnitObject.innerHTML = values[0];
	var doseCalcWeightUnitObject = getElementByID("dosecalc_weight_unit");
	doseCalcWeightUnitObject.innerHTML = values[1];
	var doseCalcIntervalUnitObject = getElementByID("dosecalc_interval_unit");
	doseCalcIntervalUnitObject.innerHTML = values[2];

	setValueByName("dosecalc_weight","1");
	setValueByName("dosecalc_interval","1");

	calculateDose();
}

function isZero(value)
{
	return (!value || isNaN(value) || (value <= 0));
}

function parseStrength(strength)
{
	if (!strength)
		return null;

	var tokens = new Array();
	var slash = strength.indexOf("/");
	if (slash != -1)
	{
		var numerator = strength.substring(0,slash);
		var denominator = strength.substring(slash+1);
		var numeratorTokens = parseStrengthNumerator(numerator);
		var denominatorTokens = parseStrengthDenominator(denominator);
		tokens[0] = numeratorTokens[0];
		tokens[1] = numeratorTokens[1];
		tokens[2] = numeratorTokens[2];
		tokens[3] = denominatorTokens[0];
		tokens[4] = denominatorTokens[1];
	}
	else
	{
		var numerator = strength;
		var numeratorTokens = parseStrengthNumerator(numerator);
		tokens[0] = numeratorTokens[0];
		tokens[1] = numeratorTokens[1];
		tokens[2] = numeratorTokens[2];
		tokens[3] = "1";
		tokens[4] = numeratorTokens[0];
	}

	return tokens;
}

function parseStrengthNumerator(strength) 
{
	var form = "";
	var tokens = new Array();
	tokens[0] = "";
	tokens[1] = "";
	tokens[2] = "";

	var drugStrengthRE = /^\s*([\w ]+)\s+([\d\.]+)\s*(\w*)/
	var values = drugStrengthRE.exec(strength);
	tokens[0] = values[1].toLowerCase();
	tokens[1] = values[2];
	tokens[2] = values[3];
	return tokens;
}

function parseStrengthDenominator(strength) 
{
	var form = "";
	var tokens = new Array();
	tokens[0] = "";
	tokens[1] = "";

	var drugStrengthRE = /^\s*([\d\.]+)\s*(\w*)/
	var values = drugStrengthRE.exec(strength);
  
	if (values != null)
	{
		tokens[0] = values[1];
		tokens[1] = values[2];
	}
	else
	{
		tokens[0] = "1";
		tokens[1] = trim(strength);
	}

	return tokens;
}


/*

  if (!strength)
    return tokens;

  var start = skipToDigit(strength, 0);
  if (start == -1)
    return tokens;

  var stop = skipDigits(strength, start);
  if (stop == -1)
  {
    tokens[0] = strength.substring(start);
    return tokens;
  }

  tokens[0] = strength.substring(start, stop);
  tokens[1] = strength.substring(stop+1).trim();
  return tokens;
}

function skipToDigit(text, start)
{
  var len = text.length;
  var index = start;
  for(; index < len; index++)
  {
    if (isDigit(text.charAt(index)))
      return index;
  }
  
  return -1;
}

function skipDigits(text, start)
{
  var len = text.length;
  var index = start;
  for(; index < len; index++)
  {
    if (!isDigit(text.charAt(index)))
      return index;
  }
  
  return -1;
}

function isNumeric(text)
{
  for (index = 0; index < text.length; index++) 
  { 
    var oneChar = text.charAt(index); 
    if (!isDigit(oneChar))
      return false;
  }
    
  return true;
}

function isDigit(oneChar)
{
  return (digits.indexOf(oneChar) != -1);
}
*/

function howManyOneInTwo(one, two, tryingReverse)
{
	if (!one || one == "")
		return 1;

	if (!two || two == "")
		return 1;

	one = expandAbbreviation(one.toLowerCase());
	two = expandAbbreviation(two.toLowerCase());

	//  alert('howmany, one=' + one + ', two=' + two + ',rev=' + tryingReverse);
	if (one == two)
		return 1;

	if ((one == "milliliter") && (two == "teaspoon"))
		return 5;
	if ((one == "milliliter") && (two == "tablespoon"))
		return 15;
	if ((one == "milliliter") && (two == "ounce"))
		return 30;
	if ((one == "teaspoon") && (two == "ounce"))
		return 6;
	if ((one == "tablespoon") && (two == "ounce"))
		return 2;
	if ((one == "hour") && (two == "day"))
		return 24;
	if ((one == "pound") && (two == "kilogram"))
		return 2.2;
	if ((one == "ounce") && (two == "kilogram"))
		return 33.2;
	if ((one == "gram") && (two == "kilogram"))
		return 1000;
	if ((one == "milligram") && (two == "gram"))
		return 1000;
	if((one == "milliliter") && (two == "liter"))
		return 1000;
	if ((one == "microgram") && (two == "gram"))
		return 1000000;
	if ((one == "microgram") && (two == "milligram"))
		return 1000;

	if (tryingReverse)
		return -1;  //TODO

	var result = howManyOneInTwo(two, one,true);
	return 1 / result;
}

function expandAbbreviation(unit)
{
	if (!unit)
		return "";

	if (unit == "ml")
		return "milliliter";
	if (unit == "mg")
		return "milligram";
	if (unit == "g")
		return "gram";
	if (unit == "oz")
		return "ounce";
	if (unit == "lb")
		return "pound";
	if (unit == "kg")
		return "kilogram";
	if (unit == "tsp")
		return "teaspoon";
	if (unit == "tbs")
		return "tablespoon";

	return unit;
}

var drugForm;
var strengthNumerator;
var strengthNumeratorUnit;
var strengthDenominator;
var strengthDenominatorUnit;


function calculateDose()
{
	var doseCalcResultsDose = getElementByID("dosecalc_results_dose");
	var doseCalcResultsRoundedDose = getElementByID("dosecalc_results_rounded_dose");

	if(!doseCalcResultsDose || doseCalcResultsDose == "")
		return;

	if(!doseCalcResultsRoundedDose || doseCalcResultsRoundedDose == "")
		return;

	doseCalcResultsDose.innerHTML = "";
	doseCalcResultsRoundedDose.innerHTML = "";
	setValueByID("dosecalc_results_dose_unit", "");
	setValueByID("dosecalc_results_frequency", "");
	setValueByID("dosecalc_results_action", "(More Data Required)");
	getElementByID("dosecalc_apply_button").disabled = true;

	var patientWeight = getValueByName("patient_weight_kg");
	if (isZero( patientWeight))
		return;
	var desiredDose = getValueByName("dosecalc_dose");
	if (isZero( desiredDose))
		return;
	var desiredDoseUnit = getValueByID("dosecalc_dose_unit");
	if (!desiredDoseUnit)
		return;
	var desiredWeight = getValueByName("dosecalc_weight");
	if (isZero( desiredWeight))
		return;
	var desiredWeightUnit = getValueByID("dosecalc_weight_unit");
	if (!desiredWeightUnit)
		return;
	var desiredInterval = getValueByName("dosecalc_interval");
	if (isZero( desiredInterval))
		return;
	var desiredIntervalUnit = getValueByID("dosecalc_interval_unit");
	if (!desiredIntervalUnit)
		return;

	var giveUnit = getValueByName("dosecalc_give_unit");
	if (!giveUnit)
		return;

	var frequencyDesc = getValueByName("dosecalc_frequency");
	if (!frequencyDesc)
		return;

	var frequency = getFrequency(frequencyDesc);
	if (frequency <= 0)
	{
		setValueByID("dosecalc_results_action", "(Frequency is not a specific interval)");
		return;
	}

	setStrengthValues();

	if (strengthNumerator == "")
		return;

	if (isNaN(strengthNumerator))
	{
		setValueByID("dosecalc_results_action", "(Strength Amount is not a number)");
		return;
	}

	if (isNaN(strengthDenominator))
	{
		setValueByID("dosecalc_results_action", "(Strength Amount is not a number)");
		return;
	}

	var daysPerFactor = howManyOneInTwo("day", desiredIntervalUnit);
	if (daysPerFactor <= 0)
	{
		setValueByID("dosecalc_results_action", "(Cannot convert time interval to days)");
		return;
	}

	var kilosPerFactor = howManyOneInTwo("kg", desiredWeightUnit);
	
	if(kilosPerFactor <= 0)
	{
		setValueByID("dosecalc_results_action", "(Cannot convert weight interval to kilograms)");
		return;
	}

	var daysPerInterval = desiredInterval * daysPerFactor;
	var kilosPerWeight = desiredWeight * kilosPerFactor;
	var dailyDoseInDesiredUnits = (desiredDose * patientWeight)
	/ (daysPerInterval * kilosPerWeight);


	dcDebugAlert(
		"giveUnit=" + giveUnit
		+ ", drugForm=" + drugForm
		+ ", strengthNumerator=" + strengthNumerator
		+ ", strengthNumeratorUnit=" + strengthNumeratorUnit
		+ ", strengthDenominator=" + strengthDenominator
		+ ", strengthDenominatorUnit=" + strengthDenominatorUnit
		+ ", desiredInterval=" + desiredInterval
		+ ", daysPerFactor=" + daysPerFactor
		+ ", daysPerInterval=" + daysPerInterval
		+ ", desiredWeight=" + desiredWeight
		+ ", kilosPerFactor=" + kilosPerFactor
		+ ", kilosPerWeight=" + kilosPerWeight
		+ ", desiredDose=" + desiredDose
		+ ", patientWeight=" + patientWeight
		+ ", daysPerInterval=" + daysPerInterval
		+ ", dailyDoseInDesiredUnits=" + dailyDoseInDesiredUnits
		);

	var giveUnitsPerDrugStrength = getUnitsPerDrugStrength(giveUnit, "give");
	if (giveUnitsPerDrugStrength <= 0)
		return;

	var desiredDoseUnitsPerDrugStrength = getUnitsPerDrugStrength(desiredDoseUnit, "guideline");
	if (desiredDoseUnitsPerDrugStrength <= 0)
		return;

	var dailyDoseInGiveUnits = dailyDoseInDesiredUnits
	* giveUnitsPerDrugStrength / desiredDoseUnitsPerDrugStrength;

	var dividedDoseInGiveUnits = dailyDoseInGiveUnits  / frequency;
  
	var trimmedDose = trimDose(dividedDoseInGiveUnits);
	var roundedDose = roundDose(dividedDoseInGiveUnits);

	if (trimmedDose == roundedDose)
	{
		doseCalcResultsDose.innerHTML = "&nbsp;";
		doseCalcResultsRoundedDose.innerHTML = trimmedDose;
	}
	else
	{
		doseCalcResultsDose.innerHTML = "&nbsp;(" + trimmedDose + ") ";
		doseCalcResultsRoundedDose.innerHTML = roundedDose;
	}

	dcDebugAlert("giveUnitsPerDrugStrength=" + giveUnitsPerDrugStrength
		+ ", desiredDoseUnitsPerDrugStrength=" + desiredDoseUnitsPerDrugStrength
		+ ", dailyDoseInGiveUnits=" + dailyDoseInGiveUnits
		+ ", dividedDoseInGiveUnits=" + dividedDoseInGiveUnits
		+", trimmedDose=" + trimmedDose
		+", roundedDose=" + roundedDose);



	setValueByID("dosecalc_results_dose_unit", giveUnit);
	setValueByID("dosecalc_results_frequency", frequencyDesc);
	setValueByID("dosecalc_results_action", "Give");

	getElementByID("dosecalc_apply_button").disabled = false;
}

function setStrengthValues()
{
	var drugID = getDoseCalcDrugID();
	if (doseCalculatorShouldBeFreeStrength())
	{
		drugForm = "";
		strengthNumerator = getValueByName("dosecalc_strength_amount");
		strengthNumeratorUnit = getValueByName("dosecalc_strength_unit");
		strengthDenominator = getValueByName("dosecalc_strength_per_amount");
		strengthDenominatorUnit = getValueByName("dosecalc_strength_per_unit");
	}
	else //not free strength
	{
		var drugIndex = getDoseCalcDrugIndex(drugID);
		if (drugIndex < 0)
			return;

		drugForm = doseCalcDrugForms[drugIndex].toLowerCase();
		strengthNumerator = doseCalcDrugStrengthAmounts[drugIndex];
		strengthNumeratorUnit = doseCalcDrugStrengthUnits[drugIndex];
		strengthDenominator = doseCalcDrugPerAmounts[drugIndex];
		strengthDenominatorUnit = doseCalcDrugPerUnits[drugIndex];
	}

	if (strengthDenominator == "")
		strengthDenominator = 1;
}

/* Try to convert the drug strength into the given units. 
     Try the strength denominator unit first, if any, 
     then the strength numerator unit, then the drug form. 
     If none succeed, indicate to the user that the strength cannot be converted to give units and return.
     Otherwise, set giveUnitsPerStrength to the number of giveUnits in one drug strength. 
     For example, if the drug were Solution 1 gram/250 ml,
     The units per strength would be the number of given units in one gram, or 250 ml.
*/

function getUnitsPerDrugStrength(unitToConvert, description)
{
	var unitsPerDrugStrength = 0;

	if (strengthDenominatorUnit != "")
	{
		unitsPerDrugStrength = howManyOneInTwo(unitToConvert, strengthDenominatorUnit) * strengthDenominator;
	}

	if (unitsPerDrugStrength <= 0)
	{
		unitsPerDrugStrength = howManyOneInTwo(unitToConvert, strengthNumeratorUnit) * strengthNumerator;
	}

	if (unitsPerDrugStrength <= 0)
	{
		if (drugForm != "")
			unitsPerDrugStrength = howManyOneInTwo(unitToConvert, drugForm);
	}

	if (unitsPerDrugStrength <= 0)
	{
		setValueByID("dosecalc_results_action", "(Cannot convert drug strength to " + description + " units)");
	}

	return unitsPerDrugStrength;
}


function getDoseCalcDrugIndex(drugID)
{
	for (var index = 0; index < doseCalcDrugIDs.length; index++)
	{
		if (doseCalcDrugIDs[index] == drugID)
			return index;
	}

	return -1;
}


function getDoseCalcDrugID()
{
	var strengthObject = getElementByName("dosecalc_formula_options");
	if (strengthObject)
		return getValue(strengthObject);
	else
		return getValueByName("dose_calculator_drug_id");
}

function getDoseCalcStrength()
{
	var strengthObject = getElementByName("dosecalc_formula_options");
	if (strengthObject)
	{
		return getSelectorText(strengthObject);
	}
	else
	{
		return getValueByID("dosecalc_formula_text");
	}
}

function trimDose(dose)
{
	if (dose == null)
		return "";

	var doseString = "" + dose;
	var period = doseString.indexOf('.');
	if (period == -1)
		return doseString;

	if ((doseString.length - period) > 3)
	{
		return doseString.substring(0, period+3);
	}

	return doseString;
}

function roundDose(dose)
{
	if (dose == null)
		return "";

	if (dose <= 0)
		return "";

	var doseString = "" + dose;
	var period = doseString.indexOf('.');
	if (period == -1)
		return doseString;

	var numeral = parseInt(doseString.substring(0, period));
	var decimal = parseFloat(doseString.substring(period));

	var numString = "";
	if (numeral > 0)
		numString = "" + numeral;

	if (decimal > 0.87)
		return "" + (numeral + 1);

	if (decimal > 0.62)
		return numString + " 3/4";

	if (decimal > 0.37)
		return numString + " 1/2";

	if (decimal > 0.12)
		return numString + " 1/4";

	return trimDose(doseString);
}


function applyDoseCheckValues()
{
	//  alert("applydosecheckvalues");
	var drugID = getDoseCalcDrugID();
	var doseCalcResultsDose = getValueByID("dosecalc_results_dose");
	var doseCalcResultsRoundedDose = getValueByID("dosecalc_results_rounded_dose");
	var doseCalcResultsDoseUnit = getValueByID("dosecalc_results_dose_unit");
	var doseCalcResultsFrequency = getValueByID("dosecalc_results_frequency");

	if ((doseCalcResultsRoundedDose == "")
		|| (doseCalcResultsDoseUnit == "")
		|| (doseCalcResultsFrequency == ""))
		{
		return false;
	}

	if (drugID > 0)
		setValueByName("prescriptions_drug_id", drugID);

	setValueByName("prescriptions_dose", doseCalcResultsRoundedDose);
	setValueByName("prescriptions_dose_unit", doseCalcResultsDoseUnit);
	setValueByName("prescriptions_dose_timing", doseCalcResultsFrequency);
	changeDose();
	changeDoseUnit();
	changeDoseTiming();

	//  setQuantity(true, true);
	return false;
}

function setDoseCalculatorStrength()
{
	var drugID = getValueByName("prescriptions_drug_id");
	var formulaOptions = getElementByName("dosecalc_formula_options");
	if (formulaOptions)
		setValue(formulaOptions, drugID);

	calculateDose();
}

function setDoseCalculatorFrequency()
{
	var freq = getValueByName("prescriptions_dose_timing");
	setValueByName("dosecalc_frequency", freq);
	calculateDose();
}

function resetDoseCalculator()
{
	if(!doseCalculatorExists())
		return;

	hideDoseCalculator();
	setDoseCalculatorStrength();
	setValueByName("dosecalc_dose", "");
	setValueByName("dosecalc_weight", "1");
	setValueByName("dosecalc_interval", "1");

	var freq = getValueByName("prescriptions_dose_timing");
	setValueByName("dosecalc_frequency", freq);
	calculateDose();
}

function doseCalculatorExists()
{
	var doseCalculatorObj = getElementById("dose_calc_table", true);
	return (doseCalculatorObj != null);
}

function keyPressInDoseCalc(element, event)
{
	if (ignoreEnter(element, event))
	{
		return true;
	}
	else
	{
		calculateDose();
		//    alert('keypresindc,returning false');
		return false;
	}
}

function toggleDoseCalculator()
{
	if (getValueByID("dose_calculator_toggle") == "Hide Dose Calculator")
		hideDoseCalculator();
	else
		showDoseCalculator();
}

function showDoseCalculator() 
{
	var doseCalculatorObj = getElementById("dose_calc_table");
	doseCalculatorObj.style.display = "";
	doseCalculatorObj.style.visibility="visible";
	setValueByID("dose_calculator_toggle", "Hide Dose Calculator");
	determineDoseCalculatorFreeStrength();
}

function hideDoseCalculator() 
{
	var doseCalculatorObj = getElementById("dose_calc_table");
	doseCalculatorObj.style.display = "none";
	doseCalculatorObj.style.visibility="hidden";
	setValueByID("dose_calculator_toggle", "Show Dose Calculator");
}

function hideDoseCalculatorFreeStrength()
{
	var freeStrengthObj = getElementByID("dosecalc_free_strength");
	freeStrengthObj.style.display = "none";
	freeStrengthObj.style.visibility="hidden";

	return false;
}

function showDoseCalculatorFreeStrength()
{
	var freeStrengthObj = getElementByID("dosecalc_free_strength");
	freeStrengthObj.style.display = "";
	freeStrengthObj.style.visibility="visible";

	return false;
}

function determineDoseCalculatorFreeStrength()
{
	if (doseCalculatorShouldBeFreeStrength())
		showDoseCalculatorFreeStrength();
	else
		hideDoseCalculatorFreeStrength();
}

function doseCalculatorShouldBeFreeStrength()
{
	//alert('shouldbefreestr');
	var drugID = getDoseCalcDrugID();
	if (drugID <= 0)
		return true;

	var drugIndex = getDoseCalcDrugIndex(drugID);

	if (drugIndex < 0)
		return true;

	var strengthNumerator = doseCalcDrugStrengthAmounts[drugIndex];
	if(isNaN(strengthNumerator))
		return true;

	var strengthNumeratorUnit = doseCalcDrugStrengthUnits[drugIndex];
	var strengthDenominatorUnit = doseCalcDrugPerUnits[drugIndex];

	if (!strengthUnitConvertsToGuidelineUnit(strengthNumeratorUnit)
		&& !strengthUnitConvertsToGuidelineUnit(strengthDenominatorUnit))
		{
		return true;
	}

	if (!strengthUnitConvertsToGiveUnit(strengthNumeratorUnit)
		&& !strengthUnitConvertsToGiveUnit(strengthDenominatorUnit))
		{
		return true;
	}

	return false;
}

function strengthUnitConvertsToGuidelineUnit(strengthUnit)
{
	if (!strengthUnit || (strengthUnit == ""))
		return false;

	var guidelineObject = getElementByName("dosecalc_guideline");
	var guidelineOptions = guidelineObject.options;
	for (var index = 0; index < guidelineOptions.length; index++)
	{
		var guidelineOption = guidelineOptions[index];
		var guideline = getSelectorOptionValue(guidelineOption);
		if(!guideline || (guideline == ''))
			continue;

		var guidelineValues = guideline.split("/");
		var guidelineDoseUnit = guidelineValues[0];
		var conversion = howManyOneInTwo(guidelineDoseUnit, strengthUnit);
		if (conversion > 0)
			return true;
	}

	return false;
}

function strengthUnitConvertsToGiveUnit(strengthUnit)
{
	var giveUnitObject = getElementByName("dosecalc_give_unit");
	var giveUnitOptions = giveUnitObject.options;
	for (var index = 0; index < giveUnitOptions.length; index++)
	{
		var giveUnitOption = giveUnitOptions[index];
		var giveUnit = getSelectorOptionValue(giveUnitOption);
		if(!giveUnit || (giveUnit == ''))
			continue;

		var conversion = howManyOneInTwo(giveUnit, strengthUnit);
		if (conversion > 0)
			return true;
	}

	return false;
}




