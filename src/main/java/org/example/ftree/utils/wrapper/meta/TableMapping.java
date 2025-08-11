package org.example.ftree.utils.wrapper.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.JdbcType;

/**
 * @Author kx
 * @Date 2024/2/4 17:20
 */
@Getter
@AllArgsConstructor
public class TableMapping {
    private String columnName;
    private String filedName;
    private JdbcType jdbcType;

}
