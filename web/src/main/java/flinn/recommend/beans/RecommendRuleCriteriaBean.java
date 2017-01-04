package flinn.recommend.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "rule")
@XmlAccessorType(XmlAccessType.FIELD)

public class RecommendRuleCriteriaBean extends AbstractDataBean {
	@XmlElement(name = "criteriaid")
	protected int criteriaid;
	@XmlElement(name = "ruleid")
	protected int ruleid;
	@XmlElement(name = "priority")
	protected int priority;
	@XmlElement(name = "type")
	protected String type;	
	@XmlElement(name = "element")
	protected String element;
	@XmlElement(name = "operator")
	protected String operator;	
	@XmlElement(name = "value")
	protected String value;	
	
	public RecommendRuleCriteriaBean() {
		super();
	}

	public int getCriteriaid() {
		return criteriaid;
	}

	public void setCriteriaid(int criteriaid) {
		this.criteriaid = criteriaid;
	}

	public int getRuleid() {
		return ruleid;
	}

	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
