package flinn.recommend.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import flinn.beans.request.RequestActionBean;
import flinn.beans.request.RequestContainerBean;
import flinn.beans.response.ErrorBean;
import flinn.beans.response.ErrorContainerBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.beans.response.ResponseTreatmentBean;
import flinn.dao.AbstractBaseDao;
import flinn.dao.AuthcodeDao;
import flinn.dao.imp.AppUserDaoImp;
import flinn.dao.imp.TreatmentDaoImp;
import flinn.recommend.beans.RecommendDiagnosisBean;
import flinn.recommend.beans.request.RequestMessageBean;
import flinn.recommend.beans.request.RequestRuleBean;
import flinn.recommend.beans.request.RequestSettingBean;
import flinn.recommend.beans.response.ResponseMessageBean;
import flinn.recommend.beans.response.ResponseMessageContainerBean;
import flinn.recommend.beans.response.ResponseRuleBean;
import flinn.recommend.beans.response.ResponseRuleContainerBean;
import flinn.recommend.beans.response.ResponseSettingBean;
import flinn.recommend.beans.response.ResponseSettingContainerBean;
import flinn.recommend.dao.imp.MessageDaoImp;
import flinn.recommend.dao.imp.RuleDaoImp;
import flinn.recommend.dao.imp.SettingDaoImp;

public class DaoRecommendManager extends AbstractBaseDao {
	public static final Logger LOG = Logger
			.getLogger(DaoRecommendManager.class);
	public String nullDate = "0000-00-00 00:00:00";

	/*
	 * public DaoAppManager() { connection = getConnection(); }
	 */

	static {
		LOG.debug("Log appender instantiated for " + DaoRecommendManager.class);
	}

	public static ErrorContainerBean generateErrorBean(
			final RequestActionBean reqab, final String message,
			final int number) {
		ErrorContainerBean rcb = new ErrorContainerBean();
		ErrorBean eb = new ErrorBean();
		eb.setMessage(message);
		eb.setNumber(new BigDecimal(number));
		rcb.setError(eb);
		if (reqab != null) {
			rcb.setAction(new ResponseActionBean(reqab));
		} else {
			ResponseActionBean rab = new ResponseActionBean();
			rab.setCorrelationid("none");
			rab.setCommand("none");
			rab.setMessageid("none");
			rab.setTimestamp(new BigDecimal(new java.util.Date().getTime()));
			rab.setType("none");
			rcb.setAction(rab);
		}
		return rcb;
	}

	public ResponseSessionContainerBean getSession(String authcode,
			HttpServletRequest req) {
		LOG.debug("<getSession>");
		ResponseSessionContainerBean session = null;
		RequestContainerBean input = new RequestContainerBean();
		input.setAction(new RequestActionBean());
		// Currently only the authcode is needed,
		// no other pieces of the RequestActionBean are validated for
		// consistency.
		input.getAction().setAuthcode(authcode);

		AuthcodeDao acd = new AuthcodeDao();
		session = acd.validate(input, req);
		return session;
	}

	public Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				renewConnection();
				if (connection == null || connection.isClosed()) {
					LOG.debug("Unable to renew connection in DaoAppManager.getConnection");
				}
			}
		} catch (Exception e) {
			LOG.debug("Unable to renew connection in DaoAppManager.getConnection: "
					+ e);
		}
		return connection;
	}

	public void commitConnection(String functionCall) {
		try {
			connection.commit();
		} catch (Exception e) {
			LOG.warn(functionCall + " post-update failed (unknown error): " + e);
			rollbackConnection(functionCall);
		}
	}

	public void rollbackConnection(String functionCall) {
		try {
			connection.rollback();
		} catch (SQLException e1) {
			LOG.warn("Error rolling back connection in " + functionCall + ": "
					+ e1);
		}
	}

	public void closeConnection(String functionCall) {
		try {
			if (connection != null && !connection.isClosed()) {
				closeConnection();
				connection = null;
			}
		} catch (SQLException e) {
			LOG.debug("Unable to close connection after use in " + functionCall);
		}
	}

	public void disposeConnection(String functionCall) {
		try {
			if (connection != null && !connection.isClosed()) {
				closeConnection();
				connection = null;
			}
		} catch (SQLException e) {
			LOG.error("Unable to close connection after use in " + functionCall);
		}
	}

	public int updateLastActivity(ResponseSessionContainerBean session)
			throws Exception {
		LOG.debug("<updateLastActivity>");
		int result = -1;
		if (session != null) {
			connection = getConnection(); // Get connection

			try {
				result = (new AppUserDaoImp().updateLastActivity(session,
						connection));
			} catch (Exception e) {
				LOG.error(e);
				rollbackConnection("updateLastActivity");
			}
			commitConnection("updateLastActivity");

		}

		return result;
	}

	public List<ResponseMessageBean> findAllMessages(
			RequestContainerBean input, ResponseSessionContainerBean session,
			String orderBy) throws Exception {

		LOG.debug("<findAllMessages>");
		RequestMessageBean bean = new RequestMessageBean();
		List<ResponseMessageBean> rtnList = null;
		if (session != null) {
			connection = getConnection(); // Get connection

			// Show all
			rtnList = new MessageDaoImp().findAllMessages(bean, orderBy,
					connection);
		}

		return rtnList;
	}

	public ResponseMessageBean findMessage(int messageid) throws Exception {
		LOG.debug("<findMessage>");
		ResponseMessageBean message = null;
		RequestMessageBean bean = new RequestMessageBean();
		bean.setMessageid(messageid);
		connection = getConnection(); // Get connection
		List<flinn.recommend.beans.response.ResponseMessageBean> messages = (new MessageDaoImp()
				.find(bean, null, connection));

		if (messages != null && messages.size() > 0) {
			message = messages.get(0);
		}

		return message;
	}

	public int createMessage(RequestContainerBean input,
			ResponseSessionContainerBean session) throws Exception {
		LOG.debug("<createMessage>");
		int newid = -1;
		RequestMessageBean bean = new RequestMessageBean();
		bean.setPriority(input.getMessage().getPriority());
		bean.setMessage(input.getMessage().getMessage());
		bean.setMessagetag(input.getMessage().getMessagetag());

		if (input != null) {
			connection = getConnection(); // Get connection
			try {
				// Create new message record
				newid = new MessageDaoImp().create(bean, connection);
			} catch (Exception e) {
				LOG.error(e);
				rollbackConnection("createMessage");
			}
			commitConnection("createMessage");
		}
		return newid;
	}

	public ResponseContainerBean updateMessage(RequestContainerBean input,
			ResponseSessionContainerBean session) {
		LOG.debug("<updateMessage>");
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("update");
		input.setAction(rqABean);
		ResponseMessageContainerBean rcb = new ResponseMessageContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));

		if (input != null) {
			connection = getConnection(); // Get connection
			rcb = (ResponseMessageContainerBean) (new MessageDaoImp()
					.handleMessageUpdate(input, session, connection));
		}

		return rcb;
	}

	public List<ResponseRuleBean> findAllRules(RequestContainerBean input,
			ResponseSessionContainerBean session, String orderBy)
			throws Exception {

		LOG.debug("<findAllRules>");
		RequestRuleBean bean = new RequestRuleBean();
		List<ResponseRuleBean> rtnList = null;
		if (session != null) {
			connection = getConnection(); // Get connection

			// Show all
			rtnList = new RuleDaoImp().findAllRules(bean, orderBy, connection);
		}

		return rtnList;
	}

	public ResponseRuleBean findRule(int ruleid) throws Exception {
		LOG.debug("<findRule>");
		ResponseRuleBean rule = null;
		RequestRuleBean bean = new RequestRuleBean();
		bean.setRuleid(ruleid);
		connection = getConnection(); // Get connection
		List<flinn.recommend.beans.response.ResponseRuleBean> rules = (new RuleDaoImp()
				.find(bean, null, connection));

		if (rules != null && rules.size() > 0) {
			rule = rules.get(0);
		}

		return rule;
	}

	public int createRule(RequestContainerBean input,
			ResponseSessionContainerBean session) throws Exception {
		LOG.debug("<createRule>");
		int newid = -1;
		RequestRuleBean bean = new RequestRuleBean();
		bean.setPriority(input.getRule().getPriority());
		bean.setMessages(input.getRule().getMessages());
		bean.setRuletype(input.getRule().getRuletype());
		bean.setRulename(input.getRule().getRulename());
		bean.setValid(input.getRule().getValid());
		bean.setDiagnoses(input.getRule().getDiagnoses());

		if (input != null) {
			connection = getConnection(); // Get connection
			try {
				// Create new message record
				newid = new RuleDaoImp().create(bean, connection);
			} catch (Exception e) {
				LOG.error(e);
				e.printStackTrace();
				rollbackConnection("createRule");
			}
			commitConnection("createRule");
		}
		return newid;
	}

	public ResponseContainerBean updateRule(RequestContainerBean input,
			ResponseSessionContainerBean session) {
		LOG.debug("<updateRule>");
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("update");
		input.setAction(rqABean);
		ResponseRuleContainerBean rcb = new ResponseRuleContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));

		if (input != null) {
			connection = getConnection(); // Get connection
			rcb = (ResponseRuleContainerBean) (new RuleDaoImp()
					.handleRuleUpdate(input, session, connection));
		}

		return rcb;
	}

	public RecommendDiagnosisBean[] findAllDiagnoses() throws Exception {
		LOG.debug("<findAllDiagnoses>");
		RecommendDiagnosisBean[] allDiagnoses = null;
		connection = getConnection(); // Get connection
		allDiagnoses = new RuleDaoImp().getAllDiagnoses(connection);

		return allDiagnoses;
	}

	public List<ResponseSettingBean> findAllSettings(
			RequestContainerBean input, ResponseSessionContainerBean session,
			String orderBy) throws Exception {

		LOG.debug("<findAllSettings>");
		RequestSettingBean bean = new RequestSettingBean();
		List<ResponseSettingBean> rtnList = null;
		if (session != null) {
			connection = getConnection(); // Get connection

			// Show all
			rtnList = new SettingDaoImp().findAllSettings(bean, orderBy,
					connection);
		}

		return rtnList;
	}

	public ResponseSettingBean findSetting(int settingid) throws Exception {
		LOG.debug("<findSetting>");
		ResponseSettingBean setting = null;
		RequestSettingBean bean = new RequestSettingBean();
		bean.setSettingid(settingid);
		connection = getConnection(); // Get connection
		List<flinn.recommend.beans.response.ResponseSettingBean> settings = (new SettingDaoImp()
				.find(bean, null, connection));

		if (settings != null && settings.size() > 0) {
			setting = settings.get(0);
		}

		return setting;
	}

	public int createSetting(RequestContainerBean input,
			ResponseSessionContainerBean session) throws Exception {
		LOG.debug("<createSetting>");
		int newid = -1;
		RequestSettingBean bean = new RequestSettingBean();
		bean.setSettingname(input.getSetting().getSettingname());
		bean.setSettingvalue(input.getSetting().getSettingvalue());

		if (input != null) {
			connection = getConnection(); // Get connection
			try {
				// Create new setting record
				newid = new SettingDaoImp().create(bean, connection);
			} catch (Exception e) {
				LOG.error(e);
				rollbackConnection("createSetting");
			}
			commitConnection("createSetting");
		}
		return newid;
	}

	public ResponseContainerBean updateSetting(RequestContainerBean input,
			ResponseSessionContainerBean session) {
		LOG.debug("<updateSetting>");
		RequestActionBean rqABean = new RequestActionBean();
		rqABean.setType("update");
		input.setAction(rqABean);
		ResponseSettingContainerBean rcb = new ResponseSettingContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));

		if (input != null) {
			connection = getConnection(); // Get connection
			rcb = (ResponseSettingContainerBean) (new SettingDaoImp()
					.handleSettingUpdate(input, session, connection));
		}

		return rcb;
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

}
