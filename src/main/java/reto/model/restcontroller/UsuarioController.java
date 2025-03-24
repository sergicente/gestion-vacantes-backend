package reto.model.restcontroller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import proyectos.modelo.entity.Factura;
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
        Usuario nuevoUsuario = uservice.insertar(usuario); 
        UsuarioDto usuarioDtoRespuesta = modelMapper.map(nuevoUsuario, UsuarioDto.class);  
        return new ResponseEntity<>(usuarioDtoRespuesta, HttpStatus.CREATED); 
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
    
    
    
    @PutMapping("/modificar")
	public ResponseEntity<?> modificarUsuario(@RequestBody Usuario usuario) {
		try {
			Usuario usuarioModificado = uservice.modificar(usuario);
			if (usuarioModificado != null) {
				return new ResponseEntity<>(usuarioModificado, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error al modificar el Usuario", HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

}

 

