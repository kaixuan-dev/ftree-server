package org.example.ftree.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.example.ftree.model.vo.PageVo;

import java.util.ArrayList;
import java.util.Objects;

public class PageUtils {

    public static <E> boolean isNotEmpty(PageVo<E> pageVo) {
        if (Objects.isNull(pageVo)) {
            return false;
        }
        if (CollectionUtils.isEmpty(pageVo.getData())) {
            return false;
        }
        return true;
    }

    public static <E> PageVo<E> empty() {
        return new PageVo<>(0, new ArrayList<>());
    }

}
