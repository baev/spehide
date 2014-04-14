package ru.ifmo.baev.network;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.14
 */
public abstract class AbstractProcessor implements Runnable {
    boolean running = false;

    public boolean isRunning() {
        return running;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }
}
