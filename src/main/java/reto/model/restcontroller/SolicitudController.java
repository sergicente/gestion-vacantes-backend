package reto.model.restcontroller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import reto.model.dto.SolicitudDto;
import reto.model.entity.Solicitud;
import reto.model.entity.Usuario;
import reto.model.entity.Vacante;
import reto.model.service.SolicitudService;
import reto.model.service.UsuarioService;
import reto.model.service.VacanteService;

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
	@GetMapping("/todas")
	public ResponseEntity<List<SolicitudDto>> obtenerTodasSolicitudes() {
		List<Solicitud> solicitudes = sservice.buscarTodos();
		List<SolicitudDto> solicitudesDto = solicitudes.stream()
				.map(solicitud -> modelMapper.map(solicitud, SolicitudDto.class)).collect(Collectors.toList());
		return new ResponseEntity<>(solicitudesDto, HttpStatus.OK);
	}
	
	// Endpoint para obtener todas las solicitudes de un usuario concreto
	@GetMapping("/usuario/{email}")
	public ResponseEntity<List<SolicitudDto>> verSolicitudesPorUsuario(@PathVariable String email) {
	    List<Solicitud> solicitudes = sservice.findBySolicitudPorUsuario(email);
	    List<SolicitudDto> solicitudesDto = solicitudes.stream()
	            .map(solicitud -> modelMapper.map(solicitud, SolicitudDto.class))
	            .collect(Collectors.toList());
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

	@PostMapping("/enviarSolicitud")
	public ResponseEntity<SolicitudDto> crearSolicitudConArchivo(
	    @RequestParam("email") String email,
	    @RequestParam("comentarios") String comentarios,
	    @RequestParam("idVacante") Long idVacante,
	    @RequestParam("archivo") MultipartFile archivo
	) {
	    Usuario usuario = uservice.buscar(email);
	    Vacante vacante = vservice.buscar(idVacante.intValue());

	    if (usuario == null || vacante == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    // Guardar archivo en disco
	    String nombreArchivo = archivo.getOriginalFilename();
	    Path rutaArchivo = Paths.get("/home/ubuntu/uploads", nombreArchivo);
	    try {
	        Files.createDirectories(rutaArchivo.getParent()); 
	        archivo.transferTo(rutaArchivo.toFile()); 
	        System.out.println("Archivo guardado en: " + rutaArchivo.toAbsolutePath());
	    } catch (IOException e) {
	        System.err.println("ERROR AL GUARDAR ARCHIVO");
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    // Crear solicitud
	    Solicitud nuevaSolicitud = new Solicitud();
	    nuevaSolicitud.setFecha(LocalDate.now());
	    nuevaSolicitud.setArchivo(nombreArchivo); // nombre del archivo guardado
	    nuevaSolicitud.setCurriculum(nombreArchivo); // si usas otro campo
	    nuevaSolicitud.setComentarios(comentarios);
	    nuevaSolicitud.setUsuario(usuario);
	    nuevaSolicitud.setVacante(vacante);

	    try {
	        Solicitud solicitudGuardada = sservice.enviarSolicitud(nuevaSolicitud);

	        SolicitudDto respuestaDto = new SolicitudDto();
	        respuestaDto.setIdSolicitud(solicitudGuardada.getIdSolicitud());
	        respuestaDto.setFecha(solicitudGuardada.getFecha());
	        respuestaDto.setArchivo(solicitudGuardada.getArchivo());
	        respuestaDto.setComentarios(solicitudGuardada.getComentarios());
	        respuestaDto.setCurriculum(solicitudGuardada.getCurriculum());
	        respuestaDto.setEstado(solicitudGuardada.getEstado());
	        respuestaDto.setIdVacante(solicitudGuardada.getVacante().getIdVacante());
	        respuestaDto.setEmail(solicitudGuardada.getUsuario().getEmail());
	        return new ResponseEntity<>(respuestaDto, HttpStatus.CREATED);

	    } catch (Exception e) {
	        System.err.println("ERROR AL GUARDAR LA SOLICITUD");
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
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
	
	@PutMapping("/{vacanteId}/asignar/{solicitudId}")
	public ResponseEntity<Void> asignarSolicitud(@PathVariable Integer vacanteId, @PathVariable Integer solicitudId) {
		try {
			Vacante vacante = vservice.buscar(vacanteId);
			Solicitud solicitud = sservice.buscar(solicitudId);
			vservice.asignarVacante(vacante, solicitud);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Endpoint para ver las solicitudes de una vacante
	@GetMapping("/vacante/{vacanteId}")
	public ResponseEntity<List<SolicitudDto>> verSolicitudesPorVacante(@PathVariable Integer vacanteId) {
	    List<Solicitud> solicitudes = sservice.buscarSolicitudesPorVacante(vacanteId);

	    List<SolicitudDto> solicitudesDto = solicitudes.stream().map(solicitud -> {
	        SolicitudDto dto = modelMapper.map(solicitud, SolicitudDto.class);

	        // Añadir manualmente el email del usuario
	        if (solicitud.getUsuario() != null) {
	            dto.setEmail(solicitud.getUsuario().getEmail());
	        }

	        // Añadir manualmente el nombre de la vacante
	        if (solicitud.getVacante() != null) {
	            dto.setNombreVacante(solicitud.getVacante().getNombre());
	        }

	        return dto;
	    }).collect(Collectors.toList());

	    return new ResponseEntity<>(solicitudesDto, HttpStatus.OK);
	}
	
	// Endpoint para ver si ya existe una solicitud
	@GetMapping("/verificar/{idUsuario}/{vacanteId}")
	public Boolean verificarSolicitudExistente(@PathVariable String idUsuario, @PathVariable Integer vacanteId) {
		return sservice.buscarSolicitudExistente(idUsuario, vacanteId);
	}

	
	@GetMapping("/archivo/{nombre}")
	public ResponseEntity<Resource> descargarArchivo(@PathVariable String nombre) {
	    try {
	    	Path ruta = Paths.get("/home/ubuntu/uploads").resolve(nombre).normalize();
	    	Resource archivo = new UrlResource(ruta.toUri());

	        if (!archivo.exists()) {
	            return ResponseEntity.notFound().build();
	        }

	        return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(archivo);
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().build();
	    }
	}
}
