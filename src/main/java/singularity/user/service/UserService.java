package singularity.user.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import singularity.user.domain.User;
import singularity.user.exception.ExistedUserException;
import singularity.user.repository.UserRepository;

@Service
@Transactional
public class UserService {

    @Resource
    private UserRepository userRepository;

    public User create(User user) throws ExistedUserException {
        if (null != userRepository.findOneByEmail(user.getEmail())) {
            throw new ExistedUserException("이미 가입된 E-mail 주소입니다.");
        }
        return userRepository.save(user);
    }

    public void accept(User user) {
        User dbUser = userRepository.findOneByEmail(user.getEmail());
        if (null == dbUser) {
            throw new IllegalArgumentException("가입하지 않은 유저입니다.");
        }
        if (!dbUser.isReady()) {
            throw new IllegalArgumentException("승인 대상이 아닌 유저입니다.");
        }
        dbUser.accept();
    }

    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }

    public void update(User user) {
        User dbUser = userRepository.findOne(user.getId());
        dbUser.changeEmail(user.getEmail());
        dbUser.changeProfileImage(user.getProfileImage());
    }

    public void delete(User user) {
        User dbUser = userRepository.findOne(user.getId());
        dbUser.delete();
    }

    public User findForLogin(User user) throws IllegalArgumentException {
        User dbUser = userRepository.findOne(user.getId());
        if (null == dbUser) {
            throw new IllegalArgumentException("가입하지 않은 메일 주소입니다.");
        }
        if (!dbUser.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return dbUser;
    }

}
