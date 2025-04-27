package reto.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {
	    private String email;
	    private String nombre;
	    private String apellidos;
	    private String password;
	    private String rol;
	    private int enabled;
	    private LocalDate fechaRegistro;

	    
	}



