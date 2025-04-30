package reto.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Solicitudes")
public class Solicitud {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSolicitud;

    private LocalDate fecha;
    private String archivo;
    private String comentarios;
    private int estado; // 0 presentada, 1 adjudicada
    private String curriculum;

    @ManyToOne
    @JoinColumn(name = "id_vacante")
    private Vacante vacante;

    @ManyToOne
    @JoinColumn(name = "email")
    private Usuario usuario;
    

    
}
