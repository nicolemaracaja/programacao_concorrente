package prova1_2025_1;

import java.util.concurrent.Semaphore;

public class Cluster {

    static int M = 5; 
    static int jobs = 0; // quantidade atual no buffer

    static Semaphore mutex = new Semaphore(1);
    static Semaphore cheio = new Semaphore(0); 
    static Semaphore vazio = new Semaphore(1); 

    static Semaphore acordarManager = new Semaphore(0);
    static boolean managerAcordado = false;

    static int getJob() {
        return 1;
    }

    static void exec(int j) {
        System.out.println("Executando job");
    }

    static void create_jobs(int m) {
        System.out.println("Criando " + m + " jobs");
    }

    static void compute() {
        while (true) {
            try {
                mutex.acquire();
                if (jobs == 0 && !managerAcordado) {
                    managerAcordado = true;
                    acordarManager.release();
                }
                mutex.release();
                cheio.acquire();
                mutex.acquire();
                jobs--;
                mutex.release();
                int j = getJob();
                exec(j);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static void manage() {
        while (true) {
            try {
                // espera ser acordado
                acordarManager.acquire();
                mutex.acquire();
                create_jobs(M);
                jobs = M;
                managerAcordado = false;
                mutex.release();
                for (int i = 0; i < M; i++) {
                    cheio.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
