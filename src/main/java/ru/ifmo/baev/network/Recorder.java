package ru.ifmo.baev.network;

import ru.ifmo.baev.network.message.Voice;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class Recorder {

    private final Config config = new Config();

    private final TargetDataLine microphone;

    private Thread recorderThread;

    private boolean isRecording;

    private Queue<byte[]> framesRead = new LinkedList<>();

    public Recorder() throws LineUnavailableException {
        microphone = AudioSystem.getTargetDataLine(config.getAudioFormat());
        microphone.open(config.getAudioFormat());
    }

    public void start() {
        recorderThread = new Thread(new RecorderThread());
        isRecording = true;
        recorderThread.start();
    }

    public void stop() {
        isRecording = false;
    }

    private class RecorderThread implements Runnable {
        @Override
        public void run() {
            microphone.start();

            while (isRecording) {
                byte[] bytes = readFrame();
                framesRead.add(bytes);
                System.out.println("Frame read");
            }

            microphone.stop();
            microphone.flush();
            microphone.close();
        }
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

    public Queue<byte[]> getFramesRead() {
        return framesRead;
    }

    public static void main(String[] args) throws Exception {
        Config config = new Config();

        Recorder recorder = new Recorder();
        recorder.start();
        Thread.sleep(4000);
        recorder.stop();
        System.out.println(String.format("Frames read: %d", recorder.getFramesRead().size()));

        Thread.sleep(1000);

//        SourceDataLine speaker = AudioSystem.getSourceDataLine(config.getAudioFormat());
//        speaker.open();
//        System.out.println("play...");
//        speaker.start();
//        for (byte[] bytes : recorder.getFramesRead()) {
//            speaker.write(bytes, 0, bytes.length);
//        }
//        speaker.drain();
//        speaker.stop();
//        speaker.close();

        InetAddress address = InetAddress.getByName("localhost");
        DatagramSocket socket = new DatagramSocket();
        long number = 0;
        for (byte[] bytes : recorder.getFramesRead()) {
            Voice voice = new Voice();
            voice.setNumber(number++);
            voice.setFrame(bytes);
            DatagramPacket packet = new DatagramPacket(voice.toBytes(), Voice.SIZE, address, new Config().getClientUDPPort());
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }


}
