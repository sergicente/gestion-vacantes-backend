package reto.model.restcontroller;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reto.model.dto.EmpresaDto;
import reto.model.dto.VacanteDto;
import reto.model.entity.Empresa;
import reto.model.entity.Usuario;
import reto.model.entity.Vacante;
import reto.model.repository.UsuarioRepository;
import reto.model.service.EmpresaService;
import reto.model.service.UsuarioService;
import reto.model.service.VacanteService;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

	@Autowired
	private EmpresaService eservice;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UsuarioRepository urepo;

	// Obtener todas las empresas
	@GetMapping("/")
	public ResponseEntity<List<EmpresaDto>> obtenerTodasLasEmpresas() {
		List<Empresa> empresas = eservice.buscarTodos();
		List<EmpresaDto> empresasDto = empresas.stream().map(empresa -> modelMapper.map(empresa, EmpresaDto.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(empresasDto, HttpStatus.OK);
	}

	@GetMapping("/con-vacantes")
	public ResponseEntity<List<EmpresaDto>> obtenerEmpresasConVacantes() {
		List<EmpresaDto> empresasDto = eservice.obtenerEmpresasConNumeroVacantes();
		return new ResponseEntity<>(empresasDto, HttpStatus.OK);
	}

	// Obtener una empresa por su ID
	@GetMapping("/{id}")
	public ResponseEntity<EmpresaDto> obtenerEmpresa(@PathVariable Integer id) {
		Empresa empresa = eservice.buscar(id);
		if (empresa == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EmpresaDto empresaDto = modelMapper.map(empresa, EmpresaDto.class);
		return new ResponseEntity<>(empresaDto, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<?> crearEmpresa(@RequestBody EmpresaDto empresaDto) {
		try {
			Empresa empresa = modelMapper.map(empresaDto, Empresa.class);

			empresa.setIdEmpresa(null);

			empresa.setEmailEmpresa(empresaDto.getEmail());

			Empresa nuevaEmpresa = eservice.insertar(empresa);

			EmpresaDto respuesta = modelMapper.map(nuevaEmpresa, EmpresaDto.class);

			return new ResponseEntity<>(respuesta, HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>("Error al crear empresa: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> modificarEmpresa(@PathVariable("id") Integer id, @RequestBody EmpresaDto empresaDto) {
		try {
			Empresa empresa = modelMapper.map(empresaDto, Empresa.class);
			empresa.setIdEmpresa(id); // Este es el campo real en la entidad

			empresa.setEmailEmpresa(empresaDto.getEmail());

			Empresa empresaModificada = eservice.modificar(empresa);
			EmpresaDto respuesta = modelMapper.map(empresaModificada, EmpresaDto.class);

			return new ResponseEntity<>(respuesta, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("Error al modificar empresa: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// Eliminar una empresa
	@DeleteMapping("/empresas/{id}")
	public ResponseEntity<?> eliminarEmpresa(@PathVariable("id") Integer id) {
		int resultado = eservice.borrar(id); 
		switch (resultado) {
		case 1:
			return ResponseEntity.ok().build(); // 200 OK
		case 0:
			return ResponseEntity.notFound().build(); // 404 Not Found
		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar empresa");
		}
	}

	// Obtener todas las vacantes de una empresa (si la empresa tiene vacantes
	// asociadas)
	@GetMapping("/{id}/vacantes")
	public ResponseEntity<List<VacanteDto>> obtenerVacantesDeEmpresa(@PathVariable Integer id) {
		Empresa empresa = eservice.buscar(id);

		// Buscar las vacantes asociadas a esa empresa
		List<Vacante> vacantes = eservice.buscarVacantesPorEmpresa(empresa);

		// Convertir las vacantes a DTOs
		List<VacanteDto> vacantesDto = vacantes.stream().map(vacante -> modelMapper.map(vacante, VacanteDto.class))
				.collect(Collectors.toList());

		// Retornar las vacantes como respuesta
		return new ResponseEntity<>(vacantesDto, HttpStatus.OK);
	}

}
