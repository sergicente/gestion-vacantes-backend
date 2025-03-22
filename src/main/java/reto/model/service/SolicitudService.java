package reto.model.service;

import java.util.List;

import reto.model.entity.Solicitud;

public interface SolicitudService extends InterfaceGenericoCrud<Solicitud, Integer>{
    Solicitud enviarSolicitud(Integer vacanteId, String email, Solicitud solicitud);
    List<Solicitud> findBySolicitudPorUsuario(String email);
    void cancelarSolicitud(int idSolicitud);
    List<Solicitud> buscarSolicitudesPorVacante(Integer Idvacante);
}
