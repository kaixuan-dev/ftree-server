package org.example.ftree.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class PageDTO<T> {

    private long total;
    private List<T> data;

}
