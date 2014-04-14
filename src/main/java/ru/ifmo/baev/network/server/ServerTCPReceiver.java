package ru.ifmo.baev.network.server;

import ru.ifmo.baev.network.AbstractTCPReceiver;
import ru.ifmo.baev.network.Task;
import ru.ifmo.baev.network.VOIPConfig;
import ru.ifmo.baev.network.message.LoginRequest;
import ru.ifmo.baev.network.message.MessageFactory;

import java.io.IOException;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class ServerTCPReceiver extends AbstractTCPReceiver {

    private final int port;

    public ServerTCPReceiver(Queue<Task> tasks) throws IOException {
        super(tasks);
        this.port = new VOIPConfig().getServerTCPPort();
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void process(byte[] received, String from) {
        char messageType = (char) received[0];
        switch (messageType) {
            case 'l':
                LoginRequest message = MessageFactory.createLoginRequest(received);
                addTask(new LoginTask(message, from));
                break;
            case 'o':
                break;
        }
    }
}
