package org.example.ftree.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EncValueDto {

    //值
    private String val;

    //过期时间
    private Long ts;

}
