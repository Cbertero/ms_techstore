package cl.tienda.techstore.security;

import cl.tienda.techstore.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Carga los usuarios desde la tabla "usuario" de MySQL.
// Al existir este bean, Spring ya no crea el usuario en memoria con contraseña generada.
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String rut) throws UsernameNotFoundException {
        return usuarioRepository.findByRut(rut)
                .map(usuario -> User.withUsername(usuario.getRut())
                        .password(usuario.getContrasena())
                        .roles(usuario.getRol())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con RUT: " + rut));
    }
}
