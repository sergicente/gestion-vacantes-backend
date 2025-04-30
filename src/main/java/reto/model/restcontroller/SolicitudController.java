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
import org.springframework.web.bind.annotation.*;
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

    // Obtener todas las solicitudes
    @GetMapping("/todas")
    public ResponseEntity<List<SolicitudDto>> obtenerTodasSolicitudes() {
        List<Solicitud> solicitudes = sservice.buscarTodos();
        List<SolicitudDto> solicitudesDto = solicitudes.stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(solicitudesDto, HttpStatus.OK);
    }

    // Obtener solicitudes por usuario
    @GetMapping("/usuario/{email}")
    public ResponseEntity<List<SolicitudDto>> verSolicitudesPorUsuario(@PathVariable String email) {
        List<Solicitud> solicitudes = sservice.findBySolicitudPorUsuario(email);
        List<SolicitudDto> solicitudesDto = solicitudes.stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(solicitudesDto, HttpStatus.OK);
    }

    // Obtener una solicitud por ID
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDto> obtenerSolicitud(@PathVariable Integer id) {
        Solicitud solicitud = sservice.buscar(id);
        if (solicitud == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        SolicitudDto solicitudDto = modelMapper.map(solicitud, SolicitudDto.class);
        return new ResponseEntity<>(solicitudDto, HttpStatus.OK);
    }

    // Crear una nueva solicitud con archivo
    @PostMapping("/enviarSolicitud")
    public ResponseEntity<SolicitudDto> crearSolicitudConArchivo(
            @RequestParam("email") String email,
            @RequestParam("comentarios") String comentarios,
            @RequestParam("idVacante") Long idVacante,
            @RequestParam("archivo") MultipartFile archivo) {

        Usuario usuario = uservice.buscar(email);
        Vacante vacante = vservice.buscar(idVacante.intValue());

        if (usuario == null || vacante == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String nombreArchivo = archivo.getOriginalFilename();
        Path rutaArchivo = Paths.get(System.getProperty("user.dir"), "uploads", nombreArchivo);

        try {
            Files.createDirectories(rutaArchivo.getParent());
            archivo.transferTo(rutaArchivo.toFile());
            System.out.println("Archivo guardado en: " + rutaArchivo.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("ERROR AL GUARDAR ARCHIVO");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Solicitud nuevaSolicitud = new Solicitud();
        nuevaSolicitud.setFecha(LocalDate.now());
        nuevaSolicitud.setArchivo(nombreArchivo);
        nuevaSolicitud.setCurriculum(nombreArchivo);
        nuevaSolicitud.setComentarios(comentarios);
        nuevaSolicitud.setUsuario(usuario);
        nuevaSolicitud.setVacante(vacante);

        try {
            Solicitud solicitudGuardada = sservice.enviarSolicitud(nuevaSolicitud);
            SolicitudDto respuestaDto = modelMapper.map(solicitudGuardada, SolicitudDto.class);
            return new ResponseEntity<>(respuestaDto, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("ERROR AL GUARDAR LA SOLICITUD");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cancelar una solicitud
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarSolicitud(@PathVariable Integer id) {
        try {
            sservice.cancelarSolicitud(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Ver solicitudes por vacante
    @GetMapping("/vacante/{vacanteId}")
    public ResponseEntity<List<SolicitudDto>> verSolicitudesPorVacante(@PathVariable Integer vacanteId) {
        List<Solicitud> solicitudes = sservice.buscarSolicitudesPorVacante(vacanteId);
        List<SolicitudDto> solicitudesDto = solicitudes.stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(solicitudesDto, HttpStatus.OK);
    }

    // Verificar si ya existe una solicitud
    @GetMapping("/verificar/{idUsuario}/{vacanteId}")
    public Boolean verificarSolicitudExistente(@PathVariable String idUsuario, @PathVariable Integer vacanteId) {
        return sservice.buscarSolicitudExistente(idUsuario, vacanteId);
    }

    // Responder a una solicitud
    @PutMapping("/{id}/responder")
    public ResponseEntity<Void> responderSolicitud(@PathVariable Integer id, @RequestBody String respuesta) {
        Solicitud solicitud = sservice.buscar(id);
        if (solicitud == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        sservice.enviarSolicitud(solicitud); // reutilizando método
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Obtener solicitudes por empresa
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<SolicitudDto>> obtenerSolicitudesPorEmpresa(@PathVariable Integer empresaId) {
        List<Solicitud> solicitudes = sservice.buscarSolicitudesPorEmpresa(empresaId);
        List<SolicitudDto> solicitudesDto = solicitudes.stream()
                .map(s -> modelMapper.map(s, SolicitudDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(solicitudesDto, HttpStatus.OK);
    }

    // Descargar archivo PDF
    @GetMapping("/archivo/{nombre}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable String nombre) {
        try {
            Path ruta = Paths.get(System.getProperty("user.dir"), "uploads", nombre);
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
