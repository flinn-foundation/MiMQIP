package flinn.recommend.beans.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;
import flinn.recommend.beans.response.ResponseSettingBean;

@XmlRootElement(name = "setting")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestSettingBean  extends AbstractDataBean {
	@XmlElement(name = "settingid")
	protected int settingid;
	@XmlElement(name = "settingname")
	protected String settingname;
	@XmlElement(name = "settingvalue")
	protected String settingvalue;

	public RequestSettingBean() {
		super();
	}
	
	public RequestSettingBean(ResponseSettingBean bean) {
		setSettingname(bean.getSettingname());
		setSettingvalue(bean.getSettingvalue());
	}

	public int getSettingid() {
		return settingid;
	}

	public void setSettingid(int settingid) {
		this.settingid = settingid;
	}

	public String getSettingname() {
		return settingname;
	}

	public void setSettingname(String settingname) {
		this.settingname = settingname;
	}

	public String getSettingvalue() {
		return settingvalue;
	}

	public void setSettingvalue(String settingvalue) {
		this.settingvalue = settingvalue;
	}
	
}
