package stag.utility;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import stag.dto.out.SessionUser;

public class ServletRequestUtil {
	private ServletRequestUtil() {
	}
	
	public static boolean existedUserIdFromSession(HttpSession session) throws IOException {
		if (session.getAttribute("sessionUser") == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public static String getUserIdFromSession(HttpSession session) throws IOException {
		if(!existedUserIdFromSession(session)){
			return null;
		}
		return ((SessionUser)session.getAttribute("sessionUser")).getId();
	}
}
