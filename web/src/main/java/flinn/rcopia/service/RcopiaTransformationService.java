package flinn.rcopia.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import flinn.beans.PatientDetailsBean;
import flinn.beans.response.ResponsePatientBean;
import flinn.rcopia.model.CallerType;
import flinn.rcopia.model.CommandType;
import flinn.rcopia.model.ObjectFactory;
import flinn.rcopia.model.PatientListTypeReq;
import flinn.rcopia.model.PatientTypeReq;
import flinn.rcopia.model.ProviderType;
import flinn.rcopia.model.RcExtRequestType;
import flinn.rcopia.model.RcExtResponseType;
import flinn.rcopia.model.RcopiaOrExternalID;
import flinn.rcopia.model.RequestType;
import flinn.rcopia.model.TraceInformationType;
import flinn.rcopia.service.RcopiaSenderService.PreferredMapper;
import flinn.util.ApplicationProperties;
import flinn.util.DateString;

public class RcopiaTransformationService
{

	private static final Logger LOG = Logger.getLogger(RcopiaTransformationService.class);

	private static final JAXBContext JAXB_CONTEXT = initJaxbContext();

	private final ObjectFactory factory = new ObjectFactory();

	private static JAXBContext initJaxbContext()
	{
		try
		{
			return JAXBContext.newInstance("flinn.flinn.rcopia.model");
		}
		catch (JAXBException e)
		{
			throw new RuntimeException(e);
		}
	}

	/*
	 * Get the request object required to retrieve a list of medications updated since the specified date
	 */
	protected RcExtRequestType createUpdateMedicationRequest(Date lastUpdateDate)
	{
		RcExtRequestType rcExtRequest = createRcExtRequestType();
		RequestType requestType = createUpdateMedicationRequestType(lastUpdateDate);
		rcExtRequest.setRequest(requestType);
		return rcExtRequest;
	}

	/*
	 * Get the request object required to retrieve a list of medications for the specified patient and updated since the specified date
	 */
	protected RcExtRequestType createUpdateMedicationRequest(Date lastUpdateDate, int patientId)
	{
		RcExtRequestType rcExtRequest = createRcExtRequestType();
		RequestType requestType = createUpdateMedicationRequestType(lastUpdateDate, patientId);
		rcExtRequest.setRequest(requestType);
		return rcExtRequest;
	}

	/*
	 * Get the request object required to retrieve a list of medications for the specified patient and updated since the specified date
	 */
	protected RcExtRequestType createGetNotificationCountRequest(String userId, String notificationType)
	{
		RcExtRequestType rcExtRequest = createRcExtRequestType();
		RequestType requestType = createGetNotificationCountRequestType(userId, notificationType);
		rcExtRequest.setRequest(requestType);
		return rcExtRequest;
	}

	/*
	 * Get the request object required to discontinue a medication in DrFirst
	 */
	protected RcExtRequestType createSendMedicationRequest(String lastUpdateDate, String patientId)
	{
		return null;
	}

	/*
	 * Get the request object required to create a patient in DrFirst
	 */
	protected RcExtRequestType createSendPatientRequest(ResponsePatientBean patientBean) throws RcopiaTransformationException
	{
		RcExtRequestType rcExtRequest = createRcExtRequestType();
		RequestType requestType = createSendPatientRequestType(patientBean);
		rcExtRequest.setRequest(requestType);
		return rcExtRequest;
	}

	/*
	 * Common request structure for all Rcopia requests
	 */
	private RcExtRequestType createRcExtRequestType()
	{
		RcExtRequestType rcRequest = factory.createRcExtRequestType();

		rcRequest.setVersion("1.9");

		rcRequest.setRcopiaPracticeUsername(ApplicationProperties.getProperty("requestRcopiaPracticeUsername"));
		rcRequest.setSystemName(ApplicationProperties.getProperty("requestSystemName"));

		TraceInformationType traceInformation = factory.createTraceInformationType();
		traceInformation.setRequestMessageID("Test1");
		rcRequest.setTraceInformation(traceInformation);

		CallerType caller = factory.createCallerType();
		caller.setVendorName(ApplicationProperties.getProperty("callerVendorName"));
		caller.setVendorPassword(ApplicationProperties.getProperty("callerVendorPassword"));
		caller.setApplication(ApplicationProperties.getProperty("callerApplication"));
		caller.setVersion(ApplicationProperties.getProperty("callerVersion"));
		caller.setPracticeName(ApplicationProperties.getProperty("callerPracticeName"));
		caller.setStation(ApplicationProperties.getProperty("callerStation"));
		rcRequest.setCaller(caller);

		return rcRequest;
	}

	private RequestType createUpdateMedicationRequestType(Date lastUpdateDate)
	{
		RequestType request = factory.createRequestType();
		request.setCommand(CommandType.UPDATE_MEDICATION);
		request.setLastUpdateDate(DateString.format(lastUpdateDate, "MM/dd/yyyy HH:mm:ss"));

		return request;
	}

	private RequestType createUpdateMedicationRequestType(Date lastUpdateDate, int patientId)
	{
		RequestType request = factory.createRequestType();
		request.setCommand(CommandType.UPDATE_MEDICATION);
		request.setLastUpdateDate(DateString.format(lastUpdateDate, "MM/dd/yyyy HH:mm:ss"));

		RcopiaOrExternalID rcopiaOrExternalID = factory.createRcopiaOrExternalID();
		rcopiaOrExternalID.setExternalID(patientId + "");
		request.setPatient(rcopiaOrExternalID);

		return request;
	}

	private RequestType createGetNotificationCountRequestType(String userId, String notificationType)
	{
		RequestType request = factory.createRequestType();
		request.setCommand(CommandType.GET_NOTIFICATION_COUNT);
		request.setReturnPrescriptionIDs("n");
		request.setType(notificationType);

		ProviderType providerType = new ProviderType();
		providerType.setExternalID(userId);
		request.setProvider(providerType);

		return request;
	}

	private RequestType createSendPatientRequestType(ResponsePatientBean patientBean)
	{
		RequestType request = factory.createRequestType();
		request.setCommand(CommandType.SEND_PATIENT);
		request.setSynchronous("y");
		request.setCheckEligibility("y");

		PatientTypeReq patientType = new PatientTypeReq();
		patientType.setExternalID(patientBean.getPatientid() + "");

		HashMap<String, PatientDetailsBean []> patientDetails = patientBean.getDetails();

		for (Iterator<String> iter = patientDetails.keySet().iterator(); iter.hasNext();)
		{
			String key = iter.next();
			PatientDetailsBean [] patientDetailsArray = patientDetails.get(key);
			String value = (patientDetailsArray.length > 0 && patientDetailsArray[0] != null) ? patientDetailsArray[0].getValue() : "";

			LOG.debug("Patient details for key " + key + "=" + value);
			if (key.equalsIgnoreCase("firstname"))
			{
				patientType.setFirstName(value);
			}
			else if (key.equalsIgnoreCase("lastname"))
			{
				patientType.setLastName(value);
			}
			else if (key.equalsIgnoreCase("sex"))
			{
				patientType.setSex(value);
			}
			else if (key.equalsIgnoreCase("birth"))
			{
				patientType.setDOB(toRcopiaDate(value));
			}
			else if (key.equalsIgnoreCase("zip"))
			{
				patientType.setZip(value);
			}
			else if (key.equalsIgnoreCase("patientidentifier"))
			{
				//FIXME: Not sure if this should be used instead of the internal id
			}
		}

		PatientListTypeReq patientListTypeReq = new PatientListTypeReq();
		patientListTypeReq.getPatient().add(patientType);
		request.setPatientList(patientListTypeReq);
		return request;
	}

	/*
	 * Convert a request object to xml
	 */
	protected String convertRcopiaRequestToXml(RcExtRequestType rcRequest) throws RcopiaTransformationException
	{
		String xml = null;
		try
		{
			Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
			StringWriter writer = new StringWriter();

			NamespacePrefixMapper mapper = new PreferredMapper();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", mapper);

			marshaller.marshal(rcRequest, writer);
			xml = writer.toString();
			xml = xml.replaceAll("ns1:", "");
		}
		catch (JAXBException e)
		{
			LOG.error(e);
			throw new RcopiaTransformationException("JAXBException converting RcExtRequestType to xml: " + e.getMessage(), e);
		}
		return xml;
	}

	/*
	 * Convert a response object to xml
	 */
	public String convertRcopiaResponseToXml(RcExtResponseType rcResponse) throws RcopiaTransformationException
	{
		String xml = null;
		try
		{
			Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
			StringWriter writer = new StringWriter();

			NamespacePrefixMapper mapper = new PreferredMapper();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", mapper);

			marshaller.marshal(rcResponse, writer);
			xml = writer.toString();
			xml = xml.replaceAll("ns1:", "");
		}
		catch (JAXBException e)
		{
			LOG.error(e);
			throw new RcopiaTransformationException("JAXBException converting RcExtRequestType to xml: " + e.getMessage(), e);
		}
		return xml;
	}

	/*
	 * Convert an xml string to a response object
	 */
	protected RcExtResponseType convertXmlToRcopiaResponse(String xml) throws RcopiaTransformationException
	{
		RcExtResponseType rcResponse = null;
		try
		{
			StringReader reader = new StringReader(xml);
			Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
			JAXBElement<RcExtResponseType> root = unmarshaller.unmarshal(new StreamSource(reader), RcExtResponseType.class);
			rcResponse = root.getValue();
		}
		catch (JAXBException e)
		{
			LOG.error(e);
			throw new RcopiaTransformationException("JAXBException converting xml to RcExtResponseType: " + e.getMessage(), e);
		}
		return rcResponse;
	}

	private String toRcopiaDate(String date)
	{
		if (date == null || date.length() != 10 || !date.substring(4, 5).equals("-") || !date.substring(7, 8).equals("-"))
		{
			LOG.error("Could not convert date " + date + " to Rcopia date!");
			return "";
		}
		return date.substring(5, 7) + "/" + date.substring(8, 10) + "/" + date.substring(0, 4);
	}

}
