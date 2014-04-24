package ru.ifmo.baev.network.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.Data;
import ru.ifmo.baev.network.message.IsUserOnline;
import ru.ifmo.baev.network.message.MessageContainer;
import ru.ifmo.baev.network.message.UserStatusInfo;
import ru.ifmo.baev.network.model.ClientAuth;
import ru.ifmo.baev.network.model.ClientInfo;
import ru.ifmo.baev.network.server.ServerData;

import java.net.InetAddress;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class IsClientOnlineTask extends Task<IsUserOnline> {

    private final Logger logger = LogManager.getLogger(getClass());

    public IsClientOnlineTask(IsUserOnline message, InetAddress address, int port) {
        super(message, address, port);
    }

    @Override
    public MessageContainer process(Data data) throws Exception {
        if (!(data instanceof ServerData)) {
            throw new Exception("Data should be instance of ServerData");
        }

        ServerData serverData = (ServerData) data;
        String login = getContainer().getMessage().getLogin();
        String key = getContainer().getMessage().getKey();
        long time = getContainer().getMessage().getLastNotificationTime();

        ClientAuth clientAuth = serverData.getAuthData().get(login);

        if (clientAuth == null) {
            logger.info("IsUserOnline - There are no info about client " + login);
            return null;
        }

        if (!clientAuth.getFriendsToken().equals(key)) {
            logger.info(String.format(
                    "IsUserOnline - Public key check failed\nFor user %s: excepted %s but was %s",
                    login,
                    clientAuth.getFriendsToken(),
                    key
            ));
            return null;
        }

        ClientInfo clientInfo = serverData.getClients().get(clientAuth.getUid());

        if (clientInfo == null) {
            logger.info("IsUserOnline - There are no info about " + login);
            return null;
        }

        if (time == clientInfo.getLastNotificationTime()) {
            logger.info("IsUserOnline - skipped");
            return null;
        }

        logger.info("IsUserOnline - Success");

        UserStatusInfo userStatusInfo = new UserStatusInfo();
        userStatusInfo.setAddress(clientInfo.getAddress());
        userStatusInfo.setLogin(login);
        userStatusInfo.setLastNotificationTime(clientInfo.getLastNotificationTime());

        return new MessageContainer<>(
                userStatusInfo,
                getContainer().getAddress(),
                getContainer().getPort()
        );
    }

}
