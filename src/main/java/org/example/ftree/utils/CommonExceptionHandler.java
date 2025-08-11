package org.example.ftree.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.ftree.model.exception.BusinessException;
import org.example.ftree.model.vo.ResultEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    //处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResultEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        return new ResultEntity<>(-1, String.format("参数校验失败:%s", msg), null);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResultEntity<String> businessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return new ResultEntity<>(-1, e.getMessage(), null);
    }
}
