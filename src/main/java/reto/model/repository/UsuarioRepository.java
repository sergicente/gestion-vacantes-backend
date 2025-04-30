package reto.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import reto.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String>{


}
