package singularity.user.service;

import java.util.Objects;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.val;
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
		val dbUser = userRepository.findOneByEmail(user.getEmail());
		if (null == dbUser) {
			throw new IllegalArgumentException("가입하지 않은 유저입니다.");
		}
		if (!dbUser.isReady()) {
			throw new IllegalArgumentException("승인 대상이 아닌 유저입니다.");
		}
		dbUser.accept();
	}

	public User findOne(long userId) {
		return Objects.requireNonNull(userRepository.findOne(userId),
				() -> "회원 가입이 되어 있지 않습니다.");
	}

	public void update(User requestUser) {
		this.findOne(requestUser.getId())
				.changeEmail(requestUser.getEmail())
				.changeProfileImage(requestUser.getProfileImage());
	}

	public void delete(User user) {
		this.findOne(user.getId()).delete();
	}

	public User findForLogin(User requestUser) throws IllegalArgumentException {
		val repositoryUser = this.findOne(requestUser.getId());
		if (!repositoryUser.isValidatedPassword(requestUser.getPassword())) {
			throw new IllegalArgumentException("잘못된 비밀번호입니다.");
		}
		return repositoryUser;
	}

}
