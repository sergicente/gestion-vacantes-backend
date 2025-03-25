package reto.model.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import reto.model.dto.EmpresaDto;
import reto.model.entity.Empresa;
import reto.model.entity.Usuario;
import reto.model.entity.Vacante;
import reto.model.repository.EmpresaRepository;
import reto.model.repository.SolicitudRepository;
import reto.model.repository.UsuarioRepository;
import reto.model.repository.VacanteRepository;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	@Autowired
	private EmpresaRepository erepo;

	@Autowired
	private VacanteRepository vrepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private SolicitudRepository srepo;

	@Override
	public Empresa buscar(Integer clave) {
		return erepo.findById(clave).orElse(null);
	}

	@Override
	public List<Empresa> buscarTodos() {
		return erepo.findAll();
	}

	@Override
	public Empresa insertar(Empresa empresa) {
	    // Verificar si el usuario ya existe
	    Optional<Usuario> optionalUsuario = usuarioRepo.findById(empresa.getEmailEmpresa());
	    Usuario usuario;

	    if (optionalUsuario.isPresent()) {
	        usuario = optionalUsuario.get();
	    } else {
	        // Crear nuevo usuario con datos básicos
	        usuario = new Usuario();
	        usuario.setEmail(empresa.getEmailEmpresa());
	        usuario.setNombre(empresa.getNombreEmpresa()); // O nombreEmpresa si no hay nombre del responsable

	        usuario.setPassword("1234"); // Contraseña fija por defecto (sin encoder)
	        usuario.setEnabled(1);
	        usuario.setFechaRegistro(LocalDate.now());
	        usuario.setRol("EMPRESA");

	        usuario = usuarioRepo.save(usuario);
	    }


	    return erepo.save(empresa);
	}


	@Override
	public Empresa modificar(Empresa empresa) {
	    return erepo.findById(empresa.getIdEmpresa()).map(empresaExistente -> {
	        // Actualizar solo los campos permitidos
	        empresaExistente.setCif(empresa.getCif());
	        empresaExistente.setNombreEmpresa(empresa.getNombreEmpresa());
	        empresaExistente.setDireccionFiscal(empresa.getDireccionFiscal());
	        empresaExistente.setPais(empresa.getPais());
	        empresaExistente.setVacantes(empresa.getVacantes());

	        if (empresa.getVacantes() != null) {
	            empresaExistente.setVacantes(empresa.getVacantes());
	        }

	        return erepo.save(empresaExistente);
	    }).orElseThrow(() -> new RuntimeException("Empresa no encontrada con ID: " + empresa.getIdEmpresa()));
	}
	@Override
	@Transactional
	public int borrar(Integer idEmpresa) {
		try {
			if (erepo.existsById(idEmpresa)) {
				List<Vacante> vacantes = vrepo.findByEmpresa_IdEmpresa(idEmpresa);

				for (Vacante v : vacantes) {
					srepo.deleteByVacante_IdVacante(v.getIdVacante());
				}

				vrepo.deleteByEmpresa_IdEmpresa(idEmpresa);

				erepo.deleteById(idEmpresa);

				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
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

}
