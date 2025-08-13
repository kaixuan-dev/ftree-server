package org.example.ftree.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.ftree.utils.wrapper.meta.Entity;

@Setter
@Getter
@ToString
@Entity.Table(table_name = "t_user_role_rel")
public class UserRoleRel {

    @Entity.TableField(column_name = "rel_id")
    private Long relId;

    @Entity.TableField(column_name = "user_id")
    private String userId;

    @Entity.TableField(column_name = "role_id")
    private Long roleId;

}