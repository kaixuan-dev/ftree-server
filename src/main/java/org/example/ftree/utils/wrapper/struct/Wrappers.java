package org.example.ftree.utils.wrapper.struct;


import org.example.ftree.utils.wrapper.Reflects;

public class Wrappers {

    public static <E> QueryWrapper<E> lambdaQuery(Class<E> entityClass) {
        String tableName = Reflects.getTableName(entityClass);
        return new QueryWrapper<>(entityClass, tableName, Boolean.TRUE);
    }

    public static <E> QueryWrapper<E> lambdaQuery(Class<E> entityClass, String tableName) {
        return new QueryWrapper<>(entityClass, tableName, Boolean.FALSE);
    }

    public static <E> UpdateWrapper<E> lambdaUpdate(Class<E> entityClass) {
        String tableName = Reflects.getTableName(entityClass);
        return new UpdateWrapper<>(entityClass, tableName, Boolean.TRUE);
    }
}
