package reto.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    // Obtiene todas las solicitudes hechas por un usuario a través de su email
    List<Solicitud> findByUsuarioEmail(String email);
    
    // Obtiene todas las solicitudes asociadas a una vacante específica
    List<Solicitud> findByVacante(Vacante vacante);
    
    // Verifica si ya existe una solicitud para una vacante concreta por parte de un usuario dado su email
    boolean existsByUsuarioEmailAndVacanteIdVacante(String email, Integer idVacante);
    
    // Devuelve todas las solicitudes de una vacante, excluyendo una solicitud concreta por su ID
    List<Solicitud> findByVacanteAndIdSolicitudNot(Vacante vacante, int idSolicitud);
	
}

