package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractTCPSender;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.MessageContainer;

import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 15.04.14
 */
public class ClientClientSender extends AbstractTCPSender {
    private final int port;

    public ClientClientSender(Queue<MessageContainer> outgoing) {
        super(outgoing);
        port = new Config().getClientTCPPort();
    }

    @Override
    public int getPort() {
        return port;
    }
}
