package cl.tienda.techstore.controller;

import cl.tienda.techstore.model.Producto;
import cl.tienda.techstore.repository.ProductoRepository;
import cl.tienda.techstore.security.JwtAuthenticationFilter;
import cl.tienda.techstore.security.JwtUtil;
import cl.tienda.techstore.security.SecurityConfig;
import cl.tienda.techstore.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Repositorio simulado: no se conecta a MySQL
@WebMvcTest(ProductoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtil.class, ProductoService.class})
class ProductoPermisosTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockitoBean
    private ProductoRepository repository;

    @BeforeEach
    void preparar() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(new Producto(1L, "Mouse", "desc", "Mouse", 1000, 5)));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void vendedorNoPuedeCambiarPrecio() throws Exception {
        editar("VENDEDOR", 2000, 5, 403);
        verify(repository, never()).save(any());
    }

    @Test
    void vendedorNoPuedeCambiarStock() throws Exception {
        editar("VENDEDOR", 1000, 6, 403);
        verify(repository, never()).save(any());
    }

    @Test
    void vendedorNoPuedeAjustarStock() throws Exception {
        ajustarStock("VENDEDOR", 403);
        verify(repository, never()).save(any());
    }

    @Test
    void vendedorPuedeEditarOtrosDatos() throws Exception {
        editar("VENDEDOR", 1000, 5, 200);
    }

    @Test
    void administradorPuedeCambiarPrecioYStock() throws Exception {
        editar("ADMINISTRADOR", 2000, 6, 200);
        ajustarStock("ADMINISTRADOR", 200);
    }

    @Test
    void vendedorNoPuedeCrear() throws Exception {
        crear("VENDEDOR", 403);
        verify(repository, never()).save(any());
    }

    @Test
    void administradorPuedeCrear() throws Exception {
        crear("ADMINISTRADOR", 201);
    }

    private void crear(String rol, int esperado) throws Exception {
        mvc.perform(post("/api/productos")
                        .header("Authorization", "Bearer " + jwtUtil.generarToken("12345678-5", rol))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Nuevo","descripcion":"d","categoria":"Mouse","precio":1000,"stock":5}
                                """))
                .andExpect(status().is(esperado));
    }

    private void editar(String rol, int precio, int stock, int esperado) throws Exception {
        mvc.perform(put("/api/productos/1")
                        .header("Authorization", "Bearer " + jwtUtil.generarToken("12345678-5", rol))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Nuevo","descripcion":"d","categoria":"Mouse","precio":%d,"stock":%d}
                                """.formatted(precio, stock)))
                .andExpect(status().is(esperado));
    }

    private void ajustarStock(String rol, int esperado) throws Exception {
        mvc.perform(patch("/api/productos/1/stock")
                        .header("Authorization", "Bearer " + jwtUtil.generarToken("12345678-5", rol))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stock\":9}"))
                .andExpect(status().is(esperado));
    }
}
