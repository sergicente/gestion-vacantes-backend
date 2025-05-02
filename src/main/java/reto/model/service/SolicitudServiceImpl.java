package reto.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto.model.entity.Estatus;
import reto.model.entity.Solicitud;
import reto.model.entity.Usuario;
import reto.model.entity.Vacante;
import reto.model.repository.SolicitudRepository;
import reto.model.repository.VacanteRepository;

@Service
public class SolicitudServiceImpl implements SolicitudService {

	@Autowired
	private SolicitudRepository srepo;
	@Autowired
	private UsuarioService userv;
	@Autowired
	private VacanteService vserv;
	@Autowired
	private VacanteRepository vrepo;

	@Override
	public Solicitud buscar(Integer clave) {
		return srepo.findById(clave).orElse(null);
	}

	@Override
	public List<Solicitud> buscarTodos() {
		return srepo.findAll();
	}

	@Override
	public Solicitud insertar(Solicitud entidad) {
		return srepo.save(entidad);
	}

	@Override
	public Solicitud modificar(Solicitud entidad) {
		try {
			if (srepo.existsById(entidad.getIdSolicitud())) {
				return srepo.save(entidad);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int borrar(Integer clave) {
		try {
			if (srepo.existsById(clave)) {
				srepo.deleteById(clave);
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public Solicitud enviarSolicitud(Solicitud solicitud) {
		Vacante vacante = solicitud.getVacante();
		String email = solicitud.getUsuario().getEmail();

		if (vacante == null || email == null) {
			throw new IllegalArgumentException("Vacante o usuario no proporcionados");
		}

		if (vacante.getEstatus() != Estatus.CREADA) {
			throw new IllegalArgumentException("La vacante no está disponible");
		}

		solicitud.setEstado(0);
		return srepo.save(solicitud);
	}

	@Override
	public List<Solicitud> findBySolicitudPorUsuario(String email) {
		return srepo.findByUsuarioEmail(email);
	}

	@Override
	public void cancelarSolicitud(int idSolicitud) {
		Solicitud solicitud = srepo.findById(idSolicitud).orElseThrow();
		solicitud.setEstado(2);
		srepo.save(solicitud);
	}

	@Override
	public List<Solicitud> buscarSolicitudesPorVacante(Integer idVacante) {
		Vacante vacante = vserv.buscar(idVacante);
		if (vacante == null) {
			throw new IllegalArgumentException("Vacante no encontrada");
		}
		return srepo.findByVacante(vacante);
	}

	@Override
	public Boolean buscarSolicitudExistente(String email, Integer idVacante) {
		return srepo.existsByUsuarioEmailAndVacanteIdVacante(email, idVacante);
	}

	@Override
	public List<Solicitud> buscarSolicitudesPorEmpresa(Integer empresaId) {
		List<Vacante> vacantes = vserv.buscarPorEmpresa(empresaId);
		List<Solicitud> todas = new ArrayList<>();
		for (Vacante v : vacantes) {
			List<Solicitud> solicitudes = srepo.findByVacante(v);
			todas.addAll(solicitudes);
		}
		return todas;
	}

	@Override
	public void aceptarSolicitud(int idSolicitud) {
		Solicitud solicitud = srepo.findById(idSolicitud).orElse(null);
		if (solicitud == null)
			return;

		// Aceptar la solicitud actual
		solicitud.setEstado(1); // ACEPTADA
		srepo.save(solicitud);

		// Cancelar otras solicitudes asociadas a la misma vacante
		List<Solicitud> otrasSolicitudes = srepo.findByVacanteIdVacante(solicitud.getVacante().getIdVacante());
		for (Solicitud s : otrasSolicitudes) {
			if (!Objects.equals(s.getIdSolicitud(), idSolicitud)) {
				s.setEstado(2); // CANCELADA
				srepo.save(s);
			}
		}

		// Marcar la vacante como adjudicada
		Vacante vacante = solicitud.getVacante();
		vacante.setEstatus(Estatus.ASIGNADA); // Asegúrate de que el enum o campo sea correcto
		vrepo.save(vacante);
	}
}
