package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractUDPReceiver;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.task.Task;
import ru.ifmo.baev.network.message.Voice;

import java.net.InetAddress;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class ClientUDPReceiver extends AbstractUDPReceiver {
    private final int port;

    private final VoicePlayer player;

    protected ClientUDPReceiver(Queue<Task> tasks) {
        super(tasks);
        port = new Config().getClientUDPPort();
        player = new VoicePlayer();
        player.start();
        new Thread(player).start();
    }

    @Override
    public void process(byte[] received, InetAddress from, int port) {
        char messageType = (char) received[0];
        switch (messageType) {
            case 'v':
                Voice voice = Voice.fromBytes(received);
                System.out.println(voice.getNumber());
                player.play(voice);
                break;
        }
    }

    @Override
    public int getPort() {
        return port;
    }
}
