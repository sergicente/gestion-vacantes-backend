package reto.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuarios")
public class Usuario {
	@Id
	private String email;
	
	private String nombre;
	private String apellidos;
	private String password;
	private int enabled;
	private LocalDate fechaRegistro;
	private String rol;
	

}
