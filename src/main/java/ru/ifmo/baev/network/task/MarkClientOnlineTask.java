package ru.ifmo.baev.network.task;

import ru.ifmo.baev.network.Data;
import ru.ifmo.baev.network.message.AliveNotification;
import ru.ifmo.baev.network.message.MessageContainer;
import ru.ifmo.baev.network.model.ClientInfo;
import ru.ifmo.baev.network.server.ServerData;

import java.net.InetAddress;
import java.util.Map;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class MarkClientOnlineTask extends Task<AliveNotification> {

    public MarkClientOnlineTask(AliveNotification message, InetAddress address, int port) {
        super(message, address, port);
    }

    @Override
    public MessageContainer process(Data data) throws Exception {
        if (!(data instanceof ServerData)) {
            throw new Exception("Data should be instance of ServerData");
        }

        Map<String, ClientInfo> clients = ((ServerData) data).getClients();
        String uid = getContainer().getMessage().getUid();
        long time = getContainer().getMessage().getTime();

        ClientInfo info = new ClientInfo()
                .withAddress(getContainer().getAddress().getHostAddress())
                .withLastNotificationTime(time)
                .withUid(uid);
        clients.put(uid, info);

        return null;
    }
}
