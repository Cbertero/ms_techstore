package cl.tienda.techstore.controller;

import cl.tienda.techstore.model.Producto;
import cl.tienda.techstore.repository.ProductoRepository;
import cl.tienda.techstore.security.JwtAuthenticationFilter;
import cl.tienda.techstore.security.JwtUtil;
import cl.tienda.techstore.security.SecurityConfig;
import cl.tienda.techstore.service.CompraService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Repositorio simulado: no se conecta a MySQL
@WebMvcTest(CompraController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtil.class, CompraService.class})
class CompraControllerTest {

    private static final String COMPRA = "{\"items\":[{\"productoId\":1,\"cantidad\":2}]}";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockitoBean
    private ProductoRepository repository;

    @Test
    void todosLosRolesPuedenComprar() throws Exception {
        when(repository.descontarStock(1L, 2)).thenReturn(1);
        for (String rol : new String[]{"CLIENTE", "VENDEDOR", "ADMINISTRADOR"}) {
            mvc.perform(compra(rol, COMPRA)).andExpect(status().isNoContent());
        }
    }

    @Test
    void sinSesionNoPuedeComprar() throws Exception {
        mvc.perform(post("/api/compras").contentType(MediaType.APPLICATION_JSON).content(COMPRA))
                .andExpect(status().isForbidden());
        verify(repository, never()).descontarStock(any(), any());
    }

    @Test
    void stockInsuficienteRechazaLaCompra() throws Exception {
        when(repository.findById(1L))
                .thenReturn(Optional.of(new Producto(1L, "Mouse", "d", "Mouse", 1000, 1)));
        mvc.perform(compra("CLIENTE", COMPRA))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("No hay stock suficiente de Mouse"));
    }

    @Test
    void productoInexistente() throws Exception {
        mvc.perform(compra("CLIENTE", COMPRA)).andExpect(status().isNotFound());
    }

    @Test
    void cantidadNegativaNoAumentaStock() throws Exception {
        mvc.perform(compra("CLIENTE", COMPRA.replace(":2}", ":-2}")));
        verify(repository, never()).descontarStock(any(), any());
    }

    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder compra(String rol, String json) {
        return post("/api/compras")
                .header("Authorization", "Bearer " + jwtUtil.generarToken("20456789-1", rol))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }
}
