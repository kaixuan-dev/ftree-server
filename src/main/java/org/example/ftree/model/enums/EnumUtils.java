package org.example.ftree.model.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnumUtils {

    public static <E extends Enum<E> & EnumMap> Map<String, String> toMap(Class<E> enumClass) {
        Map<String, String> map = new LinkedHashMap<>();
        for (E e : enumClass.getEnumConstants()) {
            map.put(e.getVal(), e.getDescription());
        }
        return map;
    }
}
