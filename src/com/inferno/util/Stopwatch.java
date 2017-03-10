package com.inferno.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by Fernando on 3/5/2017.
 */
public class Stopwatch {
    private boolean running;
    private boolean interrupted;
    private boolean stopped;
    private float limit;
    private float accumulator;
    private BiConsumer<Float, Float> tickAction;
    private Runnable interruptedAction;
    private Runnable stoppedAction;

    public Stopwatch(float limit, Runnable interruptedAction, Runnable stoppedAction) {
        this.limit = limit;
        this.interruptedAction = interruptedAction;
        this.stoppedAction = stoppedAction;
        this.tickAction = (time, timeLimit) -> {};
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
                running = false;
            }

            tickAction.accept(accumulator, limit);
            accumulator += dt;
        }
    }

    public void interrupt() {
        interruptedAction.run();
        interrupted = true;
    }

    public void restart() {
        running = true;
        stopped = false;
        interrupted = false;
        accumulator = 0;
    }

    public void endTimer() {
        running = false;
        stopped = true;
        interrupted = false;
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

    public void setTickAction(BiConsumer<Float, Float> tick) {
        this.tickAction = tick;
    }

    public void setStoppedAction(Runnable stoppedAction) {
        this.stoppedAction = stoppedAction;
    }

    public float getTimeLimit() {
        return limit;
    }

    public float getTime() {
        return accumulator;
    }
}
