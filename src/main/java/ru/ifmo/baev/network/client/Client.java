package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.MessageProcessor;
import ru.ifmo.baev.network.Task;
import ru.ifmo.baev.network.message.LoginRequest;
import ru.ifmo.baev.network.message.MessageContainer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class Client {

    private ClientData data;

    public Client(ClientData data) {
        this.data = data;
    }

    private Queue<Task> tasks = new ConcurrentLinkedQueue<>();

    private Queue<MessageContainer> outgoing = new ConcurrentLinkedQueue<>();

    private List<AbstractProcessor> processors = new ArrayList<>();

    private boolean running = false;

    public void login(String server, String login, String pass) throws UnknownHostException {
        LoginRequest request = new LoginRequest();
        request.setLogin(login);
        request.setPass(pass);
        outgoing.add(new MessageContainer<>(request, InetAddress.getByName(server)));
    }

    public void start() throws IOException {
        if (running) {
            return;
        }
        running = true;
        processors.add(new ClientTCPReceiver(tasks));
        processors.add(new MessageProcessor<>(data, tasks, outgoing));
        processors.add(new ClientSender(outgoing));
        processors.add(new AliveNotifier(data));

        for (AbstractProcessor processor : processors) {
            new Thread(processor).start();
            processor.start();
        }
    }

    public void stop() {
        if (!running) {
            return;
        }

        for (AbstractProcessor processor : processors) {
            processor.stop();
        }
        processors.clear();
        running = false;
    }

    public ClientData getData() {
        return data;
    }
}
