package reto.model.service;

import reto.model.entity.Categoria;

public interface CategoriaService extends InterfaceGenericoCrud<Categoria, Integer>{

	Categoria findByNombre(String nombreCategoria);

}
