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
    private T data;

    public ResultEntity(String message, T data) {
        this.code = 0;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultEntity<T> success(String message, T data) {
        ResultEntity<T> result = new ResultEntity<>();
        result.code = 0;
        result.message = message;
        result.data = data;
        return result;
    }
}
