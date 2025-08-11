package org.example.ftree.service;

import org.example.ftree.model.dto.auth.LoginDto;

public interface AuthService {

    /**
     * 用户登录
     *
     * @param dto
     * @return
     */
    String userLogin(LoginDto dto);

}
