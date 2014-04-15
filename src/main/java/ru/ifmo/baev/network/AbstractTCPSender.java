package ru.ifmo.baev.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.message.MessageContainer;

import java.net.Socket;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 15.04.14
 */
public abstract class AbstractTCPSender extends AbstractProcessor {

    private final Logger logger = LogManager.getLogger(getClass());

    private static final long CHECK_TIMEOUT = 1000;

    private final Queue<MessageContainer> outgoing;

    public AbstractTCPSender(Queue<MessageContainer> outgoing) {
        this.outgoing = outgoing;
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
                if (outgoing.isEmpty()) {
                    Thread.sleep(CHECK_TIMEOUT);
                    continue;
                }
                MessageContainer container = outgoing.poll();
                logger.info(String.format("%s sending message to %s", getClass(), container.getAddress()));
                Socket socket = new Socket(container.getAddress(), getPort());
                socket.getOutputStream().write(container.getMessage().toBytes());
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public abstract int getPort();
}
