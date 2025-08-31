package org.example.ftree.model.exception;

import java.util.Formatter;

/**
 * @author zengk
 */
public class AuthFailException extends RuntimeException {

    public AuthFailException(String message) {
        super(message);
    }

    public AuthFailException(String format, Object... args) {
        super(new Formatter()
                .format(format.replace("{}", "%s"), args)
                .toString()
        );
    }
}
