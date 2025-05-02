package reto.model.restcontroller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reto.model.dto.UsuarioDto;
import reto.model.entity.Empresa;
import reto.model.entity.Usuario;
import reto.model.repository.EmpresaRepository;
import reto.model.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService uservice;

    @Autowired
    private ModelMapper modelMapper;

    // Devuelve todos los usuarios
	

	 
	 @Autowired
	 private EmpresaRepository empresaRepository;
	
    // Devuelve todas las empresas
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return uservice.buscarTodos();
    }

    // Devuelve un usuario por su email
    @GetMapping("/{email}")
    public ResponseEntity<UsuarioDto> obtenerUsuarioPorId(@PathVariable String email) {
        Usuario usuario = uservice.buscar(email);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  
        }
        UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class); 
        return new ResponseEntity<>(usuarioDto, HttpStatus.OK);  
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UsuarioDto> crearUsuario(@RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class); 
        if (uservice.buscar(usuario.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);  // Ya existe un usuario con ese email
        } else {
            Usuario nuevoUsuario = uservice.insertar(usuario); 
            UsuarioDto usuarioDtoRespuesta = modelMapper.map(nuevoUsuario, UsuarioDto.class);  
            return new ResponseEntity<>(usuarioDtoRespuesta, HttpStatus.CREATED);   
        }
    }

    // Eliminar un usuario por email
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String email) {
        Usuario usuario = uservice.buscar(email);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  
        }
        uservice.borrar(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
    }

    // Modificar un usuario existente
    @PutMapping("/{email}")
    public ResponseEntity<UsuarioDto> modificarUsuario(@PathVariable String email, @RequestBody UsuarioDto usuarioDto) {
        if (usuarioDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request si el DTO es null
        }

        if (usuarioDto.getNombre() == null || usuarioDto.getApellidos() == null || usuarioDto.getRol() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request si falta algún campo esencial
        }

        Usuario usuarioExistente = uservice.buscar(email); 
        if (usuarioExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // 404 si el usuario no existe
        }

        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class); 
        usuario.setEmail(email);  // Asegurarse de que el email no se sobrescriba

        Usuario usuarioModificado = uservice.modificar(usuario);  
        if (usuarioModificado == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // 500 si la modificación falla
        }

        UsuarioDto usuarioDtoRespuesta = modelMapper.map(usuarioModificado, UsuarioDto.class);  
        return new ResponseEntity<>(usuarioDtoRespuesta, HttpStatus.OK);  // 200 OK si la modificación es exitosa
    }
    
    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> loginUsuario(@RequestBody UsuarioDto usuarioLoginDto) {
        Usuario usuario = uservice.buscar(usuarioLoginDto.getEmail());

        if (usuario == null || !usuario.getPassword().equals(usuarioLoginDto.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class);

        if ("EMPRESA".equals(usuario.getRol())) {
            List<Empresa> empresas = empresaRepository.findByEmailEmpresa(usuario.getEmail());
            if (!empresas.isEmpty()) {
                usuarioDto.setIdEmpresa(empresas.get(0).getIdEmpresa());
            }
        }

        return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
    }


    // Deshabilitar usuario
    @PutMapping("/deshabilitar/{email}")
    public ResponseEntity<String> deshabilitarUsuario(@PathVariable String email) {
        email = URLDecoder.decode(email, StandardCharsets.UTF_8); 
        System.out.println("Email recibido: " + email); 

        Optional<Usuario> usuarioOpt = uservice.buscarUsuarioPorEmail(email); 
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEnabled(0);  // Deshabilitar usuario
            uservice.actualizarUsuario(usuario);  // Guardar cambios
            return ResponseEntity.ok("Usuario deshabilitado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    // Habilitar usuario
    @PutMapping("/habilitar/{email}")
    public ResponseEntity<String> habilitarUsuario(@PathVariable String email) {
        email = URLDecoder.decode(email, StandardCharsets.UTF_8);

        boolean resultado = uservice.habilitarUsuarioPorEmail(email);
        if (resultado) {
            return ResponseEntity.ok("Usuario habilitado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }
}