package flinn.recommend.dao.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.request.RequestContainerBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.recommend.beans.request.RequestSettingBean;
import flinn.recommend.beans.response.ResponseSettingBean;
import flinn.recommend.beans.response.ResponseSettingContainerBean;
import flinn.recommend.dao.DaoRecommendManager;
import flinn.recommend.dao.SettingDao;
import flinn.util.AdminRole;

public class SettingDaoImp extends SettingDao {

	protected static final Logger LOG = Logger.getLogger(SettingDaoImp.class);
	static {
		LOG.debug("Log appender instantiated for " + SettingDaoImp.class);
	}

	public ResponseContainerBean handleSettingCreate(
			RequestContainerBean input, ResponseSessionContainerBean session,
			Connection connection) {
		// sanity checks on incoming data. Ensure no changes to aspects of the
		// data we don't want changed.
		RequestSettingBean bean = input.getSetting();
		if (bean == null)
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Setting create submitted with no appropriate info", 41);

		// Only Admins can create new messages
		if (!AdminRole.isRecommendAdmin(session)) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"User does not have Admin permissions to create new settings",
							41);
		}

		int newid = 0;
		List<ResponseSettingBean> settings = null;

		try {
			// Create new Setting record
			newid = create(bean, connection);

		} catch (Exception e) {
			LOG.error(e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handleSettingCreate: "
						+ e);
			}
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Setting create failed (unknown error): " + e.getMessage(),
					48);
		}

		try {
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handleSettingCreate: "
						+ e);
			}
			LOG.error("Setting retrieval post-update failed (unknown error): "
					+ e);
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Setting retrieval post-create failed (unknown error): "
							+ e.getMessage(), 49);
		}
		if (newid < 1)
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Setting create failed (unknown error - no returned ID)",
					47);
		bean = new RequestSettingBean();
		bean.setSettingid(newid);
		try {
			settings = find(bean, null, connection);
		} catch (Exception e) {
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Setting retrieval post-create failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (settings == null || settings.size() == 0) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"Message retrieval post-create failed (unknown error) No Exception",
							49);
		}

		ResponseSettingContainerBean rcb = new ResponseSettingContainerBean();
		rcb.setSetting(settings.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handleSettingRead(RequestContainerBean input,
			ResponseSessionContainerBean session, Connection connection) {
		RequestSettingBean bean = input.getSetting();
		List<ResponseSettingBean> settings = null;

		try {
			settings = find(bean, null, connection);
		} catch (Exception e) {
			return DaoRecommendManager.generateErrorBean(
					input.getAction(),
					"Setting retrieval failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (settings == null || settings.size() == 0) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"Setting retrieval failed (unknown error) No Exception",
							49);
		}

		ResponseSettingContainerBean rcb = new ResponseSettingContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setSetting(settings.get(0));
		return rcb;
	}

	public ResponseContainerBean handleSettingUpdate(
			RequestContainerBean input, ResponseSessionContainerBean session,
			Connection connection) {
		RequestSettingBean bean = input.getSetting();
		if (bean == null)
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Setting update submitted with no appropriate profile", 41);
		List<ResponseSettingBean> settings = null;
		RequestSettingBean b2 = new RequestSettingBean();
		b2.setSettingid(bean.getSettingid());

		try {
			settings = find(b2, null, connection);
		} catch (Exception e) {
			return DaoRecommendManager.generateErrorBean(
					input.getAction(),
					"Setting retrieval failed in update (unknown error): "
							+ e.getMessage(), 47);
		}

		if (settings == null || settings.size() == 0) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"Setting retrieval failed in update (unknown error) No Exception",
							49);
		}
		try {
			// Update current message record
			update(bean, settings.get(0), connection);
		} catch (Exception e) {
			LOG.warn(e);
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.warn("Error rolling back connection in handleSettingUpdate: "
						+ e);
			}
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Setting update failed (unknown error): " + e.getMessage(),
					48);
		}

		try {
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.warn("Error rolling back connection in handleSettingUpdate: "
						+ e);
			}
			LOG.warn("Setting retrieval post-update failed (unknown error): "
					+ e);
			return DaoRecommendManager.generateErrorBean(
					input.getAction(),
					"Setting retrieval post-update failed (unknown error): "
							+ e.getMessage(), 49);
		}
		b2 = new RequestSettingBean();
		b2.setSettingid(bean.getSettingid());
		try {
			settings = find(b2, null, connection);
		} catch (Exception e) {
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Setting retrieval post-update failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (settings == null || settings.size() == 0) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"Setting retrieval post-update failed (unknown error) No Exception",
							49);
		}

		ResponseSettingContainerBean rcb = new ResponseSettingContainerBean();
		rcb.setSetting(settings.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

}
