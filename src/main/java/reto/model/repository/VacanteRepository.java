package reto.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reto.model.entity.Categoria;
import reto.model.entity.Empresa;
import reto.model.entity.Estatus;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;

public interface VacanteRepository extends JpaRepository<Vacante, Integer>{


    List<Vacante> findByEstatus(Estatus estatus);
    List<Vacante> findByCategoria(Categoria categoria);
    List<Vacante> findByEmpresa(Empresa empresa);
    
    @Query("SELECT s FROM Solicitud s WHERE s.vacante.id = :idVacante")
    List<Solicitud> findSolicitudesByVacanteId(@Param("idVacante") Integer idVacante);
    void deleteByEmpresa_IdEmpresa(Integer idEmpresa);
	List<Vacante> findByEmpresa_IdEmpresa(Integer idEmpresa);

}
