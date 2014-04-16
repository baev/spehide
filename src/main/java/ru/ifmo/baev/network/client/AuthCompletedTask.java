package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.Data;
import ru.ifmo.baev.network.Task;
import ru.ifmo.baev.network.message.LoginSuccessfully;
import ru.ifmo.baev.network.message.MessageContainer;

import java.net.InetAddress;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 15.04.14
 */
public class AuthCompletedTask extends Task<LoginSuccessfully> {

    private final Logger logger = LogManager.getLogger(getClass());

    public AuthCompletedTask(LoginSuccessfully message, InetAddress address) {
        super(message, address);
    }

    @Override
    public MessageContainer process(Data data) throws Exception {
        if (!(data instanceof ClientData)) {
            throw new Exception("Data should be instance of ClientData");
        }

        ClientData clientData = (ClientData) data;
        clientData.uid = getContainer().getMessage().getUid();
        clientData.token = getContainer().getMessage().getToken();
        clientData.friendsToken = getContainer().getMessage().getFriendsToken();
        clientData.authorized = true;
        clientData.servers.add(getContainer().getAddress());
        logger.info("Client auth completed successfully");
        return null;
    }
}
