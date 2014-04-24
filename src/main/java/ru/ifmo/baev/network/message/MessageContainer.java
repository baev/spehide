package ru.ifmo.baev.network.message;

import java.net.InetAddress;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class MessageContainer<T extends Message> {
    private T message;

    private InetAddress address;

    private int port;

    public MessageContainer(T message, InetAddress address, int port) {
        this.message = message;
        this.address = address;
        this.port = port;
    }

    public T getMessage() {
        return message;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
