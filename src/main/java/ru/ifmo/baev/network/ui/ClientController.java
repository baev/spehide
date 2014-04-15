package ru.ifmo.baev.network.ui;

import ru.ifmo.baev.network.client.Client;
import ru.ifmo.baev.network.model.ClientStatus;
import ru.ifmo.baev.network.model.FriendInfo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

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

                String uid = panel.getUidField().getText();
                String friendsToken = panel.getFriendsTokenField().getText();

                if (option == JOptionPane.OK_OPTION && !uid.isEmpty() && !friendsToken.isEmpty()) {
                    client.getData().friends.put(uid, new FriendInfo()
                                    .withUid(uid)
                                    .withFriendToken(friendsToken)
                                    .withStatus(ClientStatus.UNKNOWN)
                    );
                    System.out.println(uid + ": " + friendsToken);
                    view.updateFriendsList();
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
                    view.getListModel().remove(view.getContactList().getSelectedIndex());
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
                System.out.println(Arrays.toString(view.getContactList().getSelectedIndices()));
            }
        });


        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.updateFriendsList();

            }
        });
        timer.start();
    }

}
