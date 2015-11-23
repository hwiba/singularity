package me.singularityfor.user.service;

import lombok.val;
import me.singularityfor.user.domain.User;
import me.singularityfor.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Order on 2015. 10. 22..
 */
@Service
@Transactional
public class UserService {

    @Resource
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public User findOne(long userId) {
        return Objects.requireNonNull(userRepository.findOne(userId), () -> "회원가입이 되어 있지 않습니다");
    }

    public User create(User requestUser) throws Exception {
        if (null != findOneByEmail(requestUser.getEmail())) {
            throw new Exception("error");
        }
        requestUser.hashingPassword(bCryptPasswordEncoder::encode);
        return userRepository.save(requestUser);
    }

    private User findOneByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    public void delete(User requestUser) {
        val repositoryUser = this.findOne(requestUser.getId());
        repositoryUser.delete();
    }


}
