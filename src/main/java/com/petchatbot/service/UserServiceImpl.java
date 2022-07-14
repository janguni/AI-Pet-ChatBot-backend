package com.petchatbot.service;

import com.petchatbot.model.User;
import com.petchatbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public void join(User user){
        userRepository.save(user);
    }


}
