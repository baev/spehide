package ru.ifmo.baev.network.task;

import ru.ifmo.baev.network.Data;
import ru.ifmo.baev.network.client.ClientData;
import ru.ifmo.baev.network.message.CallDeny;
import ru.ifmo.baev.network.message.MessageContainer;
import ru.ifmo.baev.network.model.CallStatus;

import java.net.InetAddress;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class CallDenyTask extends Task<CallDeny> {

    public CallDenyTask(CallDeny message, InetAddress address, int port) {
        super(message, address, port);
    }

    @Override
    public MessageContainer process(Data data) throws Exception {
        if (!(data instanceof ClientData)) {
            throw new Exception("Data should be instance of ClientData");
        }

        ClientData clientData = (ClientData) data;

        switch (clientData.callStatus) {
            case CALL:
            case REQUEST:
                clientData.callStatus = CallStatus.NONE;
                break;
            case NONE:
            case CONVERSATION:
                break;
        }

        return null;
    }
}
