package prova1_2025_2;

import java.util.concurrent.Semaphore;

public class ConfigCache {
    static int readers = 0;
    static Semaphore mutex = new Semaphore(1);
    static Semaphore writeLock = new Semaphore(1);
    static Semaphore catraca = new Semaphore(1);

    String getValue(String key) {
        return "valor";
    }

    void putValue(String key, String value) {
    }

    String safe_getValue(String key) {
        try {
            catraca.acquire();
            catraca.release();
            mutex.acquire();
            readers++;
            if (readers == 1) {
                writeLock.acquire();
            }
            mutex.release();
            String value = getValue(key);
            mutex.acquire();
            readers--;
            if (readers == 0) {
                writeLock.release();
            }
            mutex.release();
            return value;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    void safe_putValue(String key, String value) {
        try {
            catraca.acquire();
            writeLock.acquire();
            putValue(key, value);
            writeLock.release();
            catraca.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
