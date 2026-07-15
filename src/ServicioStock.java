import java.util.HashMap;
import java.util.Map;

// Clase encargada de controlar el stock de productos
public class ServicioStock {

    // Mapa que almacena los productos por nombre
    private Map<String, Producto> productos = new HashMap<>();

    // Métricas del sistema
    private MetricasSistema metricas;

    // Constructor que recibe las métricas
    public ServicioStock(MetricasSistema metricas) {
        this.metricas = metricas;

        // Carga productos iniciales
        productos.put("Empanada", new Producto("Empanada", 10));
        productos.put("Jugo", new Producto("Jugo", 8));
        productos.put("Pan con pollo", new Producto("Pan con pollo", 6));

        // Registra stock inicial en métricas
        metricas.registrarStockInicial("Empanada", 10);
        metricas.registrarStockInicial("Jugo", 8);
        metricas.registrarStockInicial("Pan con pollo", 6);
    }

    // Método sincronizado para evitar conflictos en el stock
    public synchronized boolean descontarStock(String nombreProducto) {

        // Busca el producto solicitado
        Producto producto = productos.get(nombreProducto);

        // Si no existe, devuelve false
        if (producto == null) {
            System.out.println("Producto no encontrado: " + nombreProducto);
            return false;
        }

        // Si hay stock disponible, descuenta una unidad
        if (producto.getStock() > 0) {
            producto.descontarUno();

            // Actualiza métricas de stock
            metricas.actualizarStock(nombreProducto, producto.getStock());

            System.out.println("Stock descontado: "
                    + nombreProducto
                    + " | Stock restante: "
                    + producto.getStock());

            return true;
        }

        // Si no hay stock, rechaza el pedido
        System.out.println("Sin stock disponible para: " + nombreProducto);
        return false;
    }

    // Repone stock de un producto de forma sincronizada
    public synchronized void reponerStock(String nombreProducto, int cantidad) {

        Producto producto = productos.get(nombreProducto);

        if (producto != null) {
            producto.aumentarStock(cantidad);
            metricas.actualizarStock(nombreProducto, producto.getStock());

            System.out.println("Reposición de stock: "
                    + nombreProducto
                    + " +"
                    + cantidad
                    + " | Stock actual: "
                    + producto.getStock());
        }
    }
}