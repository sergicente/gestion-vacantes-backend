package reto.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    List<Solicitud> findByUsuarioEmail(String email);
    List<Solicitud> findByVacante(Vacante vacante);
    boolean existsByUsuarioEmailAndVacanteIdVacante(String email, Integer idVacante);
	List<Solicitud> findByVacanteAndIdSolicitudNot(Vacante vacante, int idSolicitud);
}