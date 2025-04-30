package reto.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDto implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int idSolicitud;
    private LocalDate fecha;
    private String archivo;
    private String comentarios;
    private int estado; 
    private String curriculum;
    private int idVacante;
    private String email;
    

    // Información del usuario solicitante
    private String emailUsuario;
    private String nombreUsuario;


}
