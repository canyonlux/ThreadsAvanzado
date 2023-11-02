package org.example;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class Filosofos {

    public static void main(String[] args) {
        int numFilosofos = 5; // Número de filósofos
        Filosofo[] filosofos = new Filosofo[numFilosofos];
        Tenedor[] tenedores = new Tenedor[numFilosofos];

        for (int i = 0; i < numFilosofos; i++) { // Crear tenedores
            tenedores[i] = new Tenedor();
        }

        for (int i = 0; i < numFilosofos; i++) { // Crear filósofos
            filosofos[i] = new Filosofo(i, tenedores[i], tenedores[(i + 1) % numFilosofos]);
            new Thread(filosofos[i]).start();
        }
    }

    static class Tenedor {
        private Lock lock = new ReentrantLock();

        public boolean adquirir() { // Intentar adquirir el tenedor
            try {
                return lock.tryLock(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                return false;
            }
        }

        public void liberar() { // Liberar el tenedor
            lock.unlock();
        }
    }

    static class Filosofo implements Runnable { // Filósofo
        private int id;
        private Tenedor tenedorIzquierdo;
        private Tenedor tenedorDerecho;
        private Random random = new Random();

        public Filosofo(int id, Tenedor tenedorIzquierdo, Tenedor tenedorDerecho) {
            this.id = id;
            this.tenedorIzquierdo = tenedorIzquierdo;
            this.tenedorDerecho = tenedorDerecho;
        }

        @Override
        public void run() { // Pensar y comer
            try {
                while (true) {
                    pensar();
                    comer();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void pensar() throws InterruptedException {
            System.out.println("Filósofo " + id + " está pensando");
            Thread.sleep(random.nextInt(5000 - 1000) + 1000);
        }

        private void comer() throws InterruptedException {
            if (tenedorIzquierdo.adquirir()) {
                if (tenedorDerecho.adquirir()) {
                    System.out.println("Filósofo " + id + " está comiendo");
                    Thread.sleep(random.nextInt(5000 - 1000) + 1000);
                    tenedorDerecho.liberar();
                }
                tenedorIzquierdo.liberar();
            }
        }
    }
}
