package ru.ifmo.baev.network;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public final class Utils {

    private static final String SHA_256 = "SHA-256";

    private static final String UTF_8 = "UTF-8";

    private static final int HEX = 16;

    private static final int SIGNUM = 1;

    private static final ByteOrder BYTE_ORDER = ByteOrder.BIG_ENDIAN;

    private Utils() {
    }

    public static byte[] orderBytes(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(BYTE_ORDER).array();
    }

    public static byte[] getBytes(byte[] bytes, int from, int len) {
        return orderBytes(Arrays.copyOfRange(bytes, from, from + len));
    }

    public static byte[] sha256(String s) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
        messageDigest.update(s.getBytes(getCharset()));
        return messageDigest.digest();
    }

    public static String bytesToHexString(byte[] bytes) {
        return new BigInteger(SIGNUM, bytes).toString(HEX);
    }

    public static String sha256String(String s) throws NoSuchAlgorithmException {
        return bytesToHexString(sha256(s));
    }

    public static Charset getCharset() {
        return Charset.forName(UTF_8);
    }

    public static byte[] getBytesFrom(String s) {
        return orderBytes(s.getBytes(getCharset()));
    }

    public static long longFromBytes(byte[] bytes) {
        long value = 0;
        for (byte b : bytes) {
            value = (value << 8) + (b & 0xff);
        }
        return value;
    }
}
