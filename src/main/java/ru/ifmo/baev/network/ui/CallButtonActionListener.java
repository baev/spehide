package ru.ifmo.baev.network.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public class CallButtonActionListener implements ActionListener {

    private JList list;

    public CallButtonActionListener(JList list) {
        this.list = list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(Arrays.toString(list.getSelectedIndices()));
    }
}
