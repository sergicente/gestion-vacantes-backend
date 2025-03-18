package reto.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto.model.entity.Categoria;
import reto.model.entity.Empresa;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;
import reto.model.repository.CategoriaRepository;
import reto.model.repository.EmpresaRepository;
import reto.model.repository.SolicitudRepository;
import reto.model.repository.VacanteRepository;
@Service
public class VacanteServiceImpl implements VacanteService{
	
	@Autowired
	private VacanteRepository vrepo;

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

}
