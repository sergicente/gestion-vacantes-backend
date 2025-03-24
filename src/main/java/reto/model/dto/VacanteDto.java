package reto.model.dto;

import java.io.Serializable;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reto.model.entity.Categoria;
import reto.model.entity.Empresa;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacanteDto implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private int idVacante;
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private double salario;
    private boolean destacado;
    private String imagen;
    private String detalles;
    private Empresa empresa;
    private Categoria categoria;


}



