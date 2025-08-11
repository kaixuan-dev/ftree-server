package org.example.ftree.utils.wrapper;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.ftree.utils.wrapper.meta.Entity;
import org.example.ftree.utils.wrapper.meta.TableMapping;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @Author kx
 * @Date 2024/1/26 13:41
 */
@Slf4j
public abstract class Reflects {

    private static final Pattern GET_PATTERN = Pattern.compile("^get[A-Z].*");
    private static final Pattern IS_PATTERN = Pattern.compile("^is[A-Z].*");


    /**
     * 获取表名
     *
     * @param entityClass
     * @return
     */
    public static String getTableName(Class<?> entityClass) {
        Entity.Table tableName = entityClass.getAnnotation(Entity.Table.class);
        if (tableName == null || StringUtils.isBlank(tableName.table_name())) {
            throw new RuntimeException(String.format("class %s don't have TableName", entityClass.getName()));
        }
        return tableName.table_name().trim();
    }

    public static String getIndexName(Class<?> entityClass) {
        Entity.Index index = entityClass.getAnnotation(Entity.Index.class);
        if (index == null || StringUtils.isBlank(index.name())) {
            throw new RuntimeException(String.format("class %s don't have index", entityClass.getName()));
        }
        return index.name().trim();
    }

    /**
     * lambda 转 属性名称
     *
     * @param annotationMapping 是否按注解严格匹配
     * @param entityClass       实体类
     * @param fn                lambda
     * @return
     */
    public static TableMapping fnMapping(Boolean annotationMapping, Class<?> entityClass, Entity.ConditionFunction<?, ?> fn) {
        //STEP 0:解析lambda信息
        TableMapping firstTableMapping = fnToTableMapping(fn);
        TableMapping secondTableMapping = fnToTableMappingStrict(firstTableMapping.getFiledName(), entityClass);
        //STEP 1:注解匹配模式
        if (Boolean.TRUE.equals(annotationMapping)) {
            return secondTableMapping;
        }
        //STEP 2:就算不是 注解匹配模式 还是优先使用 注解匹配，能匹配到就直接返回，忽略所有异常
        if (secondTableMapping == null) {
            return firstTableMapping;
        }
        return secondTableMapping;
    }

    /**
     * lambda 转 属性名称
     *
     * @param fn
     * @return
     */
    public static TableMapping fnToTableMapping(Entity.ConditionFunction<?, ?> fn) {
        try {
            //序列化 lambda
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            //方法名称
            String methodName = serializedLambda.getImplMethodName();
            StringBuilder filedNameBuilder = new StringBuilder();
            if (GET_PATTERN.matcher(methodName).matches()) {
                filedNameBuilder.append(methodName.substring(3));
            } else if (IS_PATTERN.matcher(methodName).matches()) {
                filedNameBuilder.append(methodName.substring(2));
            } else {
                throw new RuntimeException(String.format("%s isn't filed getXXX or isXXX method", methodName));
            }
            //属性名称
            String filedName = Introspector.decapitalize(filedNameBuilder.toString());
            String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, filedName);
            return new TableMapping(columnName, filedName, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * lambda 转 属性名称(严格模式，属性必须有注解)
     *
     * @param finalFieldName 属性名称
     * @param entityClass
     * @return
     */
    private static TableMapping fnToTableMappingStrict(String finalFieldName, Class<?> entityClass) {
        //校验属性
        //可能有null，要判断
        Optional<TableMapping> columnOptional = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(Boolean.TRUE);
                    return field.getName().equals(finalFieldName);
                })
                .map(Reflects::fieldToTableMappingStrict)
                .filter(Objects::nonNull)
                .findFirst();
        return columnOptional.orElse(null);
    }

    /**
     * field 转 属性名称
     *
     * @param field
     * @return tp2(属性名称, 字段名称)
     */
    public static TableMapping fieldToTableMappingStrict(Field field) {
        Entity.TableField tableField = field.getAnnotation(Entity.TableField.class);
        if (tableField != null) {
            if (StringUtils.isNotBlank(tableField.column_name())) {
                return new TableMapping(tableField.column_name(), field.getName(), tableField.jdbcTypeCode());
            }
        }
        return null;
    }

    /**
     * 获取ES属性名
     *
     * @param entityClass
     * @param fn
     * @return
     */
    public static <T> String getEsFieldName(Class<T> entityClass, Entity.ConditionFunction<T, ?> fn) {
        TableMapping tableMapping = Reflects.fnToTableMapping(fn);
        Optional<String> fieldNameOptional = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(Boolean.TRUE);
                    return field.getName().equals(tableMapping.getFiledName());
                })
                .map(field -> {
                    Entity.TableField tableField = field.getAnnotation(Entity.TableField.class);
                    if (Objects.nonNull(tableField)) {
                        return tableField.column_name();
                    }
                    JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                    if (Objects.nonNull(jsonProperty)) {
                        return jsonProperty.value();
                    }
                    return null;
                })
                .filter(StringUtils::isNoneBlank)
                .findFirst();
        //优先使用注解的值，没有再把getXXXX中的XXXX驼峰转下划线
        return fieldNameOptional.orElseGet(tableMapping::getColumnName);
    }

    /**
     * 获取实体类属性名
     *
     * @param fn
     * @return
     */
    public static <T> String getEntityFieldName(Entity.ConditionFunction<T, ?> fn) {
        TableMapping tableMapping = Reflects.fnToTableMapping(fn);
        return tableMapping.getFiledName();
    }

    /**
     * 实体类转map ,column_name作为key
     * @param entity
     * @return
     * @param <T>
     */
    public static <T> Map<String, Object> entity2columnMap(T entity) {
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        Map<String, Object> columnMap = new HashMap<>();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(Boolean.TRUE);
            try {
                Entity.TableField annotation = declaredField.getAnnotation(Entity.TableField.class);
                if (annotation != null) {
                    columnMap.put(annotation.column_name(), declaredField.get(entity));
                }
            } catch (Exception e) {
                log.warn("entity2columnMap error", e);
            }
        }
        return columnMap;
    }

    /**
     * 获取字段的数据库字段名
     * @param entityClass
     * @param fn
     * @return
     * @param <T>
     */
    public static <T> String getFieldColumnName(Class<T> entityClass, Entity.ConditionFunction<T, ?> fn) {
        TableMapping tableMapping = Reflects.fnToTableMapping(fn);
        Optional<String> fieldNameOptional = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(Boolean.TRUE);
                    return field.getName().equals(tableMapping.getFiledName());
                })
                .map(field -> {
                    Entity.TableField tableField = field.getAnnotation(Entity.TableField.class);
                    if (Objects.nonNull(tableField)) {
                        return tableField.column_name();
                    }
                    return null;
                })
                .filter(StringUtils::isNoneBlank)
                .findFirst();
        //优先使用注解的值，没有再把getXXXX中的XXXX驼峰转下划线
        return fieldNameOptional.orElseGet(tableMapping::getColumnName);
    }
}
