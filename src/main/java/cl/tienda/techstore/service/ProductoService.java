package cl.tienda.techstore.service;


import cl.tienda.techstore.dto.ProductoRequest;
import cl.tienda.techstore.dto.ProductoResponse;
import cl.tienda.techstore.exception.RecursoNoEncontradoException;
import cl.tienda.techstore.model.Producto;
import cl.tienda.techstore.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    // ---------- Consultas ----------

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(ProductoResponse::desdeEntidad)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorId(Long id) {
        return ProductoResponse.desdeEntidad(obtenerProducto(id));
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoriaIgnoreCase(categoria)
                .stream()
                .map(ProductoResponse::desdeEntidad)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(ProductoResponse::desdeEntidad)
                .toList();
    }

    // ---------- Escritura ----------

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        Producto producto = new Producto();
        copiarDatos(request, producto);
        return ProductoResponse.desdeEntidad(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = obtenerProducto(id);
        copiarDatos(request, producto);
        return ProductoResponse.desdeEntidad(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizarStock(Long id, Integer nuevoStock) {
        Producto producto = obtenerProducto(id);
        producto.setStock(nuevoStock);
        return ProductoResponse.desdeEntidad(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe un producto con id " + id);
        }
        productoRepository.deleteById(id);
    }

    // ---------- Métodos auxiliares ----------

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un producto con id " + id));
    }

    private void copiarDatos(ProductoRequest request, Producto producto) {
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setCategoria(request.getCategoria());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
    }
}