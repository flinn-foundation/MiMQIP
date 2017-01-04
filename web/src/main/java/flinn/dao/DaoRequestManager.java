package flinn.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import flinn.beans.request.RequestActionBean;
import flinn.beans.request.RequestContainerBean;
import flinn.beans.response.ErrorBean;
import flinn.beans.response.ErrorContainerBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.dao.imp.AppUserDaoImp;
import flinn.dao.imp.FacilityDaoImp;
import flinn.dao.imp.LabDaoImp;
import flinn.dao.imp.PatientDaoImp;
import flinn.dao.imp.PrescriptionDaoImp;
import flinn.dao.imp.ProgressNoteDaoImp;
import flinn.util.PatientLog;

public class DaoRequestManager extends AbstractBaseDao
{

	protected static final Logger LOG = Logger.getLogger(DaoRequestManager.class);

	static
	{
		LOG.debug("Log appender instantiated for " + DaoRequestManager.class);
	}

	public static ErrorContainerBean generateErrorBean(final RequestActionBean reqab, final String message, final int number)
	{
		ErrorContainerBean rcb = new ErrorContainerBean();
		ErrorBean eb = new ErrorBean();
		eb.setMessage(message);
		eb.setNumber(new BigDecimal(number));
		rcb.setError(eb);
		if (reqab != null)
		{
			rcb.setAction(new ResponseActionBean(reqab));
		}
		else
		{
			ResponseActionBean rab = new ResponseActionBean();
			rab.setCorrelationid("none");
			rab.setCommand("none");
			rab.setMessageid("none");
			rab.setTimestamp(new BigDecimal(new Date().getTime()));
			rab.setType("none");
			rcb.setAction(rab);
		}
		return rcb;
	}

	protected static final String ACTION_CREATE_TYPE = "create";
	protected static final String ACTION_READ_TYPE = "read";
	protected static final String ACTION_UPDATE_TYPE = "update";
	protected static final String ACTION_DELETE_TYPE = "delete";
	protected static final String ACTION_REPORT_TYPE = "report";
	protected static final String ACTION_LOGIN_TYPE = "login";

	protected static final String ACTION_COMMAND_SESSION = "session";
	protected static final String ACTION_COMMAND_USER = "user";
	protected static final String ACTION_COMMAND_FACILITY = "facility";
	protected static final String ACTION_COMMAND_PATIENT = "patient";
	protected static final String ACTION_COMMAND_PATIENTSEARCH = "patientsearch";
	protected static final String ACTION_COMMAND_AUTHENTICATE = "authenticate";
	protected static final String ACTION_COMMAND_PROGRESSNOTE = "progressnote";
	protected static final String ACTION_COMMAND_PROGRESSNOTETAGS = "progressnotetags";
	protected static final String ACTION_COMMAND_NOTESEARCH = "progressnotesearch";
	protected static final String ACTION_COMMAND_PRESCRIPTION = "prescription";
	protected static final String ACTION_COMMAND_PRESCRIPTIONS = "prescriptions";
	protected static final String ACTION_COMMAND_PRESCRIPTIONSEARCH = "prescriptionsearch";
	protected static final String ACTION_COMMAND_LABTEST = "labtest";
	protected static final String ACTION_COMMAND_LAB = "lab";
	protected static final String ACTION_COMMAND_LABSEARCH = "labsearch";
	protected static final String ACTION_COMMAND_TREATMENT = "treatment";
	protected static final String ACTION_COMMAND_RECOMMEND_INITIALSTAGING = "initialstaging";
	protected static final String ACTION_COMMAND_RECOMMEND_GUIDELINE_REASON = "guidelinereason";
	protected static final String ACTION_COMMAND_RECOMMEND_REQUEST = "recommendation";
	protected static final String ACTION_COMMAND_TRAIN_AGE = "age";
	protected static final String ACTION_COMMAND_NONE = "none";

	protected static final long TIME_CONSTRAINT = 3 * 60 * 1000;

	public ResponseContainerBean handleRequest(RequestContainerBean input, HttpServletRequest request)
	{
		String type = input.getAction().getType();
		String command = input.getAction().getCommand();
		
		// Check the clock.  This is first because it doesn't require DB connectivity.
		long diff = Math.abs(input.getAction().getTimestamp().longValue() - (new Date()).getTime());
		if (diff > TIME_CONSTRAINT)
		{
			// TODO: Determine if this is required for this application.
			// Commented out for now to make it work more transparently.
			// return (generateErrorBean(input.getAction(),"Timestamp mismatch - Check local clock",(int)diff));
		}
		if (type == null || command == null)
		{
			return (generateErrorBean(input.getAction(), "Type or Command missing", 3));
		}

		// Check the authcode for validity  Only requires DB Read, so no need to commit.
		// The session bean contains the authenticated user's ResponseAppUser and ResponseFacility flinn.beans.
		ResponseSessionContainerBean session = null;

		//Authenticate only if user has logged in
		if (!type.equals(ACTION_LOGIN_TYPE))
		{
			AuthcodeDao acd = new AuthcodeDao();
			session = acd.validate(input, request);
			if (session == null)
			{
				return generateErrorBean(input.getAction(), "Invalid authcode (not permitted)", -1);
			}
		}

		try
		{
			if (connection == null || connection.isClosed())
			{
				renewConnection();
				if (connection == null || connection.isClosed())
				{
					LOG.error("Unable to renew connection in handleRequest");
					return generateErrorBean(input.getAction(), "Unable to renew connection in handleRequest", 4);
				}
			}
		}
		catch (Exception e)
		{
			LOG.error("Unable to renew connection in handleRequest: " + e);
		}
		System.out.println("Request Setup Complete time: " + new Date().getTime());
		ResponseContainerBean ret = null;
		try
		{
			if (type.equals(ACTION_LOGIN_TYPE))
			{
				if (command.equals(ACTION_COMMAND_AUTHENTICATE))
				{
					ret = (new AppUserDaoImp()).loginUser(input, request, connection);
				}
				else
				{
					ret = generateErrorBean(input.getAction(), "Login type with inappropriate command", 5);
				}
			}
			else if (type.equals(ACTION_CREATE_TYPE))
			{
				if (command.equals(ACTION_COMMAND_USER))
				{
					ret = (new AppUserDaoImp()).handleUserCreate(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_SESSION))
				{
					ret = session;
				}
				else if (command.equals(ACTION_COMMAND_FACILITY))
				{
					ret = (new FacilityDaoImp()).handleFacilityCreate(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_PATIENTSEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Patient Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_PATIENT))
				{
					ret = (new PatientDaoImp()).handlePatientCreate(input, session, connection);
					// add here
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTION))
				{
					ret = (new PrescriptionDaoImp()).handlePrescriptionCreate(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTIONS))
				{
					ret = (new PrescriptionDaoImp()).handlePrescriptionsCreate(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTIONSEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Prescription Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_PROGRESSNOTE))
				{
					ret = (new ProgressNoteDaoImp()).handleProgressNoteCreate(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_NOTESEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Progress Note Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_LAB))
				{
					ret = (new LabDaoImp()).handleLabCreate(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_LABSEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Lab Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_LABTEST))
				{
					ret = generateErrorBean(input.getAction(), "Lab Tests can only be read via web flinn.service", 5);
				}
				else if (command.equals(ACTION_COMMAND_TREATMENT))
				{
					ret = generateErrorBean(input.getAction(), "Treatments can only be read via web flinn.service", 5);
				}
			}
			else if (type.equals(ACTION_READ_TYPE))
			{
				if (command.equals(ACTION_COMMAND_USER))
				{
					ret = (new AppUserDaoImp()).handleUserRead(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_SESSION))
				{
					ret = session;
				}
				else if (command.equals(ACTION_COMMAND_FACILITY))
				{
					ret = (new FacilityDaoImp()).handleFacilityRead(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_PATIENTSEARCH))
				{
					ret = (new PatientDaoImp()).handlePatientSearch(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_PATIENT))
				{
					ret = (new PatientDaoImp()).handlePatientRead(input, session, connection);
					// add here
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTION))
				{
					ret = (new PrescriptionDaoImp()).handlePrescriptionRead(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTIONS))
				{
					ret = generateErrorBean(input.getAction(), "Prescriptions can only be searched or managed individually", 5);
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTIONSEARCH))
				{
					ret = (new PrescriptionDaoImp()).handlePrescriptionSearch(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_PROGRESSNOTETAGS))
				{
					ret = (new ProgressNoteDaoImp()).handleProgressNoteTagsRead(input, session, connection, true);
				}
				else if (command.equals(ACTION_COMMAND_PROGRESSNOTE))
				{
					ret = (new ProgressNoteDaoImp()).handleProgressNoteRead(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_NOTESEARCH))
				{
					ret = (new ProgressNoteDaoImp()).handleProgressNoteSearch(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_LAB))
				{
					ret = (new LabDaoImp()).handleLabRead(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_LABSEARCH))
				{
					ret = (new LabDaoImp()).handleLabSearch(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_LABTEST))
				{
					ret = (new LabDaoImp()).handleLabTestRead(input, session, connection, true, null);
				}
				else if (command.equals(ACTION_COMMAND_TREATMENT))
				{
					ret = (new DaoAppManager()).handleTreatmentRead(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_RECOMMEND_INITIALSTAGING))
				{
					ret = (new Recommendation()).handleInitialStaging(input, session, connection, true);
				}
				else if (command.equals(ACTION_COMMAND_RECOMMEND_GUIDELINE_REASON))
				{
					ret = (new Recommendation()).handleReason(input, session, connection, true);
				}
				else if (command.equals(ACTION_COMMAND_RECOMMEND_REQUEST))
				{
					ret = (new Recommendation()).handleRequest(input, session, connection, true);
				}
			}
			else if (type.equals(ACTION_UPDATE_TYPE))
			{
				if (command.equals(ACTION_COMMAND_USER))
				{
					ret = (new AppUserDaoImp()).handleUserUpdate(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_SESSION))
				{
					ret = session;
				}
				else if (command.equals(ACTION_COMMAND_FACILITY))
				{
					ret = (new FacilityDaoImp()).handleFacilityUpdate(input, session, connection);
				}
				else if (command.equals(ACTION_COMMAND_PATIENTSEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Patient Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_PATIENT))
				{
					ret = (new PatientDaoImp()).handlePatientUpdate(input, session, connection);
					// add here
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTION))
				{
					ret = generateErrorBean(input.getAction(), "Prescription is create/read operations", 5);
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTIONS))
				{
					ret = generateErrorBean(input.getAction(), "Prescriptions can only be searched or managed individually", 5);
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTIONSEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Presctiption Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_PROGRESSNOTE))
				{
					ret = generateErrorBean(input.getAction(), "Progress Notes are create/read operations", 5);
				}
				else if (command.equals(ACTION_COMMAND_NOTESEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Progress Note Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_LAB))
				{
					ret = generateErrorBean(input.getAction(), "Labs are create/read operations", 5);
				}
				else if (command.equals(ACTION_COMMAND_LABSEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Lab Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_LABTEST))
				{
					ret = generateErrorBean(input.getAction(), "Lab Tests can only be read via web flinn.service", 5);
				}
				else if (command.equals(ACTION_COMMAND_TREATMENT))
				{
					ret = generateErrorBean(input.getAction(), "Treatments can only be read via web flinn.service", 5);
				}
			}
			else if (type.equals(ACTION_DELETE_TYPE))
			{
				if (command.equals(ACTION_COMMAND_AUTHENTICATE))
				{
					ret = (new AppUserDaoImp()).logoutUser(input, session);
				}
				else if (command.equals(ACTION_COMMAND_USER))
				{
					ret = generateErrorBean(input.getAction(), "Command/Type arguments incompatible - User Delete unimplemented", 13);
				}
				else if (command.equals(ACTION_COMMAND_SESSION))
				{
					ret = (new AppUserDaoImp()).logoutUser(input, session);
				}
				else if (command.equals(ACTION_COMMAND_FACILITY))
				{
					ret = generateErrorBean(input.getAction(), "Command/Type arguments incompatible - Facility Delete unimplemented", 13);
				}
				else if (command.equals(ACTION_COMMAND_PATIENTSEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Patient Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_PATIENT))
				{
					ret = generateErrorBean(input.getAction(), "Command/Type arguments incompatible - Patient Delete unimplemented", 13);
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTION))
				{
					ret = generateErrorBean(input.getAction(), "Command/Type arguments incompatible - Prescription Delete unimplemented", 13);
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTIONS))
				{
					ret = generateErrorBean(input.getAction(), "Prescriptions can only be searched or managed individually", 5);
				}
				else if (command.equals(ACTION_COMMAND_PRESCRIPTIONSEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Prescription Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_PROGRESSNOTE))
				{
					ret = generateErrorBean(input.getAction(), "Command/Type arguments incompatible - Progress Note Delete unimplemented", 13);
				}
				else if (command.equals(ACTION_COMMAND_NOTESEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Progress Note Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_LAB))
				{
					ret = generateErrorBean(input.getAction(), "Labs are create/read operations", 5);
				}
				else if (command.equals(ACTION_COMMAND_LABSEARCH))
				{
					ret = generateErrorBean(input.getAction(), "Lab Search is a read operation", 5);
				}
				else if (command.equals(ACTION_COMMAND_LABTEST))
				{
					ret = generateErrorBean(input.getAction(), "Lab Tests can only be read via web flinn.service", 5);
				}
				else if (command.equals(ACTION_COMMAND_TREATMENT))
				{
					ret = generateErrorBean(input.getAction(), "Treatments can only be read via web flinn.service", 5);
				}
			}
			else if (type.equals(ACTION_REPORT_TYPE))
			{
				// Don't have to switch on command, command has a different meaning for reports.
				ret = generateErrorBean(input.getAction(), "Type arguments incompatible - Report unimplemented", 13);
			}
			if (ret == null)
			{
				ret = generateErrorBean(input.getAction(), "Request Parameters unhandled - Check Command/Type arguments", 10);
			}
		}
		catch (Exception e)
		{
			LOG.error("Inappropriate Exception caught: " + e.getMessage(), e);
			ret = generateErrorBean(input.getAction(), "Inappropriate Exception caught: " + e.getMessage(), 16);
		}
		finally
		{
			try
			{
				if (connection != null && !connection.isClosed())
				{
					closeConnection();
					connection = null;
				}
			}
			catch (SQLException e)
			{
				LOG.error("Unable to close connection after use in handleRequest");
			}
		}
		
		String options = ""; // currently not used.
		PatientLog.WriteEntry(input, request, ret, session, options);
		return ret;
	}
	
	public ResponseContainerBean handleTrainingRequest(RequestContainerBean input, HttpServletRequest request)
	{
		String type = input.getAction().getType();
		String command = input.getAction().getCommand();
		
		// Check the clock.  This is first because it doesn't require DB connectivity.
		long diff = Math.abs(input.getAction().getTimestamp().longValue() - (new Date()).getTime());
		if (diff > TIME_CONSTRAINT)
		{
			// TODO: Determine if this is required for this application.
			// Commented out for now to make it work more transparently.
			// return (generateErrorBean(input.getAction(),"Timestamp mismatch - Check local clock",(int)diff));
		}
		if (type == null || command == null)
		{
			return (generateErrorBean(input.getAction(), "Type or Command missing", 3));
		}

		// Check the authcode for validity  Only requires DB Read, so no need to commit.
		// The session bean contains the authenticated user's ResponseAppUser and ResponseFacility flinn.beans.
		ResponseSessionContainerBean session = null;

		//Authenticate only if user has logged in
		if (!type.equals(ACTION_LOGIN_TYPE))
		{
			AuthcodeDao acd = new AuthcodeDao();
			session = acd.validate(input, request);
			if (session == null)
			{
				return generateErrorBean(input.getAction(), "Invalid authcode (not permitted)", -1);
			}
		}

		try
		{
			if (connection == null || connection.isClosed())
			{
				renewConnection();
				if (connection == null || connection.isClosed())
				{
					LOG.error("Unable to renew connection in handleTrainingRequest");
					return generateErrorBean(input.getAction(), "Unable to renew connection in handleTrainingRequest", 4);
				}
			}
		}
		catch (Exception e)
		{
			LOG.error("Unable to renew connection in handleTrainingRequest: " + e);
		}
		System.out.println("Request Setup Complete time: " + new Date().getTime());
		ResponseContainerBean ret = null;
		try
		{
			if (type.equals(ACTION_UPDATE_TYPE))
			{
				if (command.equals(ACTION_COMMAND_TRAIN_AGE))
				{
					ret = (new PatientDaoImp()).handlePatientAge(input, session, connection);
				}
			}
			if (ret == null)
			{
				ret = generateErrorBean(input.getAction(), "Request Parameters unhandled - Check Command/Type arguments", 10);
			}
		}
		catch (Exception e)
		{
			LOG.error("Inappropriate Exception caught: " + e.getMessage(), e);
			ret = generateErrorBean(input.getAction(), "Inappropriate Exception caught: " + e.getMessage(), 16);
		}
		finally
		{
			try
			{
				if (connection != null && !connection.isClosed())
				{
					closeConnection();
					connection = null;
				}
			}
			catch (SQLException e)
			{
				LOG.error("Unable to close connection after use in handleTrainingRequest");
			}
		}
		
		String options = ""; // currently not used.
		PatientLog.WriteEntry(input, request, ret, session, options);
		return ret;
	}

}
