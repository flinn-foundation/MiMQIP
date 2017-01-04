package flinn.dao.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.ProgressNoteTagBean;
import flinn.beans.request.RequestContainerBean;
import flinn.beans.request.RequestProgressNoteBean;
import flinn.beans.request.RequestProgressNoteSearchBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponsePatientBean;
import flinn.beans.response.ResponseProgressNoteBean;
import flinn.beans.response.ResponseProgressNoteContainerBean;
import flinn.beans.response.ResponseProgressNoteSearchContainerBean;
import flinn.beans.response.ResponseProgressNoteTagsContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.dao.DaoRequestManager;
import flinn.dao.ProgressNoteDao;

public class ProgressNoteDaoImp extends ProgressNoteDao
{

	protected static final Logger LOG = Logger.getLogger(ProgressNoteDaoImp.class);
	static
	{
		LOG.debug("Log appender instantiated for " + ProgressNoteDaoImp.class);
	}

	public ResponseContainerBean handleProgressNoteCreate(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		// sanity checks on incoming data.  Ensure no changes to aspects of the data we don't want changed.
		RequestProgressNoteBean bean = input.getProgressnote();
		if (bean == null)
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note create submitted with no appropriate info", 41);
		if (bean.getPatientid() < 1)
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note create submitted with no patient id", 41);
		if (bean.getNotetext() == null || bean.getNotetext().toLowerCase().replace("&nbsp;", " ").replace("<br>", " ").replace("<p>", " ").replace("</p>", " ").trim().equals(""))
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note create submitted with empty text", 41);
		}

		int newid = 0;
		List<ResponseProgressNoteBean> progressnotes = null;

		try
		{
			// Create new facility record
			newid = create(bean, session, connection);

		}
		catch (Exception e)
		{
			LOG.error(e);
			try
			{
				connection.rollback();
			}
			catch (SQLException e1)
			{
				LOG.error("Error rolling back connection in handleProgressNoteCreate: " + e);
			}
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note create failed (unknown error): " + e.getMessage(), 48);
		}

		try
		{
			connection.commit();
		}
		catch (Exception e)
		{
			try
			{
				connection.rollback();
			}
			catch (SQLException e1)
			{
				LOG.error("Error rolling back connection in handleProgressNoteCreate: " + e);
			}
			LOG.error("Progress Note retrieval post-update failed (unknown error): " + e);
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note retrieval post-create failed (unknown error): " + e.getMessage(), 49);
		}
		if (newid < 1)
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note create failed (unknown error - no returned ID)", 47);
		bean = new RequestProgressNoteBean();
		bean.setProgressnoteid(newid);
		try
		{
			progressnotes = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note retrieval post-create failed (unknown error): " + e.getMessage(), 47);
		}

		if (progressnotes == null || progressnotes.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note retrieval post-create failed (unknown error) No Exception", 49);
		}

		ResponseProgressNoteContainerBean rcb = new ResponseProgressNoteContainerBean();
		rcb.setProgressNote(progressnotes.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handleProgressNoteRead(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		RequestProgressNoteBean bean = input.getProgressnote();
		List<ResponseProgressNoteBean> progressnotes = null;

		try
		{
			progressnotes = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note retrieval failed (unknown error): " + e.getMessage(), 47);
		}

		if (progressnotes == null || progressnotes.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note retrieval failed (unknown error) No Exception", 49);
		}

		ResponseProgressNoteContainerBean rcb = new ResponseProgressNoteContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setProgressNote(progressnotes.get(0));
		return rcb;
	}

	public ResponseContainerBean handleProgressNoteSearch(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		RequestProgressNoteSearchBean search = input.getProgressnotesearch();

		//Check patient facility to see if it = facility before returning pnotes
		ResponsePatientBean patient = null;
		try
		{
			patient = new PatientDaoImp().findPatientById(search.getPatientid(), connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Patient retrieval failed (Progress Note) (unknown error): " + e.getMessage(), 47);
		}
		if (patient == null)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Patient retrieval failed (Progress Note) (Incorrect patientid)", 49);
		}
		if (patient.getFacilityid() != session.getFacility().getFacilityid())
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Patient retrieval failed (Progress Note) (Incorrect facility/patientid)", 49);
		}

		// Set up the search for progress notes.
		RequestProgressNoteBean bean = new RequestProgressNoteBean();
		bean.setPatientid(search.getPatientid());

		// We need to do the narrowing by tags and search text separately, because those are not in the progressnote table.
		List<ResponseProgressNoteBean> notes = null;
		try
		{
			notes = find(bean, "EntryDate DESC", connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Notes retrieval failed (unknown error): " + e.getMessage(), 47);
		}

		// Now, cull the results by removing any that don't match the tags being searched for....
		if (notes != null && notes.size() > 0)
		{
			ProgressNoteTagBean [] tags = search.getTags();
			if (tags != null && tags.length > 0)
			{
				// find will only check against direct note properties.  Not tags or keyword "token" elements.
				for (int i = 0; i < tags.length; i++)
				{
					for (int j = 0; j < notes.size(); j++)
					{
						ProgressNoteTagBean [] ntags = notes.get(j).getTags();
						boolean matches = false;
						// It can only match if there are tags.
						if (ntags != null)
						{
							for (int k = 0; k < ntags.length; k++)
							{
								if (tags[i].getProgressnotetagid() == ntags[k].getProgressnotetagid())
								{
									matches = true;
								}
							}
						}
						if (!matches)
						{
							notes.remove(j);
							j--;
						}
					}
				}
			}
		}
		// Now, cull the results by removing any that don't match the keywords being searched for....
		if (notes != null && notes.size() > 0 && search.getSearchtext() != null)
		{
			String [] keywords = search.getSearchtext().split(" ");
			if (keywords != null && keywords.length > 0)
			{
				// find will only check against direct note properties.  Not tags or keyword "token" elements.
				for (int i = 0; i < keywords.length; i++)
				{
					String keyword = keywords[i].toLowerCase();
					for (int j = 0; j < notes.size(); j++)
					{
						boolean matches = false;
						String notetext = notes.get(j).getNotetext().toLowerCase();
						if (notetext.contains(keyword))
						{
							matches = true;
						}
						if (!matches)
						{
							notes.remove(j);
							j--;
						}
					}
				}
			}
		}

		int page = search.getPage();
		if (page < 1)
			page = 1;
		page--;
		int pagecount = search.getPagecount();
		if (pagecount < 1)
			pagecount = 10;

		// After this, thispagecount refers to the number of entries on this page, rather than the number requested.
		int thispagecount = pagecount;
		if (page * pagecount + pagecount > notes.size())
			thispagecount = notes.size() - page * pagecount;
		if (thispagecount < 0)
			thispagecount = 0;

		ResponseProgressNoteBean [] pnotes = new ResponseProgressNoteBean[thispagecount];
		for (int i = 0; i < thispagecount; i++)
		{
			pnotes[i] = notes.get((page * pagecount) + i);
		}

		ResponseProgressNoteSearchContainerBean rcb = new ResponseProgressNoteSearchContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setNotes(pnotes);
		rcb.setPage(page + 1);
		rcb.setPagecount(pagecount);
		rcb.setTotal(notes.size());
		return rcb;
	}

	public ResponseContainerBean handleProgressNoteTagsRead(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection, boolean valid)
	{

		ProgressNoteTagBean [] tags = null;
		try
		{
			tags = getAllTags(connection, valid);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Progress Note Tags retrieval failed (unknown error): " + e.getMessage(), 47);
		}
		if (tags == null || tags.length == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "No Progress Note Tags retrieved (unknown error): ", 49);
		}

		ResponseProgressNoteTagsContainerBean rcb = new ResponseProgressNoteTagsContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setTags(tags);
		return rcb;
	}

}
