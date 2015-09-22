package singularity.app.utility;

import javax.servlet.http.HttpSession;

import singularity.user.dto.SessionUser;

public class RequestUtil {

	public static Long getSessionId(HttpSession session) {
		if(!existedUserIdFromSession(session)){
			return null;
		}
		return ((SessionUser) session.getAttribute("sessionUser")).getId();
	}
	
	public static boolean existedUserIdFromSession(HttpSession session) {
		if (session.getAttribute("sessionUser") == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
