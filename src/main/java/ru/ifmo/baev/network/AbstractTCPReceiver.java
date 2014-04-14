package ru.ifmo.baev.network;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.04.14
 */
public abstract class AbstractTCPReceiver extends AbstractProcessor {

    private final ServerSocket serverSocket;

    private final Queue<Task> tasks;

    public AbstractTCPReceiver(Queue<Task> tasks) throws IOException {
        this.tasks = tasks;
        this.serverSocket = new ServerSocket(getPort());
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
                Socket socket = serverSocket.accept();
                String address = socket.getInetAddress().toString();

                byte[] bytes = IOUtils.toByteArray(socket.getInputStream());
                process(bytes, address);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public abstract int getPort();

    public abstract void process(byte[] received, String from);

    public void addTask(Task task) {
        getTasks().add(task);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Queue<Task> getTasks() {
        return tasks;
    }
}
