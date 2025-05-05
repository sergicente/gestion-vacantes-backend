package reto.model.restcontroller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reto.model.dto.UsuarioDto;
import reto.model.entity.Usuario;
import reto.model.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService uservice;
	 @Autowired
	    private ModelMapper modelMapper;
	
    // Devuelve todas las empresas
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return uservice.buscarTodos();
    }
    
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
    	if(uservice.buscar(usuario.getEmail()) != null) {
    		return new ResponseEntity<>(HttpStatus.CONFLICT);
    	}else {
            Usuario nuevoUsuario = uservice.insertar(usuario); 
            UsuarioDto usuarioDtoRespuesta = modelMapper.map(nuevoUsuario, UsuarioDto.class);  
            return new ResponseEntity<>(usuarioDtoRespuesta, HttpStatus.CREATED);   		
    	}


    }

    // Eliminar un usuario
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
    @PutMapping("/modificar/{email}")
    public ResponseEntity<UsuarioDto> modificarUsuario(@PathVariable String email, @RequestBody UsuarioDto usuarioDto) {
        if (usuarioDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (usuarioDto.getNombre() == null || usuarioDto.getApellidos() == null || usuarioDto.getRol() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Usuario usuarioExistente = uservice.buscar(email);
        if (usuarioExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Mapear los campos actualizados
        usuarioExistente.setNombre(usuarioDto.getNombre());
        usuarioExistente.setApellidos(usuarioDto.getApellidos());
        usuarioExistente.setRol(usuarioDto.getRol());

        // Si se envía una nueva contraseña, actualizarla
        if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isBlank()) {
            usuarioExistente.setPassword(usuarioDto.getPassword()); // Asegúrate de codificarla si usas encoder
        }

        Usuario usuarioModificado = uservice.modificar(usuarioExistente);
        if (usuarioModificado == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        UsuarioDto usuarioDtoRespuesta = modelMapper.map(usuarioModificado, UsuarioDto.class);
        return new ResponseEntity<>(usuarioDtoRespuesta, HttpStatus.OK);
    }
    
 // Habilitar un usuario
    @PutMapping("/habilitar/{email}")
    public ResponseEntity<Void> habilitarUsuario(@PathVariable String email) {
        Usuario usuario = uservice.buscar(email);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        usuario.setEnabled(1);
        uservice.modificar(usuario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Deshabilitar un usuario
    @PutMapping("/deshabilitar/{email}")
    public ResponseEntity<Void> deshabilitarUsuario(@PathVariable String email) {
        Usuario usuario = uservice.buscar(email);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        usuario.setEnabled(0);
        uservice.modificar(usuario);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

 

