package org.example.ftree.model.vo.user.manage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.ftree.utils.wrapper.meta.Entity;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class UserTableVo {

    //用户名
    private String userName;

    //用户类型
    private String userType;

    //角色
    private String roles;

    //姓名
    private String name;

    //手机号
    private String mobilePhone;

    //状态
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updateTime;

}
