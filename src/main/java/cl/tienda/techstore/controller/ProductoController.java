package cl.tienda.techstore.controller;



import cl.tienda.techstore.dto.ProductoRequest;
import cl.tienda.techstore.dto.ProductoResponse;
import cl.tienda.techstore.dto.StockRequest;
import cl.tienda.techstore.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Productos", description = "Catálogo y CRUD de productos")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // GET /api/productos
    // GET /api/productos?categoria=Mouse
    // GET /api/productos?nombre=logitech
    @Operation(summary = "Listar productos (filtros opcionales: categoria, nombre). Público")
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String nombre) {

        List<ProductoResponse> productos;

        if (categoria != null && !categoria.isBlank()) {
            productos = productoService.buscarPorCategoria(categoria);
        } else if (nombre != null && !nombre.isBlank()) {
            productos = productoService.buscarPorNombre(nombre);
        } else {
            productos = productoService.listarTodos();
        }

        return ResponseEntity.ok(productos);
    }

    // GET /api/productos/5
    @Operation(summary = "Obtener un producto por id. Público")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    // POST /api/productos
    @Operation(summary = "Crear producto. Solo ADMINISTRADOR")
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse creado = productoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/productos/5
    // Solo ADMINISTRADOR puede cambiar precio o stock
    @Operation(summary = "Actualizar producto. ADMINISTRADOR o VENDEDOR (vendedor no puede cambiar precio ni stock)")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request,
            Authentication authentication) {
        boolean esAdministrador = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMINISTRADOR".equals(a.getAuthority()));
        return ResponseEntity.ok(productoService.actualizar(id, request, esAdministrador));
    }

    // PATCH /api/productos/5/stock
    @Operation(summary = "Ajustar stock. Solo ADMINISTRADOR")
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoResponse> actualizarStock(
            @PathVariable Long id,
            @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(productoService.actualizarStock(id, request.getStock()));
    }

    // DELETE /api/productos/5
    @Operation(summary = "Eliminar producto. Solo ADMINISTRADOR")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}