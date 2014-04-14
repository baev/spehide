package ru.ifmo.baev.network.server;

import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.VOIPConfig;
import ru.ifmo.baev.network.message.MessageContainer;

import java.net.Socket;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class ServerSender extends AbstractProcessor {

    private static final long CHECK_TIMEOUT = 1000;

    private final Queue<MessageContainer> outgoing;

    public ServerSender(Queue<MessageContainer> outgoing) {
        this.outgoing = outgoing;
    }

    @Override
    public void run() {
        VOIPConfig config = new VOIPConfig();

        while (isRunning()) {
            try {
                if (outgoing.isEmpty()) {
                    Thread.sleep(CHECK_TIMEOUT);
                    continue;
                }

                MessageContainer container = outgoing.poll();
                Socket socket = new Socket(container.getAddress(), config.getClientTCPPort());
                socket.getOutputStream().write(container.getMessage().toBytes());
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
