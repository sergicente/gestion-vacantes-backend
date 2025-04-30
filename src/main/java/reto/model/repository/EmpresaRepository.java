package reto.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import reto.model.dto.EmpresaDto;
import reto.model.entity.Empresa;
import reto.model.entity.Vacante;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
	@Query("SELECT v FROM Vacante v WHERE v.empresa = :empresa")
	List<Vacante> findVacantesByEmpresa(@Param("empresa") Empresa empresa);

	@Query("SELECT new reto.model.dto.EmpresaDto(e.idEmpresa, e.cif, e.nombreEmpresa, e.direccionFiscal, e.pais, e.emailEmpresa, COUNT(v)) "
			+ "FROM Empresa e LEFT JOIN Vacante v ON v.empresa = e "
			+ "GROUP BY e.idEmpresa, e.cif, e.nombreEmpresa, e.direccionFiscal, e.pais, e.emailEmpresa")
	List<EmpresaDto> obtenerEmpresasConNumeroVacantes();
	
	List <Empresa> findByEmailEmpresa(String emailEmpresa);


}
