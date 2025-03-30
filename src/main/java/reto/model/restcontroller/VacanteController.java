package reto.model.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

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
import reto.model.dto.SolicitudDto;
import reto.model.dto.VacanteDto;
import reto.model.dto.VacanteInputDto;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;
import reto.model.entity.Categoria;
import reto.model.entity.Empresa;
import reto.model.entity.Estatus;
import reto.model.service.SolicitudService;
import reto.model.service.VacanteService;

@RestController
@RequestMapping("/api/vacantes")
@CrossOrigin(origins = "*")
public class VacanteController {
	
	@Autowired
	private VacanteService vservice;
	@Autowired
	private SolicitudService sservice;
	  @Autowired
	private ModelMapper modelMapper;

	
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

    @PostMapping
    public ResponseEntity<VacanteDto> crearVacante(@RequestBody VacanteInputDto vacanteInputDto) {
        
    	Categoria categoria = new Categoria();
    	categoria.setIdCategoria(vacanteInputDto.getIdCategoria());
    	
    	Empresa empresa = new Empresa();
    	empresa.setIdEmpresa(vacanteInputDto.getIdEmpresa());
    	
    	Vacante vacante = new Vacante();
    	vacante.setNombre(vacanteInputDto.getNombre());
    	vacante.setDescripcion(vacanteInputDto.getDescripcion());
    	vacante.setFecha(vacanteInputDto.getFecha());
    	vacante.setSalario(vacanteInputDto.getSalario());
    	vacante.setEstatus(Estatus.CREADA);
    	vacante.setImagen(vacanteInputDto.getImagen());
    	vacante.setDetalles(vacanteInputDto.getDetalles());
    	vacante.setCategoria(categoria);
    	vacante.setEmpresa(empresa);
    	
        Vacante nuevaVacante = vservice.publicarVacante(vacante);
        VacanteDto vacanteDtoRespuesta = modelMapper.map(nuevaVacante, VacanteDto.class); // Convertir de vuelta a DTO
        return new ResponseEntity<>(vacanteDtoRespuesta, HttpStatus.CREATED);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<VacanteDto>> obtenerTodasLasVacantes() {
        List<Vacante> vacantes = vservice.buscarTodos();
        List<VacanteDto> vacantesDto = vacantes.stream()
                .map(vacante -> modelMapper.map(vacante, VacanteDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(vacantesDto, HttpStatus.OK);
    }

 
    @PostMapping("/{vacanteId}/asignar/{solicitudId}")
    public ResponseEntity<Void> asignarVacanteACandidato(@PathVariable Integer vacanteId, @PathVariable Integer solicitudId) {
        
    	Vacante vacante = vservice.buscar(vacanteId);
    	Solicitud solicitud = sservice.buscar(solicitudId);

    	if (vacante != null && solicitud != null) {
    		vservice.asignarVacante(vacante, solicitud);
    	    solicitud.setEstado(1);
    	    sservice.modificar(solicitud);
    	}
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/{idVacante}")
    public ResponseEntity<VacanteDto> modificarVacante(@PathVariable Integer idVacante, @RequestBody VacanteDto vacanteDto) {
        Vacante vacanteExistente = vservice.buscar(idVacante);
        if (vacanteExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Vacante vacante = modelMapper.map(vacanteDto, Vacante.class);
        vacante.setIdVacante(idVacante);
        Vacante vacanteActualizada = vservice.modificar(vacante);
        VacanteDto vacanteDtoRespuesta = modelMapper.map(vacanteActualizada, VacanteDto.class);
        return new ResponseEntity<>(vacanteDtoRespuesta, HttpStatus.OK);
    }

    // Cancelar vacante (cambiar el estado a CANCELADA)
    @DeleteMapping("/{idVacante}")
    public ResponseEntity<Void> cancelarVacante(@PathVariable Integer idVacante) {
        Vacante vacante = vservice.buscar(idVacante);
        if (vacante == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        vservice.eliminarVacante(vacante);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Ver solicitudes de una vacante específica
    @GetMapping("/{idVacante}/solicitudes")
    public ResponseEntity<List<SolicitudDto>> verSolicitudes(@PathVariable Integer idVacante) {
        List<Solicitud> solicitudes = vservice.buscarSolicitudesPorVacante(idVacante);
        List<SolicitudDto> solicitudesDto = solicitudes.stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(solicitudesDto, HttpStatus.OK);
    }
}


