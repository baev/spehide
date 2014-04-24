package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractTCPReceiver;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.Task;
import ru.ifmo.baev.network.message.LoginSuccessfully;
import ru.ifmo.baev.network.message.UserStatusInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 15.04.14
 */
public class ClientTCPReceiver extends AbstractTCPReceiver {

    private final Logger logger = LogManager.getLogger(getClass());

    private final int port;

    public ClientTCPReceiver(Queue<Task> tasks) throws IOException {
        super(tasks);
        this.port = new Config().getClientTCPPort();
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void process(byte[] received, InetAddress from) {
        char messageType = (char) received[0];
        switch (messageType) {
            case 's':
                logger.info("Received TCP login successfully from " + from);
                LoginSuccessfully loginSuccessfully = LoginSuccessfully.fromBytes(received);
                addTask(new AuthCompletedTask(loginSuccessfully, from));
                break;
            case 'i':
                logger.info("Received TCP user status info from " + from);
                UserStatusInfo userStatusInfo = UserStatusInfo.fromBytes(received);
                addTask(new UpdateFriendStatusTask(userStatusInfo, from));
                break;
        }
    }
}
