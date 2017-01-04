package flinn.beans.response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)

public class ResponseProgressNoteContainerBean  extends ResponseContainerBean{
	@XmlElement(name = "progressnote")
	protected ResponseProgressNoteBean progressnote;

	public ResponseProgressNoteBean ProgressNote() {
		return progressnote;
	}
	public void setProgressNote(ResponseProgressNoteBean progressnote) {
		this.progressnote = progressnote;
	}

}
