package org.example.ftree.utils.wrapper.struct;

import lombok.Getter;
import org.example.ftree.utils.wrapper.Reflects;
import org.example.ftree.utils.wrapper.meta.Entity;
import org.example.ftree.utils.wrapper.meta.TableMapping;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SelectColumnBuilder<E> {
    @Getter
    private final Map<String, String> columnBuilder;
    protected final Class<E> entityClass;
    protected final Boolean annotationMapping;

    public SelectColumnBuilder(Class<E> entityClass, Boolean annotationMapping) {
        this.columnBuilder = new ConcurrentHashMap<>();
        this.entityClass = entityClass;
        this.annotationMapping = annotationMapping;
    }

    //构建字段
    public SelectColumnBuilder<E> column(Entity.ConditionFunction<E, ?> fn) {
        TableMapping tableMapping = Reflects.fnMapping(this.annotationMapping, this.entityClass, fn);
        //fnToColumnName不会返回null,不用判断
        columnBuilder.put(tableMapping.getColumnName().trim(), tableMapping.getFiledName().trim());
        return this;
    }


    //构建字段
    public final SelectColumnBuilder<E> columns(List<Entity.ConditionFunction<E, ?>> fns) {
        for (Entity.ConditionFunction<E, ?> fn : fns) {
            TableMapping tableMapping = Reflects.fnMapping(this.annotationMapping, this.entityClass, fn);
            //fnToColumnName不会返回null,不用判断
            columnBuilder.put(tableMapping.getColumnName().trim(), tableMapping.getFiledName().trim());
        }
        return this;
    }

    public SelectColumnBuilder<E> columnMethod(Entity.ConditionFunction<E, ?> fn, Entity.DB_METHOD dbMethod) {
        TableMapping tableMapping = Reflects.fnMapping(this.annotationMapping, this.entityClass, fn);
        //fnToColumnName不会返回null,不用判断
        //比如 article_id
        String columnName = tableMapping.getColumnName().trim();
        String filedName = tableMapping.getFiledName().trim();
        //COUNT(article_id)
        String columnMethod = String.format("%s(%s)", dbMethod.name(), columnName);
        //articleIdCOUNT
        String columnMethodAs = dbMethod.propertyName(filedName);
        //select ...,COUNT(article_id) as articleIdCOUNT
        columnBuilder.put(columnMethod, columnMethodAs);
        return this;
    }

    //查所有字段（有TableFiled注解修饰的属性）
    public SelectColumnBuilder<E> columnAll() {
        for (Field field : this.entityClass.getDeclaredFields()) {
            TableMapping tableMapping = Reflects.fieldToTableMappingStrict(field);
            //filedToColumnName可能会不会返回null,要判断
            if (tableMapping != null) {
                columnBuilder.put(tableMapping.getColumnName().trim(), tableMapping.getFiledName().trim());
            }
        }
        if (columnBuilder.isEmpty()) {
            throw new RuntimeException(String.format("class %s don't have any filed with %s", this.entityClass.getName(), Entity.TableField.class.getName()));
        }
        return this;
    }

    public String toColumns() {
        return this.columnBuilder
                .entrySet()
                .stream()
                .map(entry -> {
                    return String.format("%s as %s", entry.getKey(), entry.getValue());
                })
                .collect(Collectors.joining(","))
                .trim();
    }

}
