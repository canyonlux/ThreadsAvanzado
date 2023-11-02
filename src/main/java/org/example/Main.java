package org.example;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // Crear un buffer circular con capacidad para 4 elementos
        BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(4);

        // Crear e iniciar los productores y consumidores
        Thread[] productores = new Thread[3];
        Thread[] consumidores = new Thread[2];

        for (int i = 0; i < 3; i++) { // 3 productores
            productores[i] = new Thread(new Productor(buffer), "ProductorPepe " + i);
            productores[i].start();
        }

        for (int i = 0; i < 2; i++) { // 2 consumidores
            consumidores[i] = new Thread(new Consumidor(buffer), "ConsumidorEva " + i);
            consumidores[i].start();
        }
    }

    static class Productor implements Runnable { // Productor
        private BlockingQueue<Integer> buffer;
        private Random random = new Random();

        public Productor(BlockingQueue<Integer> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() { // Generar datos aleatorios
            while (true) {
                try {
                    int dato = random.nextInt(30) + 1;
                    System.out.println(Thread.currentThread().getName() + " produciendo dato " + dato);
                    buffer.put(dato);
                    Thread.sleep(random.nextInt(2000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class Consumidor implements Runnable { // Consumidor
        private BlockingQueue<Integer> buffer;
        private Random random = new Random();

        public Consumidor(BlockingQueue<Integer> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() { // Consumir datos aleatorios
            while (true) {
                try {
                    int dato = buffer.take();
                    System.out.println(Thread.currentThread().getName() + " consumiendo dato " + dato);
                    Thread.sleep(random.nextInt(2000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
