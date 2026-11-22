package cl.tienda.techstore.dto;

import cl.tienda.techstore.model.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private Integer precio;
    private Integer stock;

    // Convierte una entidad Producto en un ProductoResponse
    public static ProductoResponse desdeEntidad(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}