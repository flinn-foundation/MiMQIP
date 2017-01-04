package flinn.dao.imp;

import flinn.beans.LabTestBean;
import flinn.beans.request.RequestContainerBean;
import flinn.beans.request.RequestLabBean;
import flinn.beans.request.RequestLabSearchBean;
import flinn.beans.response.*;
import flinn.dao.DaoRequestManager;
import flinn.dao.LabDao;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LabDaoImp extends LabDao
{

	protected static final Logger LOG = Logger.getLogger(LabDaoImp.class);

	static
	{
		LOG.debug("Log appender instantiated for " + LabDaoImp.class);
	}

	public ResponseContainerBean handleLabCreate(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		// sanity checks on incoming data.  Ensure no changes to aspects of the data we don't want changed.
		RequestLabBean bean = input.getLab();
		if (bean == null)
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab create submitted with no appropriate info", 41);
		if (bean.getPatientid() < 1)
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab create submitted with no patient id", 41);
		if (bean.getLabtext() == null || bean.getLabtext().trim().equals(""))
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab create submitted with empty text", 41);

		int newid = 0;
		List<ResponseLabBean> labs = null;

		try
		{
			// Create new lab record
			newid = create(bean, connection);

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
				LOG.error("Error rolling back connection in handleLabCreate: " + e);
			}
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab create failed (unknown error): " + e.getMessage(), 48);
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
				LOG.error("Error rolling back connection in handleLabCreate: " + e);
			}
			LOG.error("Lab retrieval post-update failed (unknown error): " + e);
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab retrieval post-create failed (unknown error): " + e.getMessage(), 49);
		}
		if (newid < 1)
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab create failed (unknown error - no returned ID)", 47);
		bean = new RequestLabBean();
		bean.setLabid(newid);
		try
		{
			labs = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab retrieval post-create failed (unknown error): " + e.getMessage(), 47);
		}

		if (labs == null || labs.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab retrieval post-create failed (unknown error) No Exception", 49);
		}

		ResponseLabContainerBean rcb = new ResponseLabContainerBean();
		rcb.setLab(labs.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handleLabRead(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		RequestLabBean bean = input.getLab();
		List<ResponseLabBean> labs = null;

		try
		{
			labs = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab retrieval failed (unknown error): " + e.getMessage(), 47);
		}

		if (labs == null || labs.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab retrieval failed (unknown error) No Exception", 49);
		}

		ResponseLabContainerBean rcb = new ResponseLabContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setLab(labs.get(0));
		return rcb;
	}

	public ResponseContainerBean handleLabSearch(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		RequestLabSearchBean search = input.getLabsearch();

		//Check patient facility to see if it = facility before returning labs
		ResponsePatientBean patient = null;
		try
		{
			patient = new PatientDaoImp().findPatientById(search.getPatientid(), connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Patient retrieval failed (Lab) (unknown error): " + e.getMessage(), 47);
		}
		if (patient == null)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Patient retrieval failed (Lab) (Incorrect patientid)", 49);
		}
		if (patient.getFacilityid() != session.getFacility().getFacilityid())
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Patient retrieval failed (Lab) (Incorrect facility/patientid)", 49);
		}

		// Set up the search by labs.
		RequestLabBean bean = new RequestLabBean();
		bean.setPatientid(search.getPatientid());
		if (search.getLabtest() != null && search.getLabtest().getLabtestid() > 0)
		{
			bean.setLabtest(new LabTestBean());
			bean.getLabtest().setLabtestid(search.getLabtest().getLabtestid());
		}

		List<ResponseLabBean> labs = null;
		try
		{
			labs = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Labs retrieval failed (unknown error): " + e.getMessage(), 47);
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
		if (page * pagecount + pagecount > labs.size())
			thispagecount = labs.size() - page * pagecount;
		if (thispagecount < 0)
			thispagecount = 0;

		ResponseLabBean[] labsArr = new ResponseLabBean[thispagecount];
		for (int i = 0; i < thispagecount; i++)
		{
			labsArr[i] = labs.get((page * pagecount) + i);
		}

		ResponseLabSearchContainerBean rcb = new ResponseLabSearchContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setLabs(labsArr);
		rcb.setPage(page + 1);
		rcb.setPagecount(pagecount);
		rcb.setTotal(labs.size());
		return rcb;
	}

	public ResponseContainerBean handleLabTestRead(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection, boolean valid, String orderby)
	{

		List<LabTestBean> testList = null;
		LabTestBean[] tests = null;
		try
		{
			LabTestBean bean = new LabTestBean();
			if (valid) //Set bean to show valid only, if !valid bean isn't set to show all
			{
				bean.setValid(valid);
			}
			testList = findTest(bean, orderby, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Lab Tests retrieval failed (unknown error): " + e.getMessage(), 47);
		}
		if (testList == null || testList.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "No Lab Tests retrieved (unknown error): ", 49);
		}
		if (testList.size() > 0)
		{
			tests = new LabTestBean[testList.size()];
			for (int i = 0; i < testList.size(); i++)
			{
				tests[i] = testList.get(i);
			}
		}

		ResponseLabTestsContainerBean rcb = new ResponseLabTestsContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setLabtests(tests);
		return rcb;
	}

	public ResponseLabBean[] findByPatientId(int patientid,
                                             Connection connection) {
		RequestLabBean bean = new RequestLabBean();
		bean.setPatientid(patientid);

		List<ResponseLabBean> labs = null;
		try
		{
			labs = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return null;
		}


		ResponseLabBean[] labsArr = new ResponseLabBean[labs.size()];
		labsArr = labs.toArray(labsArr);
		return labsArr;
	}
}
