import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Clase principal del sistema
public class SistemaCafeteria {

    @SuppressWarnings("resource")
    public static void main(String[] args) {

        boolean modoMonitor = args.length > 0 && args[0].equalsIgnoreCase("monitor");
        boolean modoSimulacion = args.length > 0 && args[0].equalsIgnoreCase("simulacion");

        BlockingQueue<Pedido> colaPedidos = new LinkedBlockingQueue<>();
        MetricasSistema metricas = new MetricasSistema();
        ServicioStock servicioStock = new ServicioStock(metricas);

        if (modoSimulacion) {
            ejecutarModoSimulacion(colaPedidos, servicioStock, metricas);
        } else {
            ejecutarModoNormal(colaPedidos, servicioStock, metricas, modoMonitor);
        }
    }

    private static void ejecutarModoNormal(BlockingQueue<Pedido> colaPedidos,
                                           ServicioStock servicioStock,
                                           MetricasSistema metricas,
                                           boolean modoMonitor) {

        Thread productor = new Thread(new ProductorPedidos(colaPedidos));
        Thread consumidor = new Thread(new ConsumidorPedidos(colaPedidos, servicioStock, metricas));

        if (modoMonitor) {
            Thread servidorMetricas = new Thread(new ServidorMetricas(8081, metricas));
            servidorMetricas.setDaemon(true);
            servidorMetricas.start();
        }

        productor.start();
        consumidor.start();

        try {
            productor.join();
            consumidor.join();

            if (modoMonitor) {
                System.out.println("Monitoreo activo. Presione ENTER para cerrar el sistema.");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Modo simulación: productor de pedidos variable + múltiples consumidores
    private static void ejecutarModoSimulacion(BlockingQueue<Pedido> colaPedidos,
                                               ServicioStock servicioStock,
                                               MetricasSistema metricas) {

        Thread servidorMetricas = new Thread(new ServidorMetricas(8081, metricas));
        servidorMetricas.setDaemon(true);
        servidorMetricas.start();

        Thread productorVariable = new Thread(new ProductorPedidosVariable(colaPedidos), "Productor variable");
        Thread repositorStock = new Thread(new RepositorStock(servicioStock), "Repositor de stock");

        List<Thread> consumidores = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            Thread consumidor = new Thread(
                    new ConsumidorPedidos(colaPedidos, servicioStock, metricas, "Consumidor-" + i),
                    "Consumidor-" + i
            );

            consumidores.add(consumidor);
        }

        productorVariable.start();
        repositorStock.start();

        for (Thread consumidor : consumidores) {
            consumidor.start();
        }

        System.out.println("Simulación concurrente activa.");
        System.out.println("Métricas disponibles en http://localhost:8081/metrics");
        System.out.println("Presione ENTER para cerrar la simulación.");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        productorVariable.interrupt();
        repositorStock.interrupt();

        for (Thread consumidor : consumidores) {
            consumidor.interrupt();
        }

        System.out.println("Simulación finalizada.");
    }
}
