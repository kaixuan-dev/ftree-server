package org.example.ftree.model.vo;

import lombok.*;

/**
 * @author zengkaixuan
 * @since 2022-01-23 19:54
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity<T> {
    private Integer code;
    private String message;
    private T entity;

    public ResultEntity(String message, T entity) {
        this.code = 1;
        this.message = message;
        this.entity = entity;
    }

    public static <T> ResultEntity<T> success(String message, T entity) {
        ResultEntity<T> resultEntity = new ResultEntity<>();
        resultEntity.code = 1;
        resultEntity.message = message;
        resultEntity.entity = entity;
        return resultEntity;
    }
}
