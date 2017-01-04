package flinn.recommend.dao.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.request.RequestContainerBean;
import flinn.beans.request.RequestGuidelineReasonBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseGuidelineReasonBean;
import flinn.beans.response.ResponseGuidelineReasonContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.recommend.dao.DaoRecommendManager;
import flinn.recommend.dao.GuidelineReasonDao;

public class GuidelineReasonDaoImp extends GuidelineReasonDao {
	protected static final Logger LOG = Logger.getLogger(GuidelineReasonDaoImp.class);
	static {
		LOG.debug("Log appender instantiated for " + GuidelineReasonDaoImp.class);
	}

	public ResponseContainerBean handleGuidelineReasonCreate(
			RequestContainerBean input, ResponseSessionContainerBean session,
			Connection connection) {
		// sanity checks on incoming data. Ensure no changes to aspects of the
		// data we don't want changed.
		RequestGuidelineReasonBean bean = input.getGuidelinereason();
		if (bean == null)
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Guideline reason create submitted with no appropriate info", 41);

		// *NOT* only Admins can create new reasons
		/*
		if (!AdminRole.isRecommendAdmin(session)) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"User does not have Admin permissions to create new reasons",
							41);
		}
		*/

		int newid = 0;
		List<ResponseGuidelineReasonBean> reasons = null;
		
		try {
			// Set doctorname
			if (bean != null){			
				bean.setDoctorname(session.getUser().getSettings().get("FullName"));
			}
			
			// Create new Guideline reason record
			newid = create(bean, connection);

		} catch (Exception e) {
			LOG.error(e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handleGuidelineReasonCreate: "
						+ e);
			}
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Guideline reason create failed (unknown error): " + e.getMessage(),
					48);
		}

		try {
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handleGuidelineReasonCreate: "
						+ e);
			}
			LOG.error("Guideline reason retrieval post-update failed (unknown error): "
					+ e);
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Guideline reason retrieval post-create failed (unknown error): "
							+ e.getMessage(), 49);
		}
		if (newid < 1)
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Guideline reason create failed (unknown error - no returned ID)",
					47);
		bean = new RequestGuidelineReasonBean();
		bean.setReasonid(newid);
		try {
			reasons = find(bean, null, connection);
		} catch (Exception e) {
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Guideline reason retrieval post-create failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (reasons == null || reasons.size() == 0) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"Guideline reason retrieval post-create failed (unknown error) No Exception",
							49);
		}

		ResponseGuidelineReasonContainerBean rcb = new ResponseGuidelineReasonContainerBean();
		rcb.setGuidelinereason(reasons.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handleGuidelineReasonRead(RequestContainerBean input,
			ResponseSessionContainerBean session, Connection connection) {
		RequestGuidelineReasonBean bean = input.getGuidelinereason();
		List<ResponseGuidelineReasonBean> reasons = null;

		try {
			reasons = find(bean, null, connection);
		} catch (Exception e) {
			return DaoRecommendManager.generateErrorBean(
					input.getAction(),
					"Guideline reason retrieval failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (reasons == null || reasons.size() == 0) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"Guideline reason retrieval failed (unknown error) No Exception",
							49);
		}

		ResponseGuidelineReasonContainerBean rcb = new ResponseGuidelineReasonContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setGuidelinereason(reasons.get(0));
		return rcb;
	}

}
