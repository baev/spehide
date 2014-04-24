package ru.ifmo.baev.network.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.Data;
import ru.ifmo.baev.network.client.ClientData;
import ru.ifmo.baev.network.message.MessageContainer;
import ru.ifmo.baev.network.message.UserStatusInfo;
import ru.ifmo.baev.network.model.ClientStatus;
import ru.ifmo.baev.network.model.FriendInfo;

import java.net.InetAddress;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class UpdateFriendStatusTask extends Task<UserStatusInfo> {

    private final Logger logger = LogManager.getLogger(getClass());

    public UpdateFriendStatusTask(UserStatusInfo message, InetAddress address, int port) {
        super(message, address, port);
    }

    @Override
    public MessageContainer process(Data data) throws Exception {
        if (!(data instanceof ClientData)) {
            throw new Exception("Data should be instance of ClientData");
        }

        InetAddress address = getContainer().getMessage().getAddress();

        ClientData clientData = (ClientData) data;

        String loginSha256 = getContainer().getMessage().getLogin();
        String login = clientData.loginHashes.get(loginSha256);
        if (login == null) {
            logger.info("Can't find login for sha256 " + loginSha256);
            return null;
        }

        FriendInfo info = clientData.friends.get(login);

        if (info == null) {
            logger.info("Can't find info for login " + login);
            return null;
        }
        info.setStatus(ClientStatus.ONLINE);
        info.setLastNotificationTime(System.currentTimeMillis());
        info.setAddress(address);

        return null;
    }
}
