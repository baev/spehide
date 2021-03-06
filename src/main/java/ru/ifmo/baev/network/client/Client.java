package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.Config;
import ru.ifmo.baev.network.MessageProcessor;
import ru.ifmo.baev.network.TCPSender;
import ru.ifmo.baev.network.message.*;
import ru.ifmo.baev.network.task.Task;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class Client {

    private static final Config config = new Config();

    private ClientData data;

    public Client(ClientData data) {
        this.data = data;
    }

    private Queue<Task> tasks = new ConcurrentLinkedQueue<>();

    private Queue<MessageContainer> outgoing = new ConcurrentLinkedQueue<>();

    private List<Voice> incomingVoice = new ArrayList<>(Collections.<Voice>nCopies(10000, null));

    private AtomicLong lastVoiceNum = new AtomicLong(-1);

    private List<AbstractProcessor> processors = new ArrayList<>();

    private boolean running = false;

    public void login(String server, String login, String pass) throws UnknownHostException {
        LoginRequest message = new LoginRequest();
        message.setLogin(login);
        message.setPass(pass);
        outgoing.add(new MessageContainer<>(
                message,
                InetAddress.getByName(server),
                config.getServerTCPPort()
        ));
    }

    public void call(InetAddress address) {
        CallRequest message = new CallRequest();
        outgoing.add(new MessageContainer<>(
                message,
                address,
                config.getClientTCPPort()
        ));
    }

    public void allowCall() throws UnknownHostException {
        CallAllow message = new CallAllow();
        for (InetAddress address : data.callWith) {
            outgoing.add(new MessageContainer<>(
                    message,
                    address,
                    config.getClientTCPPort()
            ));
        }
    }

    public void denyCall() throws UnknownHostException {
        CallDeny message = new CallDeny();
        for (InetAddress address : data.callWith) {
            outgoing.add(new MessageContainer<>(
                    message,
                    address,
                    config.getClientTCPPort()
            ));
        }
    }

    public void start() throws IOException {
        if (running) {
            return;
        }
        running = true;
        processors.add(new ClientTCPReceiver(tasks));
        processors.add(new ClientUDPReceiver(data, tasks, incomingVoice, lastVoiceNum));
        processors.add(new MessageProcessor<>(data, tasks, outgoing));
        processors.add(new TCPSender(outgoing));
        processors.add(new AliveNotifier(data));
        processors.add(new VoiceRecorder(data));
        processors.add(new VoicePlayer(data, incomingVoice, lastVoiceNum));

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
