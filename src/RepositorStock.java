import java.util.Random;

// Clase que repone stock cada cierto tiempo
public class RepositorStock implements Runnable {

    private ServicioStock servicioStock;
    private Random random = new Random();

    private String[] productos = {
            "Empanada", "Jugo", "Pan con pollo"
    };

    public RepositorStock(ServicioStock servicioStock) {
        this.servicioStock = servicioStock;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                Thread.sleep(8000);

                String producto = productos[random.nextInt(productos.length)];
                int cantidad = random.nextInt(4) + 1;

                servicioStock.reponerStock(producto, cantidad);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
