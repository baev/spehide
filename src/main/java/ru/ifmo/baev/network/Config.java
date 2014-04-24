package ru.ifmo.baev.network;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;

import javax.sound.sampled.AudioFormat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
@Resource.Classpath("voip.properties")
@SuppressWarnings("all")
public class Config {

    public Config() {
        PropertyLoader.populate(this);
    }

    @Property("audio.sample.rate")
    private int audioSampleRate = 32000;

    @Property("audio.sample.size")
    private int audioSampleSize = 2;

    @Property("audio.channels")
    private int audioChannels = 1;

    @Property("audio.signed")
    private boolean audioSigned = true;

    @Property("audio.bigendian")
    private boolean audioBigEndian = false;

    @Property("audio.frame.samples.count")
    private int audioFrameSamplesCount = 256;

    @Property("server.tcp.prot")
    private int serverTCPPort = 6676;

    @Property("server.udp.prot")
    private int serverUDPPort = 6677;

    @Property("client.tcp.port")
    private int clientTCPPort = 6576;

    @Property("client.alive.notify.delay")
    private long clientAliveNotifyDelay = 10;

    @Property("client.online.delay")
    private long clientOnlineDelay = 60;

    public int getAudioSampleRate() {
        return audioSampleRate;
    }

    public int getAudioSampleSize() {
        return audioSampleSize;
    }

    public int getAudioSampleSizeInBits() {
        return audioSampleSize * Byte.SIZE;
    }

    public int getAudioChannels() {
        return audioChannels;
    }

    public boolean isAudioSigned() {
        return audioSigned;
    }

    public boolean isAudioBigEndian() {
        return audioBigEndian;
    }

    public int getAudioFrameSamplesCount() {
        return audioFrameSamplesCount;
    }

    public int getAudioFrameRate() {
        return audioSampleRate / audioFrameSamplesCount;
    }

    public int getAudioFrameSize() {
        return audioSampleSize * audioFrameSamplesCount;
    }

    public int getAudioBufferSize() {
        return getAudioFrameSize() * getAudioFrameRate();
    }

    public AudioFormat getAudioFormat() {
        return new AudioFormat(
                getAudioSampleRate(),
                getAudioSampleSizeInBits(),
                getAudioChannels(),
                isAudioSigned(),
                isAudioBigEndian()
        );
    }

    public int getServerTCPPort() {
        return serverTCPPort;
    }

    public int getServerUDPPort() {
        return serverUDPPort;
    }

    public int getClientTCPPort() {
        return clientTCPPort;
    }

    public long getClientAliveNotifyDelay() {
        return clientAliveNotifyDelay;
    }

    public long getClientOnlineDelay() {
        return clientOnlineDelay;
    }
}
