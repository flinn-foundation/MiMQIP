package flinn.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import flinn.beans.AppUserRoleBean;
import flinn.beans.LabTestBean;
import flinn.beans.ProgressNoteTagBean;
import flinn.beans.TreatmentGroupBean;
import flinn.beans.request.RequestActionBean;
import flinn.beans.request.RequestAppUserBean;
import flinn.beans.request.RequestContainerBean;
import flinn.beans.request.RequestFacilityBean;
import flinn.beans.request.RequestPrescriptionBean;
import flinn.beans.request.RequestTreatmentBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseAppUserBean;
import flinn.beans.response.ResponseAppUserContainerBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseFacilityBean;
import flinn.beans.response.ResponseFacilityContainerBean;
import flinn.beans.response.ResponseLabTestsContainerBean;
import flinn.beans.response.ResponsePrescriptionBean;
import flinn.beans.response.ResponseProgressNoteTagsContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.beans.response.ResponseTreatmentBean;
import flinn.beans.response.ResponseTreatmentContainerBean;
import flinn.beans.response.ResponseTreatmentGroupContainerBean;
import flinn.beans.response.ResponseTreatmentSearchContainerBean;
import flinn.dao.imp.AppUserDaoImp;
import flinn.dao.imp.FacilityDaoImp;
import flinn.dao.imp.LabDaoImp;
import flinn.dao.imp.PrescriptionDaoImp;
import flinn.dao.imp.ProgressNoteDaoImp;
import flinn.dao.imp.TreatmentDaoImp;

public class DaoAppManager extends AbstractBaseDao
{

	public static final Logger LOG = Logger.getLogger(DaoAppManager.class);
	public String nullDate = "0000-00-00 00:00:00";

	static
	{
		LOG.debug("Log appender instantiated for " + DaoAppManager.class);
	}

	public ResponseFacilityBean getFacility(int facilityid) throws Exception
	{
		LOG.debug("<getFacility>");
		ResponseFacilityBean facility = null;
		if (facilityid > 0)
		{
			connection = getConnection(); //Get connection

			flinn.beans.request.RequestFacilityBean facreq = new flinn.beans.request.RequestFacilityBean();
			facreq.setFacilityid(facilityid);
			List<flinn.beans.response.ResponseFacilityBean> facilities = new FacilityDaoImp().find(facreq, null, connection);

			if (facilities != null && facilities.size() > 0)
			{
				facility = facilities.get(0);
			}
		}
		return facility;
	}

	public List<ResponseFacilityBean> findAllFacilities(RequestContainerBean input, ResponseSessionContainerBean session, boolean valid, boolean isSuperAdmin, String orderBy) throws Exception
	{
		LOG.debug("<findAllFacilities>");
		RequestFacilityBean bean = new RequestFacilityBean();
		List<ResponseFacilityBean> rtnList = null;
		bean.setValid(valid);

		if (session != null)
		{
			connection = getConnection(); //Get connection

			if (valid)
			{ //Show only active
				rtnList = new FacilityDaoImp().findValid(bean, orderBy, connection, isSuperAdmin);
			}
			else
			{
				//Show all
				rtnList = new FacilityDaoImp().findAllFacilities(bean, orderBy, connection);
			}
		}

		return rtnList;
	}

	public int createFacility(RequestContainerBean input, ResponseSessionContainerBean session) throws Exception
	{
		LOG.debug("<createFacility>");
		int newid = -1;
		RequestFacilityBean bean = new RequestFacilityBean();
		bean.setAdministratorid(input.getFacility().getAdministratorid());
		bean.setFacilityid(session.getFacility().getFacilityid());
		bean.setFacilityname(input.getFacility().getFacilityname());
		bean.setFacilityshortcut(input.getFacility().getFacilityshortcut());
		bean.setFacilityemail(input.getFacility().getFacilityemail());
		bean.setValid(input.getFacility().getValid());
		bean.setLaunch(input.getFacility().getLaunch());
		bean.setExpiration(input.getFacility().getExpiration());
		bean.setIp(input.getFacility().getIp());
		bean.setSettings(input.getFacility().getSettings());

		if (input != null)
		{
			connection = getConnection(); //Get connection
			try
			{
				// Create new appuser record
				newid = new FacilityDaoImp().create(bean, connection);
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("createFacility");
			}
			commitConnection("createFacility");
		}
		return newid;
	}

	public ResponseContainerBean updateFacility(RequestContainerBean input, ResponseSessionContainerBean session)
	{
		LOG.debug("<updateFacility>");
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("update");
		input.setAction(rqABean);
		ResponseFacilityContainerBean rcb = new ResponseFacilityContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));

		//Force value of "0000-00-00 00:00:00" to avoid null exception in flinn.dao update
		if (input.getFacility().getExpiration() == null)
			input.getFacility().setExpiration(nullDate);

		if (input != null)
		{
			connection = getConnection(); //Get connection
			rcb = (ResponseFacilityContainerBean) (new FacilityDaoImp().handleFacilityUpdate(input, session, connection));
		}

		return rcb;
	}

	public int createUser(RequestContainerBean input, ResponseSessionContainerBean session) throws Exception
	{
		LOG.debug("<createUser>");
		int newid = -1;
		RequestAppUserBean bean = new RequestAppUserBean();
		bean.setFacilityid(session.getFacility().getFacilityid());
		bean.setLogin(input.getUser().getLogin());
		bean.setPassword(input.getUser().getPassword());
		bean.setValid(input.getUser().getValid());
		bean.setLaunch(input.getUser().getLaunch());
		bean.setExpiration(input.getUser().getExpiration());
		bean.setRoles(input.getUser().getRoles());
		bean.setSettings(input.getUser().getSettings());

		if (input != null)
		{
			connection = getConnection(); //Get connection
			try
			{
				// Create new appuser record
				newid = new AppUserDaoImp().create(bean, connection);
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("createUser");
			}
			commitConnection("createUser");
		}
		return newid;
	}

	public ResponseContainerBean findUser(RequestContainerBean input, ResponseSessionContainerBean session) throws Exception
	{
		LOG.debug("<findUser>");
		List<ResponseAppUserBean> users = null;
		RequestAppUserBean appUserBean = new RequestAppUserBean();

		appUserBean.setAppuserid(input.getUser().getAppuserid());

		if (input != null)
		{
			connection = getConnection(); //Get connection
			users = new AppUserDaoImp().find(appUserBean, null, connection);
		}

		ResponseAppUserContainerBean rcb = new ResponseAppUserContainerBean();
		rcb.setUser(users.get(0));
		return rcb;
	}

	public List<ResponseAppUserBean> findAllUsers(RequestContainerBean input, ResponseSessionContainerBean session, boolean isSuperAdmin, boolean invalid, String orderBy) throws Exception
	{
		LOG.debug("<findAllUsers>");
		RequestAppUserBean bean = new RequestAppUserBean();
		List<ResponseAppUserBean> userList = null;

		if (!isSuperAdmin)
			bean.setFacilityid(session.getFacility().getFacilityid());

		if (session != null)
		{
			connection = getConnection(); //Get connection

			if (invalid)
			{ //Show all
				userList = new AppUserDaoImp().find(bean, orderBy, connection);
			}
			else
			{ //Show only active
				userList = new AppUserDaoImp().findValid(bean, orderBy, connection, isSuperAdmin);
			}
		}

		return userList;
	}

	public ResponseContainerBean updateUser(RequestContainerBean input, ResponseSessionContainerBean session)
	{
		LOG.debug("<updateUser>");
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("update");
		input.setAction(rqABean);
		ResponseAppUserContainerBean rcb = new ResponseAppUserContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));

		//Force value of "0000-00-00 00:00:00" to avoid null exception in flinn.dao update
		if (input.getUser().getExpiration() == null)
			input.getUser().setExpiration(nullDate);

		if (input != null)
		{
			connection = getConnection(); //Get connection
			rcb = (ResponseAppUserContainerBean) (new AppUserDaoImp().handleUserUpdate(input, session, connection));
		}

		return rcb;
	}

	public int updateLastActivity(ResponseSessionContainerBean session) throws Exception
	{
		LOG.debug("<updateLastActivity>");
		int result = -1;
		if (session != null)
		{
			connection = getConnection(); //Get connection

			try
			{
				result = (new AppUserDaoImp().updateLastActivity(session, connection));
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("updateLastActivity");
			}
			commitConnection("updateLastActivity");

		}

		return result;
	}

	public AppUserRoleBean [] getAllRolesUser() throws Exception
	{
		LOG.debug("<getAllRolesUser>");
		AppUserRoleBean [] allRoles = null;
		connection = getConnection(); //Get connection
		allRoles = new AppUserDaoImp().getAllRoles(connection);

		return allRoles;
	}

	public ResponseContainerBean findAllLabTests(RequestContainerBean input, ResponseSessionContainerBean session, boolean valid, String orderby) throws Exception
	{
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("read");
		input.setAction(rqABean);
		ResponseLabTestsContainerBean rcb = new ResponseLabTestsContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		if (input != null)
		{
			connection = getConnection(); //Get connection
			rcb = (ResponseLabTestsContainerBean) (new LabDaoImp().handleLabTestRead(input, session, connection, valid, orderby));
		}

		return rcb;
	}

	public LabTestBean findLabTest(int labtestid) throws Exception
	{
		LOG.debug("<findLabTest>");
		LabTestBean labtest = new LabTestBean();
		labtest.setLabtestid(labtestid);
		if (labtest != null)
		{
			connection = getConnection(); //Get connection
			List<flinn.beans.LabTestBean> labtests = (new LabDaoImp().findTest(labtest, null, connection));

			if (labtests != null && labtests.size() > 0)
			{
				labtest = labtests.get(0);
			}
		}

		return labtest;
	}

	public int createLabTest(RequestContainerBean input, ResponseSessionContainerBean session) throws Exception
	{
		LOG.debug("<createLabTest>");
		int newid = -1;
		LabTestBean bean = new LabTestBean();
		bean.setLabtestid(input.getLab().getLabtest().getLabtestid());
		bean.setLabtestname(input.getLab().getLabtest().getLabtestname());
		bean.setValid(input.getLab().getLabtest().getValid());
		bean.setStartdate(input.getLab().getLabtest().getStartdate());
		bean.setDiscontinuedate(input.getLab().getLabtest().getDiscontinuedate());
		bean.setDetails(input.getLab().getLabtest().getDetails());

		if (input != null)
		{
			connection = getConnection(); //Get connection
			try
			{
				// Create new lab test record
				newid = new LabDaoImp().createTest(bean, connection);
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("createLabTest");
			}
			commitConnection("createLabTest");
		}
		return newid;
	}

	public ResponseContainerBean updateLabTest(RequestContainerBean input, ResponseSessionContainerBean session)
	{
		LOG.debug("<updateLabTest>");
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("update");
		input.setAction(rqABean);
		ResponseLabTestsContainerBean rcb = new ResponseLabTestsContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));

		LabTestBean bean = input.getLab().getLabtest();
		if (bean == null)
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab Test update submitted with no appropriate profile", 41);
		List<LabTestBean> labtests = null;
		LabTestBean b2 = new LabTestBean();
		b2.setLabtestid(bean.getLabtestid());

		if (input != null)
		{
			connection = getConnection(); //Get connection

			try
			{
				labtests = (new LabDaoImp().findTest(b2, null, connection));
			}
			catch (Exception e)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Lab Test retrieval failed in update (unknown error): " + e.getMessage(), 47);
			}

			if (labtests == null || labtests.size() == 0)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Lab Test retrieval failed in update (unknown error) No Exception", 49);
			}
			try
			{
				//Force value of "0000-00-00 00:00:00" to avoid null exception in flinn.dao update
				if (labtests.get(0).getStartdate() == null)
					labtests.get(0).setStartdate(nullDate);
				if (labtests.get(0).getDiscontinuedate() == null)
					labtests.get(0).setDiscontinuedate(nullDate);

				int ltid = (new LabDaoImp().updateTest(bean, labtests.get(0), connection));
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("updateLabTest");
				return DaoRequestManager.generateErrorBean(input.getAction(), "Lab Test update failed (unknown error): " + e.getMessage(), 48);
			}
			commitConnection("updateLabTest");

			b2 = new LabTestBean();
			b2.setLabtestid(bean.getLabtestid());
			try
			{
				labtests = (new LabDaoImp().findTest(b2, null, connection));
			}
			catch (Exception e)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Lab Test retrieval post-update failed (unknown error): " + e.getMessage(), 47);
			}

			if (labtests == null || labtests.size() == 0)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Lab Test retrieval post-update failed (unknown error) No Exception", 49);
			}

			LabTestBean [] lt = (LabTestBean []) labtests.toArray(new LabTestBean[0]);
			rcb.setLabtests(lt);
		}
		return rcb;
	}

	public ProgressNoteTagBean getNoteTag(int tagid) throws Exception
	{
		LOG.debug("<getNoteTag>");
		ProgressNoteTagBean notetag = null;
		if (tagid > 0)
		{
			connection = getConnection(); //Get connection

			ProgressNoteTagBean [] notetags = new ProgressNoteDaoImp().getTag(tagid, connection);

			if (notetags != null && notetags.length > 0)
			{
				notetag = notetags[0];
			}
		}
		return notetag;
	}

	public ResponseContainerBean findAllNoteTags(RequestContainerBean input, ResponseSessionContainerBean session, boolean invalid) throws Exception
	{
		LOG.debug("<findAllNoteTags>");
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("read");
		input.setAction(rqABean);
		ResponseProgressNoteTagsContainerBean rcb = new ResponseProgressNoteTagsContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		if (input != null)
		{
			connection = getConnection(); //Get connection
			rcb = (ResponseProgressNoteTagsContainerBean) (new ProgressNoteDaoImp().handleProgressNoteTagsRead(input, session, connection, !invalid));
		}

		return rcb;
	}

	public int createNoteTag(ProgressNoteTagBean bean, ResponseSessionContainerBean session) throws Exception
	{
		LOG.debug("<createNoteTag>");
		int newid = -1;

		if (bean != null)
		{
			connection = getConnection(); //Get connection
			try
			{
				// Create new progress note tag record
				newid = new ProgressNoteDaoImp().createTag(bean, session, connection);
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("createNoteTag");
			}
			commitConnection("createNoteTag");
		}
		return newid;
	}

	public int updateNoteTag(ProgressNoteTagBean bean, ResponseSessionContainerBean session)
	{
		LOG.debug("<updateNoteTag>");
		int pnotetagid = -1;
		if (bean == null)
		{
			LOG.error("Progress note tag update submitted with no appropriate profile");
		}

		ProgressNoteTagBean [] notetags = null;
		ProgressNoteTagBean b2 = new ProgressNoteTagBean();
		b2.setProgressnotetagid(bean.getProgressnotetagid());

		if (bean != null)
		{
			connection = getConnection(); //Get connection

			try
			{
				notetags = (new ProgressNoteDaoImp().getTag(bean.getProgressnotetagid(), connection));
			}
			catch (Exception e)
			{
				LOG.error("Progress note tag retrieval failed in update (unknown error): " + e.getMessage());
			}

			if (notetags == null || notetags.length == 0)
			{
				LOG.error("Progress note tag retrieval failed in update (unknown error) No Exception");
			}
			try
			{
				// Update current lab test record
				int pntid = (new ProgressNoteDaoImp().updateTag(bean, notetags[0], connection));
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("updateNoteTag");
			}
			commitConnection("updateNoteTag");

			b2 = new ProgressNoteTagBean();
			b2.setProgressnotetagid(bean.getProgressnotetagid());
			try
			{
				notetags = (new ProgressNoteDaoImp().getTag(b2.getProgressnotetagid(), connection));
			}
			catch (Exception e)
			{
				LOG.error("Progress note tag retrieval post-update failed (unknown error): " + e.getMessage());
			}

			if (notetags == null || notetags.length == 0)
			{
				LOG.error("Progress note tag retrieval post-update failed (unknown error) No Exception");
			}

			pnotetagid = notetags[0].getProgressnotetagid();
		}
		return pnotetagid;
	}

	public ResponseTreatmentBean getTreatment(int treatmentid) throws Exception
	{
		LOG.debug("<getTreatment>");
		ResponseTreatmentBean treatment = null;
		if (treatmentid > 0)
		{
			connection = getConnection(); //Get connection
			treatment = new TreatmentDaoImp().getTreatmentByID(treatmentid, connection);
		}
		return treatment;
	}

	public List<ResponseTreatmentBean> findAllTreatments(RequestContainerBean input, ResponseSessionContainerBean session, boolean valid, boolean isSuperAdmin, String orderBy) throws Exception
	{
		LOG.debug("<findAllTreatments>");
		List<ResponseTreatmentBean> rtnList = null;

		if (session != null)
		{
			connection = getConnection(); //Get connection

			if (valid)
			{ //Show only active
				rtnList = new TreatmentDaoImp().findValid(orderBy, connection, isSuperAdmin);
			}
			else
			{
				//Show all
				rtnList = new TreatmentDaoImp().findAllTreatments(orderBy, connection);
			}
		}

		return rtnList;
	}

	public ResponseContainerBean handleTreatmentRead(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		LOG.debug("<handleTreatmentRead>");
		List<ResponseTreatmentBean> treatments = null;

		try
		{
			treatments = findAllTreatments(input, session, true, true, null);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval failed (unknown error): " + e.getMessage(), 47);
		}

		if (treatments == null || treatments.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval failed (unknown error) No Exception", 49);
		}

		ResponseTreatmentSearchContainerBean rcb = new ResponseTreatmentSearchContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setTreatments(treatments.toArray(new ResponseTreatmentBean[treatments.size()]));
		return rcb;
	}

	public int createTreatment(RequestContainerBean input, ResponseSessionContainerBean session) throws Exception
	{
		LOG.debug("<createTreatment>");
		int newid = -1;
		RequestTreatmentBean bean = new RequestTreatmentBean();
		bean.setTreatmentgroupid(input.getTreatment().getTreatmentgroupid());
		bean.setTreatmentname(input.getTreatment().getTreatmentname());
		bean.setTreatmentabbreviation(input.getTreatment().getTreatmentabbreviation());
		bean.setValid(input.getTreatment().getValid());
		bean.setDetails(input.getTreatment().getDetails());

		if (input != null)
		{
			connection = getConnection(); //Get connection
			try
			{
				// Create new treatment record
				newid = new TreatmentDaoImp().create(bean, connection);
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("createTreatment");
			}
			commitConnection("createTreatment");
		}
		return newid;
	}

	public ResponseContainerBean updateTreatment(RequestContainerBean input, ResponseSessionContainerBean session)
	{
		LOG.debug("<updateTreatment>");
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("update");
		input.setAction(rqABean);
		ResponseTreatmentContainerBean rcb = new ResponseTreatmentContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));

		if (input != null)
		{
			connection = getConnection(); //Get connection
			rcb = (ResponseTreatmentContainerBean) (new TreatmentDaoImp().handleTreatmentUpdate(input, session, connection));
		}

		return rcb;
	}

	public ResponseContainerBean findAllTreatmentGroups(RequestContainerBean input, ResponseSessionContainerBean session, boolean valid, boolean isSuperAdmin, String orderBy) throws Exception
	{
		LOG.debug("<findAllTreatmentGroups>");
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("read");
		input.setAction(rqABean);
		ResponseTreatmentGroupContainerBean rcb = new ResponseTreatmentGroupContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		if (session != null)
		{
			connection = getConnection(); //Get connection
			rcb = (ResponseTreatmentGroupContainerBean) (new TreatmentDaoImp().handleTreatmentGroupRead(input, session, connection, valid));
		}

		return rcb;
	}

	public TreatmentGroupBean getTreatmentGroup(int treatmentgroupid) throws Exception
	{
		LOG.debug("<getTreatmentGroup>");
		TreatmentGroupBean group = new TreatmentGroupBean();
		group.setTreatmentgroupid(treatmentgroupid);
		if (group != null)
		{
			connection = getConnection(); //Get connection
			List<flinn.beans.TreatmentGroupBean> groups = (new TreatmentDaoImp().findGroup(group, null, connection));

			if (groups != null && groups.size() > 0)
			{
				group = groups.get(0);
			}
		}
		return group;
	}

	public int createTreatmentGroup(RequestContainerBean input, ResponseSessionContainerBean session) throws Exception
	{
		LOG.debug("<createTreatmentGroup>");
		int newid = -1;
		TreatmentGroupBean bean = new TreatmentGroupBean();
		bean.setTreatmentgroupid(input.getTreatment().getGroup().getTreatmentgroupid());
		bean.setTreatmentgroupname(input.getTreatment().getGroup().getTreatmentgroupname());
		bean.setTreatmentgroupabbreviation(input.getTreatment().getGroup().getTreatmentgroupabbreviation());
		bean.setValid(input.getTreatment().getGroup().getValid());

		if (input != null)
		{
			connection = getConnection(); //Get connection
			try
			{
				// Create new treatment group record
				newid = new TreatmentDaoImp().createGroup(bean, connection);
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("createTreatmentGroup");
			}
			commitConnection("createTreatmentGroup");
		}
		return newid;
	}

	public ResponseContainerBean updateTreatmentGroup(RequestContainerBean input, ResponseSessionContainerBean session)
	{
		LOG.debug("<updateTreatmentGroup>");
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("update");
		input.setAction(rqABean);
		ResponseTreatmentGroupContainerBean rcb = new ResponseTreatmentGroupContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));

		TreatmentGroupBean bean = input.getTreatment().getGroup();
		if (bean == null)
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment group update submitted with no appropriate profile", 41);
		List<TreatmentGroupBean> groups = null;
		TreatmentGroupBean b2 = new TreatmentGroupBean();
		b2.setTreatmentgroupid(bean.getTreatmentgroupid());

		if (input != null)
		{
			connection = getConnection(); //Get connection

			try
			{
				groups = (new TreatmentDaoImp().findGroup(b2, null, connection));
			}
			catch (Exception e)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment group retrieval failed in update (unknown error): " + e.getMessage(), 47);
			}

			if (groups == null || groups.size() == 0)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment group retrieval failed in update (unknown error) No Exception", 49);
			}
			try
			{
				// Update current treatment group record
				int tgid = (new TreatmentDaoImp().updateGroup(bean, groups.get(0), connection));
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("updateTreatmentGroup");
				return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment group update failed (unknown error): " + e.getMessage(), 48);
			}
			commitConnection("updateTreatmentGroup");

			b2 = new TreatmentGroupBean();
			b2.setTreatmentgroupid(bean.getTreatmentgroupid());
			try
			{
				groups = (new TreatmentDaoImp().findGroup(b2, null, connection));
			}
			catch (Exception e)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment group retrieval post-update failed (unknown error): " + e.getMessage(), 47);
			}

			if (groups == null || groups.size() == 0)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment group retrieval post-update failed (unknown error) No Exception", 49);
			}

			TreatmentGroupBean [] tg = (TreatmentGroupBean []) groups.toArray(new TreatmentGroupBean[0]);
			rcb.setGroups(tg);
		}
		return rcb;
	}

	public ResponsePrescriptionBean getPrescription(int prescriptionid) throws Exception
	{
		LOG.debug("<getPrescription>");
		ResponsePrescriptionBean facility = null;
		if (prescriptionid > 0)
		{
			connection = getConnection(); //Get connection

			flinn.beans.request.RequestPrescriptionBean beanreq = new flinn.beans.request.RequestPrescriptionBean();
			beanreq.setPrescriptionid(prescriptionid);
			List<flinn.beans.response.ResponsePrescriptionBean> prescriptions = new PrescriptionDaoImp().find(beanreq, null, connection);

			if (prescriptions != null && prescriptions.size() > 0)
			{
				facility = prescriptions.get(0);
			}

			disposeConnection("getPrescription");
		}
		return facility;
	}

	public List<ResponsePrescriptionBean> findAllPrescriptions(RequestContainerBean input, ResponseSessionContainerBean session, boolean valid, boolean isSuperAdmin, boolean invalid, String orderBy)
	    throws Exception
	{
		LOG.debug("<findAllPrescriptions>");
		RequestPrescriptionBean bean = new RequestPrescriptionBean();
		List<ResponsePrescriptionBean> rtnList = null;

		if (session != null)
		{
			connection = getConnection(); //Get connection

			rtnList = new PrescriptionDaoImp().findAllPrescriptions(bean, orderBy, connection);
		}

		disposeConnection("findAllPrescriptions");

		return rtnList;
	}

	public int createPrescription(RequestContainerBean input, ResponseSessionContainerBean session) throws Exception
	{
		LOG.debug("<createPrescription>");
		int newid = -1;
		RequestPrescriptionBean bean = new RequestPrescriptionBean();
		bean.setPrescriptionid(input.getPrescription().getPrescriptionid());
		bean.setTreatmentid(input.getPrescription().getTreatmentid());
		bean.setPatientid(input.getPrescription().getPatientid());
		bean.setRcopiaid(input.getPrescription().getRcopiaid());
		bean.setEntrydate(input.getPrescription().getEntrydate());
		bean.setDiscontinue(input.getPrescription().getDiscontinue());
		bean.setDailydose(input.getPrescription().getDailydose());
		bean.setDoctorname(input.getPrescription().getDoctorname());

		if (input != null)
		{
			connection = getConnection(); //Get connection
			try
			{
				// Create new prescription record
				newid = new PrescriptionDaoImp().create(bean, connection);
			}
			catch (Exception e)
			{
				LOG.error(e);
				rollbackConnection("createPrescription");
			}
			commitConnection("createPrescription");

			disposeConnection("createPrescription");
		}
		return newid;
	}

	public ResponseSessionContainerBean getSession(String authcode, HttpServletRequest req)
	{
		LOG.debug("<getSession>");
		ResponseSessionContainerBean session = null;
		RequestContainerBean input = new RequestContainerBean();
		input.setAction(new RequestActionBean());
		// Currently only the authcode is needed, 
		// no other pieces of the RequestActionBean are validated for consistency.
		input.getAction().setAuthcode(authcode);

		AuthcodeDao acd = new AuthcodeDao();
		session = acd.validate(input, req);
		return session;
	}

	public Connection getConnection()
	{
		LOG.debug("<getConnection>");
		try
		{
			if (connection == null || connection.isClosed())
			{
				renewConnection();
				if (connection == null || connection.isClosed())
				{
					LOG.error("Unable to renew connection in DaoAppManager.getConnection");
				}
			}
		}
		catch (Exception e)
		{
			LOG.error("Unable to renew connection in DaoAppManager.getConnection: " + e);
		}
		return connection;
	}

	public void commitConnection(String functionCall)
	{
		try
		{
			connection.commit();
		}
		catch (Exception e)
		{
			LOG.error(functionCall + " post-update failed (unknown error): " + e);
			rollbackConnection(functionCall);
		}
	}

	public void rollbackConnection(String functionCall)
	{
		try
		{
			connection.rollback();
		}
		catch (SQLException e1)
		{
			LOG.error("Error rolling back connection in " + functionCall + ": " + e1);
		}
	}

	public void disposeConnection(String functionCall)
	{
		try
		{
			if (connection != null && !connection.isClosed())
			{
				closeConnection();
				connection = null;
			}
		}
		catch (SQLException e)
		{
			LOG.error("Unable to close connection after use in " + functionCall);
		}
	}
}
