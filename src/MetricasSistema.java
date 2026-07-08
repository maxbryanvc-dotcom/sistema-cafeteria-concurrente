import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// Clase que almacena métricas básicas del sistema
public class MetricasSistema {

    // Total de pedidos procesados
    private AtomicInteger pedidosTotales = new AtomicInteger(0);

    // Total de pedidos confirmados
    private AtomicInteger pedidosConfirmados = new AtomicInteger(0);

    // Total de pedidos rechazados
    private AtomicInteger pedidosRechazados = new AtomicInteger(0);

    // Stock actual por producto
    private Map<String, AtomicInteger> stockPorProducto = new ConcurrentHashMap<>();

    // Registra el stock inicial de un producto
    public void registrarStockInicial(String producto, int stock) {
        stockPorProducto.put(producto, new AtomicInteger(stock));
    }

    // Actualiza el stock actual de un producto
    public void actualizarStock(String producto, int stockActual) {
        stockPorProducto.put(producto, new AtomicInteger(stockActual));
    }

    // Incrementa el contador de pedidos procesados
    public void incrementarPedidosTotales() {
        pedidosTotales.incrementAndGet();
    }

    // Incrementa el contador de pedidos confirmados
    public void incrementarPedidosConfirmados() {
        pedidosConfirmados.incrementAndGet();
    }

    // Incrementa el contador de pedidos rechazados
    public void incrementarPedidosRechazados() {
        pedidosRechazados.incrementAndGet();
    }

    // Genera la salida en formato compatible con Prometheus
    public String generarMetricasPrometheus() {
        StringBuilder sb = new StringBuilder();

        // Métrica total de pedidos procesados
        sb.append("# HELP cafeteria_pedidos_totales Total de pedidos procesados\n");
        sb.append("# TYPE cafeteria_pedidos_totales counter\n");
        sb.append("cafeteria_pedidos_totales ").append(pedidosTotales.get()).append("\n\n");

        // Métrica total de pedidos confirmados
        sb.append("# HELP cafeteria_pedidos_confirmados Total de pedidos confirmados\n");
        sb.append("# TYPE cafeteria_pedidos_confirmados counter\n");
        sb.append("cafeteria_pedidos_confirmados ").append(pedidosConfirmados.get()).append("\n\n");

        // Métrica total de pedidos rechazados
        sb.append("# HELP cafeteria_pedidos_rechazados Total de pedidos rechazados\n");
        sb.append("# TYPE cafeteria_pedidos_rechazados counter\n");
        sb.append("cafeteria_pedidos_rechazados ").append(pedidosRechazados.get()).append("\n\n");

        // Métrica de stock por producto
        sb.append("# HELP cafeteria_stock_actual Stock actual por producto\n");
        sb.append("# TYPE cafeteria_stock_actual gauge\n");

        for (Map.Entry<String, AtomicInteger> entry : stockPorProducto.entrySet()) {
            sb.append("cafeteria_stock_actual{producto=\"")
              .append(entry.getKey())
              .append("\"} ")
              .append(entry.getValue().get())
              .append("\n");
        }

        return sb.toString();
    }
}