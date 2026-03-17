package prova1_2024_1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class BufferQueue<T> {

    int capacity;
    private final Queue<T> queue;
    Semaphore mutex = new Semaphore(1);     
    Semaphore empty;                        
    Semaphore spaces = new Semaphore(0);      

    public BufferQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.empty = new Semaphore(capacity);
    }

    public T dequeue() {
        try {
            spaces.acquire();   // se não tiver item, bloqueia
            mutex.acquire();  
            T item = queue.poll();
            mutex.release();
            empty.release();    // libera espaço
            return item;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void enqueue(T item) {
        try {
            empty.acquire();  // espera ter espaço
            mutex.acquire();  
            queue.add(item);
            mutex.release();
            spaces.release();   // agora tem item disponível
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
