package flinn.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import flinn.rcopia.model.RcExtResponseType;
import flinn.rcopia.service.RcopiaService;
import flinn.rcopia.service.RcopiaTransformationService;

public class DrFirstUtils {

	private static String portalName = ApplicationProperties.getProperty("rcopiaPortalSystemName");
	private static String service = ApplicationProperties.getProperty("flinn/service");
	private static String action = ApplicationProperties.getProperty("action");
	private static String startupScreen = ApplicationProperties.getProperty("startupScreen");
	private static String rcopiaPatientSystemName = ApplicationProperties.getProperty("rcopiaPatientSystemName");
	private static String closeWindow = ApplicationProperties.getProperty("closeWindow");
	private static String allowPopupScreens = ApplicationProperties.getProperty("allowPopupScreens");

//  Currently not used or provided via querystring parameters	
//	private static String rcopiaPracticeUserName = ApplicationProperties.getProperty("rcopiaPracticeUserName");
//	private static String logoutUrl = ApplicationProperties.getProperty("logoutUrl");

	private static final Logger LOG = Logger.getLogger(DrFirstUtils.class);
	
	public static String buildDrFirstUrl(String practiceUserName,
			String patientId, String userId, String logout) {
		
		// Sanity check patientId
		int pid = 0;
		try {
			if (patientId != null)
				pid = Integer.decode(patientId).intValue();
		} catch (NumberFormatException e) {
		}

		if (pid > 0) {
			// Make sure meds are up to date first...
			RcopiaService svc = new RcopiaService();
			RcopiaTransformationService transformation = new RcopiaTransformationService();

			try {
				RcExtResponseType rcResponse = svc.updateMedications(pid);
				String xml = transformation.convertRcopiaResponseToXml(rcResponse);
			} catch (Exception e) {
				LOG.error("Failure calling updateMedications from buildDrFirstUrl for patient "
						+ patientId);
				LOG.error(e.toString());
			}

			StringBuilder queryString = new StringBuilder();
			queryString.append("rcopia_portal_system_name=" + portalName);
			queryString.append("&rcopia_practice_user_name=" + practiceUserName);
			queryString.append("&rcopia_user_external_id=" + userId);
			queryString.append("&rcopia_patient_system_name=" + rcopiaPatientSystemName);
			queryString.append("&rcopia_patient_external_id=" + pid);
			queryString.append("&flinn.service=" + service);
			queryString.append("&action=" + action);
			queryString.append("&startup_screen=" + startupScreen);
			queryString.append("&close_window=" + closeWindow);
			queryString.append("&allow_popup_screens=" + allowPopupScreens);
			queryString.append("&logout_url=" + logout);
			queryString.append("&time=" + getTimestamp());
			queryString.append("&skip_auth=n");

			LOG.debug("Rcopia Access URL: " + queryString.toString());

			String MACValue = generateMD5Hash(queryString.toString(),
					ApplicationProperties.getProperty("secretKey"));
			
			queryString.append("&MAC=" + MACValue);
			String path = ApplicationProperties.getProperty("drFirstURL")
					+ queryString.toString();

			LOG.info("Accessing DrFirst for patient id "
					+ patientId
					+ " time of access "
					+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
							.format(new Date()));

			return path;
		}
		return logout;
	}

	public static String buildDrFirstMessageURL(String userId, String logout) {
		String path = "";

		try {

			// check to make sure a userId was provided
			if (userId == null) {
				throw new Exception("Invalid userId provided, value = null");
			}
			if (userId.isEmpty()) {
				throw new Exception("Empty userId provided");
			}

			// check to make sure a logout URL was provided
			if (logout == null) {
				throw new Exception("Invalid logout URL provided, value = null");
			}
			if (logout.isEmpty()) {
				throw new Exception("Empty logout URL provided");
			}

			// setup required parameters for this call
			String startupScreen = "message";
			String skipAuth = "n";
			String limpMode = "y";

			StringBuilder queryString = new StringBuilder();
			queryString.append("rcopia_portal_system_name=" + portalName);
			queryString.append("&rcopia_user_external_id=" + userId);
			queryString.append("&flinn.service=" + service);
			queryString.append("&action=" + action);
			queryString.append("&startup_screen=" + startupScreen);
			queryString.append("&limp_mode=" + limpMode);
			queryString.append("&logout_url=" + logout);
			queryString.append("&time=" + getTimestamp());
			queryString.append("&skip_auth=" + skipAuth);

			LOG.debug("Rcopia Message URL: " + queryString);

			String MACValue = generateMD5Hash(queryString.toString(),
					ApplicationProperties.getProperty("secretKey"));
			
			queryString.append("&MAC=" + MACValue);

			path = ApplicationProperties.getProperty("drFirstURL")
					+ queryString;
		} catch (Exception e) {
			LOG.debug("Error building message URL for DrFirst: "
					+ e.getMessage());
		}

		return path;
	}

	public static String buildDrFirstReportURL(String userId, String logout) {
		String path = "";

		try {

			// check to make sure a userId was provided
			if (userId == null) {
				throw new Exception("Invalid userId provided, value = null");
			}
			if (userId.isEmpty()) {
				throw new Exception("Empty userId provided");
			}

			// check to make sure a logout URL was provided
			if (logout == null) {
				throw new Exception("Invalid logout URL provided, value = null");
			}
			if (logout.isEmpty()) {
				throw new Exception("Empty logout URL provided");
			}

			// setup required parameters for this call
			String startupScreen = "report";
			String skipAuth = "n";
			String limpMode = "y";
			
			StringBuilder queryString = new StringBuilder();
			queryString.append("rcopia_portal_system_name=" + portalName);
			queryString.append("&rcopia_user_external_id=" + userId);
			queryString.append("&flinn.service=" + service);
			queryString.append("&action=" + action);
			queryString.append("&startup_screen=" + startupScreen);
			queryString.append("&limp_mode=" + limpMode);
			queryString.append("&logout_url=" + logout);
			queryString.append("&time=" + getTimestamp());
			queryString.append("&skip_auth=" + skipAuth);

			LOG.debug("Rcopia Report URL: " + queryString);

			String MACValue = generateMD5Hash(queryString.toString(),
					ApplicationProperties.getProperty("secretKey"));

			queryString.append("&MAC=" + MACValue);

			path = ApplicationProperties.getProperty("drFirstURL")
					+ queryString;

		} catch (Exception e) {
			LOG.debug("Error building report URL for DrFirst: "
					+ e.getMessage());
		}

		return path;
	}

	private static String getTimestamp() {
		SimpleDateFormat df = new SimpleDateFormat("MMddyyHHmmss");
		TimeZone tz = TimeZone.getTimeZone("GMT");
		Calendar time = new GregorianCalendar(tz);
		String timeStamp = df.format(time.getTime()).toString();
		return timeStamp;
	}

	private static String generateMD5Hash(String queryString, String secretKey) {
		String stringToHash = queryString + secretKey;
		String result = "";
		try {
			byte[] defaultBytes = stringToHash.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(defaultBytes, 0, defaultBytes.length);
			byte[] md5hash = md.digest();
			result = convertToHex(md5hash).toUpperCase();
		} catch (NoSuchAlgorithmException nsae) {
			LOG.error("NoSuchAlgorithmException encountered when generating md5 hash string for Dr. First"
					+ nsae);
		} catch (UnsupportedEncodingException ee) {
			LOG.error("UnsupportedEncodingException when generating MD5: " + ee);
		}

		return result;
	}

	private static String convertToHex(byte[] data) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					sb.append((char) ('0' + halfbyte));
				else
					sb.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return sb.toString();
	}

}
