package reto.model.service;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto.model.entity.Categoria;
import reto.model.entity.Empresa;
import reto.model.entity.Solicitud;
import reto.model.entity.Usuario;
import reto.model.repository.CategoriaRepository;
import reto.model.repository.EmpresaRepository;
import reto.model.repository.SolicitudRepository;
import reto.model.repository.UsuarioRepository;
@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	@Autowired
	private UsuarioRepository urepo;

	@Override
	public Usuario buscar(String clave) {
		return urepo.findById(clave).orElse(null);
	}

	@Override
	public List<Usuario> buscarTodos() {
		return urepo.findAll();
	}
	

	@Override
	public Usuario insertar(Usuario entidad) {
	   String password = generarPassword();
	   
	   entidad.setPassword(password);
	   return urepo.save(entidad);
	}

	private String generarPassword() {
		    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
		    SecureRandom random = new SecureRandom();
		    StringBuilder password = new StringBuilder();

		    for (int i = 0; i < 10; i++) { 
		        int index = random.nextInt(caracteres.length());
		        password.append(caracteres.charAt(index));
		    }
		    
		    return password.toString();
		}
	

	@Override
	public Usuario modificar(Usuario entidad) {
		try {
			if(urepo.existsById(entidad.getEmail())) {
				return urepo.save(entidad);
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int borrar(String clave) {
		try {
			if(urepo.existsById(clave)) {
				urepo.deleteById(clave);
				return 1;
			}else {
				return 0;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}


	

}
