package ru.ifmo.baev.network;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.task.Task;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.04.14
 */
public abstract class AbstractTCPReceiver extends AbstractProcessor {

    private final Logger logger = LogManager.getLogger(getClass());

    private final Queue<Task> tasks;

    public AbstractTCPReceiver(Queue<Task> tasks) throws IOException {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
                ServerSocket serverSocket = new ServerSocket(getPort());
                Socket socket = serverSocket.accept();
                InetAddress address = socket.getInetAddress();
                int port = socket.getPort();

                logger.info(String.format(
                        "%s receive tcp message from %s",
                        getClass().getSimpleName(),
                        address
                ));

                byte[] bytes = IOUtils.toByteArray(socket.getInputStream());
                process(bytes, address, port);
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public abstract int getPort();

    public abstract void process(byte[] received, InetAddress from, int port) throws UnknownHostException;

    public void addTask(Task task) {
        getTasks().add(task);
    }

    public Queue<Task> getTasks() {
        return tasks;
    }
}
