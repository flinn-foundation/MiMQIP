package flinn.util;

import javax.servlet.http.HttpServletRequest;

import flinn.beans.FacilityIPBean;
import flinn.beans.response.ResponseFacilityBean;

public class IpAddress {
	
	public static final long parse(String address) throws Exception {
	    String[] octets;
	    int oct1, oct2, oct3, oct4;
	    // System.out.println("IP: "+address);
	    try {
	      octets = address.split("\\.");

	      if (java.lang.reflect.Array.getLength(octets) < 4) return -1;
	      oct1 = Integer.parseInt(octets[0]);
	      oct2 = Integer.parseInt(octets[1]);
	      oct3 = Integer.parseInt(octets[2]);
	      if (octets[3].length() > 3) {
	        int i=0;
	        while (i<=octets[3].length() && octets[3].charAt(i) >= '0' && octets[3].charAt(i) <= '9') i++;
	        oct4 = Integer.parseInt(octets[3].substring(0,i));
	      } else {
	        oct4 = Integer.parseInt(octets[3]);
	      }
	    } catch (Exception e) {
	      // System.out.println("IpAddress.parse(): Failed -1");
	      // System.out.println("IpAddress.parse(): "+e.toString());
	      return -1;
	    }
	    // System.out.println("IpAddress.parse(): Success "+(oct1*256*256*256 + oct2*256*256 + oct3*256 + oct4));
	    return (((long)oct1)*256*256*256 + oct2*256*256 + oct3*256 + oct4);
	}
	
	public static final String toAddress(long l) {
		int oct1, oct2, oct3, oct4;
	    oct1 = (int)(l / (256*256*256));
	    oct2 = (int)((l / (256*256)) % 256);
	    oct3 = (int)((l / 256) % 256);
	    oct4 = (int)(l % 256);
	    String ret = ""+oct1+"."+oct2+"."+oct3+"."+oct4;
		return ret;
	}
	
	public static final boolean validateFacilityIP(ResponseFacilityBean facility, HttpServletRequest req) {

		boolean pass = false;
		FacilityIPBean[] ips = facility.getIp();
		String remote = req.getRemoteAddr();
		String fremote = req.getHeader("x-forwarded-for");
		long remoteL = -1;
		try { 
			remoteL = flinn.util.IpAddress.parse(remote);
		} catch (Exception e) {
			// No valid Remote.  Leave it -1.
		}
		long fremoteL = -1;
		if (fremote != null) {
			try {
				fremoteL = flinn.util.IpAddress.parse(fremote);
			} catch (Exception e) {
				// No valid forwarded remote.  Leave it -1.
			}
		}
		if (ips == null || ips.length == 0) {
			pass = true;
		} else {
			for (int i=0; i<ips.length; i++) {
				if (remoteL >= ips[i].getIpfrom() && remoteL <= ips[i].getIpto()) pass = true;
				if (fremoteL >= ips[i].getIpfrom() && fremoteL <= ips[i].getIpto()) pass = true;
			}
		}
		return pass;
	}

}
