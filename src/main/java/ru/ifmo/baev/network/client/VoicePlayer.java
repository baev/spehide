package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractVoiceProcessor;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.Voice;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class VoicePlayer extends AbstractVoiceProcessor {

    private final Logger logger = LogManager.getLogger(getClass());

    private final List<Voice> frames;

    private long counter = 0;

    private SourceDataLine speaker;

    private AtomicLong last;

    protected VoicePlayer(ClientData data, List<Voice> frames, AtomicLong last) {
        super(data);
        this.frames = frames;
        this.last = last;
    }

    @Override
    protected void before() throws Exception {
        speaker = AudioSystem.getSourceDataLine(new Config().getAudioFormat());
        speaker.open();
        speaker.start();
        counter = 0;
        Thread.sleep(1000);
    }

    @Override
    public void process() {
        if (frames == null || frames.isEmpty()) {
            logger.info("Empty frames, nothing to play...");
            return;
        }
        if (last.get() == -1) {
            return;
        }

        if (last.get() - counter > 100) {
            logger.info("normalize delay...");
            counter = last.get();
        }

        try {
            int index = (int) (counter % frames.size());
            Voice voice = frames.get(index);

            for (int i = 10; i < 100; i += 10) {
                if (isGoodFrame(voice, counter)) {
                    speaker.write(voice.getFrame(), 0, voice.getFrame().length);
                    break;
                }
                logger.info("wait " + i + "ms " + counter);
                Thread.sleep(i);
                index = (int) (counter % frames.size());
                voice = frames.get(index);
            }
            counter++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isGoodFrame(Voice voice, long counter) {
        return voice != null && voice.getNumber() == counter;
    }

    @Override
    protected void after() {
        speaker.drain();
        speaker.stop();
        speaker.close();
        counter = 0;
        for (int i = 0; i < frames.size(); i++) {
            frames.set(i, null);
        }
    }

}
