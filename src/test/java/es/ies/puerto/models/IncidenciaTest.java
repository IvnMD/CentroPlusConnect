package es.ies.puerto.models;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;

class IncidenciaTest {

    Incidencia incidencia;

    @BeforeEach
    void  setUp(){
        Incidencia incidencia;
        int id = 1;
        String asunto = "asunto";
        String descripcion = "descripcion";
        String fechaStr = "26/05/2026";
        LocalDate fecha = LocalDate.parse(fechaStr);

        @BeforeEach
        void setUp(){
            incidencia = new Incidencia(id, asunto, descripcion, fechaStr);
        }
    }

}
