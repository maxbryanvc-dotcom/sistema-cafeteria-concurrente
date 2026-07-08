import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Clase principal del sistema
public class SistemaCafeteria {

    @SuppressWarnings("resource")
    public static void main(String[] args) {

        // Activa modo monitoreo si el usuario escribe "monitor"
        boolean modoMonitor = args.length > 0 && args[0].equalsIgnoreCase("monitor");

        // Cola compartida entre productor y consumidor
        BlockingQueue<Pedido> colaPedidos = new LinkedBlockingQueue<>();

        // Instancia de métricas
        MetricasSistema metricas = new MetricasSistema();

        // Servicio de stock usando métricas
        ServicioStock servicioStock = new ServicioStock(metricas);

        // Hilo productor
        Thread productor = new Thread(new ProductorPedidos(colaPedidos));

        // Hilo consumidor
        Thread consumidor = new Thread(new ConsumidorPedidos(colaPedidos, servicioStock, metricas));

        // Si está en modo monitor, inicia el servidor de métricas
        if (modoMonitor) {
            Thread servidorMetricas = new Thread(new ServidorMetricas(8081, metricas));
            servidorMetricas.setDaemon(true);
            servidorMetricas.start();
        }

        // Inicia la ejecución concurrente
        productor.start();
        consumidor.start();

        try {
            // Espera a que productor y consumidor terminen
            productor.join();
            consumidor.join();

            // Si está en modo monitor, mantiene el programa abierto
            if (modoMonitor) {
                System.out.println("Monitoreo activo. Presione ENTER para cerrar el sistema.");

                // No se cierra el Scanner porque cerraría también System.in
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}