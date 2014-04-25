package ru.ifmo.baev.network.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.baev.network.message.Voice;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.14
 */
public class ClientVoiceSender {

    private final Logger logger = LogManager.getLogger(getClass());

    private DatagramSocket socket;

    public ClientVoiceSender() throws SocketException {
        socket = new DatagramSocket();
    }

    public void send(Voice voice, InetAddress address, int port) {

        try {
            byte[] bytes = voice.toBytes();
            socket.send(new DatagramPacket(
                    bytes,
                    bytes.length,
                    address,
                    port

            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (socket != null) {
            socket.close();
        }
    }
}
