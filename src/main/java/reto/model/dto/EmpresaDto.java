package reto.model.dto;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDto implements Serializable {
    
	
	private static final long serialVersionUID = 1L;
	
	private int idEmpresa;
	private String cif;
    private String nombreEmpresa;
    private String direccionFiscal;
    private String pais;
    private String email;
    private String nombre;
	private String apellidos;
}


