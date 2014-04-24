package ru.ifmo.baev.network.server;

import ru.ifmo.baev.network.AbstractTCPSender;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.MessageContainer;

import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class ServerSender extends AbstractTCPSender {
    private final int port;

    public ServerSender(Queue<MessageContainer> outgoing) {
        super(outgoing);
        port = new Config().getClientTCPPort();
    }

    @Override
    public int getPort() {
        return port;
    }
}
