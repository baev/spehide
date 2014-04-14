package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.AbstractProcessor;
import ru.ifmo.baev.network.MessageProcessor;
import ru.ifmo.baev.network.Task;
import ru.ifmo.baev.network.message.MessageContainer;
import ru.ifmo.baev.network.model.ClientStatus;
import ru.ifmo.baev.network.model.FriendInfo;
import ru.ifmo.baev.network.server.ServerSender;
import ru.ifmo.baev.network.server.ServerTCPReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public void addFriend(String uid, String friendToken) {
        data.friends.put(uid, new FriendInfo()
                        .withUid(uid)
                        .withFriendToken(friendToken)
                        .withStatus(ClientStatus.OFFLINE)
        );
    }

    public void removeFriend(String uid) {
        data.friends.remove(uid);
    }

    public Map<String, FriendInfo> getFriends() {
        return data.friends;
    }

}
