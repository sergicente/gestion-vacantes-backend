package reto.model.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class VacanteController {

    @Autowired
    private VacanteService vservice;
    @Autowired
    private SolicitudService sservice;
    @Autowired
    private ModelMapper modelMapper;

    // Devuelve todas las vacantes
    @GetMapping
    public List<Vacante> obtenerTodos() {
        return vservice.buscarTodos();
    }

    @GetMapping("/creadas")
    public List<Vacante> obtenerTodasLasVacantesCreadas() {
        List<Vacante> vacantes = vservice.buscarTodos();
        List<Vacante> vacantesCreadas = new java.util.ArrayList<>();

        for (Vacante v : vacantes) {
            if (v.getEstatus() == Estatus.CREADA) {
                vacantesCreadas.add(v);
            }
        }

        return vacantesCreadas;
    }

    // Devuelve una vacante por su id
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
        vacante.setDestacado(vacanteInputDto.isDestacado()); 


        Vacante nuevaVacante = vservice.publicarVacante(vacante);
        VacanteDto vacanteDtoRespuesta = modelMapper.map(nuevaVacante, VacanteDto.class);
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
    public ResponseEntity<VacanteDto> modificarVacante(@PathVariable Integer idVacante,
                                                       @RequestBody VacanteInputDto vacanteInputDto) {
        Vacante vacanteExistente = vservice.buscar(idVacante);
        if (vacanteExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Actualizar campos de la vacante existente
        vacanteExistente.setNombre(vacanteInputDto.getNombre());
        vacanteExistente.setDescripcion(vacanteInputDto.getDescripcion());
        vacanteExistente.setFecha(vacanteInputDto.getFecha());
        vacanteExistente.setSalario(vacanteInputDto.getSalario());
        vacanteExistente.setImagen(vacanteInputDto.getImagen());
        vacanteExistente.setDetalles(vacanteInputDto.getDetalles());

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(vacanteInputDto.getIdCategoria());
        vacanteExistente.setCategoria(categoria);

        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(vacanteInputDto.getIdEmpresa());
        vacanteExistente.setEmpresa(empresa);

        Vacante vacanteActualizada = vservice.modificar(vacanteExistente);
        VacanteDto respuesta = modelMapper.map(vacanteActualizada, VacanteDto.class);

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }


    // Cancelar vacante
    @DeleteMapping("/borrar/{idVacante}")
    public ResponseEntity<Void> borrarVacante(@PathVariable Integer idVacante) {
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

    // Buscar vacantes por nombre o descripción
    @GetMapping("/buscar")
    public ResponseEntity<List<VacanteDto>> buscarVacantes(@RequestParam String termino) {
        List<Vacante> vacantes = vservice.buscarPorNombreODescripcion(termino, termino);
        List<VacanteDto> vacantesDto = vacantes.stream()
                .map(v -> modelMapper.map(v, VacanteDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(vacantesDto, HttpStatus.OK);
    }

    // Obtener vacantes por empresa
    @GetMapping("/empresa/{idEmpresa}")
    public ResponseEntity<List<VacanteDto>> obtenerVacantesDeEmpresa(@PathVariable Integer idEmpresa) {
        List<Vacante> vacantes = vservice.buscarPorEmpresa(idEmpresa);
        List<VacanteDto> vacantesDto = vacantes.stream()
                .map(vacante -> modelMapper.map(vacante, VacanteDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(vacantesDto, HttpStatus.OK);
    }
}
