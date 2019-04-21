package com.example.b00086142.finalproject.Activity;

import java.util.concurrent.TimeUnit;

/**
 * This class emulates a stopwatch.
 */

public class StopWatch {
    private long startTime = 0;
    private long endTime = 0;
    private boolean running = false;


    // gives you a nanosecond-precise time, relative to some arbitrary point.
    public void start() {
        this.startTime = System.nanoTime();
        this.running = true;
    }

    public void stop() {
        this.endTime = System.nanoTime();
        this.running = false;
    }

    /**
     * Determine the time that has passed since the watch has started
     *
     * @return number of seconds passed
     */
    public long getElapsedTime() {
        long elapsedTime = 0;
        if (running) {
            elapsedTime = System.nanoTime() - startTime;

        } else {
            elapsedTime = endTime - startTime;
        }

        long seconds = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);

        return seconds;
    }


}
