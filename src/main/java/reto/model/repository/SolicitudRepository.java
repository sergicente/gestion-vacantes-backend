package reto.model.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer>{

	 @Query("SELECT s FROM Solicitud s WHERE s.usuario.email = :email")
	    List<Solicitud> findByUsuarioEmail(@Param("email") String email);
    List<Solicitud> findByVacante(Vacante vacante);
    
    List<Solicitud> findByVacanteEmpresaIdEmpresa(int idEmpresa);


}
