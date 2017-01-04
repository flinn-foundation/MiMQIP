package flinn.dao.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.request.RequestContainerBean;
import flinn.beans.request.RequestFacilityBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseFacilityBean;
import flinn.beans.response.ResponseFacilityContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.dao.DaoRequestManager;
import flinn.dao.FacilityDao;
import flinn.util.cache.EHCacheImpl;
import flinn.util.cache.ICache;

public class FacilityDaoImp extends FacilityDao {

	protected static final Logger LOG = Logger.getLogger(FacilityDaoImp.class);

	public String nullDate = "0000-00-00 00:00:00";

	static {
		LOG.warn("Log appender instantiated for " + FacilityDaoImp.class);
	}

	public ResponseContainerBean handleFacilityCreate(
			RequestContainerBean input, ResponseSessionContainerBean session,
			Connection connection) {
		// sanity checks on incoming data. Ensure no changes to aspects of the
		// data we don't want changed.
		RequestFacilityBean bean = input.getFacility();
		if (bean == null)
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Facility create submitted with no appropriate info", 41);
		/*
		 * if (acd.validate(user.getAuthcode(), request) == null){ return
		 * DaoRequestManager
		 * .generateErrorBean(input.getAction(),"Invalid authcode (not permitted)"
		 * ,44); }
		 */
		int newid = 0;
		List<ResponseFacilityBean> facilities = null;

		try {
			// Create new facility record
			newid = create(bean, connection);

		} catch (Exception e) {
			LOG.error(e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handleFacilityCreate: "
						+ e);
			}
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Facility create failed (unknown error): "
									+ e.getMessage(), 48);
		}

		try {
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handleFacilityCreate: "
						+ e);
			}
			LOG.error("Facility retrieval post-update failed (unknown error): "
					+ e);
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Facility retrieval post-create failed (unknown error): "
							+ e.getMessage(), 49);
		}
		if (newid < 1)
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Facility create failed (unknown error - no returned ID)",
					47);
		bean = new RequestFacilityBean();
		bean.setFacilityid(newid);
		try {
			facilities = find(bean, null, connection);
		} catch (Exception e) {
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Facility retrieval post-create failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (facilities == null || facilities.size() == 0) {
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Facility retrieval post-create failed (unknown error) No Exception",
							49);
		}

		ResponseFacilityContainerBean rcb = new ResponseFacilityContainerBean();
		rcb.setFacility(facilities.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handleFacilityRead(RequestContainerBean input,
			ResponseSessionContainerBean session, Connection connection) {
		RequestFacilityBean bean = input.getFacility();
		List<ResponseFacilityBean> facilities = null;

		try {
			facilities = find(bean, null, connection);
		} catch (Exception e) {
			return DaoRequestManager.generateErrorBean(
					input.getAction(),
					"Facility retrieval failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (facilities == null || facilities.size() == 0) {
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Facility retrieval failed (unknown error) No Exception",
					49);
		}

		ResponseFacilityContainerBean rcb = new ResponseFacilityContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setFacility(facilities.get(0));
		return rcb;
	}

	public ResponseContainerBean handleFacilityUpdate(
			RequestContainerBean input, ResponseSessionContainerBean session,
			Connection connection) {
		RequestFacilityBean bean = input.getFacility();
		if (bean == null)
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Facility update submitted with no appropriate profile",
							41);
		List<ResponseFacilityBean> facilities = null;
		RequestFacilityBean b2 = new RequestFacilityBean();
		b2.setFacilityid(bean.getFacilityid());

		try {
			facilities = find(b2, null, connection);
		} catch (Exception e) {
			return DaoRequestManager.generateErrorBean(
					input.getAction(),
					"Facility retrieval failed in update (unknown error): "
							+ e.getMessage(), 47);
		}

		if (facilities == null || facilities.size() == 0) {
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Facility retrieval failed in update (unknown error) No Exception",
							49);
		}
		try {
			// Update current facility record
			if (facilities.get(0).getExpiration() == null)
				facilities.get(0).setExpiration(nullDate);
			update(bean, facilities.get(0), connection);
		} catch (Exception e) {
			LOG.error(e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handleFacilityUpdate: "
						+ e);
			}
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Facility update failed (unknown error): "
									+ e.getMessage(), 48);
		}

		try {
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handleFacilityUpdate: "
						+ e);
			}
			LOG.error("Facility retrieval post-update failed (unknown error): "
					+ e);
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Facility retrieval post-update failed (unknown error): "
							+ e.getMessage(), 49);
		}
		b2 = new RequestFacilityBean();
		b2.setFacilityid(bean.getFacilityid());
		try {
			facilities = find(b2, null, connection);
		} catch (Exception e) {
			return DaoRequestManager.generateErrorBean(input.getAction(),
					"Facility retrieval post-update failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (facilities == null || facilities.size() == 0) {
			return DaoRequestManager
					.generateErrorBean(
							input.getAction(),
							"Facility retrieval post-update failed (unknown error) No Exception",
							49);
		}

		ResponseFacilityContainerBean rcb = new ResponseFacilityContainerBean();
		rcb.setFacility(facilities.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseFacilityBean findFacilityById(int facilityid,
			Connection connection) throws Exception {

		ResponseFacilityBean responseFacilityBean;
		LOG.debug("findFacilityById --> looking for ID: "
				+ String.valueOf(facilityid));

		ICache<String, ResponseFacilityBean> facilityCache = EHCacheImpl
				.getDefaultInstance("facilityCache");

		// try to retrieve the object from the cache by id
		responseFacilityBean = facilityCache.get(String.valueOf(facilityid));
		if (responseFacilityBean != null) {
			LOG.debug("findFacilityById --> ID: " + String.valueOf(facilityid)
					+ " found in cache");
		}

		// if the returned object is null, it was not found in the cache
		if (responseFacilityBean == null) {

			RequestFacilityBean f = new RequestFacilityBean();
			f.setFacilityid(facilityid);
			
			List<ResponseFacilityBean> facilities = null;
			facilities = find(f, null, connection);
			if (facilities != null && facilities.size() > 0) {
				// get the first item from the response and add to cache
				responseFacilityBean = facilities.get(0);
			}
		}
		return responseFacilityBean;
	}

}
