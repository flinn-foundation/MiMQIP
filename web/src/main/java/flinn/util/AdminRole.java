package flinn.util;

import flinn.beans.AppUserRoleBean;
import flinn.beans.response.ResponseAppUserBean;
import flinn.beans.response.ResponseSessionContainerBean;

public final class AdminRole {

	public final static int ADMIN_ROLE = 1;
	public final static int FACILITY_ADMIN_ROLE = 2;
	public final static int DOCTOR_ROLE = 3;
	public final static int ASSISTANT_ROLE = 4;
	public final static int LAB_CLERK_ROLE = 5;
	public final static int RECOMMEND_ADMIN_ROLE = 6;
	public final static int RECOMMEND_EXPORT_ROLE = 7;

	public static boolean isAdmin(final ResponseSessionContainerBean session) {
		boolean isAdmin = false;
		ResponseAppUserBean user = session.getUser();
		AppUserRoleBean[] roles = user.getRoles();
		for (int i = 0; i < roles.length; i++) {
			if (roles[i].getApproleid() == ADMIN_ROLE) {
				isAdmin = true;
				break;
			}
		}
		return isAdmin;
	}

	public static boolean isFacilityAdmin(final ResponseSessionContainerBean session) {
		boolean isFacilityAdmin = false;
		ResponseAppUserBean user = session.getUser();
		AppUserRoleBean[] roles = user.getRoles();
		for (int i = 0; i < roles.length; i++) {
			if (roles[i].getApproleid() == ADMIN_ROLE) {
				isFacilityAdmin = true;
				break;
			}
			if (roles[i].getApproleid() == FACILITY_ADMIN_ROLE) {
				isFacilityAdmin = true;
				break;
			}
		}
		return isFacilityAdmin;
	}

	public static boolean isDoctor(final ResponseSessionContainerBean session) {
		boolean isDoctor = false;
		ResponseAppUserBean user = session.getUser();
		AppUserRoleBean[] roles = user.getRoles();
		for (int i = 0; i < roles.length; i++) {
			if (roles[i].getApproleid() == DOCTOR_ROLE) {
				isDoctor = true;
				break;
			}
		}
		return isDoctor;
	}

	public static boolean isAssistant(final ResponseSessionContainerBean session) {
		boolean isAssistant = false;
		ResponseAppUserBean user = session.getUser();
		AppUserRoleBean[] roles = user.getRoles();
		for (int i = 0; i < roles.length; i++) {
			if (roles[i].getApproleid() == DOCTOR_ROLE) {
				isAssistant = true;
				break;
			}
			if (roles[i].getApproleid() == ASSISTANT_ROLE) {
				isAssistant = true;
				break;
			}
		}
		return isAssistant;
	}

	public static boolean isLabClerk(final ResponseSessionContainerBean session) {
		boolean isLabClerk = false;
		ResponseAppUserBean user = session.getUser();
		AppUserRoleBean[] roles = user.getRoles();
		for (int i = 0; i < roles.length; i++) {
			if (roles[i].getApproleid() == DOCTOR_ROLE){
				isLabClerk = true;
				break;
			}
			if (roles[i].getApproleid() == ASSISTANT_ROLE){
				isLabClerk = true;
				break;
			}
			if (roles[i].getApproleid() == LAB_CLERK_ROLE){
				isLabClerk = true;
				break;
			}
		}
		return isLabClerk;
	}

	public static boolean isRecommendAdmin(final ResponseSessionContainerBean session) {
		boolean isRecommendAdmin = false;
		ResponseAppUserBean user = session.getUser();
		AppUserRoleBean[] roles = user.getRoles();
		for (int i = 0; i < roles.length; i++) {
			if (roles[i].getApproleid() == RECOMMEND_ADMIN_ROLE) {
				isRecommendAdmin = true;
				break;
			}
		}
		return isRecommendAdmin;
	}

	public static boolean isRecommendExport(final ResponseSessionContainerBean session) {
		boolean isRecommendExport = false;
		ResponseAppUserBean user = session.getUser();
		AppUserRoleBean[] roles = user.getRoles();
		for (int i = 0; i < roles.length; i++) {
			if (roles[i].getApproleid() == RECOMMEND_ADMIN_ROLE) {
				isRecommendExport = true;
				break;
			}
			if (roles[i].getApproleid() == RECOMMEND_EXPORT_ROLE) {
				isRecommendExport = true;
				break;
			}
		}
		return isRecommendExport;
	}

}
