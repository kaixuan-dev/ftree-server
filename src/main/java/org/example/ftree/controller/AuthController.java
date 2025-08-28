package org.example.ftree.controller;

import org.example.ftree.model.dto.auth.LoginDto;
import org.example.ftree.model.vo.auth.CaptchaVo;
import org.example.ftree.model.vo.ResultEntity;
import org.example.ftree.model.vo.auth.LoginVo;
import org.example.ftree.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 登录
     *
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public ResultEntity<LoginVo> login(@Validated @RequestBody LoginDto loginDto) {
        LoginVo loginVo = authService.userLogin(loginDto);
        return ResultEntity.success("登录成功", loginVo);
    }

    /**
     * 获取验证码
     *
     * @return
     */
    @GetMapping("/captcha")
    public ResultEntity<CaptchaVo> captcha() {
        CaptchaVo captcha = authService.captcha();
        return ResultEntity.success("获取验证码成功", captcha);
    }

}
