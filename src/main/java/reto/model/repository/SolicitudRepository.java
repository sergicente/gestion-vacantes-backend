package reto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reto.model.entity.Empresa;
import reto.model.entity.Solicitud;
import reto.model.entity.Usuario;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer>{

}
