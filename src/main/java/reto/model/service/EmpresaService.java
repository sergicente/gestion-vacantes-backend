package reto.model.service;

import java.util.List;

import reto.model.entity.Empresa;
import reto.model.entity.Vacante;

public interface EmpresaService extends InterfaceGenericoCrud<Empresa, Integer>{

	List<Vacante> buscarVacantesPorEmpresa(Empresa empresa);

}
