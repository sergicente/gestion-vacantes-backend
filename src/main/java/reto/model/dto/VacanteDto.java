package reto.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


public class VacanteDto implements Serializable {
    private int idVacante;
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private double salario;
    private boolean destacado;
    private String imagen;
    private String detalles;
    private int idCategoria;
    private int idEmpresa;
	public int getIdVacante() {
		return idVacante;
	}
	public void setIdVacante(int idVacante) {
		this.idVacante = idVacante;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public double getSalario() {
		return salario;
	}
	public void setSalario(double salario) {
		this.salario = salario;
	}
	public boolean isDestacado() {
		return destacado;
	}
	public void setDestacado(boolean destacado) {
		this.destacado = destacado;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public String getDetalles() {
		return detalles;
	}
	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}
	public int getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	public int getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idVacante);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof VacanteDto))
			return false;
		VacanteDto other = (VacanteDto) obj;
		return idVacante == other.idVacante;
	}
	public VacanteDto(int idVacante, String nombre, String descripcion, LocalDate fecha, double salario,
			boolean destacado, String imagen, String detalles, int idCategoria, int idEmpresa) {
		super();
		this.idVacante = idVacante;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.salario = salario;
		this.destacado = destacado;
		this.imagen = imagen;
		this.detalles = detalles;
		this.idCategoria = idCategoria;
		this.idEmpresa = idEmpresa;
	}
	public VacanteDto() {
		super();
	}

}


