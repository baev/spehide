package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.Utils;
import ru.ifmo.baev.network.message.AliveNotification;
import ru.ifmo.baev.network.message.IsUserOnline;
import ru.ifmo.baev.network.model.ClientStatus;
import ru.ifmo.baev.network.model.FriendInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class AliveNotifier extends AbstractProcessor {

    private final Logger logger = LogManager.getLogger(getClass());

    private final long notificationDelay;

    private final ClientData data;

    private final int port;

    private final long onlineDelay;

    public AliveNotifier(ClientData data) {
        Config config = new Config();
        this.data = data;
        notificationDelay = config.getClientAliveNotifyDelay();
        port = config.getServerUDPPort();
        onlineDelay = config.getClientOnlineDelay();
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(notificationDelay));
                if (!data.authorized) {
                    continue;
                }
            } catch (InterruptedException e) {
                logger.error(e);
                continue;
            }

            for (InetAddress server : data.servers) {
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket();
                    sendAlive(socket, server);
                    for (String uid : data.friends.keySet()) {
                        FriendInfo friendInfo = data.friends.get(uid);
                        logger.info(String.format(
                                "Uid: %s, public key: %s",
                                uid,
                                friendInfo.getFriendToken()
                        ));

                        long delay = TimeUnit.SECONDS.toMillis(onlineDelay);
                        if (System.currentTimeMillis() - friendInfo.getLastNotificationTime() > delay) {
                            friendInfo.setStatus(ClientStatus.UNKNOWN);
                        }

                        String login = friendInfo.getLogin();
                        String loginSha256 = Utils.sha256String(login);
                        data.loginHashes.put(loginSha256, login);
                        logger.info(String.format(
                                "Sha256 for login %s: %s",
                                login,
                                loginSha256
                        ));

                        sendIsOnline(socket, server, login, friendInfo.getFriendToken());
                    }
                } catch (IOException | NoSuchAlgorithmException e) {
                    logger.error("Can't notify server " + server, e);
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                }
            }
        }
    }

    private void sendAlive(DatagramSocket socket, InetAddress server) throws IOException {
        AliveNotification message = new AliveNotification();
        message.setUid(data.uid);
        message.setToken(data.token);
        message.setTime(System.currentTimeMillis());

        DatagramPacket packet = new DatagramPacket(message.toBytes(), AliveNotification.SIZE, server, port);
        socket.send(packet);

        logger.info("Alive notification send to " + server);
    }

    private void sendIsOnline(DatagramSocket socket, InetAddress server, String login, String key) throws IOException, NoSuchAlgorithmException {
        IsUserOnline message = new IsUserOnline();
        message.setLogin(login);
        message.setKey(key);
        message.setLastNotificationTime(System.currentTimeMillis());

        DatagramPacket packet = new DatagramPacket(message.toBytes(), IsUserOnline.SIZE, server, port);
        socket.send(packet);

        logger.info("Is user online send to " + server);
    }
}
