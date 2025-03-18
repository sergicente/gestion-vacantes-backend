package reto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reto.model.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer>{

}
