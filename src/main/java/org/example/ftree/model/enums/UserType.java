package org.example.ftree.model.enums;

import lombok.Getter;

import java.util.Map;

@Getter
public enum UserType implements EnumMap {

    PC("1", "PC端"),
    H5("2", "移动端");


    private final String val;
    private final String description;

    UserType(String val, String description) {
        this.val = val;
        this.description = description;
    }

    public static final Map<String, String> ENUM_MAP = EnumUtils.toMap(UserType.class);

}
