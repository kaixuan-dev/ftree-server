package org.example.ftree.mapper;



import org.example.ftree.model.vo.PageVo;
import org.example.ftree.utils.SpringContextUtils;
import org.example.ftree.utils.wrapper.struct.QueryWrapper;
import org.example.ftree.utils.wrapper.struct.UpdateWrapper;
import org.example.ftree.utils.wrapper.struct.Wrappers;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author zengkaixuan
 * @Date 2024/8/6 14:42
 */
public interface SimpleBaseMapper<T> {

    default SimpleQueryMapper getSimpleQueryMapper() {
        SimpleQueryMapper bean = SpringContextUtils.getBean(SimpleQueryMapper.class);
        if (Objects.isNull(bean)) {
            throw new RuntimeException("can not getBean SimpleQueryMapper");
        }
        return bean;
    }

    /**
     * insert
     *
     * @param clazz  类型
     * @param entity 对象
     * @return
     */
    default int insert(Class<T> clazz, T entity) {
        return Wrappers.lambdaUpdate(clazz)
                .update()
                .mapper(getSimpleQueryMapper())
                .insert(entity);
    }

    /**
     * 查单个
     *
     * @param where
     * @return
     */
    default T selectOne(QueryWrapper<T> where) {
        return where.mapper(getSimpleQueryMapper()).selectOne(false);
    }


    /**
     * 查全部
     *
     * @param where
     * @return
     */
    default List<T> selectAll(QueryWrapper<T> where) {
        return where.mapper(getSimpleQueryMapper()).selectAll();
    }


    /**
     * 分页查 对象
     *
     * @param where
     * @param page
     * @param size
     * @return
     */
    default PageVo<T> selectPage(QueryWrapper<T> where, int page, int size) {
        return where.mapper(getSimpleQueryMapper()).selectPage(page, size);
    }

    /**
     * 分页查原始数据
     *
     * @param where
     * @param page
     * @param size
     * @return
     */
    default PageVo<Map<String, Object>> selectSourcePage(QueryWrapper<T> where, int page, int size) {
        return where.mapper(getSimpleQueryMapper()).selectSourcePage(page, size);
    }

    /**
     * 计数
     *
     * @param where
     * @return
     */
    default long count0(QueryWrapper<T> where) {
        return where.mapper(getSimpleQueryMapper()).count0();
    }


    /**
     * 删除
     *
     * @param where
     * @return
     */
    default long delete(UpdateWrapper<T> where) {
        return where.mapper(getSimpleQueryMapper()).delete();
    }


    /**
     * 更新
     * @param where
     * @return
     */
    default long update(UpdateWrapper<T> where) {
        return where.mapper(getSimpleQueryMapper()).update();
    }

}
