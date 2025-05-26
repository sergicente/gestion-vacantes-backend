package reto.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reto.model.entity.Categoria;
import reto.model.entity.Empresa;
import reto.model.entity.Estatus;
import reto.model.entity.Solicitud;
import reto.model.entity.Vacante;

public interface VacanteRepository extends JpaRepository<Vacante, Integer>{

    // Busca todas las vacantes que tienen un estatus específico (por ejemplo, ACTIVAS o INACTIVAS)
    List<Vacante> findByEstatus(Estatus estatus);

    // Busca todas las vacantes que pertenecen a una categoría concreta
    List<Vacante> findByCategoria(Categoria categoria);

    // Busca todas las vacantes de una empresa con un estatus determinado
    List<Vacante> findAllByEmpresaAndEstatus(Empresa empresa, Estatus estatus);

    // Busca todas las vacantes publicadas por una empresa específica
    List<Vacante> findAllByEmpresa(Empresa empresa);

    // Consulta personalizada para obtener todas las solicitudes asociadas a una vacante específica
    @Query("SELECT s FROM Solicitud s WHERE s.vacante.id = :idVacante")
    List<Solicitud> findSolicitudesByVacanteId(@Param("idVacante") Integer idVacante);

    // Busca vacantes filtrando por estatus y que su nombre o descripción contengan cierta palabra (ignorando mayúsculas/minúsculas)
    List<Vacante> findByEstatusAndNombreContainingIgnoreCaseOrEstatusAndDescripcionContainingIgnoreCase(
        Estatus estatus1, String nombre,
        Estatus estatus2, String descripcion
    );

    }
























































