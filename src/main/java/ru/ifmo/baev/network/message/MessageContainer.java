package ru.ifmo.baev.network.message;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class MessageContainer<T extends Message> {
    private T message;

    private String address;

    public MessageContainer(T message, String address) {
        this.message = message;
        this.address = address;
    }

    public T getMessage() {
        return message;
    }

    public String getAddress() {
        return address;
    }
}
