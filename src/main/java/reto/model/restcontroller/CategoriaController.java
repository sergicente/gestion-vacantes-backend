package reto.model.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import reto.model.dto.CategoriaDto;
import reto.model.entity.Categoria;
import reto.model.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
	
	@Autowired
	private CategoriaService cservice;
	@Autowired
    private ModelMapper modelMapper;
	
	 @GetMapping
	 public ResponseEntity<List<CategoriaDto>> obtenerTodasLasCategorias() {
	        List<Categoria> categorias = cservice.buscarTodos();
	        List<CategoriaDto> categoriasDto = categorias.stream()
	                .map(categoria -> modelMapper.map(categoria, CategoriaDto.class))
	                .collect(Collectors.toList());
	        return new ResponseEntity<>(categoriasDto, HttpStatus.OK);
	    }

	    // Obtener una categoría por su ID
	    @GetMapping("/{id}")
	    public ResponseEntity<CategoriaDto> obtenerCategoria(@PathVariable Integer id) {
	        Categoria categoria = cservice.buscar(id);
	        if (categoria == null) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	        CategoriaDto categoriaDto = modelMapper.map(categoria, CategoriaDto.class);
	        return new ResponseEntity<>(categoriaDto, HttpStatus.OK);
	    }


	    // Crear una nueva categoría
	    @PostMapping
	    public ResponseEntity<CategoriaDto> crearCategoria(@RequestBody CategoriaDto categoriaDto) {
	        Categoria categoria = modelMapper.map(categoriaDto, Categoria.class);
	        Categoria nuevaCategoria = cservice.insertar(categoria);
	        CategoriaDto categoriaDtoRespuesta = modelMapper.map(nuevaCategoria, CategoriaDto.class);
	        return new ResponseEntity<>(categoriaDtoRespuesta, HttpStatus.CREATED);
	    }

	    // Modificar una categoría existente
	    @PutMapping("/{id}")
	    public ResponseEntity<CategoriaDto> modificarCategoria(@PathVariable Integer id, @RequestBody CategoriaDto categoriaDto) {
	        Categoria categoriaExistente = cservice.buscar(id);
	        if (categoriaExistente == null) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	        Categoria categoria = modelMapper.map(categoriaDto, Categoria.class);
	        categoria.setIdCategoria(id); // Asegurarse de que el ID no se cambie
	        Categoria categoriaModificada = cservice.modificar(categoria);
	        CategoriaDto categoriaDtoRespuesta = modelMapper.map(categoriaModificada, CategoriaDto.class);
	        return new ResponseEntity<>(categoriaDtoRespuesta, HttpStatus.OK);
	    }

	    // Eliminar una categoría
	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> eliminarCategoria(@PathVariable int id) {
	        try {
	        	cservice.borrar(id);
	            return ResponseEntity.noContent().build();
	        } catch (DataIntegrityViolationException e) {
	            return ResponseEntity.status(HttpStatus.CONFLICT)
	                    .body("No se puede eliminar la categoría porque tiene vacantes asociadas.");
	        } catch (Exception e) {
	            return ResponseEntity.internalServerError().body("Error interno al eliminar la categoría.");
	        }
	    }
	}


