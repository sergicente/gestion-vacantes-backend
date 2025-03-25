package reto.model.entity;

import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empresas")
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEmpresa;
	@NotNull
	private String cif;
	private String nombreEmpresa;
	private String direccionFiscal;
	private String pais;
	@Column(name = "email") // Nombre diferente en la base de datos
	private String emailEmpresa;

	@OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
	private List<Vacante> vacantes;

}
