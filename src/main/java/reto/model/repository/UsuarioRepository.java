package reto.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import reto.model.entity.Empresa;
import reto.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String>{
	


}
