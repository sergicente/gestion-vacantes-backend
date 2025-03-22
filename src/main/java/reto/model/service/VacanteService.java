package reto.model.service;

import java.util.List;

import reto.model.entity.Empresa;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;

public interface VacanteService extends InterfaceGenericoCrud<Vacante, Integer>{
	
	 Vacante publicarVacante(Vacante vacante);
	 void eliminarVacante(Vacante vacante);
	 void asignarVacante(Integer vacanteId, Integer solicitudId);

	List<Vacante> buscarVacantesPorEmpresa(Empresa empresa);
	List<Solicitud> buscarSolicitudesPorVacante(Integer idVacante);
	

}
