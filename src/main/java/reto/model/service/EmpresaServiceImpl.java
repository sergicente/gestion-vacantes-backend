package reto.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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
		// Verificar que el usuario existe antes de guardar
		Usuario usuario = usuarioRepo.findById(empresa.getEmailEmpresa()).orElseThrow(
				() -> new RuntimeException("El usuario con email " + empresa.getEmailEmpresa() + " no existe"));

		empresa.setUsuario(usuario); // o empresa.setEmailUsuario(usuario);
		return erepo.save(empresa);
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
	@Transactional
	public int borrar(Integer idEmpresa) {
		try {
			if (erepo.existsById(idEmpresa)) {
				// Paso 1: obtener todas las vacantes de la empresa
				List<Vacante> vacantes = vrepo.findByEmpresa_IdEmpresa(idEmpresa);

				// Paso 2: borrar solicitudes por cada vacante
				for (Vacante v : vacantes) {
					srepo.deleteByVacante_IdVacante(v.getIdVacante());
				}

				// Paso 3: borrar vacantes
				vrepo.deleteByEmpresa_IdEmpresa(idEmpresa);

				// Paso 4: borrar empresa
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

}
