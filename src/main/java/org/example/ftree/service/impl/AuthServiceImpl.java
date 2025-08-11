package org.example.ftree.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.ftree.mapper.UserMapper;
import org.example.ftree.model.dto.auth.LoginDto;
import org.example.ftree.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public String userLogin(LoginDto dto) {
        return "";
    }

}
