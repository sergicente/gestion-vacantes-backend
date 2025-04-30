package reto.model.service;

import java.util.List;

import reto.model.dto.EmpresaDto;
import reto.model.dto.SolicitudDto;
import reto.model.entity.Empresa;
import reto.model.entity.EmpresaYPassword;
import reto.model.entity.Usuario;
import reto.model.entity.Vacante;

public interface EmpresaService extends InterfaceGenericoCrud<Empresa, Integer> {

    List<Vacante> buscarVacantesPorEmpresa(Empresa empresa);
    List<EmpresaDto> obtenerEmpresasConNumeroVacantes();

    // Método para insertar empresa y usuario asociado con generación de contraseña
    EmpresaYPassword insertarConUsuario(Empresa empresa, Usuario usuario);
}
