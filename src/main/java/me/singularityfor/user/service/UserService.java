package me.singularityfor.user.service;

import lombok.val;
import me.singularityfor.user.domain.User;
import me.singularityfor.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Objects;

/**
 * Created by Order on 2015. 10. 22..
 */
@Service
@Transactional
public class UserService {

    @Resource
    UserRepository userRepository;

    public User findOne(long userId) {
        return Objects.requireNonNull(userRepository.findOne(userId), () -> "회원가입이 되어 있지 않습니다");
    }

    public User create(User requestUser) {
        //TODO 가입 상태 체크하기.
        return userRepository.save(requestUser);
    }

    public void delete(User requestUser) {
        val repositoryUser = this.findOne(requestUser.getId());
        repositoryUser.delete();
    }
}
