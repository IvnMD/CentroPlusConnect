package es.ies.puerto.repositories;

import es.ies.puerto.models.Inscripcion;
import java.util.List;

public interface InscripcionRepositoryInterface {
    List<Inscripcion> findAll();
    Inscripcion findById(int id);
    List<Inscripcion> findByUsuario(int idUsuario);
    List<Inscripcion> findByActividad(int idActividad);
    boolean save(Inscripcion inscripcion);
    boolean update(Inscripcion inscripcion);
    boolean delete(int id);
}
