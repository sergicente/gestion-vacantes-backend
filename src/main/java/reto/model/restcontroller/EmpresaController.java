package reto.model.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reto.model.entity.Empresa;
import reto.model.service.EmpresaService;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {
	
	@Autowired
	private EmpresaService eservice;
	
    // Devuelve todas las empresas
    @GetMapping
    public List<Empresa> obtenerTodos() {
        return eservice.buscarTodos();
    }

    // Devuelve una empresa por su id
    @GetMapping("/{id}")
    public Empresa obtenerUno(@PathVariable int id) {
        return eservice.buscar(id);
    }

}
