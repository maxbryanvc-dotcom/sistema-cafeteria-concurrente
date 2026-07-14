import java.util.concurrent.BlockingQueue;

// Clase que consume pedidos desde la cola y los procesa
public class ConsumidorPedidos implements Runnable {

    private BlockingQueue<Pedido> colaPedidos;
    private ServicioStock servicioStock;
    private MetricasSistema metricas;
    private String nombreConsumidor;

    // Constructor compatible con la versión anterior
    public ConsumidorPedidos(BlockingQueue<Pedido> colaPedidos,
                             ServicioStock servicioStock,
                             MetricasSistema metricas) {
        this(colaPedidos, servicioStock, metricas, "Consumidor");
    }

    // Constructor para identificar consumidores diferentes
    public ConsumidorPedidos(BlockingQueue<Pedido> colaPedidos,
                             ServicioStock servicioStock,
                             MetricasSistema metricas,
                             String nombreConsumidor) {
        this.colaPedidos = colaPedidos;
        this.servicioStock = servicioStock;
        this.metricas = metricas;
        this.nombreConsumidor = nombreConsumidor;
    }

    @Override
    public void run() {
        try {
            boolean continuar = true;

            while (continuar) {

                metricas.actualizarPedidosEnCola(colaPedidos.size());

                Pedido pedido = colaPedidos.take();

                metricas.actualizarPedidosEnCola(colaPedidos.size());

                if (pedido.getId() == 0) {
                    continuar = false;
                    System.out.println(nombreConsumidor + ": No hay más pedidos por procesar");
                } else {
                    procesarPedido(pedido);
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void procesarPedido(Pedido pedido) {

        System.out.println(nombreConsumidor
                + " procesando pedido "
                + pedido.getId()
                + " de "
                + pedido.getCliente()
                + ": "
                + pedido.getProducto());

        metricas.incrementarPedidosTotales();
        metricas.incrementarPedidosPorConsumidor(nombreConsumidor);

        boolean stockDisponible = servicioStock.descontarStock(pedido.getProducto());

        if (stockDisponible) {
            metricas.incrementarPedidosConfirmados();

            System.out.println("Pedido confirmado para "
                    + pedido.getCliente()
                    + ": "
                    + pedido.getProducto());
        } else {
            metricas.incrementarPedidosRechazados();

            System.out.println("Pedido rechazado para "
                    + pedido.getCliente()
                    + ": "
                    + pedido.getProducto());
        }
    }
}
