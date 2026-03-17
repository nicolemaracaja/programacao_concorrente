package prova1_2024_2;

import java.util.concurrent.Semaphore;

public class Broker {

    class Request {
        int id;

        public Request(int id) {
            this.id = id;
        }
    }

    private Request[] buffer;
    private int N;
    private int in = 0;
    private int out = 0;


    private Semaphore mutex = new Semaphore(1);
    private Semaphore empty;
    private Semaphore spaces = new Semaphore(0);

    public Broker(int N) {
        this.N = N;
        this.buffer = new Request[N];
        this.empty = new Semaphore(N); 
    }

    public void submitRequest(Request r) {
        try {
            empty.acquire(); 
            mutex.acquire(); 
            buffer[in] = r;
            in = (in + 1) % N;
            mutex.release();
            spaces.release(); // agora tem item
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Request getWork() {
        Request r = null;
        try {
            spaces.acquire(); // espera item
            mutex.acquire(); 
            r = buffer[out];
            out = (out + 1) % N;
            mutex.release();
            empty.release(); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return r;
    }
}