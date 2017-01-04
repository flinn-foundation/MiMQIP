package flinn.dao;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import flinn.beans.AuthcodeBean;
import flinn.beans.AuthcodeBeanCache;
import flinn.beans.request.RequestContainerBean;
import flinn.beans.response.ResponseActionBean;
import flinn.beans.response.ResponseAppUserBean;
import flinn.beans.response.ResponseFacilityBean;
import flinn.beans.response.ResponseSessionContainerBean;
import flinn.util.IpAddress;

public class AuthcodeDao
{

	protected static final Logger LOG = Logger.getLogger(AuthcodeDao.class);
	static
	{
		LOG.debug("Log appender instantiated for " + AuthcodeDao.class);
	}

	public ResponseSessionContainerBean validate(RequestContainerBean input, HttpServletRequest req)
	{

		AuthcodeBeanCache bc = AuthcodeBeanCache.getAuthcodeBeanCache();
		AuthcodeBean b = bc.getAuthcodeBean(input.getAction().getAuthcode());
		if (b == null)
			return null;

		ResponseActionBean rab = new ResponseActionBean(input.getAction());
		ResponseSessionContainerBean session = new ResponseSessionContainerBean();
		session.setAction(rab);
		session.setFacility(b.getFacility());
		session.setUser(b.getUser());

		boolean pass1 = false;
		if (b.getUseragent() == null && req.getHeader("User-Agent") == null)
		{
			pass1 = true;
		}
		if (b.getUseragent().equals(req.getHeader("User-Agent")))
		{
			pass1 = true;
		}

		boolean pass2 = IpAddress.validateFacilityIP(b.getFacility(), req);

		if (pass1 && pass2)
			return session;
		return null;
	}

	public int logout(String authcode)
	{
		AuthcodeBeanCache bc = AuthcodeBeanCache.getAuthcodeBeanCache();
		AuthcodeBean b = bc.getAuthcodeBean(authcode);

		if (b != null)
		{
			bc.removeAuthcodeBean(b);
			return 0;
		}
		return -1;
	}

	public int addAuthcode(String authcode, ResponseFacilityBean facility, ResponseAppUserBean user, HttpServletRequest req)
	{
		AuthcodeBean b = new AuthcodeBean();
		b.setAddress(req.getRemoteAddr());
		b.setUseragent(req.getHeader("User-Agent"));
		b.setAuthcode(authcode);
		b.setFacility(facility);
		b.setUser(user);
		AuthcodeBeanCache bc = AuthcodeBeanCache.getAuthcodeBeanCache();
		return bc.putAuthcodeBean(b);
	}
}
