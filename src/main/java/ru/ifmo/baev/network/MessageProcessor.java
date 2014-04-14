package ru.ifmo.baev.network;

import ru.ifmo.baev.network.message.MessageContainer;

import java.util.Queue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class MessageProcessor<T extends Data> extends AbstractProcessor {

    private static final long CHECK_TIMEOUT = 1000;

    private final Queue<Task> tasks;

    private final T data;

    private final Queue<MessageContainer> outgoing;

    public MessageProcessor(T data, Queue<Task> tasks, Queue<MessageContainer> outgoing) {
        this.tasks = tasks;
        this.data = data;
        this.outgoing = outgoing;
    }

    @Override
    public void run() {
        start();
        while (isRunning()) {
            try {
                if (tasks.isEmpty()) {
                    Thread.sleep(CHECK_TIMEOUT);
                    continue;
                }

                Task task = tasks.poll();
                outgoing.add(task.process(data));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
