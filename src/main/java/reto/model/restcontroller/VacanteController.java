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
import reto.model.entity.Vacante;
import reto.model.service.CategoriaService;
import reto.model.service.EmpresaService;
import reto.model.service.SolicitudService;
import reto.model.service.UsuarioService;
import reto.model.service.VacanteService;

@RestController
@RequestMapping("/api/vacantes")
public class VacanteController {
	
	@Autowired
	private VacanteService vservice;
	
    // Devuelve todas las empresas
    @GetMapping
    public List<Vacante> obtenerTodos() {
        return vservice.buscarTodos();
    }

    // Devuelve una empresa por su id
    @GetMapping("/{id}")
    public Vacante obtenerUno(@PathVariable int id) {
        return vservice.buscar(id);
    }

}
