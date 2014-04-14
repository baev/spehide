package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.Data;
import ru.ifmo.baev.network.model.FriendAddress;
import ru.ifmo.baev.network.model.FriendInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.04.14
 */
public class ClientData implements Data {

    public final Map<String, FriendAddress> addresses = new HashMap<>();

    public final Map<String, FriendInfo> friends = new HashMap<>();

}
