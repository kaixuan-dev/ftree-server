package org.example.ftree.utils.wrapper;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @Author kx
 * @Date 2024/2/7 15:33
 */
public interface Predicates {

    //包含有效字符
    Predicate<Object> STRING_NOT_BLANK = value -> value != null && StringUtils.isNotBlank(value.toString());
    Predicate<Object> OBJECT_NOT_NULL = Objects::nonNull;
}
