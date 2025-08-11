package org.example.ftree.controller;

import org.example.ftree.model.dto.auth.LoginDto;
import org.example.ftree.model.vo.ResultEntity;
import org.example.ftree.model.vo.auth.LoginVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthController {

    /**
     * 登录
     *
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public ResultEntity<LoginVo> login(@Valid @RequestBody LoginDto loginDto) {

        return new ResultEntity<>();
    }

}
