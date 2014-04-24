package ru.ifmo.baev.network.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractUDPReceiver;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.AliveNotification;
import ru.ifmo.baev.network.message.IsUserOnline;
import ru.ifmo.baev.network.task.IsClientOnlineTask;
import ru.ifmo.baev.network.task.MarkClientOnlineTask;
import ru.ifmo.baev.network.task.Task;

import java.net.InetAddress;
import java.util.Queue;

import static ru.ifmo.baev.network.message.Prefix.ALIVE;
import static ru.ifmo.baev.network.message.Prefix.IS_USER_ONLINE;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class ServerUDPReceiver extends AbstractUDPReceiver {

    private final Logger logger = LogManager.getLogger(getClass());

    private final int port;

    protected ServerUDPReceiver(Queue<Task> tasks) {
        super(tasks);
        this.port = new Config().getServerUDPPort();
    }

    @Override
    public void process(byte[] received, InetAddress from, int port) {
        char messageType = (char) received[0];
        switch (messageType) {
            case ALIVE:
                logger.info("Received UDP alive notification from " + from + ":" + port);
                AliveNotification aliveNotification = AliveNotification.fromBytes(received);
                addTask(new MarkClientOnlineTask(aliveNotification, from, port));
                break;
            case IS_USER_ONLINE:
                logger.info("Received UDP is user online from " + from + ":" + port);
                IsUserOnline isUserOnline = IsUserOnline.fromBytes(received);
                addTask(new IsClientOnlineTask(isUserOnline, from, port));
                break;
        }
    }

    @Override
    public int getPort() {
        return port;
    }
}
