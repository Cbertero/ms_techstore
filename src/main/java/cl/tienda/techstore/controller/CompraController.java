package cl.tienda.techstore.controller;

import cl.tienda.techstore.dto.CompraRequest;
import cl.tienda.techstore.exception.RecursoNoEncontradoException;
import cl.tienda.techstore.exception.StockInsuficienteException;
import cl.tienda.techstore.service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    // POST /api/compras (cualquier usuario autenticado)
    @PostMapping
    public ResponseEntity<Void> comprar(@Valid @RequestBody CompraRequest request) {
        compraService.comprar(request);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, String>> sinStock(StockInsuficienteException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(RecursoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
    }
}
