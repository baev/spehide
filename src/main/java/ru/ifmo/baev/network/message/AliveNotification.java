package ru.ifmo.baev.network.message;

import ru.ifmo.baev.network.model.AbstractAliveNotification;

import java.nio.charset.Charset;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class AliveNotification extends AbstractAliveNotification {
    @Override
    public byte[] toBytes() {
        return uid.getBytes(Charset.forName("UTF-8"));
    }
}
