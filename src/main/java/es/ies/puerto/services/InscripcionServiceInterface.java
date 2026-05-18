package es.ies.puerto.services;

import es.ies.puerto.models.Inscripcion;
import java.util.List;

public interface InscripcionServiceInterface {
    List<Inscripcion> findAll();
    Inscripcion findById(int id);
    List<Inscripcion> findByUsuario(int idUsuario);
    List<Inscripcion> findByActividad(int idActividad);
    boolean save(Inscripcion inscripcion);
    boolean update(Inscripcion inscripcion);
    boolean delete(int id);
    public boolean cancelar(int id);
}
