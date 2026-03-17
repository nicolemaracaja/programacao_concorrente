package prova1_2024_2;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class MinMaxValue {

    static int max_value = -1;
    static int min_value = Integer.MAX_VALUE;
    static int threads_restantes;
    static Semaphore mutex = new Semaphore(1);
    static Semaphore barreira = new Semaphore(0);

    static void foo() {
        try {
            Random rand = new Random();
            int num = rand.nextInt(10) + 1;
            Thread.sleep(num * 1000);
            mutex.acquire();

            if (num > max_value) {
                max_value = num;
            }
            if (num < min_value) {
                min_value = num;
            }
            threads_restantes--;
            if (threads_restantes == 0) {
                barreira.release();
            }
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int n = 5; 
        threads_restantes = n;
        for (int i = 0; i < n; i++) {
            new Thread(() -> foo()).start();
        }
        try {
            barreira.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Max: " + max_value);
        System.out.println("Min: " + min_value);
    }
}
