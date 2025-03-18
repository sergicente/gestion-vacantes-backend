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
import reto.model.service.CategoriaService;
import reto.model.service.EmpresaService;
import reto.model.service.SolicitudService;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
	
	@Autowired
	private SolicitudService sservice;
	
    // Devuelve todas las empresas
    @GetMapping
    public List<Solicitud> obtenerTodos() {
        return sservice.buscarTodos();
    }

    // Devuelve una empresa por su id
    @GetMapping("/{id}")
    public Solicitud obtenerUno(@PathVariable int id) {
        return sservice.buscar(id);
    }

}
