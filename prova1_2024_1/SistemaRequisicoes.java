package prova1_2024_1;

import java.util.concurrent.Semaphore;

public class SistemaRequisicoes {
    
    static Semaphore mutex = new Semaphore(1);
    static Semaphore gateExternal = new Semaphore(4);
    static Semaphore gateInternal = new Semaphore(0);
    static int externalCount = 0;

    static void handle() {
        System.out.println(Thread.currentThread().getName() + " executando handle()");
    }

    static void externalRoute() {
        try {
            gateExternal.acquire();
            mutex.acquire();
            externalCount++;
            if (externalCount == 4) {
                gateInternal.release();
            }
            mutex.release();
            handle();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void internalRoute() {
        try {
            gateInternal.acquire();
            handle();
            mutex.acquire();
            externalCount = 0;
            for (int i = 0; i < 4; i++) {
                gateExternal.release();
            }
            mutex.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> externalRoute(), "EXTERNA").start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(() -> internalRoute(), "INTERNA").start();
        }
    }
}
