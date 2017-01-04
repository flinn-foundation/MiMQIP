package flinn.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import flinn.beans.request.RequestContainerBean;
import flinn.beans.response.ErrorContainerBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;

public class PatientLog {
	private static final Logger logger = Logger.getLogger(PatientLog.class);
	private static String baseDir = null;
	private static String filename = "/accesslog/<patientID>/logfile.log";
	private static PatternLayout layout = null;

	public static void initialize() {
		baseDir = ApplicationProperties.getProperty("loggingBaseDir");
		if (baseDir.isEmpty()) {
			// if baseDir is not specified use default location; AppServer/bin
			baseDir = ".";
		}
		filename = baseDir + filename;

		String pattern = "%d %m %n";
		layout = new PatternLayout(pattern);
	}

	public static synchronized void WriteEntry(RequestContainerBean input,
			HttpServletRequest request, ResponseContainerBean response,
			ResponseSessionContainerBean session, String options) {
		try {

			// initialize class properties
			if (baseDir == null) {
				initialize();
			}

			// get patient ID. Use "0" if patient object is not valid
			String patientID = "0";
			if (input.getPatient() != null) {
				patientID = String.valueOf(input.getPatient().getPatientid());
			}
			String logname = filename.replaceAll("<patientID>", patientID);

			// check to see if the logger and appender objects have been created
			Logger patientLogger = Logger.getLogger(patientID);
			patientLogger.setLevel(Level.INFO);
			patientLogger.setAdditivity(false);

			DailyRollingFileAppender appender = (DailyRollingFileAppender) patientLogger
					.getAppender(patientID);
			if (appender == null) {
				appender = new DailyRollingFileAppender(layout, logname, "'.'yyyy-MM-dd");
				appender.setName(patientID);
				appender.activateOptions();
				patientLogger.addAppender(appender);
			}

			StringBuilder sb = new StringBuilder(2048);

			// Add patientID
			sb.append(patientID);
			sb.append(" ");

			// Add action information
			sb.append(input.getAction().toStandardXmlString());
			sb.append(" ");

			// Add session information if not null
			if (session != null) {
				sb.append(session.toStandardXmlString());
				sb.append(" ");
			}

			// Add error response
			if (response instanceof ErrorContainerBean)
				sb.append(((ErrorContainerBean) response).getError()
						.toStandardXmlString());
			else
				sb.append("OK");

			patientLogger.info(sb.toString());

		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error logging object : ", e);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			logger.error("Error transforming object : ", e);
			e.printStackTrace();
		}
	}

}
