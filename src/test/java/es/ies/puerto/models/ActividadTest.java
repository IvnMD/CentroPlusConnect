package es.ies.puerto.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import es.ies.puerto.repositories.csv.ActividadCsvRepository;
import es.ies.puerto.services.ActividadService;

class ActividadTest {
    Actividad actividad;
    int id = 1;
    String nombre = "Nombre";
    String tipo = "Tipo";
    int duracion = 1;
    int precio = 2;
    int plazas = 5;
    int ocupadas = 2;

    @BeforeEach
    void setUp(){
        System.out.println("Se ejecuta antes del test");
        actividad = new Actividad(id,nombre,tipo,duracion,precio,plazas,ocupadas);
    }

    @DisplayName ("Test verifica not null")
    @Order(1)
    @Test
    void ActividadNotNullTest(){
        Assertions.assertNotNull(actividad, "La clase actividad no puede ser nula");
    }

    @DisplayName("Test que verifica equals true")
    @Order(2)
    @Test
    void actividadEqualsTrueTest(){
        Actividad actividadNueva = new Actividad(1);
        Assertions.assertEquals(actividad, actividadNueva, "Debe ser igual");
    }

    @DisplayName("Test que verifica not equals true")
    @Order(3)
    @Test
    void actividadNotEqualsTrueTest(){
        Actividad actividadNueva = new Actividad(2);
        Assertions.assertNotEquals(actividad, actividadNueva, "Debe ser diferente");
    }

    @DisplayName("Test que verifica equals de la misma clase")
    @Order(4)
    @Test
    void actividadEqualsTest(){
        Actividad actividadNueva = new Actividad();
        Assertions.assertNotEquals(actividad, actividadNueva, "Debe ser diferente");
    }
    @DisplayName("Test que verifica plazas disponibles")
    @Order(5)
    @Test
    void actividadPlazasDisponiblesTest(){
        actividad.cancelarPlaza();
        Assertions.assertEquals(plazas-ocupadas+1, 
            actividad.getPlazasDisponibles());
    };
}
