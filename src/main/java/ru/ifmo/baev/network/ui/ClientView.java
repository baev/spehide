package ru.ifmo.baev.network.ui;

import ru.ifmo.baev.network.client.Client;
import ru.ifmo.baev.network.model.FriendInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientView extends JFrame {

    private static final String FRAME_NAME = "Contact list";

    private static final String CALL_BUTTON_NAME = "Call";

    private static final String DELETE_BUTTON_NAME = "Delete";

    private static final String ADD_BUTTON_NAME = "Add";

    private Client client;

    private JPanel mainPanel;

    private JList contactList;

    private DefaultListModel<String> listModel;

    private JScrollPane scrollPane;

    private JPanel buttonsPanel;

    private JButton callButton;

    private JButton addButton;

    private JButton deleteButton;

    private AddFriendPanel addFriendPanel = new AddFriendPanel();

    private LoginPanel loginPanel = new LoginPanel();

    public ClientView(Client client) {
        super(FRAME_NAME);
        this.client = client;

        if (!login()) {
            return;
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        callButton = createCallButton();
        deleteButton = createDeleteButton();
        addButton = createAddButton();
        buttonsPanel = createButtonsPanel();
        listModel = createListModel();
        contactList = new JList<>(listModel);
        scrollPane = new JScrollPane(contactList);
        mainPanel = createMainPanel();

        getContentPane().add(mainPanel);
        setPreferredSize(new Dimension(260, 220));
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
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        return panel;
    }

    public void updateFriendsList() {
        listModel.setSize(0);
        for (String uid : client.getData().friends.keySet()) {
            FriendInfo info = client.getData().friends.get(uid);
            addFriend(listModel, info);
        }
    }

    private void addFriend(DefaultListModel<String> listModel, FriendInfo info) {
        listModel.addElement(String.format("%s: %s", info.getStatus(), info.getUid()));
    }

    private DefaultListModel<String> createListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String uid : client.getData().friends.keySet()) {
            FriendInfo info = client.getData().friends.get(uid);
            addFriend(listModel, info);
        }
        return listModel;
    }

    public JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));

        buttonsPanel.add(callButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);

        return buttonsPanel;
    }

    public JButton createCallButton() {
        JButton button = new JButton(CALL_BUTTON_NAME);
        button.setFocusable(false);
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
        private JTextField uidField = new JTextField();
        private JTextField friendsTokenField = new JTextField();
        private JPanel panel;

        public AddFriendPanel() {
            panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("User uid:"));
            panel.add(uidField);
            panel.add(new JLabel("User friend token:"));
            panel.add(friendsTokenField);
        }

        public JTextField getUidField() {
            return uidField;
        }

        public JTextField getFriendsTokenField() {
            return friendsTokenField;
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

}