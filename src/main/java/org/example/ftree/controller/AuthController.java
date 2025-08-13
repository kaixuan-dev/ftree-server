package org.example.ftree.controller;

import org.example.ftree.model.dto.auth.LoginDto;
import org.example.ftree.model.vo.ResultEntity;
import org.example.ftree.model.vo.auth.LoginVo;
import org.example.ftree.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public ResultEntity<LoginVo> login(@Valid @RequestBody LoginDto loginDto) {
        String token = authService.userLogin(loginDto);
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return ResultEntity.success("登录成功", loginVo);
    }

}
