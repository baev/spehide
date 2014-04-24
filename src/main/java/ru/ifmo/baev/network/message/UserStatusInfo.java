package ru.ifmo.baev.network.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.Utils;
import ru.ifmo.baev.network.model.AbstractUserStatusInfo;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class UserStatusInfo extends AbstractUserStatusInfo {

    public static final int PREFIX_SIZE = 1;

    public static final int LOGIN_SIZE = 64;

    public static final int ADDRESS_SIZE = 36;

    public static final int TIME_SIZE = 8;

    public static final int SIZE = PREFIX_SIZE + LOGIN_SIZE + ADDRESS_SIZE + TIME_SIZE;

    @Override
    public byte[] toBytes() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
        byteBuffer.put((byte) getPrefix());
        byteBuffer.put(Utils.getBytesFrom(login));
        byteBuffer.put(Utils.getBytesFrom(address));
        byteBuffer.putLong(lastNotificationTime);
        return byteBuffer.array();
    }

    public static UserStatusInfo fromBytes(byte[] bytes) {
        UserStatusInfo userStatusInfo = new UserStatusInfo();
        userStatusInfo.setLogin(getLoginFromBytes(bytes));
        userStatusInfo.setAddress(getAddressFromBytes(bytes));
        userStatusInfo.setLastNotificationTime(getTimeFromBytes(bytes));
        return userStatusInfo;
    }

    protected static String getLoginFromBytes(byte[] bytes) {
        byte[] login = Utils.getBytes(bytes, PREFIX_SIZE, LOGIN_SIZE);
        return new String(login);
    }

    protected static String getAddressFromBytes(byte[] bytes) {
        byte[] pass = Utils.getBytes(bytes, PREFIX_SIZE + LOGIN_SIZE, ADDRESS_SIZE);
        return new String(pass);
    }

    protected static Long getTimeFromBytes(byte[] bytes) {
        byte[] pass = Utils.getBytes(bytes, PREFIX_SIZE + LOGIN_SIZE + ADDRESS_SIZE, TIME_SIZE);
        return Utils.longFromBytes(pass);
    }

    protected char getPrefix() {
        return 'i';
    }
}
