// Clase que representa un pedido realizado por un estudiante
public class Pedido {

    // Identificador único del pedido
    private int id;

    // Nombre del estudiante que realiza el pedido
    private String cliente;

    // Nombre del producto solicitado
    private String producto;

    // Constructor para crear un pedido con sus datos principales
    public Pedido(int id, String cliente, String producto) {
        this.id = id;
        this.cliente = cliente;
        this.producto = producto;
    }

    // Devuelve el identificador del pedido
    public int getId() {
        return id;
    }

    // Devuelve el nombre del cliente
    public String getCliente() {
        return cliente;
    }

    // Devuelve el producto solicitado
    public String getProducto() {
        return producto;
    }
}
