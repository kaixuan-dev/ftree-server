package org.example.ftree.model.vo;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageVo<T> {

    private long total;
    private List<T> data;

}
