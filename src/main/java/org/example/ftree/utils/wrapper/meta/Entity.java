package org.example.ftree.utils.wrapper.meta;


import org.apache.ibatis.type.JdbcType;
import org.example.ftree.utils.wrapper.struct.PkStrategies;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * @Author kx
 * @Date 2024/2/6 14:28
 */
public interface Entity {
    @FunctionalInterface
    public interface ConditionFunction<T, R> extends Function<T, R>, Serializable {
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Table {
        String table_name();
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Index {
        String name();
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TableField {
        String column_name();

        /**
         * 字段注释
         * @return
         */
        String column_comment() default "";

        //默认 JdbcType.NULL
        JdbcType jdbcTypeCode() default JdbcType.NULL;
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TablePk {
        /**
         * 主键生成策略,INSERT之前自动执行(在JAVA里面)
         *
         * @return
         */
        PkStrategies strategy();
    }


    enum DB_METHOD {
        COUNT,
        MAX,
        MIN,
        AVG,
        SUM;

        public String propertyName(String property) {
            return String.format("%s%s", property, this.name());
        }
    }

    enum DB_ORDER {
        DESC,
        ASC;
    }

    enum DB_OPERATE {
        OR("or"),
        AND("and"),
        EQUAL("="),
        NOT_EQUAL("<>"),
        IN("in"),
        NOT_IN("not in"),
        LIKE("like"),
        NOT_LIKE("not like"),
        IS_NULL("is null"),
        IS_NOT_NULL("is not null"),
        BETWEEN("between"),
        NOT_BETWEEN("not between");

        public final String value;

        DB_OPERATE(String value) {
            this.value = value;
        }
    }

    enum TABLE_META {
        columns,
        tableName,
        childSQLParams,
        groupByClause,
        orderByClause,
        limit,
        offset,
        distinct;
    }

}
