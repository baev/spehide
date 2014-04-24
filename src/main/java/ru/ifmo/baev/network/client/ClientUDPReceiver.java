package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractUDPReceiver;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.Voice;
import ru.ifmo.baev.network.model.CallStatus;
import ru.ifmo.baev.network.task.Task;

import java.net.InetAddress;
import java.util.Map;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class ClientUDPReceiver extends AbstractUDPReceiver {
    private final int port;

    private Map<Long, byte[]> incomingVoice;

    private ClientData data;

    protected ClientUDPReceiver(ClientData data, Queue<Task> tasks, Map<Long, byte[]> incomingVoice) {
        super(tasks);
        port = new Config().getClientUDPPort();
        this.incomingVoice = incomingVoice;
        this.data = data;
    }

    @Override
    public void process(byte[] received, InetAddress from, int port) {
        char messageType = (char) received[0];
        switch (messageType) {
            case 'v':
                if (data.callStatus != CallStatus.CONVERSATION || !data.callWith.contains(from)) {
                    return;
                }
                Voice voice = Voice.fromBytes(received);
                incomingVoice.put(voice.getNumber(), voice.getFrame());
                break;
        }
    }

    @Override
    public int getPort() {
        return port;
    }
}
