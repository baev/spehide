package ru.ifmo.baev.network.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractUDPReceiver;
import ru.ifmo.baev.network.Task;
import ru.ifmo.baev.network.VOIPConfig;
import ru.ifmo.baev.network.message.AliveNotification;

import java.net.InetAddress;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class ServerUDPReceiver extends AbstractUDPReceiver {

    private final Logger logger = LogManager.getLogger(getClass());

    private final int port;

    protected ServerUDPReceiver(Queue<Task> tasks) {
        super(tasks);
        this.port = new VOIPConfig().getServerUDPPort();
    }

    @Override
    public void process(byte[] received, InetAddress from) {
        char messageType = (char) received[0];
        switch (messageType) {
            case 'a':
                AliveNotification message = AliveNotification.fromBytes(received);
                addTask(new MarkClientOnlineTask(message, from));
                break;
        }
    }

    @Override
    public int getPort() {
        return port;
    }
}
