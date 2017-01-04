package flinn.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHandler extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String getCookie(String cookieName, HttpServletRequest request)
			throws IOException, ServletException {
		// retrieve cookies
		String cookieValue = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (c.getName().equals(cookieName)) {
					cookieValue = c.getValue();
				}
			}
		}
		return cookieValue;
	}

	public static void setCookie(String cookieName, String cookieValue,
			HttpServletResponse response) throws IOException, ServletException {
		// set a new cookie
		Cookie myCookie = new Cookie(cookieName, cookieValue);
		response.addCookie(myCookie);
	}
}
