// Soluciona la race condition usando synchronized
public class SimuladorRaceConditionSolucionada {

    private static int stock = 1;
    private static int pedidosConfirmados = 0;

    public static synchronized void procesarPedido() {
        String nombreHilo = Thread.currentThread().getName();

        if (stock > 0) {
            System.out.println(nombreHilo + " encontró stock disponible.");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            stock--;
            pedidosConfirmados++;

            System.out.println(nombreHilo + " confirmó el pedido.");
        } else {
            System.out.println(nombreHilo + " no encontró stock.");
        }
    }

    public static void main(String[] args) {

        Thread hilo1 = new Thread(SimuladorRaceConditionSolucionada::procesarPedido, "Cliente-1");
        Thread hilo2 = new Thread(SimuladorRaceConditionSolucionada::procesarPedido, "Cliente-2");

        hilo1.start();
        hilo2.start();

        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Stock final: " + stock);
        System.out.println("Pedidos confirmados: " + pedidosConfirmados);
    }
}
