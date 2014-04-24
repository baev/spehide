package ru.ifmo.baev.network.message;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class CallRequest implements Message {

    public static final int SIZE = 1;

    @Override
    public byte[] toBytes() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
        byteBuffer.putChar(Prefix.CALL_REQUEST);
        return byteBuffer.array();
    }

}
