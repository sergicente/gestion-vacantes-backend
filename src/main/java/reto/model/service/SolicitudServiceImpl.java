package reto.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto.model.entity.Categoria;
import reto.model.entity.Empresa;
import reto.model.entity.Solicitud;
import reto.model.repository.CategoriaRepository;
import reto.model.repository.EmpresaRepository;
import reto.model.repository.SolicitudRepository;
@Service
public class SolicitudServiceImpl implements SolicitudService{
	
	@Autowired
	private SolicitudRepository srepo;

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

}
