package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractTCPReceiver;
import ru.ifmo.baev.network.Task;
import ru.ifmo.baev.network.VOIPConfig;
import ru.ifmo.baev.network.message.LoginSuccessfully;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 15.04.14
 */
public class ClientTCPReceiver extends AbstractTCPReceiver {

    private final int port;

    public ClientTCPReceiver(Queue<Task> tasks) throws IOException {
        super(tasks);
        this.port = new VOIPConfig().getClientTCPPort();
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
                LoginSuccessfully message = LoginSuccessfully.fromBytes(received);
                addTask(new AuthCompletedTask(message, from));
                break;
            case 'o':
                break;
        }
    }
}
