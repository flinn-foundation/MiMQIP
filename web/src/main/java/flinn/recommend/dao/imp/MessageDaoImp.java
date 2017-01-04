package flinn.recommend.dao.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import flinn.beans.request.RequestContainerBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.recommend.beans.request.RequestMessageBean;
import flinn.recommend.beans.response.ResponseMessageBean;
import flinn.recommend.beans.response.ResponseMessageContainerBean;
import flinn.recommend.dao.DaoRecommendManager;
import flinn.recommend.dao.MessageDao;
import flinn.util.AdminRole;

public class MessageDaoImp extends MessageDao {

	protected static final Logger LOG = Logger.getLogger(MessageDaoImp.class);
	static {
		LOG.debug("Log appender instantiated for " + MessageDaoImp.class);
	}

	public ResponseContainerBean handleMessageCreate(
			RequestContainerBean input, ResponseSessionContainerBean session,
			Connection connection) {
		// sanity checks on incoming data. Ensure no changes to aspects of the
		// data we don't want changed.
		RequestMessageBean bean = input.getMessage();
		if (bean == null)
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Message create submitted with no appropriate info", 41);

		// Only Admins can create new messages
		if (!AdminRole.isRecommendAdmin(session)) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"User does not have Admin permissions to create new messages",
							41);
		}

		int newid = 0;
		List<ResponseMessageBean> messages = null;

		try {
			// Create new Message record
			newid = create(bean, connection);

		} catch (Exception e) {
			LOG.error(e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handleMessageCreate: "
						+ e);
			}
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Message create failed (unknown error): " + e.getMessage(),
					48);
		}

		try {
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Error rolling back connection in handleMessageCreate: "
						+ e);
			}
			LOG.error("Message retrieval post-update failed (unknown error): "
					+ e);
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Message retrieval post-create failed (unknown error): "
							+ e.getMessage(), 49);
		}
		if (newid < 1)
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Message create failed (unknown error - no returned ID)",
					47);
		bean = new RequestMessageBean();
		bean.setMessageid(newid);
		try {
			messages = find(bean, null, connection);
		} catch (Exception e) {
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Message retrieval post-create failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (messages == null || messages.size() == 0) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"Message retrieval post-create failed (unknown error) No Exception",
							49);
		}

		ResponseMessageContainerBean rcb = new ResponseMessageContainerBean();
		rcb.setMessage(messages.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handleMessageRead(RequestContainerBean input,
			ResponseSessionContainerBean session, Connection connection) {
		RequestMessageBean bean = input.getMessage();
		List<ResponseMessageBean> messages = null;

		try {
			messages = find(bean, null, connection);
		} catch (Exception e) {
			return DaoRecommendManager.generateErrorBean(
					input.getAction(),
					"Message retrieval failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (messages == null || messages.size() == 0) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"Message retrieval failed (unknown error) No Exception",
							49);
		}

		ResponseMessageContainerBean rcb = new ResponseMessageContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setMessage(messages.get(0));
		return rcb;
	}

	public ResponseContainerBean handleMessageUpdate(
			RequestContainerBean input, ResponseSessionContainerBean session,
			Connection connection) {
		RequestMessageBean bean = input.getMessage();
		if (bean == null)
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Message update submitted with no appropriate profile", 41);
		List<ResponseMessageBean> messages = null;
		RequestMessageBean b2 = new RequestMessageBean();
		b2.setMessageid(bean.getMessageid());

		try {
			messages = find(b2, null, connection);
		} catch (Exception e) {
			return DaoRecommendManager.generateErrorBean(
					input.getAction(),
					"Message retrieval failed in update (unknown error): "
							+ e.getMessage(), 47);
		}

		if (messages == null || messages.size() == 0) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"Message retrieval failed in update (unknown error) No Exception",
							49);
		}
		try {
			// Update current message record
			update(bean, messages.get(0), connection);
		} catch (Exception e) {
			LOG.warn(e);
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.warn("Error rolling back connection in handleMessageUpdate: "
						+ e);
			}
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Message update failed (unknown error): " + e.getMessage(),
					48);
		}

		try {
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.warn("Error rolling back connection in handleMessageUpdate: "
						+ e);
			}
			LOG.warn("Message retrieval post-update failed (unknown error): "
					+ e);
			return DaoRecommendManager.generateErrorBean(
					input.getAction(),
					"Message retrieval post-update failed (unknown error): "
							+ e.getMessage(), 49);
		}
		b2 = new RequestMessageBean();
		b2.setMessageid(bean.getMessageid());
		try {
			messages = find(b2, null, connection);
		} catch (Exception e) {
			return DaoRecommendManager.generateErrorBean(input.getAction(),
					"Message retrieval post-update failed (unknown error): "
							+ e.getMessage(), 47);
		}

		if (messages == null || messages.size() == 0) {
			return DaoRecommendManager
					.generateErrorBean(
							input.getAction(),
							"Message retrieval post-update failed (unknown error) No Exception",
							49);
		}

		ResponseMessageContainerBean rcb = new ResponseMessageContainerBean();
		rcb.setMessage(messages.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

}
