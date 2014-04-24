package ru.ifmo.baev.network.message;

import ru.ifmo.baev.network.Utils;
import ru.ifmo.baev.network.model.AbstractIsOnline;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class IsUserOnline extends AbstractIsOnline {

    public static final int PREFIX_SIZE = 1;

    public static final int LOGIN_SIZE = 32;

    public static final int KEY_SIZE = 36;

    public static final int TIME_SIZE = 8;

    public static final int SIZE = PREFIX_SIZE + LOGIN_SIZE + KEY_SIZE + TIME_SIZE;

    @Override
    public byte[] toBytes() throws NoSuchAlgorithmException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
        byteBuffer.put((byte) getPrefix());
        byteBuffer.put(Utils.orderBytes(Utils.sha256(login)));
        byteBuffer.put(Utils.getBytesFrom(key));
        byteBuffer.putLong(lastNotificationTime);
        return byteBuffer.array();
    }

    public static IsUserOnline fromBytes(byte[] bytes) {
        IsUserOnline message = new IsUserOnline();
        message.setLogin(getLoginFromBytes(bytes));
        message.setKey(getKeyFromBytes(bytes));
        message.setLastNotificationTime(getTimeFromBytes(bytes));
        return message;
    }

    protected static String getLoginFromBytes(byte[] bytes) {
        byte[] login = Utils.getBytes(bytes, PREFIX_SIZE, LOGIN_SIZE);
        return Utils.bytesToHexString(login);
    }

    protected static String getKeyFromBytes(byte[] bytes) {
        byte[] pass = Utils.getBytes(bytes, PREFIX_SIZE + LOGIN_SIZE, KEY_SIZE);
        return new String(pass);
    }

    protected static Long getTimeFromBytes(byte[] bytes) {
        byte[] pass = Utils.getBytes(bytes, PREFIX_SIZE + LOGIN_SIZE + KEY_SIZE, TIME_SIZE);
        return Utils.longFromBytes(pass);
    }

    protected char getPrefix() {
        return 'o';
    }
}
