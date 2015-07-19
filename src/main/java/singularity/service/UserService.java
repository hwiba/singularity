package singularity.service;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import singularity.domain.User;
import singularity.dto.out.SessionUser;
import singularity.enums.UserStatus;
import singularity.exception.ExistedUserException;
import singularity.exception.FailedLoginException;
import singularity.exception.SessionUserMismatchException;
import singularity.repository.UserRepository;

@Service
public class UserService {
	@Resource
	private UserRepository userRepository;
	
	public SessionUser getSessionUser(HttpSession session) {
		return (SessionUser)session.getAttribute("sessionUser");
		
	}
	
	public boolean checkSessionUser(HttpSession session, String userId) {
		if (this.getSessionUser(session).equals(userId)) {
			return true;
		}
		return false;
	}
	
	public User create(User user) throws ExistedUserException {
		if (null != userRepository.findOne(user.getId())) {
			throw new ExistedUserException("이미 가입된 E-mail 주소입니다.");
		}
		user.setCreateDate(new Date());
		user.setStatus(UserStatus.READY);
		userRepository.save(user);
		return userRepository.findOne(user.getId());
	}
	
	public User signingUp(User user) {
		user.setStatus(UserStatus.OK);
		return userRepository.save(user);
	}
	
	public SessionUser findOne(User user) {
		return new SessionUser(userRepository.findOne(user.getId()));
	}
	
	public SessionUser update(User user, HttpSession session) throws SessionUserMismatchException{
		if (!checkSessionUser(session, user.getId())) {
			throw new SessionUserMismatchException("권한이 없는 요청입니다.");
		}
		User dbUser = userRepository.findOne(user.getId());
		user.setCreateDate(dbUser.getCreateDate());
		user.setStatus(dbUser.getStatus());
		return new SessionUser(userRepository.save(user));
	}
	
	public void delete(User user) {
		user.setStatus(UserStatus.DELEATE);
		userRepository.save(user);
	}
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	public SessionUser findForLogin(User user) throws FailedLoginException {
		User dbUser = userRepository.findOne(user.getId());
		if (null == dbUser) {
			throw new FailedLoginException("가입하지 않은 메일 주소입니다.");
		}
		if (!dbUser.getPassword().equals(user.getPassword())) {
			throw new FailedLoginException("잘못된 비밀번호입니다.");
		}
		return new SessionUser(dbUser);
	}
}
