// javascript-rcopia \ html \ javascript \ rcopia_help.js
/**
 * @fileOverview Script containing variables and functions specific to user help in Rcopia.
 *
 * @author DrFirst (various)
 * @author mkotelba
 */

function explainEligibility(formObject)
{
	alert('This patient has insurance information automatically obtained from his or her pharmacy benefits program. This allows automated formulary checking and enables the Medication History link below.');
}

function explainAddToMeds()
{
	alert("Select this checkbox to add each medication to its patient's medication list when sent or saved.");
}

function explainPrintPharmacy()
{
	alert("Select this checkbox to have the pharmacy name printed above the prescription. This has no effect for prescriptions that are not printed.");
}

function explainInsuranceWarnings()
{
	alert("Exclusion Warnings: Not Reimbursed, Non-formulary, Benefit Exclusion, Specific Drug Exclusion.\n"
		+ "Intermediate Warnings: Prior Authorization, Medical Necessity, Step Therapy, and Tiers higher than 1.\n"
		+ "Minor Warnings: Quantity Limits, Age Limits, Gender Limits, Tier 1, and all others.\n");
}
