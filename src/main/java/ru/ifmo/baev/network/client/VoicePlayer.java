package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.Voice;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class VoicePlayer extends AbstractProcessor {

    private final Map<Long, byte[]> frames = new HashMap<>();

    public VoicePlayer() {
    }

    public void play(Voice voice) {
        frames.put(voice.getNumber(), voice.getFrame());
    }

    @Override
    public void run() {
        long counter = 0;
        System.out.println("start vp");
        try {
            SourceDataLine speaker = AudioSystem.getSourceDataLine(new Config().getAudioFormat());
            speaker.open();
            speaker.start();
            while (isRunning()) {
                try {
                    byte[] bytes = frames.get(counter);
                    if (bytes == null) {
                        Thread.sleep(10);
                        bytes = frames.get(counter);
                        if (bytes == null) {
                            counter++;
                            continue;
                        }
                    }

                    speaker.write(bytes, 0, bytes.length);
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            speaker.drain();
            speaker.stop();
            speaker.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            stop();
        }
        System.out.println("stop vp");
    }
}
