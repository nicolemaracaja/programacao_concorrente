package prova1_2025_1;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Sum {

    static int max = Integer.MIN_VALUE;
    static int min = Integer.MAX_VALUE;
    static int restantes;
    static Semaphore mutex = new Semaphore(1);
    static Semaphore barreira = new Semaphore(0);

    static void foo() {
        try {
            Random rand = new Random();
            int num = rand.nextInt(10) + 1;
            System.out.println(Thread.currentThread().getName() + " gerou: " + num);
            Thread.sleep(num * 1000);
            mutex.acquire();
            if (num > max) max = num;
            if (num < min) min = num;
            restantes--;
            if (restantes == 0) {
                barreira.release();
            }
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int n = 5; 
        restantes = n;
        for (int i = 0; i < n; i++) {
            new Thread(() -> foo()).start();
        }
        try {
            barreira.acquire(); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int resultado = max + min;
        System.out.println("Resultado (max + min): " + resultado);
    }
}
