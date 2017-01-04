package flinn.dao.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import flinn.beans.request.RequestAppUserBean;
import flinn.beans.request.RequestContainerBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseAppUserBean;
import flinn.beans.response.ResponseAppUserContainerBean;
import flinn.beans.response.ResponseAuthenticateBean;
import flinn.beans.response.ResponseAuthenticateContainerBean;
import flinn.beans.response.ResponseContainerBean;
import flinn.beans.response.ResponseFacilityBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.dao.AppUserDao;
import flinn.dao.AuthcodeDao;
import flinn.dao.DaoRequestManager;
import flinn.util.AdminRole;
import flinn.util.IpAddress;

public class AppUserDaoImp extends AppUserDao
{

	protected static final Logger LOG = Logger.getLogger(AppUserDaoImp.class);

	public String nullDate = "0000-00-00 00:00:00";

	static
	{
		LOG.debug("Log appender instantiated for " + AppUserDaoImp.class);
	}

	public ResponseContainerBean loginUser(RequestContainerBean input, HttpServletRequest request, Connection connection) throws Exception
	{
		final RequestAppUserBean appUserBean = new RequestAppUserBean();
		List<ResponseAppUserBean> userList = null;
		ResponseAppUserBean user = null;
		ResponseFacilityBean facility = null;
		appUserBean.setFacilityid(input.getAuthenticate().getFacilityid());
		appUserBean.setLogin(input.getAuthenticate().getLogin());
		appUserBean.setPassword(input.getAuthenticate().getPassword());

		userList = find(appUserBean, null, connection);
		ResponseAuthenticateBean rab = new ResponseAuthenticateBean();

		if (userList != null && userList.size() > 0)
		{
			user = userList.get(0);

			// Get ResponseFacilityBean
			facility = (new FacilityDaoImp()).findFacilityById(input.getAuthenticate().getFacilityid(), connection);
			if (facility == null || !facility.getValid())
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Authentication Failed.  Bad Facility", 10);
			}
			//Authcode handling
			String authcode = UUID.randomUUID().toString();

			rab.setAuthcode(authcode);
			rab.setMessage("Authentication Successful");
			AuthcodeDao acd = new AuthcodeDao();
			if (acd.addAuthcode(authcode, facility, user, request) < 0)
			{
				return DaoRequestManager.generateErrorBean(input.getAction(), "Authentication Failed.  Authcode insertion failed", 10);
			}

			//Update LastLogin
			int appUserID = -1;
			if (user.getAppuserid() > 0)
			{
				appUserID = user.getAppuserid();
				int loginresult = handleLastLoginUpdate(appUserID, connection);
			}
		}
		else
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Authentication Failed.  Bad User/Password", 10);
		}
		if (!IpAddress.validateFacilityIP(facility, request))
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Authentication Failed.  Invalid Client Address", 10);
		}

		ResponseAuthenticateContainerBean rcb = new ResponseAuthenticateContainerBean();
		rcb.setAuthenticate(rab);
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean logoutUser(RequestContainerBean input, ResponseSessionContainerBean session)
	{
		AuthcodeDao acd = new AuthcodeDao();
		if (acd.logout(input.getAction().getAuthcode()) < 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "Logout Failed.  Authcode removal failed", 10);
		}

		ResponseAuthenticateBean rab = new ResponseAuthenticateBean();
		rab.setAuthcode(input.getAction().getAuthcode());
		rab.setMessage("Logout Successful");
		ResponseAuthenticateContainerBean rcb = new ResponseAuthenticateContainerBean();
		rcb.setAuthenticate(rab);
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handleUserCreate(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		// sanity checks on incoming data.  Ensure no changes to aspects of the data we don't want changed.
		RequestAppUserBean bean = input.getUser();
		if (bean == null)
			return DaoRequestManager.generateErrorBean(input.getAction(), "User create submitted with no appropriate info", 41);

		//Only Admins can create new users
		if (!AdminRole.isAdmin(session))
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "User does not have Admin permissions to create new users", 41);
		}
		int newid = 0;
		List<ResponseAppUserBean> users = null;

		try
		{
			// Create new appuser record
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
				LOG.error("Error rolling back connection in handleUserCreate: " + e);
			}
			return DaoRequestManager.generateErrorBean(input.getAction(), "User create failed (unknown error): " + e.getMessage(), 48);
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
				LOG.error("Error rolling back connection in handleUserCreate: " + e);
			}
			LOG.error("User retrieval post-update failed (unknown error): " + e);
			return DaoRequestManager.generateErrorBean(input.getAction(), "User retrieval post-create failed (unknown error): " + e.getMessage(), 49);
		}
		if (newid < 1)
			return DaoRequestManager.generateErrorBean(input.getAction(), "User create failed (unknown error - no returned ID)", 47);
		bean = new RequestAppUserBean();
		bean.setAppuserid(newid);
		try
		{
			users = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "User retrieval post-create failed (unknown error): " + e.getMessage(), 47);
		}

		if (users == null || users.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "User retrieval post-create failed (unknown error) No Exception", 49);
		}

		ResponseAppUserContainerBean rcb = new ResponseAppUserContainerBean();
		rcb.setUser(users.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public ResponseContainerBean handleUserRead(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		RequestAppUserBean bean = input.getUser();
		List<ResponseAppUserBean> users = null;

		try
		{
			users = find(bean, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "User retrieval failed (unknown error): " + e.getMessage(), 47);
		}

		if (users == null || users.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "User retrieval failed (unknown error) No Exception", 49);
		}

		ResponseAppUserContainerBean rcb = new ResponseAppUserContainerBean();
		rcb.setAction(new ResponseActionBean(input.getAction()));
		rcb.setUser(users.get(0));
		return rcb;
	}

	public ResponseContainerBean handleUserUpdate(RequestContainerBean input, ResponseSessionContainerBean session, Connection connection)
	{
		RequestAppUserBean bean = input.getUser();
		if (bean == null)
			return DaoRequestManager.generateErrorBean(input.getAction(), "User update submitted with no appropriate profile", 41);
		List<ResponseAppUserBean> users = null;
		RequestAppUserBean b2 = new RequestAppUserBean();
		b2.setAppuserid(bean.getAppuserid());

		try
		{
			users = find(b2, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "User retrieval failed in update (unknown error): " + e.getMessage(), 47);
		}

		if (users == null || users.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "User retrieval failed in update (unknown error) No Exception", 49);
		}
		try
		{
			// Update current facility record
			if (users.get(0).getExpiration() == null)
				users.get(0).setExpiration(nullDate);
			update(bean, users.get(0), connection);
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
				LOG.error("Error rolling back connection in handleUserUpdate: " + e);
			}
			return DaoRequestManager.generateErrorBean(input.getAction(), "User update failed (unknown error): " + e.getMessage(), 48);
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
				LOG.error("Error rolling back connection in handleUserUpdate: " + e);
			}
			LOG.error("User retrieval post-update failed (unknown error): " + e);
			return DaoRequestManager.generateErrorBean(input.getAction(), "User retrieval post-update failed (unknown error): " + e.getMessage(), 49);
		}
		b2 = new RequestAppUserBean();
		b2.setAppuserid(bean.getAppuserid());
		try
		{
			users = find(b2, null, connection);
		}
		catch (Exception e)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "User retrieval post-update failed (unknown error): " + e.getMessage(), 47);
		}

		if (users == null || users.size() == 0)
		{
			return DaoRequestManager.generateErrorBean(input.getAction(), "User retrieval post-update failed (unknown error) No Exception", 49);
		}

		ResponseAppUserContainerBean rcb = new ResponseAppUserContainerBean();
		rcb.setUser(users.get(0));
		rcb.setAction(new ResponseActionBean(input.getAction()));
		return rcb;
	}

	public int handleLastLoginUpdate(int appUserID, Connection connection) throws Exception
	{
		int result = -1;
		if (appUserID > 0)
		{
			result = (new flinn.dao.imp.AppUserDaoImp().updateLastLoggedIn(appUserID, connection));
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
				LOG.error("Error rolling back connection in handleLastLoginUpdate: " + e1);
			}
			LOG.error("Last login post-update failed (unknown error): " + e);
		}

		return result;
	}

}
