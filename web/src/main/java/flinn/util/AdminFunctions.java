package flinn.util;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import flinn.beans.AppUserRoleBean;
import flinn.recommend.beans.RecommendDiagnosisBean;

public class AdminFunctions extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final Logger LOG = Logger.getLogger(AdminFunctions.class);

	public static String edit_date_snippet(String field, String style,
			String date) {
		StringBuffer sb = new StringBuffer();
		if (date == null) {
			date = "0000-00-00 00:00:00";
		}
		String date_include_year = date.substring(0, 4);
		String date_include_month = date.substring(5, 7);
		String date_include_day = date.substring(8, 10);
		String date_include_hour = date.substring(11, 13);
		String date_include_min = date.substring(14, 16);
		String date_include_sec = date.substring(17, 19);

		sb.append("<SELECT name='" + field + "__year'");
		if (style != null)
			sb.append(" class='" + style + "'");
		sb.append("><OPTION value='0000'");
		if (date_include_year.equals("0000"))
			sb.append(" SELECTED");
		sb.append(">0000");
		for (int date_include_cnt = 2011; date_include_cnt <= 2031; date_include_cnt++) {
			sb.append("<OPTION value='" + date_include_cnt + "'");
			if (Integer.toString(date_include_cnt).equals(date_include_year))
				sb.append(" SELECTED");
			sb.append(">" + date_include_cnt);
		}
		sb.append("</SELECT>-");
		sb.append("<SELECT name='" + field + "__month'");
		if (style != null)
			sb.append(" class='" + style + "'");
		sb.append("><OPTION value='00'");
		if (date_include_month.equals("00"))
			sb.append(" SELECTED");
		sb.append(">00");
		for (int mo_date_include_cnt = 1; mo_date_include_cnt <= 12; mo_date_include_cnt++) {
			String mo_date_include_cnt_text = Integer
					.toString(mo_date_include_cnt);
			if (Integer.toString(mo_date_include_cnt).length() < 2)
				mo_date_include_cnt_text = "0" + mo_date_include_cnt_text;
			sb.append("<OPTION value='" + mo_date_include_cnt_text + "'");
			if (mo_date_include_cnt_text.equals(date_include_month))
				sb.append(" SELECTED");
			sb.append(">" + mo_date_include_cnt_text);
		}
		sb.append("</SELECT>-");
		sb.append("<SELECT name='" + field + "__day'");
		if (style != null)
			sb.append(" class='" + style + "'");
		sb.append("><OPTION value='00'");
		if (date_include_day.equals("00"))
			sb.append(" SELECTED");
		sb.append(">00");
		for (int day_date_include_cnt = 1; day_date_include_cnt <= 31; day_date_include_cnt++) {
			String day_date_include_cnt_text = Integer
					.toString(day_date_include_cnt);
			if (Integer.toString(day_date_include_cnt).length() < 2)
				day_date_include_cnt_text = "0" + day_date_include_cnt_text;
			sb.append("<OPTION value='" + day_date_include_cnt_text + "'");
			if (day_date_include_cnt_text.equals(date_include_day))
				sb.append(" SELECTED");
			sb.append(">" + day_date_include_cnt_text);
		}
		sb.append("</SELECT>&nbsp;&nbsp;&nbsp;&nbsp;\n");

		sb.append("<SELECT name='" + field + "__hour'");
		if (style != null)
			sb.append(" class='" + style + "'");
		sb.append("><OPTION value='00'");
		if (date_include_hour.equals("00"))
			sb.append(" SELECTED");
		sb.append(">00");
		for (int hour_date_include_cnt = 1; hour_date_include_cnt <= 23; hour_date_include_cnt++) {
			String hour_date_include_cnt_text = Integer
					.toString(hour_date_include_cnt);
			if (Integer.toString(hour_date_include_cnt).length() < 2)
				hour_date_include_cnt_text = "0" + hour_date_include_cnt_text;
			sb.append("<OPTION value='" + hour_date_include_cnt_text + "'");
			if (hour_date_include_cnt_text.equals(date_include_hour))
				sb.append(" SELECTED");
			sb.append(">" + hour_date_include_cnt_text);
		}
		sb.append("</SELECT>:");
		sb.append("<SELECT name='" + field + "__min'");
		if (style != null)
			sb.append(" class='" + style + "'");
		sb.append("><OPTION value='00'");
		if (date_include_min.equals("00"))
			sb.append(" SELECTED");
		sb.append(">00");
		for (int min_date_include_cnt = 1; min_date_include_cnt < 60; min_date_include_cnt++) {
			String min_date_include_cnt_text = Integer
					.toString(min_date_include_cnt);

			if (Integer.toString(min_date_include_cnt).length() < 2)
				min_date_include_cnt_text = "0" + min_date_include_cnt_text;
			sb.append("<OPTION value='" + min_date_include_cnt_text + "'");
			if (min_date_include_cnt_text.equals(date_include_min))
				sb.append(" SELECTED");
			sb.append(">" + min_date_include_cnt_text);
		}
		sb.append("</SELECT>:");

		sb.append("\n<SELECT name='" + field + "__sec'");
		if (style != null)
			sb.append(" class='" + style + "'");
		sb.append("><OPTION value='00'");
		if (date_include_sec.equals("00"))
			sb.append(" SELECTED");
		sb.append(">00");
		for (int sec_date_include_cnt = 1; sec_date_include_cnt < 60; sec_date_include_cnt++) {
			String sec_date_include_cnt_text = Integer
					.toString(sec_date_include_cnt);

			if (Integer.toString(sec_date_include_cnt).length() < 2)
				sec_date_include_cnt_text = "0" + sec_date_include_cnt_text;
			sb.append("<OPTION value='" + sec_date_include_cnt_text + "'");
			if (sec_date_include_cnt_text.equals(date_include_sec))
				sb.append(" SELECTED");
			sb.append(">" + sec_date_include_cnt_text);
		}
		sb.append("</SELECT>\n");

		return sb.toString();
	}

	public static String parse_date_snippet(String field,
			HttpServletRequest request) {
		StringBuffer retstr = new StringBuffer();

		if (request.getParameter(field + "__year") != ""
				&& request.getParameter(field + "__month") != ""
				&& request.getParameter(field + "__day") != ""
				&& request.getParameter(field + "__hour") != ""
				&& request.getParameter(field + "__min") != ""
				&& request.getParameter(field + "__sec") != "") {
			retstr.append(request.getParameter(field + "__year"));
			retstr.append("-");
			retstr.append(request.getParameter(field + "__month"));
			retstr.append("-");
			retstr.append(request.getParameter(field + "__day"));
			retstr.append(" ");
			retstr.append(request.getParameter(field + "__hour"));
			retstr.append(":");
			retstr.append(request.getParameter(field + "__min"));
			retstr.append(":");
			retstr.append(request.getParameter(field + "__sec"));
			return retstr.toString();

		}
		return ("0000-00-00 00:00:00");
	}

	public static AppUserRoleBean[] getRolesFromForm(HttpServletRequest request) {
		ArrayList<AppUserRoleBean> rb = new ArrayList<AppUserRoleBean>();

		Enumeration<?> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();

			// To retrieve a single value
			String value = request.getParameter(key);
			if (key.contains("userrole_")) {
				if (value != null) {
					if (request.getParameterValues(key) != null) {
						AppUserRoleBean role = new AppUserRoleBean();
						role.setApproleid(Integer.parseInt(value));
						role.setApprole(key.replace("userrole_", ""));
						rb.add(role);
					}
				}
			}
		}

		AppUserRoleBean[] updatedRoleBean = rb.toArray(new AppUserRoleBean[rb
				.size()]);
		return updatedRoleBean;
	}

	public static RecommendDiagnosisBean[] getDiagnosisFromForm(HttpServletRequest request) {
		ArrayList<RecommendDiagnosisBean> db = new ArrayList<RecommendDiagnosisBean>();

		Enumeration<?> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();

			// To retrieve a single value
			String value = request.getParameter(key);
			if (key.contains("diagnoses_")) {
				if (value != null) {
					if (request.getParameterValues(key) != null) {
						RecommendDiagnosisBean diagnoses = new RecommendDiagnosisBean();
						diagnoses.setDiagnosisid(Integer.parseInt(key.replace("diagnoses_", "")));
						diagnoses.setDiagnosis(value);
						db.add(diagnoses);
					}
				}
			}
		}

		RecommendDiagnosisBean[] updatedDiagnosisBean = db.toArray(new RecommendDiagnosisBean[db
				.size()]);
		return updatedDiagnosisBean;
	}

	public static String formatNulls(String date) {
		String rtnDate = date;
		if (rtnDate == null)
			rtnDate = "none";

		return rtnDate;
	}

	public static boolean has_role(String role, String required_role) {

		boolean retval = false;
		try {

			// check to make sure a role was provided
			if (role == null) {
				throw new Exception("Invalid role provided, value = null");
			}

			// check to make sure a required role was provided
			if (required_role == null) {
				throw new Exception(
						"Invalid required role provided, value = null");
			}

			if (role.equals(required_role))
				retval = true;

		}

		catch (Exception e) {
			LOG.debug("Error evaluating role: " + e.getMessage());
			retval = false;
		}

		return retval;
	}
}
