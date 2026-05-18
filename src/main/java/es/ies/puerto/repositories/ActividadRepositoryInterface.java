package es.ies.puerto.repositories;

import es.ies.puerto.models.Actividad;
import java.util.List;

public interface ActividadRepositoryInterface {
    List<Actividad> findAll();
    Actividad findById(int id);
    boolean save(Actividad actividad);
    boolean update(Actividad actividad);
    boolean delete(int id);
}
