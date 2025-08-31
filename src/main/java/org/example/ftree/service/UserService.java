package org.example.ftree.service;

import org.example.ftree.entity.User;
import org.example.ftree.model.vo.user.MeVo;

import java.util.List;

public interface UserService {

    /**
     * 查询我的信息
     *
     * @param user
     * @return
     */
    MeVo me(User user);


    /**
     * 查询用户的权限
     *
     * @param userId
     * @return
     */
    List<String> getUserRoleTags(String userId);
}
