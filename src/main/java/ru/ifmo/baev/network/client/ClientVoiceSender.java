package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.message.MessageContainer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class ClientVoiceSender extends AbstractProcessor {

    private final Logger logger = LogManager.getLogger(getClass());

    private static final long CHECK_TIMEOUT = 10;

    private final Queue<MessageContainer> outgoing;

    public ClientVoiceSender(Queue<MessageContainer> outgoing) {
        this.outgoing = outgoing;
    }

    @Override
    public void run() {

        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        while (isRunning()) {
            try {
                if (outgoing.isEmpty()) {
                    Thread.sleep(CHECK_TIMEOUT);
                    continue;
                }
                MessageContainer container = outgoing.poll();
                byte[] bytes = container.getMessage().toBytes();
                socket.send(new DatagramPacket(
                        bytes,
                        bytes.length,
                        container.getAddress(),
                        container.getPort()

                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
