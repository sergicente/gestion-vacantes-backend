package reto.model.dto;

import java.io.Serializable;
import java.util.Objects;



public class EmpresaDto implements Serializable {
    
	private int idEmpresa;
	private String cif;
    private String nombreEmpresa;
    private String direccionFiscal;
    private String pais;
    private String email;
//    private String nombre;
//	private String apellidos;
	private long numeroVacantes; // Nuevo campo

	
	
	public int getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	public String getDireccionFiscal() {
		return direccionFiscal;
	}
	public void setDireccionFiscal(String direccionFiscal) {
		this.direccionFiscal = direccionFiscal;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getNumeroVacantes() {
		return numeroVacantes;
	}
	public void setNumeroVacantes(long numeroVacantes) {
		this.numeroVacantes = numeroVacantes;
	}
//	public String getNombre() {
//		return nombre;
//	}
//	public void setNombre(String nombre) {
//		this.nombre = nombre;
//	}
//	public String getApellidos() {
//		return apellidos;
//	}
//	public void setApellidos(String apellidos) {
//		this.apellidos = apellidos;
//	}
	@Override
	public int hashCode() {
		return Objects.hash(cif, idEmpresa);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof EmpresaDto))
			return false;
		EmpresaDto other = (EmpresaDto) obj;
		return Objects.equals(cif, other.cif) && idEmpresa == other.idEmpresa;
	}
//	public EmpresaDto(int idEmpresa, String cif, String nombreEmpresa, String direccionFiscal, String pais,
//			String email, String nombre, String apellidos) {
//		super();
//		this.idEmpresa = idEmpresa;
//		this.cif = cif;
//		this.nombreEmpresa = nombreEmpresa;
//		this.direccionFiscal = direccionFiscal;
//		this.pais = pais;
//		this.email = email;
//		this.nombre = nombre;
//		this.apellidos = apellidos;
//	}
	
	// NUEVO CONSTRUCTOR para la query (sin nombre y apellidos, pero con numeroVacantes)
	public EmpresaDto(int idEmpresa, String cif, String nombreEmpresa, String direccionFiscal, String pais,
			String email, long numeroVacantes) {
		this.idEmpresa = idEmpresa;
		this.cif = cif;
		this.nombreEmpresa = nombreEmpresa;
		this.direccionFiscal = direccionFiscal;
		this.pais = pais;
		this.email = email;
		this.setNumeroVacantes(numeroVacantes);
	}
	
	
	public EmpresaDto() {
		super();
	}

	
}


