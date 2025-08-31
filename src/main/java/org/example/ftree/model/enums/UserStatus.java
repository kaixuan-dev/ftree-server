package org.example.ftree.model.enums;

import lombok.Getter;

import java.util.Map;

@Getter
public enum UserStatus implements EnumMap {

    DISABLE("0", "禁用"),
    ENABLE("1", "启用");


    private final String val;
    private final String description;

    UserStatus(String val, String description) {
        this.val = val;
        this.description = description;
    }

    public static final Map<String, String> ENUM_MAP = EnumUtils.toMap(UserStatus.class);

}
