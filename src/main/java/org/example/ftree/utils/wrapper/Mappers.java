package org.example.ftree.utils.wrapper;



import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.example.ftree.model.vo.PageVo;
import org.example.ftree.utils.wrapper.meta.Entity;
import org.example.ftree.utils.wrapper.meta.TableMapping;
import org.example.ftree.utils.wrapper.struct.QueryWrapper;
import org.example.ftree.utils.wrapper.struct.UpdateWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author kx
 * @Date 2024/1/29 10:50
 */
public class Mappers {

    public static class QueryMapper<E> {

        protected QueryWrapper<E> wrapper;
        protected final Integer FIRST_PAGE = 1;

        public QueryMapper(QueryWrapper<E> queryWrapper) {
            this.wrapper = queryWrapper;
        }

        public String getTableName() {
            return this.wrapper.getTableName().trim();
        }

        public String getColumns() {
            if (this.wrapper.getSelectColumnBuilder().getColumnBuilder().isEmpty()) {
                throw new RuntimeException("请指定至少1个column名称");
            }
            return this.wrapper.getSelectColumnBuilder().toColumns();
        }

        public Map<String, Object> getQueryEw() {
            Map<String, Object> ew = new HashMap<>();
            //1 表名
            String tableName = this.getTableName();
            //2、字段名。不影响原来的 拟查询字段
            String columns = getColumns();
            ew.put(Entity.TABLE_META.columns.name(), columns);
            ew.put(Entity.TABLE_META.tableName.name(), tableName);
            //group by
            if (StringUtils.isNotBlank(this.wrapper.getGroupByClause())) {
                ew.put(Entity.TABLE_META.groupByClause.name(), this.wrapper.getGroupByClause());
            }
            //order by
            if (CollectionUtils.isNotEmpty(this.wrapper.getOrderByClause())) {
                ew.put(Entity.TABLE_META.orderByClause.name(), String.join(",", this.wrapper.getOrderByClause()));
            }
            //district
            if (Boolean.TRUE.equals(this.wrapper.getDistinct())) {
                ew.put(Entity.TABLE_META.distinct.name(), "true");
            }
            if (this.wrapper.getLimit() != null && this.wrapper.getOffset() != null) {
                ew.put(Entity.TABLE_META.offset.name(), this.wrapper.getOffset());
                ew.put(Entity.TABLE_META.limit.name(), this.wrapper.getLimit());
            }
            return ew;
        }

        /**
         * select 数据 不转换，适用于使用了 count 之类的查询
         *
         * @return
         */
        public List<Map<String, Object>> query() {
            //4、查询启动
            List<Map<String, Object>> maps = this.wrapper.getMapper().select(
                    this.wrapper.getExample(),
                    this.getQueryEw()
            );
            return Optional.ofNullable(maps).orElse(new ArrayList<>());
        }

        public PageVo<Map<String, Object>> selectSourcePage(int page, int size) {
            try (Page<Map<String, Object>> result = PageHelper.startPage(page, size)) {
                //查数据
                List<Map<String, Object>> source = this.query();
                PageVo<Map<String, Object>> pageVo = new PageVo<>();
                pageVo.setTotal(result.getTotal());
                pageVo.setData(source);
                return pageVo;
            } finally {
                PageHelper.clearPage();
            }
        }

        public PageVo<E> selectPage(int page, int size) {
            PageVo<Map<String, Object>> sourcePage = this.selectSourcePage(page, size);
            PageVo<E> pageVo = new PageVo<>();
            //1、此处为返回接过来为List<Map<String, Object>>：map.key为对象的属性名，map.value均为object类型(mybatis会根据数据库类中自动转java类型),比如varchar转string
            //2、此处依靠fastJson做第二次转换，序列化再反序列化对性能有影响
            pageVo.setData(JSON.parseArray(JSON.toJSONString(sourcePage.getData()), this.wrapper.getEntityClass()));
            pageVo.setTotal(sourcePage.getTotal());
            return pageVo;
        }

        public List<E> selectAll() {
            //查数据
            List<Map<String, Object>> source = this.query();
            return JSON.parseArray(JSON.toJSONString(source), this.wrapper.getEntityClass());
        }

        @Deprecated
        public E selectOne() {
            List<E> rows = this.selectPage(FIRST_PAGE, 1).getData();
            if (CollectionUtils.isEmpty(rows)) {
                return null;
            }
            return rows.get(0);
        }

        /**
         * 不进行count
         * @return
         */
        public E selectOne(boolean count) {
            try (Page<Object> page = PageHelper.startPage(FIRST_PAGE, 1, count)) {
                List<E> source = this.selectAll();
                if (CollectionUtils.isEmpty(source)) {
                    return null;
                }
                return source.get(0);
            } finally {
                PageHelper.clearPage();
            }
        }

        /**
         * count(0)
         * PageHelper.startPage(page, size),-1 -1只会执行count 不会执行查询（没有大量数据聪DB -> APP）
         *
         * @return
         */
        public long count0() {
            PageVo<Map<String, Object>> pageVo = this.selectSourcePage(-1, -1);
            return pageVo.getTotal();
        }
    }

    public static class UpdateMapper<E> {
        protected UpdateWrapper<E> wrapper;
        public static final String COLUMN_NAME = "columnName";
        public static final String VALUE = "value";
        public static final String JDBC_TYPE = "jdbcType";

        public UpdateMapper(UpdateWrapper<E> updateWrapper) {
            this.wrapper = updateWrapper;
        }


        public int update() {
            if (CollectionUtils.isEmpty(this.wrapper.getExample().oredCriteria)) {
                throw new RuntimeException("禁止无条件更新全表，至少需一个条件");
            }
            String tableName = this.wrapper.getTableName();
            Example<E> example = this.wrapper.getExample();
            List<Map<String, Object>> setColumns = this.wrapper.getSetBuilder()
                    .entrySet()
                    .stream()
                    .map(entry -> {
                        //列名称
                        String columnName = entry.getKey();
                        JdbcType jdbcType = entry.getValue().getLeft();
                        Object value = entry.getValue().getRight();
                        Map<String, Object> columnMap = new HashMap<String, Object>() {{
                            put(COLUMN_NAME, columnName);
                            put(VALUE, value);
                        }};
                        if (!JdbcType.NULL.equals(jdbcType)) {
                            columnMap.put(JDBC_TYPE, jdbcType.name());
                        }
                        return columnMap;
                    })
                    .collect(Collectors.toList());
            return this.wrapper.getMapper().updateByExample(tableName, setColumns, example);
        }

        public int insert(E entity) {
            String tableName = this.wrapper.getTableName();
            //20240523处理主键生成
            Arrays.stream(entity.getClass().getDeclaredFields())
                    .forEach(field -> {
                        try {
                            field.setAccessible(Boolean.TRUE);
                            Entity.TablePk tablePk = field.getAnnotation(Entity.TablePk.class);
                            if (tablePk != null && tablePk.strategy() != null) {
                                field.set(entity, tablePk.strategy().getExecutor().get());
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
            //字段名 字段值  --> SQL
            List<Map<String, Object>> setColumns = Arrays.stream(entity.getClass().getDeclaredFields())
                    .filter(field -> {
                        field.setAccessible(Boolean.TRUE);
                        return field.getAnnotation(Entity.TableField.class) != null;
                    })
                    .map(field -> {
                        try {
                            TableMapping tableMapping = Reflects.fieldToTableMappingStrict(field);
                            if (tableMapping != null) {
                                field.setAccessible(Boolean.TRUE);
                                Object value = field.get(entity);
                                Map<String, Object> columnMap = new HashMap<String, Object>() {{
                                    put(COLUMN_NAME, tableMapping.getColumnName());
                                    put(VALUE, value);
                                }};
                                if (!JdbcType.NULL.equals(tableMapping.getJdbcType())) {
                                    columnMap.put(JDBC_TYPE, tableMapping.getJdbcType().name());
                                }
                                return columnMap;
                            }
                            return null;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
            return this.wrapper.getMapper().insert(tableName, setColumns);
        }

        public int delete() {
            if (CollectionUtils.isEmpty(this.wrapper.getExample().oredCriteria)) {
                throw new RuntimeException("禁止无条件删除，至少需一个条件");
            }
            String tableName = this.wrapper.getTableName();
            Example<E> example = this.wrapper.getExample();
            return this.wrapper.getMapper().deleteByExample(tableName, example);
        }

    }

}
