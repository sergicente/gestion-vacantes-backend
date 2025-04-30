package reto.model.dto;

import org.antlr.v4.runtime.misc.NotNull;

public class AltaEmpresaResponseDto {
	 private EmpresaDto empresa;
	    private String usuarioEmail;
	    private String passwordGenerada;

	    public EmpresaDto getEmpresa() {
	        return empresa;
	    }

	    public void setEmpresa(EmpresaDto empresa) {
	        this.empresa = empresa;
	    }

	    public String getUsuarioEmail() {
	        return usuarioEmail;
	    }

	    public void setUsuarioEmail(String usuarioEmail) {
	        this.usuarioEmail = usuarioEmail;
	    }

	    public String getPasswordGenerada() {
	        return passwordGenerada;
	    }

	    public void setPasswordGenerada(String passwordGenerada) {
	        this.passwordGenerada = passwordGenerada;
	    }
	}