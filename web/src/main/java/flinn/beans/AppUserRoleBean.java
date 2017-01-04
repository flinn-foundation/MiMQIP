package flinn.beans;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "role")
@XmlAccessorType(XmlAccessType.FIELD)

public class AppUserRoleBean {
	@XmlElement(name = "approleid")
	protected int approleid = 0;
	@XmlElement(name = "approle")
	protected String approle;

	public int getApproleid() {
		return approleid;
	}
	public void setApproleid(int approleid) {
		this.approleid = approleid;
	}
	public String getApprole() {
		return approle;
	}
	public void setApprole(String approle) {
		this.approle = approle;
	}
	
	public boolean equals (AppUserRoleBean b) {
		return (b.getApproleid() == approleid);
	}

}
