package org.example;

import java.util.Random;

public class PlayStation{

    public static void main(String[] args) {
        int numClientes = 200; // Número de clientes
        int numPS5Disponibles = 20; // Número de PS5 disponibles

        Tienda tienda = new Tienda(numPS5Disponibles); // Crear tienda

        for (int i = 1; i <= numClientes; i++) { // Crear clientes
            Cliente cliente = new Cliente(tienda, i);
            new Thread(cliente).start();
        }
    }

    static class Tienda {
        private int numPS5Disponibles; // Número de PS5 disponibles

        public Tienda(int numPS5Disponibles) { // Constructor
            this.numPS5Disponibles = numPS5Disponibles;
        }

        public synchronized boolean intentarEntrar(int clienteId) { // Entrar en la tienda
            if (numPS5Disponibles > 0) { // Si quedan PS5
                numPS5Disponibles--;
                System.out.println("Cliente " + clienteId + " ha entrado y comprado una PlayStation 5!!!");
                return true;
            } else {
                System.out.println("Cliente " + clienteId + " ha entrado pero no quedan PlayStation 5.....");
                return false;
            }
        }
    }

    static class Cliente implements Runnable { // Cliente
        private Tienda tienda;
        private int clienteId;
        private Random random = new Random(); // Generador de números aleatorios

        public Cliente(Tienda tienda, int clienteId) {
            this.tienda = tienda;
            this.clienteId = clienteId;
        }

        @Override
        public void run() { // Intentar entrar en la tienda
            for (int intento = 1; intento <= 10; intento++) { // 10 intentos
                int espera = random.nextInt(1000); // Tiempo de espera aleatorio
                try {
                    Thread.sleep(espera);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                boolean entroYCompro = tienda.intentarEntrar(clienteId); // Entrar en la tienda
                if (entroYCompro) {
                    return;
                }
            }
            System.out.println("Cliente " + clienteId + " se ha cansadop de esperar y se ha ido sin comprar");
        }
    }
}
