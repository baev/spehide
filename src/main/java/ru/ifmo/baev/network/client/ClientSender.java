package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractTCPSender;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.MessageContainer;

import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 15.04.14
 */
public class ClientSender extends AbstractTCPSender {
    private final int port;

    public ClientSender(Queue<MessageContainer> outgoing) {
        super(outgoing);
        port = new Config().getServerTCPPort();
    }

    @Override
    public int getPort() {
        return port;
    }
}
