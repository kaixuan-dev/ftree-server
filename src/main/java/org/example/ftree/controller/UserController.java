package org.example.ftree.controller;

import org.example.ftree.entity.User;
import org.example.ftree.model.vo.ResultEntity;
import org.example.ftree.model.vo.user.MeVo;
import org.example.ftree.service.UserService;
import org.example.ftree.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserService userService;

    /**
     * 我的信息
     *
     * @return
     */
    @GetMapping("/me")
    public ResultEntity<MeVo> me(HttpServletRequest httpServletRequest) {
        User user = userUtils.getUserFromRequest(httpServletRequest);
        MeVo meVo = userService.me(user);
        return ResultEntity.success("查询我的信息", meVo);
    }

}
