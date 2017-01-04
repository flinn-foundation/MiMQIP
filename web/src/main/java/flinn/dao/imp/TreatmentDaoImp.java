package flinn.dao.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.TreatmentGroupBean;
import flinn.beans.request.RequestContainerBean;
import flinn.beans.request.RequestTreatmentBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.beans.response.ResponseTreatmentBean;
import flinn.beans.response.ResponseTreatmentContainerBean;
import flinn.beans.response.ResponseTreatmentGroupContainerBean;
import flinn.dao.DaoRequestManager;
import flinn.dao.TreatmentDao;
import flinn.util.cache.EHCacheImpl;
import flinn.util.cache.ICache;

public class TreatmentDaoImp extends TreatmentDao
{

	protected static final Logger LOG = Logger.getLogger(TreatmentDaoImp.class);
	static
	{
		LOG.debug("Log appender instantiated for " + TreatmentDaoImp.class);
	}

	public ResponseContainerBean handleTreatmentCreate(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		// sanity checks on incoming data.  Ensure no changes to aspects of the data we don't want changed.
		RequestTreatmentBean bean = input.getTreatment();
		if (bean == null)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment create submitted with no appropriate info", 41);
		}

		int newid = 0;
		List<ResponseTreatmentBean> treatments = null;

		try
		{
			// Create new treatment record
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
				LOG.error("Error rolling back connection in handleTreatmentCreate: " + e);
			}
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment create failed (unknown error): " + e.getMessage(), 48);
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
				LOG.error("Error rolling back connection in handleTreatmentCreate: " + e);
			}
			LOG.error("Treatment retrieval post-update failed (unknown error): " + e);
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval post-create failed (unknown error): " + e.getMessage(), 49);
		}
		if (newid < 1)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment create failed (unknown error - no returned ID)", 47);
		}
		bean = new RequestTreatmentBean();
		bean.setTreatmentid(newid);
		try
		{
			treatments = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval post-create failed (unknown error): " + e.getMessage(), 47);
		}

		if (treatments == null || treatments.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval post-create failed (unknown error) No Exception", 49);
		}

		ResponseTreatmentContainerBean rcb = new ResponseTreatmentContainerBean();
		rcb.setTreatment(treatments.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handleTreatmentRead(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		RequestTreatmentBean bean = input.getTreatment();
		List<ResponseTreatmentBean> treatments = null;

		try
		{
			treatments = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval failed (unknown error): " + e.getMessage(), 47);
		}

		if (treatments == null || treatments.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval failed (unknown error) No Exception", 49);
		}

		ResponseTreatmentContainerBean rcb = new ResponseTreatmentContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setTreatment(treatments.get(0));
		return rcb;
	}

	public ResponseContainerBean handleTreatmentUpdate(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		RequestTreatmentBean bean = input.getTreatment();
		if (bean == null)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment update submitted with no appropriate profile", 41);
		}
		List<ResponseTreatmentBean> treatments = null;
		RequestTreatmentBean b2 = new RequestTreatmentBean();
		b2.setTreatmentid(bean.getTreatmentid());

		try
		{
			treatments = find(b2, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval failed in update (unknown error): " + e.getMessage(), 47);
		}

		if (treatments == null || treatments.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval failed in update (unknown error) No Exception", 49);
		}
		try
		{
			// Update current prescription record
			update(bean, treatments.get(0), connection);
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
				LOG.error("Error rolling back connection in handleTreatmentUpdate: " + e);
			}
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment update failed (unknown error): " + e.getMessage(), 48);
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
				LOG.error("Error rolling back connection in handleTreatmentUpdate: " + e);
			}
			LOG.error("Treatment retrieval post-update failed (unknown error): " + e);
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval post-update failed (unknown error): " + e.getMessage(), 49);
		}
		b2 = new RequestTreatmentBean();
		b2.setTreatmentid(bean.getTreatmentid());
		try
		{
			treatments = find(b2, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval post-update failed (unknown error): " + e.getMessage(), 47);
		}

		if (treatments == null || treatments.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment retrieval post-update failed (unknown error) No Exception", 49);
		}

		ResponseTreatmentContainerBean rcb = new ResponseTreatmentContainerBean();
		rcb.setTreatment(treatments.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handleTreatmentGroupRead(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection, boolean valid)
	{
		List<TreatmentGroupBean> groupList = null;
		TreatmentGroupBean [] groups = null;
		try
		{
			TreatmentGroupBean bean = new TreatmentGroupBean();
			if (valid) //Set bean to show valid only, if !valid bean isn't set to show all
			{
				bean.setValid(valid);
			}
			groupList = findGroup(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Treatment Groups retrieval failed (unknown error): " + e.getMessage(), 47);
		}
		if (groupList == null || groupList.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "No Treatment Groups retrieved (unknown error): ", 49);
		}
		if (groupList.size() > 0)
		{
			groups = new TreatmentGroupBean[groupList.size()];
			for (int i = 0; i < groupList.size(); i++)
			{
				groups[i] = groupList.get(i);
			}
		}

		ResponseTreatmentGroupContainerBean rcb = new ResponseTreatmentGroupContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setGroups(groups);
		return rcb;
	}

	public ResponseTreatmentBean getTreatmentByID(int treatmentid, Connection connection) throws Exception
	{
		ResponseTreatmentBean responseTreatmentBean;
		// LOG.debug("getTreatmentById --> looking for ID: " + String.valueOf(treatmentid));

		ICache<String, ResponseTreatmentBean> treatmentCache = EHCacheImpl.getDefaultInstance("treatmentCache");

		// try to retrieve the object from the cache by id
		responseTreatmentBean = treatmentCache.get(String.valueOf(treatmentid));
		if (responseTreatmentBean != null) {
			// LOG.debug("getTreatmentById --> ID: " + String.valueOf(treatmentid)
			//		+ " found in cache");
		}
		
		// if the returned object is null, it was not found in the cache
		if (responseTreatmentBean == null) {
			if (treatmentid > 0)
			{
				RequestTreatmentBean tmreq = new RequestTreatmentBean();
				tmreq.setTreatmentid(treatmentid);
				List<ResponseTreatmentBean> treatments = find(tmreq, null, connection);
	
				if (treatments != null && treatments.size() > 0)
				{
					// get the first item from the response and add to cache
					responseTreatmentBean = treatments.get(0);
					treatmentCache.put(String.valueOf(responseTreatmentBean.getTreatmentid()), responseTreatmentBean);
				}
			}
		}
		return responseTreatmentBean;
	}

	public ResponseTreatmentBean getTreatmentByGenericDrugNameAndForm(String genericDrugName, String form, Connection connection) throws Exception
	{
		ResponseTreatmentBean responseTreatmentBean;
		LOG.debug("getTreatmentByGenericDrugName --> looking for Generic Drug Name: " + String.valueOf(genericDrugName));

		ICache<String, ResponseTreatmentBean> treatmentCache = EHCacheImpl.getDefaultInstance("treatmentCache");
		
		// try to retrieve the object from the cache by id
		responseTreatmentBean = treatmentCache.get(String.valueOf(genericDrugName + "---" + form));
		if (responseTreatmentBean != null) {
			LOG.debug("getTreatmentByGenericDrugName --> ID: " + String.valueOf(genericDrugName + "---" + form)
					+ " found in cache");
		}
		
		// if the returned object is null, it was not found in the cache
		if (responseTreatmentBean == null) {
			if (genericDrugName != null && genericDrugName.length() > 0)
			{
				List<ResponseTreatmentBean> treatments = findValid(null, connection, false);
	
				if (treatments != null && treatments.size() > 0)
				{
					// Find the flinn.beans that match, and get specific.
					for (int i = 0; i < treatments.size(); i++) {
						ResponseTreatmentBean bean = treatments.get(i);
						if (bean.getDetails() != null && bean.getDetails().get("GenericNamePattern") != null 
								&& bean.getDetails().get("GenericNamePattern").equalsIgnoreCase(genericDrugName)) {
							if (bean.getDetails().get("FormulationPattern") == null || bean.getDetails().get("FormulationPattern").equals("")) {
								// If we haven't found a more specific answer already, this is the default choice.
								if (responseTreatmentBean == null) {
									responseTreatmentBean = bean;
								}
							} else if (form != null && !form.equals("")){
								String testForm = bean.getDetails().get("FormulationPattern");
								if (form.toLowerCase().matches(testForm)) {
									if (responseTreatmentBean == null 
											|| responseTreatmentBean.getDetails().get("FormulationPattern") == null 
											|| responseTreatmentBean.getDetails().get("FormulationPattern").equals("")) {
										// The formulation pattern matches, and the previous selection (if there was one) didn't have a formulation pattern.
										responseTreatmentBean = bean;
									} else if (responseTreatmentBean.getDetails().get("FormulationPattern").length() < bean.getDetails().get("FormulationPattern").length()) {
										// The pattern for this bean is more specific than the one for the previous match.
										responseTreatmentBean = bean;
									}
								}
							}
						}
					}
					if (responseTreatmentBean != null) {
						treatmentCache.put(String.valueOf(genericDrugName + "---" + form), responseTreatmentBean);
					}
				}
			}
		}
		return responseTreatmentBean;
	}

}
