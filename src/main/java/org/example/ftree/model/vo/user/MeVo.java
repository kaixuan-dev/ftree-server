package org.example.ftree.model.vo.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class MeVo {

    private String name;

    private List<String> roles;

}
