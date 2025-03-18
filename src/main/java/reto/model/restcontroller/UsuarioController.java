package reto.model.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reto.model.entity.Categoria;
import reto.model.entity.Empresa;
import reto.model.entity.Solicitud;
import reto.model.entity.Usuario;
import reto.model.service.CategoriaService;
import reto.model.service.EmpresaService;
import reto.model.service.SolicitudService;
import reto.model.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService uservice;
	
    // Devuelve todas las empresas
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return uservice.buscarTodos();
    }

    // Devuelve una empresa por su id
    @GetMapping("/{id}")
    public Usuario obtenerUno(@PathVariable String id) {
        return uservice.buscar(id);
    }

}
