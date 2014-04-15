package ru.ifmo.baev.network.message;

import ru.ifmo.baev.network.Utils;
import ru.ifmo.baev.network.model.AbstractLoginSuccessfully;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class LoginSuccessfully extends AbstractLoginSuccessfully {

    public static final int PREFIX_SIZE = 1;

    public static final int UID_SIZE = 36;

    public static final int TOKEN_SIZE = 36;

    public static final int FRIENDS_TOKEN_SIZE = 36;

    public static final int SIZE = PREFIX_SIZE + UID_SIZE + TOKEN_SIZE + FRIENDS_TOKEN_SIZE;

    @Override
    public byte[] toBytes() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
        byteBuffer.put((byte) 's');
        byteBuffer.put(Utils.getBytesFromString(uid));
        byteBuffer.put(Utils.getBytesFromString(token));
        byteBuffer.put(Utils.getBytesFromString(friendsToken));
        return byteBuffer.array();
    }

    public static LoginSuccessfully fromBytes(byte[] bytes) {
        LoginSuccessfully message = new LoginSuccessfully();
        message.setUid(getUid(bytes));
        message.setToken(getToken(bytes));
        message.setFriendsToken(getFriendsToken(bytes));
        return message;
    }

    private static String getUid(byte[] bytes) {
        byte[] login = Utils.getBytes(bytes, PREFIX_SIZE, UID_SIZE);
        return new String(login);
    }

    private static String getToken(byte[] bytes) {
        byte[] pass = Utils.getBytes(bytes, PREFIX_SIZE + UID_SIZE, TOKEN_SIZE);
        return new String(pass);
    }

    private static String getFriendsToken(byte[] bytes) {
        byte[] pass = Utils.getBytes(bytes, PREFIX_SIZE + UID_SIZE + TOKEN_SIZE, FRIENDS_TOKEN_SIZE);
        return new String(pass);
    }
}
