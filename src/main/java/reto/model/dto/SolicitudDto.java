package reto.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


public class SolicitudDto implements Serializable{
	
    private int idSolicitud;
    private LocalDate fecha;
    private String archivo;
    private String comentarios;
    private int estado; 
    private String curriculum;
    private int idVacante;
    private String email;
	public int getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public String getArchivo() {
		return archivo;
	}
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getCurriculum() {
		return curriculum;
	}
	public void setCurriculum(String curriculum) {
		this.curriculum = curriculum;
	}
	public int getIdVacante() {
		return idVacante;
	}
	public void setIdVacante(int idVacante) {
		this.idVacante = idVacante;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idSolicitud);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SolicitudDto))
			return false;
		SolicitudDto other = (SolicitudDto) obj;
		return idSolicitud == other.idSolicitud;
	}
	public SolicitudDto(int idSolicitud, LocalDate fecha, String archivo, String comentarios, int estado,
			String curriculum, int idVacante, String email) {
		super();
		this.idSolicitud = idSolicitud;
		this.fecha = fecha;
		this.archivo = archivo;
		this.comentarios = comentarios;
		this.estado = estado;
		this.curriculum = curriculum;
		this.idVacante = idVacante;
		this.email = email;
	}
	public SolicitudDto() {
		super();
	}
    
}

