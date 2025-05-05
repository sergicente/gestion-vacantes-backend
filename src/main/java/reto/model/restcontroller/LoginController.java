package reto.model.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import reto.model.entity.Usuario;
import reto.model.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class LoginController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario datosLogin, HttpSession session) {
        String email = datosLogin.getEmail();
        String password = datosLogin.getPassword();
        Usuario usuario = usuarioService.buscar(email);

        if (usuario != null && usuario.getPassword().equals(password)) {

            if (usuario.getEnabled() == 0) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("El usuario está deshabilitado. Contacta con un administrador.");
            }

            session.setAttribute("usuarioLogueado", usuario);
            return ResponseEntity.ok(usuario);
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Email o contraseña incorrectos");
    }
}
