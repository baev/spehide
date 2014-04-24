package ru.ifmo.baev.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.task.Task;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public abstract class AbstractUDPReceiver extends AbstractProcessor {

    private static final int MAX_UDP_MESSAGE_SIZE = 2048;

    private final Logger logger = LogManager.getLogger(AbstractUDPReceiver.class);

    private final Queue<Task> tasks;

    protected AbstractUDPReceiver(Queue<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        byte[] receivedMessage = new byte[MAX_UDP_MESSAGE_SIZE];

        MulticastSocket receiverSocket;
        try {
            receiverSocket = new MulticastSocket(getPort());
        } catch (IOException e) {
            logger.error("MulticastSocket initialization failed", e);
            return;
        }

        while (isRunning()) {
            try {
                DatagramPacket receivedPacket = new DatagramPacket(receivedMessage, receivedMessage.length);
                receiverSocket.receive(receivedPacket);

                process(receivedMessage, receivedPacket.getAddress(), new Config().getClientTCPPort());
            } catch (IOException e) {
                logger.error("Error upd message receiving...", e);
            }
        }
    }

    public abstract void process(byte[] received, InetAddress from, int port);

    public void addTask(Task task) {
        getTasks().add(task);
    }

    public Queue<Task> getTasks() {
        return tasks;
    }

    public abstract int getPort();
}
