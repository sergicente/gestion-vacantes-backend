package reto.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDto {

    private int idCategoria;
    private String nombre;
    private String descripcion;

}
