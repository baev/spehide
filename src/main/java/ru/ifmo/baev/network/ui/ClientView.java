package ru.ifmo.baev.network.ui;

import ru.ifmo.baev.network.client.Client;
import ru.ifmo.baev.network.model.CallStatus;
import ru.ifmo.baev.network.model.FriendInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;

public class ClientView extends JFrame {

    private static final String FRAME_NAME = "Contact list";

    private static final String CALL_BUTTON_NAME = "Call";

    private static final String DELETE_BUTTON_NAME = "Delete";

    private static final String ADD_BUTTON_NAME = "Add";

    private static final String DROP_CALL_BUTTON_NAME = "Drop";

    private Client client;

    private JPanel mainPanel;

    private JList contactList;

    private DefaultListModel<String> listModel;

    private JScrollPane scrollPane;

    private JPanel buttonsPanel;

    private JButton callButton;

    private JButton dropButton;

    private JButton addButton;

    private JButton deleteButton;

    private AddFriendPanel addFriendPanel = new AddFriendPanel();

    private LoginPanel loginPanel = new LoginPanel();

    private JTextArea publicKey;

    public ClientView(Client client) {
        super(FRAME_NAME);
        this.client = client;

        if (!login()) {
            return;
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        publicKey = createPublicKey();
        callButton = createCallButton();
        deleteButton = createDeleteButton();
        addButton = createAddButton();
        dropButton = createDropButton();
        buttonsPanel = createButtonsPanel();
        listModel = createListModel();
        contactList = createContactList();
        scrollPane = new JScrollPane(contactList);
        mainPanel = createMainPanel();

        getContentPane().add(mainPanel);
        setPreferredSize(new Dimension(340, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean login() {
        int option = JOptionPane.showConfirmDialog(
                null,
                loginPanel.getPanel(),
                "Auth",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        String login = loginPanel.getLoginField().getText();
        String pass = loginPanel.getPassField().getText();
        String server = loginPanel.getServerField().getText();
        if (option != JOptionPane.OK_OPTION || server.isEmpty() || login.isEmpty() || pass.isEmpty()) {
            return false;
        }

        try {
            this.client.login(server, login, pass);
            this.client.start();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(publicKey, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        return panel;
    }

    public void updateFriendsList() {
        int[] selected = contactList.getSelectedIndices();
        listModel.setSize(0);
        for (String uid : client.getData().friends.keySet()) {
            FriendInfo info = client.getData().friends.get(uid);
            addFriend(listModel, info);
        }
        contactList.setSelectedIndices(selected);
    }

    public void updatePublicKey() {
        if (client.getData().authorized) {
            String current = publicKey.getText();
            if (!current.contains(client.getData().friendsToken)) {
                publicKey.setText(String.format(
                        "online\nuid: %s\nkey: %s",
                        client.getData().uid,
                        client.getData().friendsToken
                ));
            }
        }
    }

    public void checkCallStatus() {
        CallStatus status = client.getData().callStatus;
        switch (status) {
            case NONE:
                getCallButton().setEnabled(true);
                getDropButton().setEnabled(false);
                return;
            case CALL:
            case CONVERSATION:
                getCallButton().setEnabled(false);
                getDropButton().setEnabled(true);
                return;
            case REQUEST:
                getCallButton().setEnabled(false);
                getDropButton().setEnabled(true);
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Start new conversation?",
                        "New incoming call...",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                try {
                    if (option == JOptionPane.OK_OPTION) {
                        client.allowCall();
                        client.getData().callStatus = CallStatus.CONVERSATION;
                    } else {
                        client.denyCall();
                        client.getData().callStatus = CallStatus.NONE;
                        client.getData().callWith.clear();
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
        }
    }

    private void addFriend(DefaultListModel<String> listModel, FriendInfo info) {
        listModel.addElement(String.format("%s: %s", info.getStatus(), info.getLogin()));
    }

    private JList<String> createContactList() {
        JList<String> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }

    private DefaultListModel<String> createListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String uid : client.getData().friends.keySet()) {
            FriendInfo info = client.getData().friends.get(uid);
            addFriend(listModel, info);
        }
        return listModel;
    }

    public JTextArea createPublicKey() {
        JTextArea pane = new JTextArea();
        pane.setText("offline");
        pane.setEditable(false);
        pane.setBorder(null);
        pane.setBackground(null);
        return pane;
    }

    public JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));

        buttonsPanel.add(callButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(dropButton);

        return buttonsPanel;
    }

    public JButton createCallButton() {
        JButton button = new JButton(CALL_BUTTON_NAME);
        button.setFocusable(false);
        button.setEnabled(false);
        return button;
    }

    public JButton createDropButton() {
        JButton button = new JButton(DROP_CALL_BUTTON_NAME);
        button.setFocusable(false);
        button.setEnabled(false);
        return button;
    }

    public JButton createDeleteButton() {
        final JButton button = new JButton(DELETE_BUTTON_NAME);
        button.setEnabled(false);
        button.setFocusable(false);
        return button;
    }

    public JButton createAddButton() {
        JButton button = new JButton(ADD_BUTTON_NAME);
        button.setFocusable(false);
        return button;
    }

    public AddFriendPanel getAddFriendPanel() {
        return addFriendPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JList getContactList() {
        return contactList;
    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

    public JButton getCallButton() {
        return callButton;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public class AddFriendPanel {
        private JTextField loginField = new JTextField();
        private JTextField publicKeyField = new JTextField();
        private JPanel panel;

        public AddFriendPanel() {
            panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("User login:"));
            panel.add(loginField);
            panel.add(new JLabel("User public key:"));
            panel.add(publicKeyField);
        }

        public JTextField getLoginField() {
            return loginField;
        }

        public JTextField getPublicKeyField() {
            return publicKeyField;
        }

        public JPanel getPanel() {
            return panel;
        }
    }

    public class LoginPanel {
        private JTextField loginField = new JTextField();
        private JTextField passField = new JTextField();
        private JTextField serverField = new JTextField();
        private JPanel panel;

        public LoginPanel() {
            serverField.setText("localhost");
            panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Server:"));
            panel.add(serverField);
            panel.add(new JLabel("Login:"));
            panel.add(loginField);
            panel.add(new JLabel("Password:"));
            panel.add(passField);
        }

        public JTextField getLoginField() {
            return loginField;
        }

        public JTextField getPassField() {
            return passField;
        }

        public JTextField getServerField() {
            return serverField;
        }

        public JPanel getPanel() {
            return panel;
        }
    }

    public JButton getDropButton() {
        return dropButton;
    }
}