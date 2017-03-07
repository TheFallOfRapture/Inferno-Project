package com.inferno.util;

/**
 * Created by Fernando on 3/5/2017.
 */
public class Stopwatch {
    private boolean running;
    private boolean interrupted;
    private boolean stopped;
    private float limit;
    private float accumulator;
    private Runnable interruptedAction;
    private Runnable stoppedAction;

    public Stopwatch(float limit, Runnable interruptedAction, Runnable stoppedAction) {
        this.limit = limit;
        this.interruptedAction = interruptedAction;
        this.stoppedAction = stoppedAction;
        running = false;
        stopped = false;
        interrupted = false;
    }

    public void start() {
        running = true;
    }

    public void tick(float dt) {
        if (running && !stopped && !interrupted) {
            if (accumulator >= limit) {
                stoppedAction.run();
                stopped = true;
            }

            accumulator += dt;
        }
    }

    public void interrupt() {
        interruptedAction.run();
        interrupted = true;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isStopped() {
        return stopped;
    }

    public float getTimeLimit() {
        return limit;
    }

    public float getTime() {
        return accumulator;
    }
}
