package reto.model.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reto.model.entity.Categoria;
import reto.model.entity.Empresa;
import reto.model.service.CategoriaService;
import reto.model.service.EmpresaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
	
	@Autowired
	private CategoriaService cservice;
	
    // Devuelve todas las empresas
    @GetMapping
    public List<Categoria> obtenerTodos() {
        return cservice.buscarTodos();
    }

    // Devuelve una empresa por su id
    @GetMapping("/{id}")
    public Categoria obtenerUno(@PathVariable int id) {
        return cservice.buscar(id);
    }

}
