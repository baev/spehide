package ru.ifmo.baev.network.message;

import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.Utils;
import ru.ifmo.baev.network.model.AbstractVoice;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class Voice extends AbstractVoice {

    public static final int PREFIX_SIZE = 1;

    public static final int NUMBER_SIZE = 8;

    public static final int FRAME_SIZE = new Config().getAudioFrameSize();

    public static final int SIZE = PREFIX_SIZE + NUMBER_SIZE + FRAME_SIZE;

    @Override
    public byte[] toBytes() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
        byteBuffer.put((byte) getPrefix());
        byteBuffer.putLong(number);
        byteBuffer.put(frame);
        return byteBuffer.array();
    }

    public static Voice fromBytes(byte[] bytes) {
        Voice voice = new Voice();
        voice.setNumber(getNumberFromBytes(bytes));
        voice.setFrame(getFrameFromBytes(bytes));
        return voice;
    }

    protected static Long getNumberFromBytes(byte[] bytes) {
        byte[] pass = Utils.getBytes(bytes, PREFIX_SIZE, NUMBER_SIZE);
        return Utils.longFromBytes(pass);
    }

    private static byte[] getFrameFromBytes(byte[] bytes) {
        return Utils.getBytes(bytes, PREFIX_SIZE + NUMBER_SIZE, FRAME_SIZE);
    }

    protected char getPrefix() {
        return 'v';
    }

}
