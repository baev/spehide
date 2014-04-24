package ru.ifmo.baev.network.ui;

import ru.ifmo.baev.network.client.Client;
import ru.ifmo.baev.network.model.CallStatus;
import ru.ifmo.baev.network.model.ClientStatus;
import ru.ifmo.baev.network.model.FriendInfo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class ClientController {

    private final Client client;

    private final ClientView view;

    public ClientController(final Client client, final ClientView view) {
        this.client = client;
        this.view = view;

        view.getAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientView.AddFriendPanel panel = view.getAddFriendPanel();

                int option = JOptionPane.showConfirmDialog(
                        null,
                        panel.getPanel(),
                        "Add friend",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                String login = panel.getLoginField().getText();
                String publicKey = panel.getPublicKeyField().getText();

                if (option == JOptionPane.OK_OPTION && !login.isEmpty() && !publicKey.isEmpty()) {
                    client.getData().friends.put(login, new FriendInfo()
                                    .withLogin(login)
                                    .withFriendToken(publicKey)
                                    .withStatus(ClientStatus.UNKNOWN)
                                    .withLastNotificationTime(System.currentTimeMillis())
                    );

                    System.out.println(login + ": " + publicKey);
                    view.updateFriendsList();

                    panel.getLoginField().setText("");
                    panel.getPublicKeyField().setText("");
                }
            }
        });

        view.getDeleteButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String toDelete = (String) view.getContactList().getSelectedValue();
                int option = JOptionPane.showConfirmDialog(view.getContactList(), String.format(
                        "Delete user %s from contact list?",
                        toDelete
                ));

                if (option == JOptionPane.OK_OPTION) {
                    String uid = getSelectedUserUid();
                    if (uid != null) {
                        client.getData().friends.remove(uid);
                        view.getListModel().remove(view.getContactList().getSelectedIndex());
                    }
                }
            }
        });

        view.getContactList().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (view.getContactList().getSelectedIndex() >= 0) {
                            view.getDeleteButton().setEnabled(true);
                        } else {
                            view.getDeleteButton().setEnabled(false);
                        }
                    }
                }
        );

        view.getCallButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.getData().callStatus = CallStatus.REQUEST;
                String uid = getSelectedUserUid();
                if (uid == null) {
                    return;
                }

                FriendInfo info = client.getData().friends.get(uid);
                if (info == null) {
                    return;
                }

                String address = info.getAddress();
                if (address == null) {
                    return;
                }

                try {
                    client.call(address);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }
            }
        });


        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.updateFriendsList();
                view.updatePublicKey();

            }
        });
        timer.start();
    }

    private String getSelectedUserUid() {
        String toDelete = (String) view.getContactList().getSelectedValue();
        for (String uid : client.getData().friends.keySet()) {
            if (toDelete.contains(uid)) {
                return uid;
            }
        }
        return null;
    }
}
