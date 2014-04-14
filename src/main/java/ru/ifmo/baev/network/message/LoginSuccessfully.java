package ru.ifmo.baev.network.message;

import ru.ifmo.baev.network.Utils;
import ru.ifmo.baev.network.model.AbstractLoginSuccessfully;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class LoginSuccessfully extends AbstractLoginSuccessfully {
    public static final int UID_SIZE = 16;

    public static final int TOKEN_SIZE = 16;

    public static final int FRIENDS_TOKEN_SIZE = 16;

    public static final int SIZE = UID_SIZE + TOKEN_SIZE + FRIENDS_TOKEN_SIZE;

    @Override
    public byte[] toBytes() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
        byteBuffer.put(Utils.getBytesFromString(uid));
        byteBuffer.put(Utils.getBytesFromString(token));
        byteBuffer.put(Utils.getBytesFromString(friendsToken));
        return byteBuffer.array();
    }
}
