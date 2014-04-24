package ru.ifmo.baev.network;

import ru.ifmo.baev.network.client.ClientData;

import static ru.ifmo.baev.network.model.CallStatus.CONVERSATION;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public abstract class AbstractVoiceProcessor extends AbstractProcessor {

    private ClientData data;

    protected AbstractVoiceProcessor(ClientData data) {
        this.data = data;
    }

    @Override
    public void run() {
        while (isRunning()) {
            waitStartOfConversation();
            try {
                before();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            while (isRunning() && CONVERSATION.equals(data.callStatus)) {
                process();
            }
            after();
        }
    }

    protected void waitStartOfConversation() {
        while (!CONVERSATION.equals(data.callStatus)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void before() throws Exception;

    protected abstract void process();

    protected abstract void after();


}
