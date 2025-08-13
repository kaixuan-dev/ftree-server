package org.example.ftree.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.ftree.utils.wrapper.meta.Entity;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity.Table(table_name = "t_user")
public class User {

    @Entity.TableField(column_name = "id")
    private Long id;

    @Entity.TableField(column_name = "user_id")
    private String userId;

    @Entity.TableField(column_name = "user_name")
    private String userName;

    @Entity.TableField(column_name = "password")
    private String password;

    @Entity.TableField(column_name = "user_type")
    private String userType;

    @Entity.TableField(column_name = "name")
    private String name;

    @Entity.TableField(column_name = "mobile_phone")
    private String mobilePhone;

    @Entity.TableField(column_name = "status")
    private String status;

    @Entity.TableField(column_name = "create_time")
    private LocalDateTime createTime;

    @Entity.TableField(column_name = "update_time")
    private LocalDateTime updateTime;

}
