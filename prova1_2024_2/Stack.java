package prova1_2024_2;

import java.util.concurrent.Semaphore;

public class Stack {

    private int capacity;
    private int[] array;
    private int topo;
    private Semaphore mutex = new Semaphore(1);     
    private Semaphore empty;                        
    private Semaphore spaces = new Semaphore(0);

    public Stack(int capacity) {
        this.capacity = capacity;
        this.array = new int[capacity];
        this.topo = -1;
        this.empty = new Semaphore(capacity); 
    }

    public boolean isEmpty() {
        return topo == -1;
    }

    public void push(int value) {
        try {
            empty.acquire(); // se não tiver espaço, bloqueia
            mutex.acquire();
            topo++;
            array[topo] = value;
            System.out.println("Push: " + value);
            mutex.release();
            spaces.release(); // indica que tem um novo item
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int pop() {
        int item = -1;
        try {
            spaces.acquire(); // se não tiver itens, bloqueia
            mutex.acquire();
            item = array[topo];
            topo--;
            System.out.println("Pop: " + item);
            mutex.release();
            empty.release(); // libera um novo espaço
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return item;
    }
}
