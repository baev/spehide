//package ru.ifmo.baev.network.ui;
//
//import javax.swing.*;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
///**
// * @author Dmitry Baev charlie@yandex-team.ru
// *         Date: 13.04.14
// */
//public class ST {
//
//    public void init() {
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BorderLayout(5, 5));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//
//        final DefaultListModel listModel = new DefaultListModel();
//
//        for (i = 0; i < 25; i++) {
//            listModel.addElement("Элемент списка " + i);
//        }
//
//        final JList list = new JList(listModel);
//        list.setSelectedIndex(0);
//        list.setFocusable(false);
//        mainPanel.add(new JScrollPane(list), BorderLayout.CENTER);
//
//
//        JPanel buttonsPanel = new JPanel();
//        buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
//        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
//
//        JButton addButton = new JButton("Добавить");
//        addButton.setFocusable(false);
//        addButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String element = "Элемент списка " + i++;
//                listModel.addElement(element);
//                int index = listModel.size() - 1;
//                list.setSelectedIndex(index);
//                list.ensureIndexIsVisible(index);
//            }
//        });
//        buttonsPanel.add(addButton);
//
//        final JButton removeButton = new JButton("Удалить");
//        removeButton.setFocusable(false);
//        removeButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                listModel.remove(list.getSelectedIndex());
//            }
//        });
//        buttonsPanel.add(removeButton);
//
//        list.addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent e) {
//                if (list.getSelectedIndex() >= 0) {
//                    removeButton.setEnabled(true);
//                } else {
//                    removeButton.setEnabled(false);
//                }
//            }
//        });
//
//        getContentPane().add(mainPanel);
//
//        setPreferredSize(new Dimension(260, 220));
//        pack();
//        setLocationRelativeTo(null);
//        setVisible(true);
//    }
//}
