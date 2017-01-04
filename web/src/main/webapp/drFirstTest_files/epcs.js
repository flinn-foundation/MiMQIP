// javascript-rcopia \ html \ javascript \ epcs.js
/**
 * @fileOverview Script containing variables and functions specific to EPCS.
 *
 * @author DrFirst (various)
 * @author mkotelba
 */

var signaturePasswordLabel = "Please enter your Signature Password.";
var signaturePasswordCancelLabel = "Are you sure you wish to cancel this transaction?";
var notTreatmentOfOpioidAddiction = "I confirm that this is not being prescribed for the treatment of opioid addiction";
var usernameObject = "provider_username";
var passwordObject = "signature_password";
var dataObject = "epcsData";
var signatureObject = "epcsSignature";
var keystoreObject = "epcsKeystore"
var locationObject = "provider_location";
var stateObject = "provider_state";
var organizationObject = "provider_organization";
var schedule2Object = "schedule2";
var scheduleLevel = "scheduleLevel";
var opioidKeyword = "isOpioid";
var epcsError_Connectivity = "Cannot communicate with Cryptokey.";
var epcsError_PasswordEntryCanceled = "Transaction has been canceled."

function epcsChange() 
{
	//Get the user's password.
	try
	{
		password = getSignaturePassword();
		if (password == "")
		{
			return true;
		}
	}
	catch (err)
	{
		return true;
	}
	return epcsInitialize();
}
function epcsInitialize()
{
	var password;
	var username;
	var location;
	var state;
	var organization;

	//Get the user's username.
	try
	{
		username = getUsername();
	}
	catch (err)
	{
		alert(epcsError_PasswordEntryCanceled);
		return false;
	}
	//Get the user's password.
	try
	{
		password = getSignaturePassword();
	}
	catch (err)
	{
		alert(epcsError_PasswordEntryCanceled);
		return false;
	}
	//Get the user's location.
	try
	{
		location = getLocation();
	}
	catch (err)
	{
		alert(epcsError_PasswordEntryCanceled);
		return false;
	}
	//Get the user's state.
	try
	{
		state = getState();
	}
	catch (err)
	{
		alert(epcsError_PasswordEntryCanceled);
		return false;
	}
	//Get the user's organization.
	try
	{
		organization = getOrganization();
	}
	catch (err)
	{
		alert(epcsError_PasswordEntryCanceled);
		return false;
	}

	//Intialize the token
	try
	{
		//alert(username + password + location + state + organization); 
		var xml = buildXMLInitialize(username, password, location, state, organization);
		var result = epcsFetch(xml);
		//manasee - added to relay error to caller page
		if (result == false)
		{
            alert(epcsError_Connectivity);
			return false;
		}
		//var textStart = "<Certificate><![CDATA[";
		//var textStop = "]]><\/Certificate>";
		//var posStart = result.indexOf(textStart) + textStart.length;
		//var posStop = result.indexOf(textStop);
		//var keystore = result.substr(posStart, posStop - posStart);
		document.getElementById(keystoreObject).value = result;
	}
	catch (err)
	{
		alert(err.description);
		return false;
	}
	return true;
}

function isOpioid(rxid)
{
	try
	{
		var opioidCheck = document.getElementById(opioidKeyword + rxid).value;
		return opioidCheck == "y";
	}
	catch (err)
	{
		return false;
	}
}

/**
 * Function for signing a prescription containing a Scheduled drug.
 * This function retrieves the password and drug data form the elements specified
 * by passwordObject and dataObject, respectively.  The signature is placed in
 * the element specified by signatureObject.
 */
function epcsSign()
{
	var foundEPCSData = false;
	var i=0;
	//Make sure there is data to sign
	var formObject = document.mainform.prescription_id;
	if (formObject == null)
	{
		formObject = document.reportform.prescription_id;
	}

	for (i = 0;i < formObject.length;i++)
	{
		if (formObject[i].checked)
		{
			var tmpEPCSData
			try
			{
				tmpEPCSData = document.getElementById(dataObject + formObject[i].value);
			}
			catch (err)
			{
				tmpEPCSData = null;
			}
			if (tmpEPCSData != null)
			{
				foundEPCSData = true;
			}
		}
	}
	if (!foundEPCSData)
	{
		tmpEPCSData = document.getElementById(dataObject + formObject.value);
		if (tmpEPCSData == null)
		{
			return true;
		}
		else
		{
			foundEPCSData = true;
		}
	}
	if (!foundEPCSData)
	{
		return false;
	}
	//Get the user's password.
	var password;
	try
	{
		password = getSignaturePassword();
		if (password == '')
		{
			alert(signaturePasswordLabel);
			return false;
		}
	}
	catch (err)
	{
		alert(epcsError_PasswordEntryCanceled);
		return false;
	}

	//Create the signature
	if (formObject.length > 0)
	{
		for (i = 0;i < formObject.length;i++)
		{
			if (!processDataBlock(formObject[i], password, foundEPCSData))
			{
				return false;
			}
		}
		return true;
	}
	else
	{
		return processDataBlock(formObject, password, foundEPCSData);
	}
}

function processDataBlock(formObject, password, foundEPCSData) 
{
	if (formObject.checked)
	{
		tmpEPCSData = "";
		try
		{
			tmpEPCSData = document.getElementById(dataObject + formObject.value);
		}
		catch (err)
		{
			tmpEPCSData = null;
		}
		if (!foundEPCSData)
		{
			tmpEPCSData = document.getElementById(dataObject + formObject.value);
		}
		if (tmpEPCSData != null)
		{
			if (isOpioid(formObject.value))
			{
				correctly = confirm(notTreatmentOfOpioidAddiction);
				if (!correctly)
				{
					return false;
				}
			}
			try
			{
				var data = tmpEPCSData.value//document.getElementById(dataObject).value;
				var xml = buildXMLSign(data, password)
				var signature = epcsFetch(xml);
				var textStart = "<Signature><![CDATA[";
				var textStop = "]]><\/Signature>";
				var posStart = signature.indexOf(textStart) + textStart.length;
				var posStop = signature.indexOf(textStop);
				var signature2 = signature.substr(posStart, posStop - posStart);
				document.getElementById(signatureObject + formObject.value).value = signature2;
			}
			catch (err)
			{
				alert(err.description);
				return false;
			}
		}
		return true;
	}
	return true;
}
function verifiyThereIsData()
{
	var data = document.getElementById(dataObject).value;
}

function buildXMLInitialize(username, password, location, state, organization)
{
	
	var xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	xml = xml + "<DigitalRxSignerRequest>";
	xml = xml + "<Command>InitToken<\/Command>";
	xml = xml + "<TokenPassword>" + password + "<\/TokenPassword>";
        xml = xml + "<UserInformation>";
        xml = xml + "<CommonName>" + username + "<\/CommonName>";
        xml = xml + "<Country>US<\/Country>";
        xml = xml + "<Location>" + location + "<\/Location>";
        xml = xml + "<State>" + state + "<\/State>";
        xml = xml + "<Organization>" + organization + "<\/Organization>";
        xml = xml + "<\/UserInformation>";
	xml = xml + "<\/DigitalRxSignerRequest>";
	return xml;
}

function buildXMLSign(data, password)
{
	var xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	xml = xml + "<DigitalRxSignerRequest>";
	xml = xml + "<Command>sign<\/Command>";
	xml = xml + "<TokenPassword>" + password + "<\/TokenPassword>";
	xml = xml + "<DocumentList>";
	xml = xml + "<Document>";
	xml = xml + "<Identifier>abc-12345<\/Identifier>";
	xml = xml + "<Data><![CDATA[" + data + "]]><\/Data>";
	xml = xml + "<\/Document>";
	xml = xml + "<\/DocumentList>";
	xml = xml + "<\/DigitalRxSignerRequest>";
	return xml;
}

/**
 * Sends the specified command to DFDigitalSigner for processing.
 * @throws Exception if unable to process.
 */
function epcsFetch(xml)
{
	//Check Connectivity to DFDigitalSigner
	try
	{
		verifyConnectivityToDFDigitalSigner();
	}
	catch (err)
	{
        alert(epcsError_Connectivity);
		alert(err.description);
		throw err;
	}

	try
	{
		DFDigitalSigner.execComand(xml);
		rc = DFDigitalSigner.resultCode;
		if (rc == 0)
		{
			var output = DFDigitalSigner.getOutput();
			return output;
		}
		else
		{
			//manasee added for checking token not found problem
			var err = new Error();
			if (rc == 23)
			{
				err.description = 'Please make sure your EPCS Token is inserted correctly.';
			}
			else if (rc == 21)
			{
				err.description = 'Please enter a valid password.';
			}
			else
			{
				err.description = 'Error communicating with the EPCS Token.';
			}
			throw err;
		}
	}
	catch (err)
	{
		throw err;
	}
}

/**
 * Verifies connectivity with the DFDigitalSigner ActiveX object.
 * @throws Exception thrown if unable to communicate.
 */
function verifyConnectivityToDFDigitalSigner()
{
	try
	{
		var cmd = "<DigitalRxSignerRequest><Command>hello<\/Command><\/DigitalRxSignerRequest>";
		DFDigitalSigner.execComand(cmd);
	//alert(DFDigitalSigner.getOutput());
	}
	catch (err)
	{
		throw err;
	}
}

function getSignaturePassword()
{
	//Get the password
	var password = "";
	try
	{
		password = document.getElementById(passwordObject).value;
		if (password == '')
		{
			var frmObject = document.reportform.signature_password;
			for (var i = 0;i < frmObject.length;i++)
			{
				if (frmObject[i].value != '')
				{
					password = frmObject[i].value;
				}
			}
		}
	}
	catch (err)
	{
		try
		{
			password = document.getElementById("new_signature_password").value;
		}
		catch (err)
		{
			password = "";
		}
	}
	var skip = false;
	//  while (!skip && (password == null || password == '') )
	//  {
	//    password = prompt(signaturePasswordLabel,'');
	//    if (password == null || password == '')
	//    {
	//      skip = confirm(signaturePasswordCancelLabel);
	//      if (skip)
	//      {
	//        throw err;
	//      }
	//    }
	//  }
	//  try
	//  {
	//    document.getElementById(passwordObject).value = password;
	//  }
	//  catch(err)
	//  {
	//    password = password;
	//  }
	return password;
}

function getUsername()
{
	//Get the Username
	var username = "";
	try
	{
		username = document.getElementById(usernameObject).value;
	}
	catch (err)
	{
		username = "";
	}
	return username;
}

function getLocation()
{
	//Get the Location
	var location = "";
	try
	{
		location = document.getElementById(locationObject).value;
	}
	catch (err)
	{
		location = "";
	}
	return location;
}

function getState()
{
	//Get the State
	var state = "";
	try
	{
		state = document.getElementById(stateObject).value;
	}
	catch (err)
	{
		state = "";
	}
	return state;
}

function getOrganization()
{
	//Get the Organization
	var organization = "";
	try
	{
		organization = document.getElementById(organizationObject).value;
	}
	catch (err)
	{
		organization = "";
	}
	return organization;
}

/**
 * Function for verifying the pin entered with the token. This method verifies the
 * password entered with the one in the token.
 */
function epcsVerify()
{
	var password;

	//Get the user's password.
	try
	{
		password = getSignaturePassword();
	}
	catch (err)
	{
		alert(epcsError_PasswordEntryCanceled);
		return false;
	}
	//Verify the token
	try
	{
		var xml = buildXMLVerify(password);
		var result = epcsFetch(xml);
		if (result == false)
		{
            alert(epcsError_Connectivity);
			return false;
		}
	}
	catch (err)
	{
		alert(err.description);
		return false;
	}
	return true;
}

/*
 * Function to build the XML for token verification
 * */
function buildXMLVerify(password)
{
	var xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	xml = xml + "<DigitalRxSignerRequest>";
	xml = xml + "<Command>check_cryptokey_password<\/Command>";
	xml = xml + "<TokenPassword>" + password + "<\/TokenPassword>";
	xml = xml + "<\/DigitalRxSignerRequest>";
	return xml;
}
