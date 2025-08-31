package org.example.ftree.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.ftree.entity.Role;
import org.example.ftree.entity.User;
import org.example.ftree.entity.UserRoleRel;
import org.example.ftree.mapper.RoleMapper;
import org.example.ftree.mapper.UserRoleRelMapper;
import org.example.ftree.model.vo.user.MeVo;
import org.example.ftree.service.UserService;
import org.example.ftree.utils.UserUtils;
import org.example.ftree.utils.wrapper.struct.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserUtils userUtils;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleRelMapper userRoleRelMapper;

    @Override
    public MeVo me(User user) {
        MeVo meVo = new MeVo();
        meVo.setName(user.getName());
        meVo.setRoles(new ArrayList<>());
        //查询角色

        meVo.setRoles(this.getUserRoleTags(user.getUserId()));
        return meVo;
    }

    /**
     * 查询用户ID
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserRoleTags(String userId) {
        List<UserRoleRel> userRoleRels = userRoleRelMapper.selectAll(Wrappers.lambdaQuery(UserRoleRel.class)
                .select(s -> s.column(UserRoleRel::getRoleId))
                .where(w -> w.createCriteria().andEqualTo(UserRoleRel::getUserId, userId))
        );
        List<Long> roleIds = Optional.ofNullable(userRoleRels)
                .orElse(new ArrayList<>())
                .stream().map(UserRoleRel::getRoleId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        List<Role> roles = roleMapper.selectAll(Wrappers.lambdaQuery(Role.class)
                .select(s -> s.column(Role::getRoleTag))
                .where(w -> w.createCriteria().andIn(Role::getRoleId, roleIds))
        );
        return Optional.ofNullable(roles)
                .orElse(new ArrayList<>())
                .stream()
                .map(Role::getRoleTag)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }

}
