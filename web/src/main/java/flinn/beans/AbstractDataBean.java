package flinn.beans;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import flinn.persistence.delegate.BigDecimalPersistenceDelegate;
import flinn.persistence.delegate.DatePersistenceDelegate;
import flinn.persistence.delegate.TimestampPersistenceDelegate;


public abstract class AbstractDataBean implements BeanInterface
{
	public String toStandardXmlString() throws JAXBException, IOException
	{
		return toStandardXmlString(1.0f);
	}

	public String toJsonString()
	{
		final Gson gson = new Gson();
		return gson.toJson(this);
	}

	public String toJsonString(final float version)
	{
		final Gson gson = new GsonBuilder().setVersion(version).create();
		return gson.toJson(this);
	}

	public String toJavaXmlString()
	{
		return toJavaXmlString(1.0f);
	}

	public String toJavaXmlString(final float version)
	{
		synchronized (this)
		{
			final ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
			final XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(baOutputStream));
			encoder.setPersistenceDelegate(Date.class, new DatePersistenceDelegate());
			encoder.setPersistenceDelegate(Timestamp.class, new TimestampPersistenceDelegate());
			encoder.setPersistenceDelegate(BigDecimal.class, new BigDecimalPersistenceDelegate());
			encoder.writeObject(this);
			encoder.close();
			return new String(baOutputStream.toByteArray());
		}
	}

	@Override
	public String toStandardXmlString(float version) throws JAXBException, IOException
	{
		final StringWriter stringWriter = new StringWriter();
		final JAXBContext jaxbContext = JAXBContext.newInstance(this.getClass());
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.marshal(this, stringWriter);
		stringWriter.flush();
		stringWriter.close();
		return stringWriter.toString();
	}
}
