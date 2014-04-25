package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractUDPReceiver;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.Voice;
import ru.ifmo.baev.network.model.CallStatus;
import ru.ifmo.baev.network.task.Task;

import java.net.InetAddress;
import java.util.List;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class ClientUDPReceiver extends AbstractUDPReceiver {

    private final Logger logger = LogManager.getLogger(getClass());

    private final int port;

    private List<Voice> incomingVoice;

    private ClientData data;

    protected ClientUDPReceiver(ClientData data, Queue<Task> tasks, List<Voice> incomingVoice) {
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
                long num = voice.getNumber();
                int index = (int) (num % 10000);
                Voice old = incomingVoice.get(index);

                if (old == null || old.getNumber() < voice.getNumber()) {
                    logger.info("received good frame " + voice.getNumber());
                    incomingVoice.set(index, voice);
                } else {
                    logger.info("received bad frame");
                }
                break;
        }
    }

    @Override
    public int getPort() {
        return port;
    }
}
