package reto.model.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto.model.entity.Categoria;
import reto.model.entity.Empresa;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;
import reto.model.entity.Estatus;
import reto.model.repository.SolicitudRepository;
import reto.model.repository.VacanteRepository;
@Service
public class VacanteServiceImpl implements VacanteService{
	
	@Autowired
	private VacanteRepository vrepo;

	@Autowired
	private CategoriaService cserv;
	
	@Autowired
	private SolicitudRepository srepo;


	@Override
	public Vacante buscar(Integer clave) {
		return vrepo.findById(clave).orElse(null);
	}

	@Override
	public List<Vacante> buscarTodos() {
		return vrepo.findAll();
	}

	@Override
	public Vacante insertar(Vacante entidad) {

	    return vrepo.save(entidad);
	}

	@Override
	public Vacante modificar(Vacante entidad) {
		try {
			if(vrepo.existsById(entidad.getIdVacante())) {
				return vrepo.save(entidad);
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
			if(vrepo.existsById(clave)) {
				vrepo.deleteById(clave);
				
				return 1;
			}else {
				return 0;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	
	}
	public Vacante publicarVacante(Vacante vacante) {
		vacante.setEstatus(Estatus.CREADA);
		return vrepo.save(vacante);
	}
	
    public void eliminarVacante(Vacante vacante) {
        Vacante vacanteExistente = insertar(vacante);
        vacanteExistente.setEstatus((Estatus.CANCELADA));
        vrepo.save(vacanteExistente);
    }
    
    
    public void asignarVacante(Vacante vacante, Solicitud solicitudAsignada) {
        vacante.setEstatus(Estatus.CUBIERTA);
        solicitudAsignada.setEstado(1);

        // Guardar la vacante y la solicitud asignada
        vrepo.save(vacante);
        srepo.save(solicitudAsignada);

        // Obtener y cancelar el resto
        List<Solicitud> otras = srepo.findByVacanteAndIdSolicitudNot(vacante, solicitudAsignada.getIdSolicitud());
        System.out.println("Solicitudes a cancelar: " + otras.size());
        for (Solicitud s : otras) {
            s.setEstado(2);
        }

        srepo.saveAll(otras);
    }
    
    
    
    public List<Vacante> filtrarVacantesPorCategoria(String nombreCategoria) {
        Categoria categoria = cserv.findByNombre(nombreCategoria);
        return vrepo.findByCategoria(categoria);
    }
    


	@Override
	public List<Solicitud> buscarSolicitudesPorVacante(Integer idVacante) {
		   return vrepo.findSolicitudesByVacanteId(idVacante);
	}

	@Override
	public List<Vacante> buscarPorNombreODescripcion(String nombre, String descripcion) {
	    return vrepo.findByEstatusAndNombreContainingIgnoreCaseOrEstatusAndDescripcionContainingIgnoreCase(
	        Estatus.CREADA, nombre,
	        Estatus.CREADA, descripcion
	    );
	}

	@Override
	public List<Vacante> findAllByEmpresaAndEstatus(Integer idEmpresa) {
	    Empresa empresa = new Empresa();
	    empresa.setIdEmpresa(idEmpresa);
	    System.out.println("hola");
	    return vrepo.findAllByEmpresaAndEstatus(empresa, Estatus.CREADA);
	}
	
    @Override
    public List<Vacante> findAllByEmpresa(Empresa empresa) {
        return vrepo.findAllByEmpresa(empresa);
    }

	@Override
	public void cambiarEstado(int idVacante, Estatus nuevoEstado) {
		Vacante v = vrepo.findById(idVacante).orElse(null);
		if(v != null) {
			v.setEstatus(nuevoEstado);
			vrepo.save(v);
		}
		
		
	}






}
