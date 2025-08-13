package org.example.ftree.utils.wrapper.struct;



import lombok.Getter;
import org.example.ftree.mapper.SimpleQueryMapper;
import org.example.ftree.utils.wrapper.Example;
import org.example.ftree.utils.wrapper.Mappers;
import org.example.ftree.utils.wrapper.Reflects;
import org.example.ftree.utils.wrapper.meta.Entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Getter
public class QueryWrapper<E> extends BaseWrapper<E> {

    //SET做 select 字段 去重，防止 select a as A, a as A
    protected final SelectColumnBuilder<E> selectColumnBuilder;
    protected Mappers.QueryMapper<E> queryMapper;
    protected List<String> orderByClause = new CopyOnWriteArrayList<>();
    protected String groupByClause;

    protected Integer limit;
    protected Integer offset;

    protected Boolean distinct;

    protected QueryWrapper(Class<E> entityClass, String tableName, Boolean annotationMapping) {
        super(entityClass, tableName, annotationMapping);
        this.queryMapper = new Mappers.QueryMapper<>(this);
        this.selectColumnBuilder = new SelectColumnBuilder<>(entityClass, annotationMapping);
    }

    public QueryWrapper<E> select(Consumer<SelectColumnBuilder<E>> selectColumnBuilder) {
        selectColumnBuilder.accept(this.selectColumnBuilder);
        return this;
    }

    public QueryWrapper<E> distinctRow() {
        this.distinct = Boolean.TRUE;
        return this;
    }

    public QueryWrapper<E> order(Entity.ConditionFunction<E, ?> fn, Entity.DB_ORDER order) {
        String columnName = Reflects.fnMapping(this.annotationMapping, this.entityClass, fn).getColumnName();
        this.orderByClause.add(String.format("%s %s", columnName.trim(), order.name().trim()));
        return this;
    }

    public QueryWrapper<E> orderMethod(Entity.ConditionFunction<E, ?> fn, Entity.DB_METHOD dbMethod, Entity.DB_ORDER order) {
        String filedName = Reflects.fnMapping(this.annotationMapping, this.entityClass, fn).getFiledName();
        //是select 中 count(xx) as
        String columnMethodAs = dbMethod.propertyName(filedName);
        this.orderByClause.add(String.format("%s %s", columnMethodAs.trim(), order.name().trim()));
        return this;
    }

    public QueryWrapper<E> groupBy(Entity.ConditionFunction<E, ?> fn) {
        this.groupByClause = Reflects.fnMapping(this.annotationMapping, this.entityClass, fn).getColumnName();
        return this;
    }

    public QueryWrapper<E> groupByMethod(Entity.ConditionFunction<E, ?> fn, Entity.DB_METHOD dbMethod) {
        String columnName = Reflects.fnMapping(this.annotationMapping, this.entityClass, fn).getColumnName();
        this.groupByClause = dbMethod.propertyName(columnName);
        return this;
    }

    //请确定真的要用这个而不是用 page
    public QueryWrapper<E> limit(Integer offset, Integer limit) {
        this.limit = limit;
        this.offset = offset;
        return this;
    }

    public QueryWrapper<E> where(Consumer<Example<E>> whereConsumer) {
        whereConsumer.accept(this.example);
        return this;
    }

    public Mappers.QueryMapper<E> mapper(SimpleQueryMapper mapper) {
        this.mapper = mapper;
        return queryMapper;
    }
}
