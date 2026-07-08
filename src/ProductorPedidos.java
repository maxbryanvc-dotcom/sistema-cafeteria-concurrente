import java.util.concurrent.BlockingQueue;

// Clase que genera pedidos y los coloca en la cola compartida
public class ProductorPedidos implements Runnable {

    // Cola compartida donde se almacenan los pedidos
    private BlockingQueue<Pedido> colaPedidos;

    // Constructor que recibe la cola creada en el sistema principal
    public ProductorPedidos(BlockingQueue<Pedido> colaPedidos) {
        this.colaPedidos = colaPedidos;
    }

    @Override
    public void run() {
        try {
            // Primer pedido normal
            colaPedidos.put(new Pedido(1, "Ana", "Empanada"));
            System.out.println("Pedido 1 registrado en la cola");

            // Segundo pedido normal
            colaPedidos.put(new Pedido(2, "Luis", "Jugo"));
            System.out.println("Pedido 2 registrado en la cola");

            // Tercer pedido normal
            colaPedidos.put(new Pedido(3, "María", "Pan con pollo"));
            System.out.println("Pedido 3 registrado en la cola");

            // Cuarto pedido: prueba de stock limitado
            colaPedidos.put(new Pedido(4, "Carlos", "Jugo"));
            System.out.println("Pedido 4 registrado en la cola");

            // Quinto pedido: prueba con producto inexistente
            colaPedidos.put(new Pedido(5, "Rosa", "Hamburguesa"));
            System.out.println("Pedido 5 registrado en la cola");

            // Pedido especial que indica que ya no habrá más pedidos
            colaPedidos.put(new Pedido(0, "FIN", "FIN"));

        } catch (InterruptedException e) {

            // Marca nuevamente la interrupción del hilo
            Thread.currentThread().interrupt();
        }
    }
}
