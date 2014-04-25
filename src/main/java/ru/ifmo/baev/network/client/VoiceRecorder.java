package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractVoiceProcessor;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.Voice;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class VoiceRecorder extends AbstractVoiceProcessor {

    private final Logger logger = LogManager.getLogger(getClass());

    private final Config config = new Config();

    private TargetDataLine microphone;

    private long counter = 0;

    private ClientVoiceSender sender;

    protected VoiceRecorder(ClientData data) {
        super(data);
    }

    @Override
    protected void before() throws Exception {
        logger.info("start recording...");
        microphone = AudioSystem.getTargetDataLine(config.getAudioFormat());
        microphone.open(config.getAudioFormat());
        microphone.start();

        counter = 0;

        sender = new ClientVoiceSender();
    }

    @Override
    protected void process() {
        byte[] bytes = readFrame();
        Voice voice = new Voice();
        voice.setNumber(counter++);
        voice.setFrame(bytes);
        if (!data.callWith.isEmpty()) {
            sender.send(voice, data.callWith.get(0), new Config().getClientTCPPort());
        }
    }

    @Override
    protected void after() {
        microphone.stop();
        microphone.flush();
        microphone.close();

        counter = 0;
        sender.close();
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
