package reto.model.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto.model.dto.EmpresaDto;
import reto.model.entity.Empresa;
import reto.model.entity.EmpresaYPassword;
import reto.model.entity.Usuario;
import reto.model.entity.Vacante;
import reto.model.repository.EmpresaRepository;
import reto.model.repository.UsuarioRepository;
@Service
public class EmpresaServiceImpl implements EmpresaService{
	
	@Autowired
	private EmpresaRepository erepo;
	@Autowired
	private UsuarioRepository urepo;

	@Override
	public Empresa buscar(Integer clave) {
		return erepo.findById(clave).orElse(null);
	}

	@Override
	public List<Empresa> buscarTodos() {
		return erepo.findAll();
	}

	@Override
	public Empresa insertar(Empresa entidad) {
	    return erepo.save(entidad);
	}

	@Override
	public Empresa modificar(Empresa entidad) {
	    return erepo.findById(entidad.getIdEmpresa()).map(empresaExistente -> {
	        if (entidad.getNombreEmpresa() != null) {
	            empresaExistente.setNombreEmpresa(entidad.getNombreEmpresa());
	        }
	        if (entidad.getDireccionFiscal() != null) {
	            empresaExistente.setDireccionFiscal(entidad.getDireccionFiscal());
	        }
	        if (entidad.getPais() != null) {
	            empresaExistente.setPais(entidad.getPais());
	        }
	        if (entidad.getEmailEmpresa() != null) {
	            empresaExistente.setEmailEmpresa(entidad.getEmailEmpresa());
	        }

	        return erepo.save(empresaExistente);
	    }).orElseThrow();
	}

	@Override
	public int borrar(Integer clave) {
		try {
			if(erepo.existsById(clave)) {
				erepo.deleteById(clave);
				return 1;
			}else {
				return 0;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public List<Vacante> buscarVacantesPorEmpresa(Empresa empresa) {
		// TODO Auto-generated method stub
	     return erepo.findVacantesByEmpresa(empresa);
	}

	@Override
	public List<EmpresaDto> obtenerEmpresasConNumeroVacantes() {
		return erepo.obtenerEmpresasConNumeroVacantes();
	}

	public EmpresaYPassword insertarConUsuario(Empresa empresa, Usuario usuario) {
	    // Guardamos primero el usuario
	    usuario.setRol("EMPRESA");

	    // Generamos la contraseña aleatoria para el usuario
	    String password = generarPasswordAleatoria(10);
	    usuario.setPassword(password); // Asignamos la contraseña generada
	    usuario.setEnabled(1); // Habilitamos al usuario
	    usuario.setFechaRegistro(LocalDate.now()); // Asignamos la fecha de registro

	    // Guardamos el usuario en la base de datos
	    urepo.save(usuario);

	    // Ahora guardamos la empresa
	    // Aseguramos que la empresa tenga el email del usuario asociado
	    empresa.setEmailEmpresa(usuario.getEmail()); // Vinculamos el email del usuario con la empresa

	    // Guardamos la empresa en la base de datos
	    Empresa nuevaEmpresa = erepo.save(empresa);

	    // Devolvemos la empresa y la contraseña generada
	    return new EmpresaYPassword(nuevaEmpresa, password);
	}


	// Método para generar una contraseña aleatoria
	public String generarPasswordAleatoria(int longitud) {
	    final String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
	    StringBuilder password = new StringBuilder();
	    for (int i = 0; i < longitud; i++) {
	        int randomIndex = (int) (Math.random() * caracteres.length());
	        password.append(caracteres.charAt(randomIndex));
	    }
	    return password.toString();
	}

	@Override
	public Empresa findByEmail(String email) {
		return erepo.findByEmailEmpresa(email);
	}
}