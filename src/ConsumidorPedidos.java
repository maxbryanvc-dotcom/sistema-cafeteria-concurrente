import java.util.concurrent.BlockingQueue;

// Clase que consume pedidos desde la cola y los procesa
public class ConsumidorPedidos implements Runnable {

    // Cola compartida del sistema
    private BlockingQueue<Pedido> colaPedidos;

    // Servicio de stock
    private ServicioStock servicioStock;

    // Métricas del sistema
    private MetricasSistema metricas;

    // Constructor
    public ConsumidorPedidos(BlockingQueue<Pedido> colaPedidos,
                             ServicioStock servicioStock,
                             MetricasSistema metricas) {
        this.colaPedidos = colaPedidos;
        this.servicioStock = servicioStock;
        this.metricas = metricas;
    }

    @Override
    public void run() {
        try {
            boolean continuar = true;

            // El consumidor sigue activo mientras haya pedidos
            while (continuar) {

                // Espera hasta que exista un pedido disponible
                Pedido pedido = colaPedidos.take();

                // Pedido especial para finalizar
                if (pedido.getId() == 0) {
                    continuar = false;
                    System.out.println("No hay más pedidos por procesar");
                } else {
                    procesarPedido(pedido);
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Procesa un pedido normal
    private void procesarPedido(Pedido pedido) {

        System.out.println("Procesando pedido "
                + pedido.getId()
                + " de "
                + pedido.getCliente()
                + ": "
                + pedido.getProducto());

        // Incrementa el total de pedidos procesados
        metricas.incrementarPedidosTotales();

        // Intenta descontar stock
        boolean stockDisponible = servicioStock.descontarStock(pedido.getProducto());

        // Si hay stock, confirma el pedido
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