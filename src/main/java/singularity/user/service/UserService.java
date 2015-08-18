package singularity.user.service;

import org.springframework.stereotype.Service;
import singularity.exception.ExistedUserException;
import singularity.exception.FailedLoginException;
import singularity.exception.SessionUserMismatchException;
import singularity.user.domain.User;
import singularity.user.dto.SessionUser;
import singularity.user.repository.UserRepository;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Resource
    private UserRepository userRepository;

    public SessionUser getSessionUser(HttpSession session) {
        return (SessionUser) session.getAttribute("sessionUser");

    }

    public boolean checkSessionUser(HttpSession session, Long userId) {
        if (this.getSessionUser(session).equals(userId)) {
            return true;
        }
        return false;
    }

    public User create(User user) throws ExistedUserException {
        if (null != userRepository.findOneByEmail(user.getEmail())) {
            throw new ExistedUserException("이미 가입된 E-mail 주소입니다.");
        }
        return userRepository.save(user);
    }

    public User signingUp(User user) {
        user.accept();
        return userRepository.save(user);
    }

    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }

    public void update(User user, HttpSession session) throws SessionUserMismatchException {
        if (!checkSessionUser(session, user.getId())) {
            throw new SessionUserMismatchException("권한이 없는 요청입니다.");
        }
        User dbUser = userRepository.findOne(user.getId());
        dbUser.changeEmail(user.getEmail());
        dbUser.changeProfileImage(user.getProfileImage());
    }

    public void delete(User user) {
        User dbUser = userRepository.findOne(user.getId());
        dbUser.delete();
    }

    public User findForLogin(User user) throws FailedLoginException {
        User dbUser = userRepository.findOne(user.getId());
        if (null == dbUser) {
            throw new FailedLoginException("가입하지 않은 메일 주소입니다.");
        }
        if (!dbUser.getPassword().equals(user.getPassword())) {
            throw new FailedLoginException("잘못된 비밀번호입니다.");
        }
        return dbUser;
    }

}
