package reto.model.dto;

import java.io.Serializable;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacanteInputDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idVacante;
	private String nombre;
	private String descripcion;
	private LocalDate fecha;
	private double salario;
	private String imagen;
	private String detalles;
	private int idEmpresa;
	private int idCategoria;
	private int destacado;
}