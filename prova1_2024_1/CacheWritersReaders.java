package prova1_2024_1;

import java.util.concurrent.Semaphore;

public class CacheWritersReaders {
    static int readers = 0; 
    static Semaphore mutex = new Semaphore(1);      
    static Semaphore writeLock = new Semaphore(1);  // exclusão para escrita
    static Semaphore turnstile = new Semaphore(1);  // prioridade para escritores


    static Object read() {
        // simula leitura
        return new Object();
    }

    static void write(Object d) {
        // simula escrita
    }

    static Object wrap_read() {
        try {
            // catraca
            turnstile.acquire();
            turnstile.release();
            mutex.acquire();
            readers++;
            if (readers == 1) {
                writeLock.acquire();
            }
            mutex.release();

            Object value = read();

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

    static void wrap_write(Object d) {

        try {
            turnstile.acquire();
            writeLock.acquire();
            write(d);
            writeLock.release();
            turnstile.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
