package ru.ifmo.baev.network.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.client.Client;
import ru.ifmo.baev.network.model.CallStatus;
import ru.ifmo.baev.network.model.ClientStatus;
import ru.ifmo.baev.network.model.FriendInfo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.14
 */
public class ClientController {

    private final Logger logger = LogManager.getLogger(getClass());

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

        view.getDropButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.getData().callWith.clear();
                client.getData().callStatus = CallStatus.NONE;
            }
        });

        view.getContactList().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (view.getContactList().getSelectedIndex() >= 0) {
                            view.getDeleteButton().setEnabled(true);
                            if (client.getData().callStatus.equals(CallStatus.NONE)) {
                                view.getCallButton().setEnabled(true);
                            }
                        } else {
                            view.getDeleteButton().setEnabled(false);
                            view.getCallButton().setEnabled(false);
                        }
                    }
                }
        );

        view.getCallButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.info("Try to call... Call status - " + client.getData().callStatus);
                if (client.getData().callStatus != CallStatus.NONE) {
                    return;
                }
                client.getData().callStatus = CallStatus.CALL;
                view.getCallButton().setEnabled(false);

                String uid = getSelectedUserUid();
                if (uid == null) {
                    logger.info("Uid is null");
                    client.getData().callStatus = CallStatus.NONE;
                    return;
                }

                FriendInfo info = client.getData().friends.get(uid);
                if (info == null) {
                    logger.info("Info is null");
                    client.getData().callStatus = CallStatus.NONE;
                    return;
                }

                InetAddress address = info.getAddress();
                if (address == null) {
                    logger.info("Address is null");
                    client.getData().callStatus = CallStatus.NONE;
                    return;
                }

                logger.info("Call to address: " + address);
                client.call(address);
                client.getData().callWith.add(address);
            }
        });


        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.updateFriendsList();
                view.updatePublicKey();
                view.checkCallStatus();

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
