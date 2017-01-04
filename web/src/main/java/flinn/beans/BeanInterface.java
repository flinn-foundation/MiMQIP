package flinn.beans;

import java.io.IOException;

import javax.xml.bind.JAXBException;

public interface BeanInterface
{
	String toJsonString(float version);

	String toJavaXmlString(float verion);

	String toStandardXmlString(float version) throws JAXBException, IOException;
}
