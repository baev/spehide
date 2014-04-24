package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractVoiceProcessor;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.Voice;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.util.Map;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class VoicePlayer extends AbstractVoiceProcessor {

    private final Map<Long, byte[]> frames;

    private long counter = 0;

    private SourceDataLine speaker;

    protected VoicePlayer(ClientData data, Map<Long, byte[]> frames) {
        super(data);
        this.frames = frames;
    }

    public void play(Voice voice) {
        frames.put(voice.getNumber(), voice.getFrame());
    }

    @Override
    protected void before() throws Exception {
        SourceDataLine speaker = AudioSystem.getSourceDataLine(new Config().getAudioFormat());
        speaker.open();
        speaker.start();
        counter = 0;
    }

    @Override
    public void process() {
        try {
            byte[] bytes = frames.get(counter);
            if (bytes == null) {
                Thread.sleep(10);
                bytes = frames.get(counter);
                if (bytes == null) {
                    counter++;
                    return;
                }
            }

            speaker.write(bytes, 0, bytes.length);
            counter++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void after() {
        speaker.drain();
        speaker.stop();
        speaker.close();
        counter = 0;
        frames.clear();
    }

}
