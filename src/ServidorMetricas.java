import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Clase que expone las métricas por HTTP
public class ServidorMetricas implements Runnable {

    // Puerto donde se expondrán las métricas
    private int puerto;

    // Referencia a las métricas del sistema
    private MetricasSistema metricas;

    // Constructor del servidor
    public ServidorMetricas(int puerto, MetricasSistema metricas) {
        this.puerto = puerto;
        this.metricas = metricas;
    }

    @Override
    public void run() {
        try (ServerSocket servidor = new ServerSocket(puerto)) {

            System.out.println("Servidor de métricas activo en http://localhost:" + puerto + "/metrics");

            // El servidor permanece escuchando solicitudes
            while (true) {
                Socket cliente = servidor.accept();
                responder(cliente);
            }

        } catch (Exception e) {
            System.out.println("Error en servidor de métricas: " + e.getMessage());
        }
    }

    // Responde una solicitud HTTP simple
    private void responder(Socket cliente) {
        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            OutputStream salida = cliente.getOutputStream()
        ) {
            // Lee la primera línea de la solicitud
            String lineaInicial = entrada.readLine();

            // Consume el resto de encabezados HTTP
            while (entrada.ready()) {
                String linea = entrada.readLine();
                if (linea == null || linea.isEmpty()) {
                    break;
                }
            }

            // Solo responde si la ruta es /metrics
            if (lineaInicial != null && lineaInicial.contains("GET /metrics")) {

                String cuerpo = metricas.generarMetricasPrometheus();

                String respuesta = ""
                        + "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/plain; version=0.0.4\r\n"
                        + "Content-Length: " + cuerpo.getBytes().length + "\r\n"
                        + "\r\n"
                        + cuerpo;

                salida.write(respuesta.getBytes());
            } else {
                String cuerpo = "Ruta no encontrada";

                String respuesta = ""
                        + "HTTP/1.1 404 Not Found\r\n"
                        + "Content-Type: text/plain\r\n"
                        + "Content-Length: " + cuerpo.getBytes().length + "\r\n"
                        + "\r\n"
                        + cuerpo;

                salida.write(respuesta.getBytes());
            }

            salida.flush();
            cliente.close();

        } catch (Exception e) {
            System.out.println("Error al responder métricas: " + e.getMessage());
        }
    }
}