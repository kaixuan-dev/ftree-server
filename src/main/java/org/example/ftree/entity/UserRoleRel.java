package org.example.ftree.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@TableName("t_user_role_rel")
public class UserRoleRel {

    @TableId("rel_id")
    private Long relId;

    @TableField("user_id")
    private String userId;

    @TableField("role_id")
    private Long roleId;

}