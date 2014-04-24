package ru.ifmo.baev.network.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractTCPReceiver;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.Task;
import ru.ifmo.baev.network.message.LoginRequest;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class ServerTCPReceiver extends AbstractTCPReceiver {

    private final Logger logger = LogManager.getLogger(getClass());

    private final int port;

    public ServerTCPReceiver(Queue<Task> tasks) throws IOException {
        super(tasks);
        this.port = new Config().getServerTCPPort();
        logger.info("Server TCP port " + port);
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void process(byte[] received, InetAddress from) {
        char messageType = (char) received[0];
        switch (messageType) {
            case 'l':
                logger.info("Received TCP login request from " + from);
                LoginRequest message = LoginRequest.fromBytes(received);
                addTask(new LoginTask(message, from));
                break;
            case 'o':
                break;
        }
    }
}
