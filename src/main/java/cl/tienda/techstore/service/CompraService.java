package cl.tienda.techstore.service;

import cl.tienda.techstore.dto.CompraRequest;
import cl.tienda.techstore.exception.RecursoNoEncontradoException;
import cl.tienda.techstore.exception.StockInsuficienteException;
import cl.tienda.techstore.model.Producto;
import cl.tienda.techstore.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final ProductoRepository productoRepository;

    // Todo o nada: si un producto falla, se revierten los descuentos anteriores
    @Transactional
    public void comprar(CompraRequest request) {
        // Orden fijo por id para evitar bloqueos cruzados entre compras simultáneas
        List<CompraRequest.Item> items = request.getItems().stream()
                .sorted(Comparator.comparing(CompraRequest.Item::getProductoId))
                .toList();

        for (CompraRequest.Item item : items) {
            if (productoRepository.descontarStock(item.getProductoId(), item.getCantidad()) == 0) {
                Producto producto = productoRepository.findById(item.getProductoId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "No existe un producto con id " + item.getProductoId()));
                throw new StockInsuficienteException(
                        "No hay stock suficiente de " + producto.getNombre());
            }
        }
    }
}
