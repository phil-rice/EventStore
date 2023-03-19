package one.xingyi.events.utils.concurrent;

import one.xingyi.events.utils.interfaces.RunnableWithException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FilenameMutex {

    private final Object lock = new Object();

    Map<String, AtomicInteger> filenameToLock = new HashMap<>(); // doesn't need to be concurrent: is protected by lock


    public <E extends Exception> void wantToUseFile(String filename, RunnableWithException<E> runnable) throws E {
        final var countWhichIsAlsoLock = getLockFor(filename);
        try {
            synchronized (countWhichIsAlsoLock) {
                runnable.run();
            }
        } finally {
            releaseLock(filename, countWhichIsAlsoLock);
        }


    }

    private void releaseLock(String filename, AtomicInteger countWhichIsAlsoLock) {
        synchronized (lock) {
            var result = countWhichIsAlsoLock.decrementAndGet();
            if (result == 0) filenameToLock.remove(filename);
        }
    }

    private AtomicInteger getLockFor(String filename) {
        synchronized (lock) {
            AtomicInteger countWhichIsAlsoLock = filenameToLock.computeIfAbsent(filename, s -> new AtomicInteger(0));
            countWhichIsAlsoLock.incrementAndGet();
            return countWhichIsAlsoLock;
        }
    }

    public int size() {
        return filenameToLock.size();
    }
}
