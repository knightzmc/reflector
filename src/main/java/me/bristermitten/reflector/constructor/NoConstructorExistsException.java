package me.bristermitten.reflector.constructor;

/**
 * Simple runtime exception thrown if no constructor was found
 */
public class NoConstructorExistsException extends RuntimeException {
    public NoConstructorExistsException() {
    }

    public NoConstructorExistsException(String message) {
        super(message);
    }

    public NoConstructorExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoConstructorExistsException(Throwable cause) {
        super(cause);
    }

    public NoConstructorExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
