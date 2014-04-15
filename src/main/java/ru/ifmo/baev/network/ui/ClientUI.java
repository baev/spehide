package ru.ifmo.baev.network.ui;

import ru.ifmo.baev.network.client.Client;
import ru.ifmo.baev.network.client.ClientData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

public class ClientUI extends JFrame {

    private static final String FRAME_NAME = "Contact list";

    private static final String CALL_BUTTON_NAME = "Call";

    private static final String DELETE_BUTTON_NAME = "Delete";

    private static final String ADD_BUTTON_NAME = "Add";

    private java.util.List<String> names = Arrays.asList("vasya", "petya", "kolya", "swith");

    public ClientUI() {
        super(FRAME_NAME);

        JTextField loginField = new JTextField();
        JTextField passField = new JTextField();
        JTextField serverField = new JTextField();
        serverField.setText("localhost");
        JPanel panel = createLoginPanel(serverField, loginField, passField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Auth", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        String login = loginField.getText();
        String pass = passField.getText();
        String server = serverField.getText();
        if (option != JOptionPane.OK_OPTION || server.isEmpty() || login.isEmpty() || pass.isEmpty()) {
            return;
        }

        try {
            Client client = new Client(new ClientData());
            client.login(server, login, pass);
            client.start();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel mainPanel = createMainPanel();

        getContentPane().add(mainPanel);

        setPreferredSize(new Dimension(260, 220));
        pack();
        setLocationRelativeTo(null);

        setVisible(true);
    }

    public JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JList list = createContactList();
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.add(createButtonsPanel(list), BorderLayout.SOUTH);

        return panel;
    }

    public JList<String> createContactList() {
        final DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String name : names) {
            listModel.addElement(name);
        }

        final JList<String> list = new JList<>(listModel);
        list.setFocusable(false);
        return list;
    }

    public JPanel createButtonsPanel(JList list) {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));

        buttonsPanel.add(createCallButton(list));
        buttonsPanel.add(createDeleteButton(list));
        buttonsPanel.add(createAddButton(list));

        return buttonsPanel;
    }

    public JButton createCallButton(final JList list) {
        JButton button = new JButton(CALL_BUTTON_NAME);
        button.setFocusable(false);
        button.addActionListener(new CallButtonActionListener(list));
        return button;
    }

    public JButton createDeleteButton(final JList list) {
        JButton button = new JButton(DELETE_BUTTON_NAME);
        button.setFocusable(false);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String toDelete = (String) list.getSelectedValue();
                int option = JOptionPane.showConfirmDialog(list, String.format(
                        "Delete user %s from contact list?",
                        toDelete
                ));

                if (option == JOptionPane.OK_OPTION) {
                    ((DefaultListModel) list.getModel()).remove(list.getSelectedIndex());
                }
            }
        });
        return button;
    }

    public JButton createAddButton(final JList list) {
        JButton button = new JButton(ADD_BUTTON_NAME);
        button.setFocusable(false);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField uidField = new JTextField();
                JTextField friendsTokenField = new JTextField();
                JPanel panel = createAddUserPanel(uidField, friendsTokenField);

                int option = JOptionPane.showConfirmDialog(null, panel, "Add friend", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                String uid = uidField.getText();
                String friendsToken = friendsTokenField.getText();

                if (option == JOptionPane.OK_OPTION && !uid.isEmpty() && !friendsToken.isEmpty()) {
                    System.out.println(uid + ": " + friendsToken);
                }
            }
        });
        return button;
    }

    public JPanel createAddUserPanel(JTextField uid, JTextField friendsToken) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("User uid:"));
        panel.add(uid);
        panel.add(new JLabel("User friend token:"));
        panel.add(friendsToken);
        return panel;
    }

    public JPanel createLoginPanel(JTextField server, JTextField login, JTextField pass) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Server:"));
        panel.add(server);
        panel.add(new JLabel("Login:"));
        panel.add(login);
        panel.add(new JLabel("Password:"));
        panel.add(pass);
        return panel;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                new ClientUI();
            }
        });
    }
}