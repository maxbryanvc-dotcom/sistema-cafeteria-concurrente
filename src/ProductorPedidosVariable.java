import java.util.Random;
import java.util.concurrent.BlockingQueue;

// Clase que genera pedidos variables de forma continua
public class ProductorPedidosVariable implements Runnable {

    private BlockingQueue<Pedido> colaPedidos;
    private MetricasSistema metricas;
    private Random random = new Random();
    private int contadorPedidos = 1;

    private String[] clientes = {
            "Ana", "Luis", "María", "Carlos", "Rosa",
            "Pedro", "Lucía", "Miguel", "Elena", "Jorge"
    };

    private String[] productos = {
            "Empanada", "Jugo", "Pan con pollo", "Hamburguesa"
    };

    public ProductorPedidosVariable(BlockingQueue<Pedido> colaPedidos, MetricasSistema metricas) {
        this.colaPedidos = colaPedidos;
        this.metricas = metricas;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                String cliente = clientes[random.nextInt(clientes.length)];
                String producto = productos[random.nextInt(productos.length)];

                Pedido pedido = new Pedido(contadorPedidos, cliente, producto);
                colaPedidos.put(pedido);

                // Se cuenta como "generado" apenas entra a la cola,
                // sin importar si un consumidor ya lo procesó o no
                metricas.incrementarPedidosGenerados();

                System.out.println("Pedido variable "
                        + contadorPedidos
                        + " registrado: "
                        + cliente
                        + " solicita "
                        + producto);

                contadorPedidos++;

                // Generación acelerada (300 ms) para que el productor
                // supere la capacidad de los consumidores y la cola crezca
                Thread.sleep(300);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
