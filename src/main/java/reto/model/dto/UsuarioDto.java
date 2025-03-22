package reto.model.dto;

import java.time.LocalDate;

public class UsuarioDto {
	    private String email;
	    private String nombre;
	    private String apellidos;
	    private String rol;
	    private int enabled;
	    private LocalDate fechaRegistro;

	    public UsuarioDto() {}

	    public UsuarioDto(String email, String nombre, String apellidos, String rol, int enabled, LocalDate fechaRegistro) {
	        this.email = email;
	        this.nombre = nombre;
	        this.apellidos = apellidos;
	        this.rol = rol;
	        this.enabled = enabled;
	        this.fechaRegistro = fechaRegistro;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public String getApellidos() {
	        return apellidos;
	    }

	    public void setApellidos(String apellidos) {
	        this.apellidos = apellidos;
	    }

	    public String getRol() {
	        return rol;
	    }

	    public void setRol(String rol) {
	        this.rol = rol;
	    }

	    public int getEnabled() {
	        return enabled;
	    }

	    public void setEnabled(int enabled) {
	        this.enabled = enabled;
	    }

	    public LocalDate getFechaRegistro() {
	        return fechaRegistro;
	    }

	    public void setFechaRegistro(LocalDate fechaRegistro) {
	        this.fechaRegistro = fechaRegistro;
	    }
	}



