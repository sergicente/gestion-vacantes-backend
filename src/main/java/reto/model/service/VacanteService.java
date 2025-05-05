package reto.model.service;

import java.util.List;

import reto.model.entity.Empresa;
import reto.model.entity.Estatus;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;

public interface VacanteService extends InterfaceGenericoCrud<Vacante, Integer>{
	
	Vacante publicarVacante(Vacante vacante);
	void eliminarVacante(Vacante vacante);
	void asignarVacante(Vacante vacante, Solicitud solicitud);
	void cambiarEstado(int idVacante, Estatus nuevoEstado);
	List<Vacante> findAllByEmpresa(Empresa empresa);
	List<Vacante> findAllByEmpresaAndEstatus(Integer idEmpresa);
	List<Solicitud> buscarSolicitudesPorVacante(Integer idVacante);
	List<Vacante> buscarPorNombreODescripcion(String nombre, String descripcion);
	

}
