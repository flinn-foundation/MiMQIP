// javascript-rcopia \ html \ javascript \ PDFPrinting.js
/**
 * @fileOverview Script containing variables and functions specific to PDF printing in Rcopia.
 * 
 * @author mkotelba
 */

/**
 * @class Abstract utility class containing methods specific to PDF printing.
 */
var PDFPrinting = Class.create("PDFPrinting");

Object.extend(PDFPrinting,
/** @lends PDFPrinting */
{
	pdfElements:
	[
		"pdfContinue",
		"pdfWindow"
	],

	medicationQueryElements:
	[
		"medicationQueryTable"
	],

	mainElements:
	[
		"control_panel",
		"medication_selection_table",
		"medication_management_table",
		"allergy_management_table",
		"status_bar_pratice",
		"status_bar_patient",
		"status_bar_test_message",
		"status_bar_message",
		"status_bar_notify_message",
		"pending_prescription_table"
	],

	onMedicationQuery: function ()
	{
		return $("#medicationQueryTable") != null;
	},

	createPDFWindow: function (url)
	{
		var pdfWindow = document.createElement("iframe");
		pdfWindow.setAttribute("src", url);
		pdfWindow.setAttribute("type", "application/pdf");
		pdfWindow.setAttribute("id", "pdfWindow");

		$("body").insertBefore(pdfWindow, $("#pdfContinue").nextSibling);
	},

	hidePDFElements: function ()
	{
		for (var a = 0; a < PDFPrinting.pdfElements.length; a++)
		{
			var element = $("#" + PDFPrinting.pdfElements[a]);

			if (element != undefined)
			{
				element.style.display = "none";
			}
		}
	},

	showPDFElements: function ()
	{
		for (var a = 0; a < PDFPrinting.pdfElements.length; a++)
		{
			var element = $("#" + PDFPrinting.pdfElements[a]);

			if (element != undefined)
			{
				element.style.display = "";
			}
		}
	},

	showMainElements: function ()
	{
		for (var a = 0; a < PDFPrinting.mainElements.length; a++)
		{
			var element = $("#" + PDFPrinting.mainElements[a]);

			if (element != undefined)
			{
				element.style.display = "";
			}
		}
	},

	hideMainElements: function ()
	{
		for (var a = 0; a < PDFPrinting.mainElements.length; a++)
		{
			var element = $("#" + PDFPrinting.mainElements[a]);

			if (element != undefined)
			{
				element.style.display = "none";
			}
		}
	},

	hideMedicationQueryElements: function ()
	{
		for (var a = 0; a < PDFPrinting.medicationQueryElements.length; a++)
		{
			var element = $("#" + PDFPrinting.medicationQueryElements[a]);

			if (element != undefined)
			{
				element.style.display = "none";
			}
		}
	},

	switchToMainElements: function ()
	{
		$("#pdfWindow", null, true).src = "";

		PDFPrinting.hidePDFElements();
		PDFPrinting.showMainElements();
	}
});