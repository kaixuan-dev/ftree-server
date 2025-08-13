package org.example.ftree.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.ftree.utils.wrapper.Example;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author zengkaixuan
 * @Date 2024/1/26 13:41
 */
@Repository
public interface SimpleQueryMapper {


    /**
     * 查列表
     *
     * @param example
     * @param ew      其他条件
     * @return
     */
    List<Map<String, Object>> select(@Param("example") Example example,
                                     @Param("ew") Map<String, Object> ew
    );


    /**
     * 更新记录
     *
     * @param tableName  表名
     * @param setColumns set 列名 = xxx
     * @param example    查询条件
     * @return
     */
    int updateByExample(@Param("tableName") String tableName, @Param("setColumns") List<Map<String, Object>> setColumns, @Param("example") Example example);


    /**
     * 插入数据
     *
     * @param tableName
     * @param setColumns
     * @return
     */
    int insert(@Param("tableName") String tableName, @Param("setColumns") List<Map<String, Object>> setColumns);


    /**
     * 批量插入数据
     *
     * @param tableName
     * @param columns
     * @param values
     * @return
     */
    int bathInsert(@Param("tableName") String tableName, @Param("columns") List<Map<String, String>> columns, @Param("values") List<Map<String, Object>> values);


    /**
     * 删除数据
     *
     * @param tableName
     * @param example
     * @return
     */
    int deleteByExample(@Param("tableName") String tableName, @Param("example") Example example);


}
