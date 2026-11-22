package cl.tienda.techstore.service;

import cl.tienda.techstore.dto.AuthResponse;
import cl.tienda.techstore.dto.LoginRequest;
import cl.tienda.techstore.model.Usuario;
import cl.tienda.techstore.repository.UsuarioRepository;
import cl.tienda.techstore.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        String rutLimpio = request.getRut().trim();

        Usuario usuario = usuarioRepository.findByRut(rutLimpio)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con RUT: " + rutLimpio));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new BadCredentialsException("Contraseña incorrecta para el RUT: " + rutLimpio);
        }

        String token = jwtUtil.generarToken(usuario.getRut(), usuario.getRol());

        return AuthResponse.builder()
                .tipo("Bearer")
                .token(token)
                .rut(usuario.getRut())
                .rol(usuario.getRol())
                .expiraEn(jwtUtil.getExpirationMs())
                .mensaje("Inicio de sesión exitoso")
                .build();
    }

    public AuthResponse validarToken(String tokenHeader) {
        String token = tokenHeader;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || !jwtUtil.validarToken(token)) {
            throw new BadCredentialsException("Token JWT inválido o expirado");
        }

        String rut = jwtUtil.extraerRut(token);
        String rol = jwtUtil.extraerRol(token);

        return AuthResponse.builder()
                .tipo("Bearer")
                .token(token)
                .rut(rut)
                .rol(rol)
                .expiraEn(jwtUtil.getExpirationMs())
                .mensaje("Token válido y activo")
                .build();
    }
}