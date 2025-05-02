package reto.model.service;

import java.util.List;

import reto.model.entity.Solicitud;

public interface SolicitudService extends InterfaceGenericoCrud<Solicitud, Integer> {

    Solicitud enviarSolicitud(Solicitud solicitud);

    List<Solicitud> findBySolicitudPorUsuario(String email);

    void cancelarSolicitud(int idSolicitud);

    List<Solicitud> buscarSolicitudesPorVacante(Integer Idvacante);

    Boolean buscarSolicitudExistente(String email, Integer idVacante);

    List<Solicitud> buscarSolicitudesPorEmpresa(Integer empresaId);
    
    void aceptarSolicitud(int idSolicitud);
}
