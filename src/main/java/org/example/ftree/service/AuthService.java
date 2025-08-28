package org.example.ftree.service;

import org.example.ftree.model.dto.auth.LoginDto;
import org.example.ftree.model.vo.auth.CaptchaVo;
import org.example.ftree.model.vo.auth.LoginVo;

public interface AuthService {

    /**
     * 用户登录
     *
     * @param dto
     * @return
     */
    LoginVo userLogin(LoginDto dto);


    /**
     * 生成一个验证码
     *
     * @return
     */
    CaptchaVo captcha();
}
