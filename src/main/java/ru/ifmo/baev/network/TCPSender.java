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
public class TCPSender extends AbstractProcessor {

    private final Logger logger = LogManager.getLogger(getClass());

    private static final long CHECK_TIMEOUT = 1000;

    private final Queue<MessageContainer> outgoing;

    public TCPSender(Queue<MessageContainer> outgoing) {
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
                logger.info(String.format(
                        "%s sending message %s to %s:%d",
                        getClass(),
                        container.getMessage().getClass(),
                        container.getAddress(),
                        container.getPort()
                ));
                Socket socket = new Socket(container.getAddress(), container.getPort());
                socket.getOutputStream().write(container.getMessage().toBytes());
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
