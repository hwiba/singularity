package singularity.utility;

import javax.servlet.http.HttpSession;

import singularity.dto.out.SessionUser;

public class ServletRequestUtil {
	private ServletRequestUtil() {
	}
	
	public static boolean existedUserIdFromSession(HttpSession session) {
		if (session.getAttribute("sessionUser") == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public static String getUserIdFromSession(HttpSession session) {
		if(!existedUserIdFromSession(session)){
			return null;
		}
		return ((SessionUser)session.getAttribute("sessionUser")).getId();
	}
}
