package org.example.ftree.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.ftree.entity.User;
import org.example.ftree.mapper.UserMapper;
import org.example.ftree.model.dto.user.manage.UserTableDto;
import org.example.ftree.model.enums.UserStatus;
import org.example.ftree.model.enums.UserType;
import org.example.ftree.model.vo.PageVo;
import org.example.ftree.model.vo.user.manage.UserTableVo;
import org.example.ftree.service.UserManageService;
import org.example.ftree.service.UserService;
import org.example.ftree.utils.PageUtils;
import org.example.ftree.utils.wrapper.Predicates;
import org.example.ftree.utils.wrapper.struct.QueryWrapper;
import org.example.ftree.utils.wrapper.struct.SelectColumnBuilder;
import org.example.ftree.utils.wrapper.struct.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Override
    public PageVo<UserTableVo> userTables(UserTableDto dto, User user) {
        QueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .select(SelectColumnBuilder::columnAll)
                .where(w -> {
                    w.createCriteria()
                            .andLike(User::getName, Predicates.likeValue(dto.getName()), Predicates.STRING_NOT_BLANK)
                            .andLike(User::getUserName, Predicates.likeValue(dto.getUsername()), Predicates.STRING_NOT_BLANK)
                            .andLike(User::getMobilePhone, Predicates.likeValue(dto.getPhone()), Predicates.STRING_NOT_BLANK);
                });
        PageVo<User> userPageVo = userMapper.selectPage(queryWrapper, dto.getCurrentPage(), dto.getSize());
        if (!PageUtils.isNotEmpty(userPageVo)) {
            return PageUtils.empty();
        }
        PageVo<UserTableVo> pageVo = new PageVo<>(userPageVo.getTotal(), new ArrayList<>());
        //枚举转换
        userPageVo.getData().forEach(u -> {
            UserTableVo vo = new UserTableVo();
            BeanUtils.copyProperties(u, vo);
            vo.setUserType(UserType.ENUM_MAP.get(vo.getUserType()));
            vo.setStatus(UserStatus.ENUM_MAP.get(vo.getStatus()));
            vo.setRoles(String.join("|", userService.getUserRoleTags(u.getUserId())));
            pageVo.getData().add(vo);
        });
        return pageVo;
    }
}
