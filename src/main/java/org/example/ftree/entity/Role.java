package org.example.ftree.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.ftree.utils.wrapper.meta.Entity;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity.Table(table_name = "t_role")
public class Role {

    @Entity.TableField(column_name ="role_id")
    private Long roleId;

    @Entity.TableField(column_name = "role_tag")
    private String roleTag;

    @Entity.TableField(column_name = "role_name")
    private String roleName;

    @Entity.TableField(column_name = "create_time")
    private LocalDateTime createTime;

    @Entity.TableField(column_name = "update_time")
    private LocalDateTime updateTime;

}
