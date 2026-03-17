package prova1_2025_1;

import java.util.concurrent.Semaphore;

public class ConfigStoreSafe {

    static int readers = 0;
    static Semaphore mutex = new Semaphore(1);      
    static Semaphore writeLock = new Semaphore(1);  

    static String lookup(String key) {
        return "valor"; 
    }

    static void update(String key, String value) {
        // simulação de escrita
    }

    static String safe_lookup(String key) {
        try {
            mutex.acquire();
            readers++;
            if (readers == 1) {
                writeLock.acquire();
            }
            mutex.release();

            String value = lookup(key);

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

    static void safe_update(String key, String value) {
        try {
            writeLock.acquire();
            update(key, value);
            writeLock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
