// Clase que representa un producto de la cafetería
public class Producto {

    // Nombre del producto
    private String nombre;

    // Cantidad disponible del producto
    private int stock;

    // Constructor para crear un producto con nombre y stock inicial
    public Producto(String nombre, int stock) {
        this.nombre = nombre;
        this.stock = stock;
    }

    // Devuelve el nombre del producto
    public String getNombre() {
        return nombre;
    }

    // Devuelve el stock actual del producto
    public int getStock() {
        return stock;
    }

    // Descuenta una unidad del producto
    public void descontarUno() {
        stock--;
    }
}
