package reto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reto.model.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{

}
