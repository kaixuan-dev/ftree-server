package org.example.ftree.model.dto.user.manage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.ftree.model.dto.PageDto;

@Setter
@Getter
@ToString
public class UserTableDto extends PageDto {

    //用户名
    private String username;

    //姓名
    private String name;

    //手机号
    private String phone;

}
