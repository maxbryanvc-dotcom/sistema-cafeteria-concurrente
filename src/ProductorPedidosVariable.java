import java.util.Random;
import java.util.concurrent.BlockingQueue;

// Clase que genera pedidos variables de forma continua
public class ProductorPedidosVariable implements Runnable {

    private BlockingQueue<Pedido> colaPedidos;
    private Random random = new Random();
    private int contadorPedidos = 1;

    private String[] clientes = {
            "Ana", "Luis", "María", "Carlos", "Rosa",
            "Pedro", "Lucía", "Miguel", "Elena", "Jorge"
    };

    private String[] productos = {
            "Empanada", "Jugo", "Pan con pollo", "Hamburguesa"
    };

    public ProductorPedidosVariable(BlockingQueue<Pedido> colaPedidos) {
        this.colaPedidos = colaPedidos;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                String cliente = clientes[random.nextInt(clientes.length)];
                String producto = productos[random.nextInt(productos.length)];

                Pedido pedido = new Pedido(contadorPedidos, cliente, producto);
                colaPedidos.put(pedido);

                System.out.println("Pedido variable "
                        + contadorPedidos
                        + " registrado: "
                        + cliente
                        + " solicita "
                        + producto);

                contadorPedidos++;

                Thread.sleep(1500);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
