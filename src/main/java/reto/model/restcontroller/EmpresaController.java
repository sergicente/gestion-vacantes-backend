package reto.model.restcontroller;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reto.model.dto.AltaEmpresaResponseDto;
import reto.model.dto.EmpresaConUsuarioDto;
import reto.model.dto.EmpresaDto;
import reto.model.dto.SolicitudDto;
import reto.model.dto.VacanteDto;
import reto.model.entity.Empresa;
import reto.model.entity.EmpresaYPassword;
import reto.model.entity.Usuario;
import reto.model.entity.Vacante;
import reto.model.service.EmpresaService;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

	@Autowired
	private EmpresaService eservice;
	@Autowired
	private ModelMapper modelMapper;

	// Obtener todas las empresas
	@GetMapping
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

	// Crear una nueva empresa
	@PostMapping
	public ResponseEntity<EmpresaDto> crearEmpresa(@RequestBody EmpresaDto empresaDto) {
		Empresa empresa = modelMapper.map(empresaDto, Empresa.class);
		Empresa nuevaEmpresa = eservice.insertar(empresa);
		EmpresaDto empresaDtoRespuesta = modelMapper.map(nuevaEmpresa, EmpresaDto.class);
		return new ResponseEntity<>(empresaDtoRespuesta, HttpStatus.CREATED);
	}

	// Modificar los datos de una empresa
	@PutMapping("/{id}")
	public ResponseEntity<EmpresaDto> modificarEmpresa(@PathVariable Integer id, @RequestBody EmpresaDto empresaDto) {
		Empresa empresaExistente = eservice.buscar(id);
		if (empresaExistente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Empresa empresa = modelMapper.map(empresaDto, Empresa.class);
		empresa.setIdEmpresa(id);
		Empresa empresaModificada = eservice.modificar(empresa);
		EmpresaDto empresaDtoRespuesta = modelMapper.map(empresaModificada, EmpresaDto.class);
		return new ResponseEntity<>(empresaDtoRespuesta, HttpStatus.OK);
	}

	// Eliminar una empresa
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarEmpresa(@PathVariable Integer id) {
		Empresa empresa = eservice.buscar(id);
		if (empresa == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		eservice.borrar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// Obtener todas las vacantes de una empresa
	@GetMapping("/{id}/vacantes")
	public ResponseEntity<List<VacanteDto>> obtenerVacantesDeEmpresa(@PathVariable Integer id) {
		Empresa empresa = eservice.buscar(id);
		List<Vacante> vacantes = eservice.buscarVacantesPorEmpresa(empresa);
		List<VacanteDto> vacantesDto = vacantes.stream().map(vacante -> modelMapper.map(vacante, VacanteDto.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(vacantesDto, HttpStatus.OK);
	}

	// Crear empresa y usuario asociado con contraseña generada
	@PostMapping("/empresaUsuario")
	public ResponseEntity<AltaEmpresaResponseDto> crearEmpresaConUsuario(@RequestBody EmpresaConUsuarioDto empresaConUsuarioDto) {
	    Empresa empresa = new Empresa();
	    empresa.setCif(empresaConUsuarioDto.getCif());
	    empresa.setNombreEmpresa(empresaConUsuarioDto.getNombreEmpresa());
	    empresa.setDireccionFiscal(empresaConUsuarioDto.getDireccionFiscal());
	    empresa.setPais(empresaConUsuarioDto.getPais());
	    empresa.setEmailEmpresa(empresaConUsuarioDto.getEmail());

	    Usuario usuario = new Usuario();
	    usuario.setEmail(empresaConUsuarioDto.getEmail());
	    usuario.setNombre(empresaConUsuarioDto.getNombreEmpresa());
	    usuario.setApellidos("");

	    EmpresaYPassword resultado = eservice.insertarConUsuario(empresa, usuario);

	    EmpresaDto empresaDto = new EmpresaDto();
	    empresaDto.setIdEmpresa(resultado.getEmpresa().getIdEmpresa());
	    empresaDto.setCif(resultado.getEmpresa().getCif());
	    empresaDto.setNombreEmpresa(resultado.getEmpresa().getNombreEmpresa());
	    empresaDto.setDireccionFiscal(resultado.getEmpresa().getDireccionFiscal());
	    empresaDto.setPais(resultado.getEmpresa().getPais());
	    empresaDto.setEmail(resultado.getEmpresa().getEmailEmpresa());

	    AltaEmpresaResponseDto response = new AltaEmpresaResponseDto();
	    response.setEmpresa(empresaDto);
	    response.setUsuarioEmail(usuario.getEmail());
	    response.setPasswordGenerada(resultado.getPassword());

	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
