package ru.ifmo.baev.network.ui;

import ru.ifmo.baev.network.client.Client;
import ru.ifmo.baev.network.client.ClientData;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class Main {

    public static void main(String[] args) {
        Client client = new Client(new ClientData());
        ClientView view = new ClientView(client);
        ClientController controller = new ClientController(client, view);

        view.setVisible(true);
    }
}
