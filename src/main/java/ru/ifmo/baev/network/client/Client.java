package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.MessageProcessor;
import ru.ifmo.baev.network.task.Task;
import ru.ifmo.baev.network.message.CallRequest;
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

    private Config config = new Config();

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
        outgoing.add(new MessageContainer<>(
                request,
                InetAddress.getByName(server),
                config.getServerTCPPort()
        ));
    }

    public void call(String address) throws UnknownHostException {
        CallRequest request = new CallRequest();
        outgoing.add(new MessageContainer<>(
                request,
                InetAddress.getByName(address),
                config.getClientTCPPort()
        ));
    }

    public void start() throws IOException {
        if (running) {
            return;
        }
        running = true;
        processors.add(new ClientTCPReceiver(tasks));
        processors.add(new ClientUDPReceiver(tasks));
        processors.add(new MessageProcessor<>(data, tasks, outgoing));
        processors.add(new ClientSender(outgoing));
        processors.add(new AliveNotifier(data));

        for (AbstractProcessor processor : processors) {
            processor.start();
            new Thread(processor).start();
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
