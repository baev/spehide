package ru.ifmo.baev.network.client;

import ru.ifmo.baev.network.Data;
import ru.ifmo.baev.network.model.FriendInfo;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.04.14
 */
public class ClientData implements Data {

    public final Map<String, FriendInfo> friends = new HashMap<>();

    public final Map<String, String> loginHashes = new HashMap<>();

    public String uid;

    public String token;

    public String friendsToken;

    public boolean authorized = false;

    public Set<InetAddress> servers = new HashSet<>();
}
