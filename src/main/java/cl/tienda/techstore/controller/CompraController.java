package cl.tienda.techstore.controller;

import cl.tienda.techstore.dto.CompraRequest;
import cl.tienda.techstore.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Compras", description = "Registro de compras con descuento de stock")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    // Los errores (sin stock 409, producto inexistente 404) los responde GlobalExceptionHandler

    // POST /api/compras (cualquier usuario autenticado)
    @Operation(summary = "Registrar compra y descontar stock (todo o nada). Requiere sesión")
    @PostMapping
    public ResponseEntity<Void> comprar(@Valid @RequestBody CompraRequest request) {
        compraService.comprar(request);
        return ResponseEntity.noContent().build();
    }
}
