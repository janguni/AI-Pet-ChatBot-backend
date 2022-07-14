package com.petchatbot.controller;

import com.petchatbot.model.DTO.UserDto;
import com.petchatbot.model.User;
import com.petchatbot.repository.UserRepository;
import com.petchatbot.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    public String join(@RequestBody UserDto userDto){
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        System.out.println("email = " + email);
        System.out.println("password = " + password);
        User user = new User(email, password);
        // 회원가입
        userServiceImpl.join(user);
        // 패스워드암호화
//      String rawPassword = user.getUser_password();
//      String encPassword = bCryptPasswordEncoder.encode(rawPassword);
//      user.setPassword(encPassword);

        List<User> users = userRepository.findAll();
        log.info("user ok", user);
        return "OK";
    }
}
