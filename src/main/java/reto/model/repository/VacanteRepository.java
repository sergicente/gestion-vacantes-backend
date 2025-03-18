package reto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reto.model.entity.Empresa;
import reto.model.entity.Solicitud;
import reto.model.entity.Usuario;
import reto.model.entity.Vacante;

public interface VacanteRepository extends JpaRepository<Vacante, Integer>{

}
