package reto.model.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto.model.entity.Categoria;
import reto.model.entity.Empresa;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;
import reto.model.entity.Estatus;
import reto.model.repository.VacanteRepository;
@Service
public class VacanteServiceImpl implements VacanteService{
	
	@Autowired
	private VacanteRepository vrepo;
//	@Autowired
//	private SolicitudService sserv;
	@Autowired
	private CategoriaService cserv;

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
    
    
    public void asignarVacante(Vacante vacante, Solicitud solicitud) {
        vacante.setEstatus(Estatus.CUBIERTA);
        solicitud.setEstado(1);
        vrepo.save(vacante);
    }
    
    
    
    public List<Vacante> filtrarVacantesPorCategoria(String nombreCategoria) {
        Categoria categoria = cserv.findByNombre(nombreCategoria);
        return vrepo.findByCategoria(categoria);
    }
    

    @Override
    public List<Vacante> buscarVacantesPorEmpresa(Empresa empresa) {
        return vrepo.findByEmpresa(empresa);
}

	@Override
	public List<Solicitud> buscarSolicitudesPorVacante(Integer idVacante) {
		// TODO Auto-generated method stub
		   return vrepo.findSolicitudesByVacanteId(idVacante);
	}




}
