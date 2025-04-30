package reto.model.service;

import java.util.Optional;

import reto.model.entity.Usuario;

public interface UsuarioService extends InterfaceGenericoCrud<Usuario, String>{
	boolean deshabilitarUsuarioPorEmail(String email);
	void actualizarUsuario(Usuario usuario);
	 Optional<Usuario> buscarUsuarioPorEmail(String email);
	 boolean habilitarUsuarioPorEmail(String email);

}
