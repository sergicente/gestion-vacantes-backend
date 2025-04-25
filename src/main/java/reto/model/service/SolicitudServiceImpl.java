package reto.model.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto.model.entity.Estatus;
import reto.model.entity.Solicitud;
import reto.model.entity.Usuario;
import reto.model.entity.Vacante;
import reto.model.repository.SolicitudRepository;

@Service
public class SolicitudServiceImpl implements SolicitudService{
	
	@Autowired
	private SolicitudRepository srepo;
	@Autowired
	private UsuarioService userv;
	@Autowired
	private VacanteService vserv;
	

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
			if(srepo.existsById(entidad.getIdSolicitud())) {
				return srepo.save(entidad);
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int borrar(Integer clave) {
		try {
			if(srepo.existsById(clave)) {
				srepo.deleteById(clave);
				return 1;
			}else {
				return 0;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
		
	public Solicitud enviarSolicitud(Solicitud solicitud) {
	    // Usa las entidades ya validadas en el controlador
	    Vacante vacante = solicitud.getVacante();
	    String email = solicitud.getEmail();
	    
	    if (vacante == null || email == null) {
	        throw new IllegalArgumentException("Vacante o usuario no proporcionados");
	    }

	    if (vacante.getEstatus() != Estatus.CREADA) {
	        throw new IllegalArgumentException("La vacante no está disponible");
	    }

	    solicitud.setEstado(0);
	    return srepo.save(solicitud);
	}

	public List <Solicitud> findBySolicitudPorUsuario (String email) {
		return srepo.findByEmail(email);
		}
	
	public void cancelarSolicitud(int idSolicitud) {
	    Solicitud solicitud = srepo.findById(idSolicitud)
	        .orElseThrow();

	    solicitud.setEstado(2);

	    srepo.save(solicitud);
	}
	  public List<Solicitud> buscarSolicitudesPorVacante(Integer idVacante) {
	        Vacante vacante = vserv.buscar(idVacante);  
	        if (vacante == null) {
	            throw new IllegalArgumentException("Vacante no encontrada");
	        }
	        return srepo.findByVacante(vacante);  // Método en el repositorio que filtra por vacante
	    }
	    
}


