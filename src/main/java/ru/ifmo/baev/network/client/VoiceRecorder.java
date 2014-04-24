package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractVoiceProcessor;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.MessageContainer;
import ru.ifmo.baev.network.message.Voice;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class VoiceRecorder extends AbstractVoiceProcessor {

    private final Logger logger = LogManager.getLogger(getClass());

    private final Config config = new Config();

    private TargetDataLine microphone;

    private Queue<MessageContainer> outgoing;

    private long counter = 0;

    protected VoiceRecorder(ClientData data, Queue<MessageContainer> outgoing) {
        super(data);
        this.outgoing = outgoing;
    }

    @Override
    protected void before() throws Exception {
        microphone = AudioSystem.getTargetDataLine(config.getAudioFormat());
        microphone.open(config.getAudioFormat());
        microphone.start();

        counter = 0;
    }

    @Override
    protected void process() {
        byte[] bytes = readFrame();
        Voice voice = new Voice();
        voice.setNumber(counter++);
        voice.setFrame(bytes);
        logger.info("read frame");
    }

    @Override
    protected void after() {
        microphone.stop();
        microphone.flush();
        microphone.close();

        counter = 0;
    }

    private byte[] readFrame() {
        int frameSize = config.getAudioFrameSize();
        int bytesRead = 0;
        byte[] bytes = new byte[frameSize];
        while (bytesRead < frameSize) {
            bytesRead += microphone.read(bytes, bytesRead, frameSize - bytesRead);
        }
        return bytes;
    }
}
