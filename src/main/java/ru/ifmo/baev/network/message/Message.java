package ru.ifmo.baev.network.message;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public interface Message {

    public byte[] toBytes() throws Exception;
}
