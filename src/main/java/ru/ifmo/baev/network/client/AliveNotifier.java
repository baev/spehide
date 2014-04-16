package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.VOIPConfig;
import ru.ifmo.baev.network.message.AliveNotification;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class AliveNotifier extends AbstractProcessor {

    private final Logger logger = LogManager.getLogger(getClass());

    private final long delay;

    private final ClientData data;

    private final int port;

    public AliveNotifier(ClientData data) {
        VOIPConfig config = new VOIPConfig();
        this.data = data;
        delay = config.getClientAliveNotifyDelay();
        port = config.getServerUDPPort();
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(delay));
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
                    AliveNotification message = new AliveNotification();
                    message.setUid(data.uid);
                    message.setToken(data.token);
                    message.setTime(System.currentTimeMillis());
                    DatagramPacket packet = new DatagramPacket(message.toBytes(), AliveNotification.SIZE, server, port);
                    socket.send(packet);
                    logger.info("Alive notification send to " + server);
                } catch (IOException e) {
                    logger.error("Can't notify server " + server, e);
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                }
            }


        }
    }
}
