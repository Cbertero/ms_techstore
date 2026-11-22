package cl.tienda.techstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    @Builder.Default
    private String tipo = "Bearer";
    private String token;
    private String rut;
    private String rol;
    private Long expiraEn;
    private String mensaje;
}