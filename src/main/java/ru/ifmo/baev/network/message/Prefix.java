package ru.ifmo.baev.network.message;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public final class Prefix {
    public static final char ALIVE = 'a';

    public static final char LOGIN_REQUEST = 'l';

    public static final char CALL_REQUEST = 'c';

    public static final char CALL_DENY = 'd';

    public static final char CALL_ALLOW = 'k';

    public static final char IS_USER_ONLINE = 'o';

    public static final char LOGIN_SUCCESSFULLY = 's';

    public static final char USER_STATUS_INFO = 'i';
}
