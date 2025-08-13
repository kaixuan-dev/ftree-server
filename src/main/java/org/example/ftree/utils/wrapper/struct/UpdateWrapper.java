package org.example.ftree.utils.wrapper.struct;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.type.JdbcType;
import org.example.ftree.mapper.SimpleQueryMapper;
import org.example.ftree.utils.wrapper.Example;
import org.example.ftree.utils.wrapper.Mappers;
import org.example.ftree.utils.wrapper.Reflects;
import org.example.ftree.utils.wrapper.meta.Entity;
import org.example.ftree.utils.wrapper.meta.TableMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class UpdateWrapper<E> extends BaseWrapper<E> {
    @Getter
    //<字段名称,(jdbcType.name(),值)>
    private final Map<String, Pair<JdbcType, Object>> setBuilder;
    private Mappers.UpdateMapper<E> updateMapper;

    public UpdateWrapper(Class<E> entityClass, String tableName, Boolean annotationMapping) {
        super(entityClass, tableName, annotationMapping);
        this.setBuilder = new HashMap<>();
    }

    public UpdateWrapper<E> update() {
        this.updateMapper = new Mappers.UpdateMapper<>(this);
        return this;
    }

    public UpdateWrapper<E> set(Entity.ConditionFunction<E, ?> fn, Object value) {
        TableMapping tableMapping = Reflects.fnMapping(this.annotationMapping, this.entityClass, fn);
        if (this.getSetBuilder().containsKey(tableMapping.getColumnName())) {
            log.warn(String.format("set 存在重复列 %s", tableMapping.getColumnName()));
        }
        this.getSetBuilder().put(tableMapping.getColumnName(), Pair.of(tableMapping.getJdbcType(), value));
        return this;
    }

    public UpdateWrapper<E> where(Consumer<Example<E>> whereConsumer) {
        whereConsumer.accept(this.example);
        return this;
    }

    public Mappers.UpdateMapper<E> mapper(SimpleQueryMapper mapper) {
        this.mapper = mapper;
        if (this.updateMapper == null) {
            throw new RuntimeException("请先执行.update()构建UpdateMapper");
        }
        return updateMapper;
    }
}
