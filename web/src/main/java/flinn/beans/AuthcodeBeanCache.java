package flinn.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthcodeBeanCache {
	private static AuthcodeBeanCache ref = null;
	private int minCacheDurationSeconds = 30*60;
	private HashMap<String,AuthcodeBean> authcodes = null;

	public static synchronized AuthcodeBeanCache getAuthcodeBeanCache() {
	    if (ref == null)
	          // it's ok, we can call this constructor
	          ref = new AuthcodeBeanCache();
	      return ref;
	  }

	  public Object clone()
	    throws CloneNotSupportedException
	  {
	    throw new CloneNotSupportedException();
	    
	  }

	  private AuthcodeBeanCache() {
		  authcodes = new HashMap<String,AuthcodeBean>();
	  }

	  public AuthcodeBean getAuthcodeBean(String authcode) {
		  AuthcodeBean b = null;
	  	synchronized(authcodes) {
			b = (AuthcodeBean) authcodes.get(authcode);
			long ct = System.currentTimeMillis();

			if (b != null) {
				if (b.getExpireTime() < ct) {
					garbageCollect();
					b = null;
				} else {
					b.setExpireTime(ct+minCacheDurationSeconds*1000);
					authcodes.put(authcode, b);
				}
			}
		}
		if (b != null) {
			AuthcodeBean ret = b.clone();
			return ret;
		}
		return null;
	  }

	  public int putAuthcodeBean(AuthcodeBean b) {
		  if (b == null || b.getAddress() == null || b.getAuthcode() == null || b.getUseragent() == null || b.getUser() == null || b.getFacility() == null) {
			  return -1;
		  }
		  long ct = System.currentTimeMillis();
		  b.setExpireTime(ct+minCacheDurationSeconds*1000);
		  authcodes.put(b.getAuthcode(), b);
		  return 0;
	  }

	  public int removeAuthcodeBean(AuthcodeBean b) {
		  if (b == null || b.getAddress() == null || b.getAuthcode() == null || b.getUseragent() == null || b.getUser() == null || b.getFacility() == null) {
			  return -1;
		  }
		  authcodes.remove(b.getAuthcode());
		  return 0;
	  }

	  private void garbageCollect() {
          ArrayList<String> deleteKeys = new ArrayList<String>();
          java.util.Set<String> _keys = authcodes.keySet();
          java.util.Iterator<String> _keyit = _keys.iterator();
          long ct = System.currentTimeMillis();
          while(_keyit.hasNext()) {
              String _keyV = _keyit.next();
              AuthcodeBean b = authcodes.get(_keyV);
              if (b != null) {
                  if (ct > b.getExpireTime()) {
                	  deleteKeys.add(_keyV);
                  }
              }
          }
          _keyit = deleteKeys.iterator();
          while(_keyit.hasNext()) {
              String _keyV = _keyit.next();
              authcodes.remove(_keyV);
          }
	  }
}

