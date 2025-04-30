package reto.model.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reto.model.dto.EmpresaDto;
import reto.model.entity.Empresa;
import reto.model.entity.EmpresaYPassword;
import reto.model.entity.Usuario;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;
import reto.model.repository.EmpresaRepository;
import reto.model.repository.SolicitudRepository;
import reto.model.repository.UsuarioRepository;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private EmpresaRepository erepo;
    @Autowired
    private UsuarioRepository urepo;
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
            if (erepo.existsById(clave)) {
                erepo.deleteById(clave);
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
        return erepo.findVacantesByEmpresa(empresa);
    }

    @Override
    public List<EmpresaDto> obtenerEmpresasConNumeroVacantes() {
        return erepo.obtenerEmpresasConNumeroVacantes();
    }

    @Override
    public EmpresaYPassword insertarConUsuario(Empresa empresa, Usuario usuario) {
        usuario.setRol("EMPRESA");
        String password = generarPasswordAleatoria(10);
        usuario.setPassword(password);
        usuario.setEnabled(1);
        usuario.setFechaRegistro(LocalDate.now());

        urepo.save(usuario);
        empresa.setEmailEmpresa(usuario.getEmail());

        Empresa nuevaEmpresa = erepo.save(empresa);

        return new EmpresaYPassword(nuevaEmpresa, password);
    }

    public String generarPasswordAleatoria(int longitud) {
        final String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            int randomIndex = (int) (Math.random() * caracteres.length());
            password.append(caracteres.charAt(randomIndex));
        }
        return password.toString();
    }
}
