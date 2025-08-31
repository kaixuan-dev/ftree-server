package org.example.ftree.controller;

import org.example.ftree.entity.User;
import org.example.ftree.model.dto.user.manage.UserTableDto;
import org.example.ftree.model.vo.PageVo;
import org.example.ftree.model.vo.ResultEntity;
import org.example.ftree.model.vo.user.manage.UserTableVo;
import org.example.ftree.service.UserManageService;
import org.example.ftree.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/user/manage")
@RestController
public class UserManageController {

    @Autowired
    private UserManageService userManageService;

    @Autowired
    private UserUtils userUtils;

    @PostMapping("/tables")
    public ResultEntity<PageVo<UserTableVo>> tables(@Validated @RequestBody UserTableDto dto, HttpServletRequest request) {
        User user = userUtils.getUserFromRequest(request);
        PageVo<UserTableVo> pageVo = userManageService.userTables(dto, user);
        return new ResultEntity<>("查询成功", pageVo);
    }

}
