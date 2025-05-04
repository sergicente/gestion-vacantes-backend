package reto.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import reto.model.entity.Categoria;
import reto.model.repository.CategoriaRepository;


@Service
public class CategoriaServiceImpl implements CategoriaService{
	
	@Autowired
	private CategoriaRepository crepo;

	@Override
	public Categoria buscar(Integer clave) {
		return crepo.findById(clave).orElse(null);
	}

	@Override
	public List<Categoria> buscarTodos() {
		return crepo.findAll();
	}

	@Override
	public Categoria insertar(Categoria entidad) {
	    return crepo.save(entidad);
	}

	@Override
	public Categoria modificar(Categoria entidad) {
		try {
			if(crepo.existsById(entidad.getIdCategoria())) {
				return crepo.save(entidad);
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
		if (!crepo.existsById(clave)) {
			return 0;
		}
		
		// Aquí no se captura la excepción, se deja propagar
		crepo.deleteById(clave);
		return 1;
	}

	@Override
	public Categoria findByNombre(String nombreCategoria) {
		// TODO Auto-generated method stub
		return crepo.findByNombre(nombreCategoria);
	}

}
