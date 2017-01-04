package flinn.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import flinn.beans.AbstractDataBean;

@XmlRootElement(name = "guidelinechart")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseGuidelineChart extends AbstractDataBean {
	@XmlElement(name = "diagnosis")
	protected String diagnosis;
	@XmlElement(name = "stage")
	protected String stage;
	@XmlElement(name = "rows")
	protected ResponseGuidelineChartRow[] rows;
	@XmlElement(name = "notes")
	protected String notes;
	
	public ResponseGuidelineChart() {
		super();
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public ResponseGuidelineChartRow[] getRows() {
		return rows;
	}

	public void setRows(ResponseGuidelineChartRow[] rows) {
		this.rows = rows;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
