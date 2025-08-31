package org.example.ftree.service;

import org.example.ftree.entity.User;
import org.example.ftree.model.dto.user.manage.UserTableDto;
import org.example.ftree.model.vo.PageVo;
import org.example.ftree.model.vo.user.manage.UserTableVo;

public interface UserManageService {

    /**
     * 查询用户
     *
     * @param dto
     * @param user
     * @return
     */
    PageVo<UserTableVo> userTables(UserTableDto dto, User user);

}
