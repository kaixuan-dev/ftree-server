package org.example.ftree.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.ftree.entity.User;
import org.example.ftree.mapper.UserMapper;
import org.example.ftree.model.exception.AuthFailException;
import org.example.ftree.model.exception.BusinessException;
import org.example.ftree.utils.wrapper.struct.QueryWrapper;
import org.example.ftree.utils.wrapper.struct.SelectColumnBuilder;
import org.example.ftree.utils.wrapper.struct.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Component
public class UserUtils {

    @Autowired
    private UserMapper userMapper;

    /**
     * 从Http请求中获取用户信息
     *
     * @param httpServletRequest
     * @return
     */
    public User getUserFromRequest(HttpServletRequest httpServletRequest) {
        Object userId = httpServletRequest.getAttribute("userId");
        if (Objects.isNull(userId)) {
            throw new AuthFailException("认证失败");
        }
        return this.getUserById(StringUtils.trim(String.valueOf(userId)));
    }

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId
     * @return
     */
    public User getUserById(String userId) {
        QueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .select(SelectColumnBuilder::columnAll)
                .where(w -> w.createCriteria()
                        .andEqualTo(User::getUserId, StringUtils.trim(userId))
                );
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new AuthFailException("ID为{}的用户不存在", userId);
        }
        return user;
    }


}
