package ru.ifmo.baev.network.server;

import ru.ifmo.baev.network.Data;
import ru.ifmo.baev.network.model.ClientAuth;
import ru.ifmo.baev.network.model.ClientInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class ServerData implements Data {

    private final Map<String, ClientAuth> authData = new HashMap<>();

    private final Map<String, ClientInfo> clients = new HashMap<>();

    public Map<String, ClientAuth> getAuthData() {
        return authData;
    }

    public Map<String, ClientInfo> getClients() {
        return clients;
    }
}
