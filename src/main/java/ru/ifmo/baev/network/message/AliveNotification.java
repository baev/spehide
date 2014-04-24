package ru.ifmo.baev.network.message;

import ru.ifmo.baev.network.Utils;
import ru.ifmo.baev.network.model.AbstractAliveNotification;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class AliveNotification extends AbstractAliveNotification {

    public static final int PREFIX_SIZE = 1;

    public static final int UID_SIZE = 36;

    public static final int TOKEN_SIZE = 36;

    public static final int TIME_SIZE = 8;

    public static final int SIZE = PREFIX_SIZE + UID_SIZE + TOKEN_SIZE + TIME_SIZE;

    @Override
    public byte[] toBytes() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
        byteBuffer.put((byte) getPrefix());
        byteBuffer.put(Utils.getBytesFrom(uid));
        byteBuffer.put(Utils.getBytesFrom(token));
        byteBuffer.putLong(time);
        return byteBuffer.array();
    }

    public static AliveNotification fromBytes(byte[] bytes) {
        AliveNotification message = new AliveNotification();
        message.setUid(getUidFromBytes(bytes));
        message.setToken(getTokenFromBytes(bytes));
        message.setTime(getTimeFromBytes(bytes));
        return message;
    }

    protected static String getUidFromBytes(byte[] bytes) {
        byte[] login = Utils.getBytes(bytes, PREFIX_SIZE, UID_SIZE);
        return new String(login);
    }

    protected static String getTokenFromBytes(byte[] bytes) {
        byte[] pass = Utils.getBytes(bytes, PREFIX_SIZE + UID_SIZE, TOKEN_SIZE);
        return new String(pass);
    }

    protected static Long getTimeFromBytes(byte[] bytes) {
        byte[] pass = Utils.getBytes(bytes, PREFIX_SIZE + UID_SIZE + TOKEN_SIZE, TIME_SIZE);
        return Utils.longFromBytes(pass);
    }

    protected char getPrefix() {
        return 'a';
    }
}
