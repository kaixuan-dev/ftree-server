package org.example.ftree.utils.wrapper.struct;


import lombok.Getter;
import org.example.ftree.utils.Snowflake;

import java.util.function.Supplier;

/**
 * @Author kx
 * @Date 2024/5/23 15:57
 */
public enum PkStrategies {

    SNOW_ID(() -> {
        return new Snowflake().nextId();
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
