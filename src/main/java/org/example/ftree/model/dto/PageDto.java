package org.example.ftree.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PageDto {

    private int currentPage;

    private int size;

}
