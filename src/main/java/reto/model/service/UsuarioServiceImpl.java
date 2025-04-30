package reto.model.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reto.model.entity.Usuario;
import reto.model.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository urepo;

    @Override
    public Usuario buscar(String clave) {
        return urepo.findById(clave).orElse(null);  // Busca por la clave primaria (email)
    }

    @Override
    public List<Usuario> buscarTodos() {
        return urepo.findAll();
    }

    @Override
    public Usuario insertar(Usuario entidad) {
        return urepo.save(entidad);
    }

    @Override
    public Usuario modificar(Usuario entidad) {
        try {
            if (urepo.existsById(entidad.getEmail())) {
                return urepo.save(entidad);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int borrar(String clave) {
        try {
            if (urepo.existsById(clave)) {
                urepo.deleteById(clave);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Usamos findById, ya que email es la clave primaria
    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        return urepo.findById(email);  // Buscar usuario por email, que es el ID
    }

    public boolean deshabilitarUsuarioPorEmail(String email) {
        Optional<Usuario> usuarioOpt = urepo.findById(email);  // Buscar por email
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEnabled(0);  // Deshabilitar usuario (0 = deshabilitado)
            urepo.save(usuario);  // Guardar usuario con nueva configuración
            return true;
        }
        return false;  // Retorna false si no se encuentra el usuario
    }

    public boolean habilitarUsuarioPorEmail(String email) {
        Optional<Usuario> usuarioOpt = urepo.findById(email);  // Buscar por email
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEnabled(1);  // Habilitar usuario (1 = habilitado)
            urepo.save(usuario);  // Guardar usuario con nueva configuración
            return true;
        }
        return false;  // Retorna false si no se encuentra el usuario
    }

    public void actualizarUsuario(Usuario usuario) {
        urepo.save(usuario);  // Actualiza el usuario
    }
}

