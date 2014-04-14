package ru.ifmo.baev.network.server;

import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.MessageProcessor;
import ru.ifmo.baev.network.Task;
import ru.ifmo.baev.network.message.MessageContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class Server {

    private ServerData data;

    private Queue<Task> tasks = new ConcurrentLinkedQueue<>();

    private Queue<MessageContainer> outgoing = new ConcurrentLinkedQueue<>();

    private List<AbstractProcessor> processors = new ArrayList<>();

    private boolean running = false;

    public Server() {
        this.data = new ServerData();
    }

    public Server(ServerData data) {
        this.data = data;
    }

    public void start() throws IOException {
        if (running) {
            return;
        }
        running = true;
        processors.add(new ServerTCPReceiver(tasks));
        processors.add(new MessageProcessor<>(data, tasks, outgoing));
        processors.add(new ServerSender(outgoing));

        for (AbstractProcessor processor : processors) {
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

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server();
        server.start();
        Thread.sleep(2000);
        server.stop();
    }
}
