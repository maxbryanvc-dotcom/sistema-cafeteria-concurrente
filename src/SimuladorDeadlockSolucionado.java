// Soluciona el deadlock usando orden fijo de bloqueo
public class SimuladorDeadlockSolucionado {

    private static final Object recursoStock = new Object();
    private static final Object recursoCaja = new Object();

    public static void main(String[] args) {

        Thread hilo1 = new Thread(() -> procesarOperacion("Hilo 1"));
        Thread hilo2 = new Thread(() -> procesarOperacion("Hilo 2"));

        hilo1.start();
        hilo2.start();

        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Operaciones finalizadas sin deadlock.");
    }

    private static void procesarOperacion(String nombreHilo) {
        synchronized (recursoStock) {
            System.out.println(nombreHilo + " bloqueó recursoStock");

            esperar();

            synchronized (recursoCaja) {
                System.out.println(nombreHilo + " bloqueó recursoCaja");
                System.out.println(nombreHilo + " completó la operación.");
            }
        }
    }

    private static void esperar() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
