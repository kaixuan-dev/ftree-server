package org.example.ftree.utils.wrapper.struct;


import lombok.Getter;

import java.util.function.Supplier;

/**
 * @Author kx
 * @Date 2024/5/23 15:57
 */
public enum PkStrategies {

    SNOW_ID(() -> {
        return UUIDString.IdGenerator.nextId();
    }),
    UUID_32(() -> {
        return UUIDString.get32UUID();
    }),
    AUTO_INC(() -> {
        return null;
    });

    @Getter
    private final Supplier<?> executor;

    PkStrategies(Supplier<?> executor) {
        this.executor = executor;
    }


}
