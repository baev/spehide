package ru.ifmo.baev.network;

import ru.ifmo.baev.network.message.Message;
import ru.ifmo.baev.network.message.MessageContainer;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public abstract class Task<T extends Message> {

    private MessageContainer<T> container;

    protected Task(MessageContainer<T> container) {
        this.container = container;
    }

    public abstract MessageContainer process(Data data) throws Exception;

    public MessageContainer<T> getContainer() {
        return container;
    }
}
