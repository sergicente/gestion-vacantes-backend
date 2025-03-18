package reto.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto.model.entity.Empresa;
import reto.model.repository.EmpresaRepository;
@Service
public class EmpresaServiceImpl implements EmpresaService{
	
	@Autowired
	private EmpresaRepository erepo;

	@Override
	public Empresa buscar(Integer clave) {
		return erepo.findById(clave).orElse(null);
	}

	@Override
	public List<Empresa> buscarTodos() {
		return erepo.findAll();
	}

	@Override
	public Empresa insertar(Empresa entidad) {
	    return erepo.save(entidad);
	}

	@Override
	public Empresa modificar(Empresa entidad) {
		try {
			if(erepo.existsById(entidad.getIdEmpresa())) {
				return erepo.save(entidad);
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
			if(erepo.existsById(clave)) {
				erepo.deleteById(clave);
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
