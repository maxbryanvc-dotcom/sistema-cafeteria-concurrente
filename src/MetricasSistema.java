import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// Clase que almacena métricas básicas del sistema
public class MetricasSistema {

    private AtomicInteger pedidosTotales = new AtomicInteger(0);
    private AtomicInteger pedidosConfirmados = new AtomicInteger(0);
    private AtomicInteger pedidosRechazados = new AtomicInteger(0);
    private AtomicInteger pedidosEnCola = new AtomicInteger(0);

    private Map<String, AtomicInteger> stockPorProducto = new ConcurrentHashMap<>();
    private Map<String, AtomicInteger> pedidosPorConsumidor = new ConcurrentHashMap<>();

    public void registrarStockInicial(String producto, int stock) {
        stockPorProducto.put(producto, new AtomicInteger(stock));
    }

    public void actualizarStock(String producto, int stockActual) {
        stockPorProducto.put(producto, new AtomicInteger(stockActual));
    }

    public void incrementarPedidosTotales() {
        pedidosTotales.incrementAndGet();
    }

    public void incrementarPedidosConfirmados() {
        pedidosConfirmados.incrementAndGet();
    }

    public void incrementarPedidosRechazados() {
        pedidosRechazados.incrementAndGet();
    }

    public void actualizarPedidosEnCola(int cantidad) {
        pedidosEnCola.set(cantidad);
    }

    public void incrementarPedidosPorConsumidor(String consumidor) {
        pedidosPorConsumidor
                .computeIfAbsent(consumidor, k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    public String generarMetricasPrometheus() {
        StringBuilder sb = new StringBuilder();

        sb.append("# HELP cafeteria_pedidos_totales Total de pedidos procesados\n");
        sb.append("# TYPE cafeteria_pedidos_totales counter\n");
        sb.append("cafeteria_pedidos_totales ").append(pedidosTotales.get()).append("\n\n");

        sb.append("# HELP cafeteria_pedidos_confirmados Total de pedidos confirmados\n");
        sb.append("# TYPE cafeteria_pedidos_confirmados counter\n");
        sb.append("cafeteria_pedidos_confirmados ").append(pedidosConfirmados.get()).append("\n\n");

        sb.append("# HELP cafeteria_pedidos_rechazados Total de pedidos rechazados\n");
        sb.append("# TYPE cafeteria_pedidos_rechazados counter\n");
        sb.append("cafeteria_pedidos_rechazados ").append(pedidosRechazados.get()).append("\n\n");

        sb.append("# HELP cafeteria_pedidos_en_cola Cantidad actual de pedidos pendientes en cola\n");
        sb.append("# TYPE cafeteria_pedidos_en_cola gauge\n");
        sb.append("cafeteria_pedidos_en_cola ").append(pedidosEnCola.get()).append("\n\n");

        sb.append("# HELP cafeteria_stock_actual Stock actual por producto\n");
        sb.append("# TYPE cafeteria_stock_actual gauge\n");

        for (Map.Entry<String, AtomicInteger> entry : stockPorProducto.entrySet()) {
            sb.append("cafeteria_stock_actual{producto=\"")
              .append(entry.getKey())
              .append("\"} ")
              .append(entry.getValue().get())
              .append("\n");
        }

        sb.append("\n");

        sb.append("# HELP cafeteria_pedidos_por_consumidor Pedidos procesados por cada consumidor\n");
        sb.append("# TYPE cafeteria_pedidos_por_consumidor counter\n");

        for (Map.Entry<String, AtomicInteger> entry : pedidosPorConsumidor.entrySet()) {
            sb.append("cafeteria_pedidos_por_consumidor{consumidor=\"")
              .append(entry.getKey())
              .append("\"} ")
              .append(entry.getValue().get())
              .append("\n");
        }

        return sb.toString();
    }
}
