package org.example.ftree.model.vo.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CaptchaVo {

    //图片编码
    private String imgCode;

    //图片内容（base64）
    private String imgSource;

}
