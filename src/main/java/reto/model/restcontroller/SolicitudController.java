package reto.model.restcontroller;

import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reto.model.dto.SolicitudDto;
import reto.model.entity.Solicitud;
import reto.model.entity.Usuario;
import reto.model.entity.Vacante;
import reto.model.service.SolicitudService;
import reto.model.service.UsuarioService;
import reto.model.service.VacanteService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

	@Autowired
	private SolicitudService sservice;
	@Autowired
	private UsuarioService uservice;
	@Autowired
	private VacanteService vservice;
	@Autowired
	private ModelMapper modelMapper;

	// Endpoint para obtener todas las solicitudes
	@GetMapping
	public ResponseEntity<List<SolicitudDto>> obtenerTodasSolicitudes() {
		List<Solicitud> solicitudes = sservice.buscarTodos();
		List<SolicitudDto> solicitudesDto = solicitudes.stream()
				.map(solicitud -> modelMapper.map(solicitud, SolicitudDto.class)).collect(Collectors.toList());
		return new ResponseEntity<>(solicitudesDto, HttpStatus.OK);
	}

	// Endpoint para obtener una solicitud específica
	@GetMapping("/{id}")
	public ResponseEntity<SolicitudDto> obtenerSolicitud(@PathVariable Integer id) {
		Solicitud solicitud = sservice.buscar(id);
		if (solicitud == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		SolicitudDto solicitudDto = modelMapper.map(solicitud, SolicitudDto.class);
		return new ResponseEntity<>(solicitudDto, HttpStatus.OK);
	}


	// Endpoint para crear una nueva solicitud
	@PostMapping
	public ResponseEntity<SolicitudDto> crearSolicitud(@RequestBody SolicitudDto solicitudDto) {
	    // Comprobamos si los datos necesarios están presentes
	    if (solicitudDto.getEmail() == null || solicitudDto.getIdVacante() == 0) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Error 400 si faltan datos
	    }

	    // Recuperamos las entidades necesarias usando los datos del DTO
	    Usuario usuario = uservice.buscar(solicitudDto.getEmail());
	    Vacante vacante = vservice.buscar(solicitudDto.getIdVacante());

	    // Verificamos si los objetos recuperados existen
	    if (usuario == null || vacante == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Error 404 si no se encuentra el usuario o vacante
	    }

	    // Creamos la nueva solicitud usando las entidades completas
	    Solicitud nuevaSolicitud = new Solicitud();
	    nuevaSolicitud.setUsuario(usuario);
	    nuevaSolicitud.setVacante(vacante);
	    nuevaSolicitud.setFecha(LocalDate.now());
        nuevaSolicitud.setEstado(1); // O lo que sea adecuado

	    // Llamamos al servicio para guardar la solicitud
	    Solicitud solicitudGuardada = sservice.enviarSolicitud(nuevaSolicitud);
	    // Convertimos la entidad guardada en un DTO para la respuesta
	    SolicitudDto solicitudDtoRespuesta = modelMapper.map(solicitudGuardada, SolicitudDto.class);

	    return new ResponseEntity<>(solicitudDtoRespuesta, HttpStatus.CREATED);
	}

		
	

	// Endpoint para cancelar una solicitud
	@PutMapping("/{id}/cancelar")
	public ResponseEntity<Void> cancelarSolicitud(@PathVariable Integer id) {
		try {
			sservice.cancelarSolicitud(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Endpoint para ver las solicitudes de una vacante
	@GetMapping("/vacante/{vacanteId}")
	public ResponseEntity<List<SolicitudDto>> verSolicitudesPorVacante(@PathVariable Integer vacanteId) {
		List<Solicitud> solicitudes = sservice.buscarSolicitudesPorVacante(vacanteId);
		List<SolicitudDto> solicitudesDto = solicitudes.stream()
				.map(solicitud -> modelMapper.map(solicitud, SolicitudDto.class)).collect(Collectors.toList());
		return new ResponseEntity<>(solicitudesDto, HttpStatus.OK);
	}

}
