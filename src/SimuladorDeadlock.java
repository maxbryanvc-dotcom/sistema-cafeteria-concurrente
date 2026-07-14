// Simula un deadlock entre dos recursos
public class SimuladorDeadlock {

    private static final Object recursoStock = new Object();
    private static final Object recursoCaja = new Object();

    public static void main(String[] args) {

        Thread hilo1 = new Thread(() -> {
            synchronized (recursoStock) {
                System.out.println("Hilo 1 bloqueó recursoStock");

                esperar();

                synchronized (recursoCaja) {
                    System.out.println("Hilo 1 bloqueó recursoCaja");
                }
            }
        });

        Thread hilo2 = new Thread(() -> {
            synchronized (recursoCaja) {
                System.out.println("Hilo 2 bloqueó recursoCaja");

                esperar();

                synchronized (recursoStock) {
                    System.out.println("Hilo 2 bloqueó recursoStock");
                }
            }
        });

        hilo1.start();
        hilo2.start();
    }

    private static void esperar() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
