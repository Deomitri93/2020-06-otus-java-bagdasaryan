package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CounterSynchronized {
    private final int threadsNumber;
    private final int minCntValue;
    private final int maxCntValue;
    private final int cyclesCount;
    private final Object synchronizer = new Object();
    private final Logger logger = LoggerFactory.getLogger(CounterSynchronized.class);
    private final List<ThreadCounter> threadList;
    private int activeThreadId = 0;
    private int cnt = 1;
    private int cntModifier = 1;

    public CounterSynchronized(int threadsNumber, int minCntValue, int maxCntValue, int cyclesCount) {
        this.threadsNumber = threadsNumber;
        this.minCntValue = minCntValue;
        this.maxCntValue = maxCntValue;
        this.cyclesCount = cyclesCount;

        threadList = new ArrayList<>();
        for (int i = 0; i < threadsNumber; i++) {
            threadList.add(new ThreadCounter(i));
        }
    }

    public void startAllThreads() {
        for (int i = 0; i < threadsNumber; i++) {
            threadList.get(i).start();
        }
    }

    private class ThreadCounter extends Thread {
        private final int threadId;

        public ThreadCounter(int threadId) {
            this.threadId = threadId;
        }

        @Override
        public void run() {
            synchronized (synchronizer) {
                for (int i = 0; i < (maxCntValue - minCntValue) * 2 * cyclesCount + 1; i++) {
                    while (activeThreadId != threadId) {
                        try {
                            synchronizer.wait(0);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            logger.error("Thread interrupted", e);
                        }
                    }
                    logger.info(String.valueOf(cnt));

                    if (threadId == threadsNumber - 1) {
                        if (cnt == minCntValue) {
                            cntModifier = 1;
                        }
                        if (cnt == maxCntValue) {
                            cntModifier = -1;
                        }

                        cnt += cntModifier;
                    }

                    if (activeThreadId < threadsNumber - 1) {
                        activeThreadId++;
                    } else {
                        activeThreadId = 0;
                    }

                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.error("Thread interrupted", e);
                    }

                    synchronizer.notifyAll();
                }
            }
        }
    }
}
