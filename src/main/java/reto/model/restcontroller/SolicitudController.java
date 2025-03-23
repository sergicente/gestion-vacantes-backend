package reto.model.restcontroller;

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
import reto.model.service.SolicitudService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

	@Autowired
	private SolicitudService sservice;
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
		Solicitud solicitud = modelMapper.map(solicitudDto, Solicitud.class);
		Solicitud nuevaSolicitud = sservice.enviarSolicitud(solicitud.getVacante().getIdVacante(),
				solicitud.getUsuario().getEmail(), solicitud);
		SolicitudDto solicitudDtoRespuesta = modelMapper.map(nuevaSolicitud, SolicitudDto.class);
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
