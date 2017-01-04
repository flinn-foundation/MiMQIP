package flinn.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "facilityip")
@XmlAccessorType(XmlAccessType.FIELD)

public class FacilityIPBean extends AbstractDataBean {
	@XmlElement(name = "ipfrom")
	protected long ipfrom = 0;
	@XmlElement(name = "ipto")
	protected long ipto = 0;

	public long getIpfrom() {
		return ipfrom;
	}
	public void setIpfrom(long ipfrom) {
		this.ipfrom = ipfrom;
	}
	public long getIpto() {
		return ipto;
	}
	public void setIpto(long ipto) {
		this.ipto = ipto;
	}
	
	boolean equals (FacilityIPBean b) {
		return (b.getIpfrom() == ipfrom && b.getIpto() == ipto);
	}
}
