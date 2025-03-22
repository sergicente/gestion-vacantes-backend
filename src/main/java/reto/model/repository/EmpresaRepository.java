package reto.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import reto.model.entity.Empresa;
import reto.model.entity.Vacante;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer>{
	@Query("SELECT v FROM Vacante v WHERE v.empresa = :empresa")
	List<Vacante> findVacantesByEmpresa(@Param("empresa") Empresa empresa);
}
