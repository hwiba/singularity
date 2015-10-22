package me.singularityfor.user.service;

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

    private User findOne(long userId) {
        return Objects.requireNonNull(userRepository.findOne(userId), () -> "해당 유저가 존재하지 않습니다.");
    }

    public User create(User requestUser) {
        //TODO 가입 상태 체크하기.
        return userRepository.save(requestUser);
    }





}
