package ru.ifmo.baev.network;

import ru.ifmo.baev.network.message.Message;
import ru.ifmo.baev.network.message.MessageContainer;

import java.net.InetAddress;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public abstract class Task<T extends Message> {

    private MessageContainer<T> container;

    public Task(T message, InetAddress address) {
        this.container = new MessageContainer<>(message, address);
    }

    public abstract MessageContainer process(Data data) throws Exception;

    public MessageContainer<T> getContainer() {
        return container;
    }
}
