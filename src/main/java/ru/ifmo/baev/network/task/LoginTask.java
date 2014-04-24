package ru.ifmo.baev.network.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.Data;
import ru.ifmo.baev.network.message.LoginRequest;
import ru.ifmo.baev.network.message.LoginSuccessfully;
import ru.ifmo.baev.network.message.MessageContainer;
import ru.ifmo.baev.network.model.ClientAuth;
import ru.ifmo.baev.network.server.ServerData;

import java.net.InetAddress;
import java.util.UUID;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class LoginTask extends Task<LoginRequest> {

    private final Logger logger = LogManager.getLogger(getClass());

    private static final Object LOCK = new Object();

    public LoginTask(LoginRequest message, InetAddress address, int port) {
        super(message, address, port);
    }

    @Override
    public MessageContainer process(Data d) throws Exception {
        if (!(d instanceof ServerData)) {
            throw new Exception("Login failed");
        }

        ServerData data = (ServerData) d;
        String login = getContainer().getMessage().getLogin();
        String pass = getContainer().getMessage().getPass();
        InetAddress address = getContainer().getAddress();

        if (data.getAuthData().containsKey(login)) {
            ClientAuth clientAuth = data.getAuthData().get(login);
            if (clientAuth.getPass().equals(pass)) {
                return loginSuccessfully(address, clientAuth);
            } else {
                throw new Exception("Login failed");
            }
        } else {
            ClientAuth clientAuth = createClientAuthData(pass);
            synchronized (LOCK) {
                logger.info(String.format(
                        "Added new auth data login: %s, uid: %s, public key: %s",
                        login,
                        clientAuth.getUid(),
                        clientAuth.getFriendsToken()
                ));
                data.getAuthData().put(login, clientAuth);
            }
            return loginSuccessfully(address, clientAuth);
        }
    }

    private MessageContainer loginSuccessfully(InetAddress address, ClientAuth auth) {
        LoginSuccessfully message = new LoginSuccessfully();
        message.setUid(auth.getUid());
        message.setToken(auth.getToken());
        message.setFriendsToken(auth.getFriendsToken());
        return new MessageContainer<>(
                message,
                address,
                getContainer().getPort()
        );
    }

    private ClientAuth createClientAuthData(String pass) {
        return new ClientAuth()
                .withPass(pass)
                .withToken(UUID.randomUUID().toString())
                .withFriendsToken(UUID.randomUUID().toString())
                .withUid(UUID.randomUUID().toString());
    }
}
