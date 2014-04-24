package ru.ifmo.baev.network.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.MessageProcessor;
import ru.ifmo.baev.network.TCPSender;
import ru.ifmo.baev.network.task.Task;
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

    private final Logger logger = LogManager.getLogger(getClass());

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
        logger.info("Server started");
        running = true;
        processors.add(new ServerTCPReceiver(tasks));
        processors.add(new ServerUDPReceiver(tasks));
        processors.add(new MessageProcessor<>(data, tasks, outgoing));
        processors.add(new TCPSender(outgoing));

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
        logger.info("Server finished...");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server();
        server.start();
        Thread.sleep(3000000);
        server.stop();
    }
}
