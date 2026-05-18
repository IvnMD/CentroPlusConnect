package es.ies.puerto.services;

import es.ies.puerto.models.Actividad;
import java.util.List;

public interface ActividadServiceInterface {
    List<Actividad> findAll();
    Actividad findById(int id);
    boolean save(Actividad actividad);
    boolean update(Actividad actividad);
    boolean delete(int id);
    List<Actividad> findCompletas();
}
