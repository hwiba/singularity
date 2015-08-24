package singularity.common.utility;

import javax.servlet.http.HttpSession;

import singularity.user.dto.SessionUser;

// XXX 제거할 것.
public class RequestUtil {
	
	private RequestUtil() {}
	
	public static boolean existedUserIdFromSession(HttpSession session) {
		if (session.getAttribute("sessionUser") == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public static SessionUser getSessionUser(HttpSession session) {
		if(!existedUserIdFromSession(session)){
			return null;
		}
		return (SessionUser) session.getAttribute("sessionUser");
	}
}
