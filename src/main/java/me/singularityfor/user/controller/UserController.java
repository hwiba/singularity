package me.singularityfor.user.controller;

import me.singularityfor.user.domain.User;
import me.singularityfor.user.service.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public User create(@Validated User user, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new Exception("유효하지 않은 유저 정보");
        }
        return userService.create(user);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void delete(User user) {
        userService.delete(user);
    }

}
