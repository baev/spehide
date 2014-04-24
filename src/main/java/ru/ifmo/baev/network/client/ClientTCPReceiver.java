package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractTCPReceiver;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.message.LoginSuccessfully;
import ru.ifmo.baev.network.message.UserStatusInfo;
import ru.ifmo.baev.network.task.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Queue;

import static ru.ifmo.baev.network.message.Prefix.*;

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
    public void process(byte[] received, InetAddress from, int port) {
        char messageType = (char) received[0];
        switch (messageType) {
            case LOGIN_SUCCESSFULLY:
                logger.info("Received TCP login successfully from " + from);
                LoginSuccessfully loginSuccessfully = LoginSuccessfully.fromBytes(received);
                addTask(new AuthCompletedTask(loginSuccessfully, from, port));
                break;
            case USER_STATUS_INFO:
                logger.info("Received TCP user status info from " + from);
                UserStatusInfo userStatusInfo = UserStatusInfo.fromBytes(received);
                addTask(new UpdateFriendStatusTask(userStatusInfo, from, port));
                break;
            case CALL_REQUEST:
                logger.info("Received TCP call request from " + from);
                addTask(new CallRequestTask(null, from, port));
                break;
            case CALL_DENY:
                logger.info("Received TCP call deny from " + from);
                addTask(new CallDenyTask(null, from, port));
                break;
            case CALL_ALLOW:
                logger.info("Received TCP call allow from " + from);
                addTask(new CallAllowTask(null, from, port));
                break;
        }
    }
}
