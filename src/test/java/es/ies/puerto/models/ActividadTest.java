package es.ies.puerto.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@DisplayName("Tests del modelo Actividad")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActividadTest {

    private Actividad actividad;

    @BeforeEach
    void setUp() {
        actividad = new Actividad(1, "Yoga", Constantes.DEPORTIVA, 60, 25.5, 15, 8);
    }

    @Test
    @Order(1)
    @DisplayName("Constructor crea actividad correctamente")
    void constructorTestOk() {
        assertNotNull(actividad);
    }

    @Test
    @Order(2)
    @DisplayName("getId devuelve el id correcto")
    void getIdTestOk() {
        assertEquals(1, actividad.getId());
    }

    @Test
    @Order(3)
    @DisplayName("getNombre devuelve el nombre correcto")
    void getNombreTestOk() {
        assertEquals("Yoga", actividad.getNombre());
    }

    @Test
    @Order(4)
    @DisplayName("getTipoActividad devuelve el tipo correcto")
    void getTipoActividadTestOk() {
        assertEquals(Constantes.DEPORTIVA, actividad.getTipoActividad());
    }

    @Test
    @Order(5)
    @DisplayName("getDuracionMinutos devuelve la duracion correcta")
    void getDuracionMinutosTestOk() {
        assertEquals(60, actividad.getDuracionMinutos());
    }

    @Test
    @Order(6)
    @DisplayName("getPrecio devuelve el precio correcto")
    void getPrecioTestOk() {
        assertEquals(25.5, actividad.getPrecio());
    }

    @Test
    @Order(7)
    @DisplayName("getPlazasMaximas devuelve las plazas maximas correctas")
    void getPlazasMaximasTestOk() {
        assertEquals(15, actividad.getPlazasMaximas());
    }

    @Test
    @Order(8)
    @DisplayName("getPlazasOcupadas devuelve las plazas ocupadas correctas")
    void getPlazasOcupadasTestOk() {
        assertEquals(8, actividad.getPlazasOcupadas());
    }

    @Test
    @Order(9)
    @DisplayName("calcularPlazasDisponibles devuelve el valor correcto")
    void calcularPlazasDisponiblesTestOk() {
        assertEquals(7, actividad.calcularPlazasDisponibles());
    }

    @Test
    @Order(10)
    @DisplayName("hayPlazasLibres devuelve true cuando hay plazas")
    void hayPlazasLibresTestOk() {
        assertTrue(actividad.hayPlazasLibres());
    }

    @Test
    @Order(11)
    @DisplayName("estaCompleta devuelve true cuando no hay plazas")
    void estaCompletaTestOk() {
        Actividad completa = new Actividad(2, "Boxeo", Constantes.DEPORTIVA, 45, 30.0, 10, 10);
        assertTrue(completa.estaCompleta());
    }

    @Test
    @Order(12)
    @DisplayName("reservarPlaza incrementa plazas ocupadas")
    void reservarPlazaTestOk() {
        actividad.reservarPlaza();
        assertEquals(9, actividad.getPlazasOcupadas());
    }

    @Test
    @Order(13)
    @DisplayName("reservarPlaza lanza excepcion si no hay plazas")
    void reservarPlazaTestSinPlazas() {
        Actividad completa = new Actividad(2, "Boxeo", Constantes.DEPORTIVA, 45, 30.0, 10, 10);
        assertThrows(IllegalStateException.class, completa::reservarPlaza);
    }

    @Test
    @Order(14)
    @DisplayName("cancelarPlaza decrementa plazas ocupadas")
    void cancelarPlazaTestOk() {
        actividad.cancelarPlaza();
        assertEquals(7, actividad.getPlazasOcupadas());
    }

    @Test
    @Order(15)
    @DisplayName("cancelarPlaza lanza excepcion si no hay plazas ocupadas")
    void cancelarPlazaTestSinOcupadas() {
        Actividad vacia = new Actividad(3, "Natacion", Constantes.DEPORTIVA, 60, 20.0, 10, 0);
        assertThrows(IllegalStateException.class, vacia::cancelarPlaza);
    }

    @Test
    @Order(16)
    @DisplayName("equals devuelve true para actividades iguales")
    void equalsTestOk() {
        Actividad a2 = new Actividad(1, "Yoga", Constantes.DEPORTIVA, 60, 25.5, 15, 8);
        assertEquals(actividad, a2);
    }

    @Test
    @Order(17)
    @DisplayName("equals devuelve false para actividades distintas")
    void equalsTestFalse() {
        Actividad a2 = new Actividad(2, "Pilates", Constantes.DEPORTIVA, 50, 20.0, 12, 5);
        assertNotEquals(actividad, a2);
    }

    @Test
    @Order(18)
    @DisplayName("hashCode es consistente para la misma actividad")
    void hashCodeTestOk() {
        Actividad a2 = new Actividad(1, "Yoga", Constantes.DEPORTIVA, 60, 25.5, 15, 8);
        assertEquals(actividad.hashCode(), a2.hashCode());
    }
}