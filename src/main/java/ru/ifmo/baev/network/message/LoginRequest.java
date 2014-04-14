package ru.ifmo.baev.network.message;

import ru.ifmo.baev.network.Utils;
import ru.ifmo.baev.network.model.AbstractLoginRequest;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class LoginRequest extends AbstractLoginRequest {
    public static final int PREFIX_SIZE = 1;

    public static final int LOGIN_SIZE = 32;

    public static final int PASS_SIZE = 32;

    public static final int SIZE = PREFIX_SIZE + LOGIN_SIZE + PASS_SIZE;

    @Override
    public byte[] toBytes() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
        byteBuffer.put((byte) 'l');
        byteBuffer.put(Utils.orderBytes(Utils.sha256(login)));
        byteBuffer.put(Utils.orderBytes(Utils.sha256(pass)));

        return byteBuffer.array();
    }

}
