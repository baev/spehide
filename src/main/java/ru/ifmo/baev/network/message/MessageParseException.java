package ru.ifmo.baev.network.message;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
@SuppressWarnings("unused")
public class MessageParseException extends Exception {
    public MessageParseException() {
        super();
    }

    public MessageParseException(String message) {
        super(message);
    }

    public MessageParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageParseException(Throwable cause) {
        super(cause);
    }

    protected MessageParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
