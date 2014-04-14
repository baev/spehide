package ru.ifmo.baev.network.message;

import ru.ifmo.baev.network.Utils;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public final class MessageFactory {
    private MessageFactory() {
    }

    public static AliveNotification createAliveNotification(byte[] bytes) {
        AliveNotification message = new AliveNotification();
        message.setUid(new String(bytes));
        return message;
    }

    public static LoginRequest createLoginRequest(byte[] bytes) {
        LoginRequest message = new LoginRequest();
        message.setLogin(getLogin(bytes));
        message.setPass(getPass(bytes));
        return message;
    }

    private static String getLogin(byte[] bytes) {
        byte[] login = Utils.getBytes(bytes, LoginRequest.PREFIX_SIZE, LoginRequest.LOGIN_SIZE);
        return new String(login);
    }

    private static String getPass(byte[] bytes) {
        byte[] pass = Utils.getBytes(bytes, LoginRequest.PREFIX_SIZE + LoginRequest.LOGIN_SIZE, LoginRequest.PASS_SIZE);
        return new String(pass);
    }

}
