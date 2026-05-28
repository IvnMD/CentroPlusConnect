package es.ies.puerto.models;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@DisplayName("Tests del modelo Inscripcion")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InscripcionTest {

    private Inscripcion inscripcion;

    @BeforeEach
    void setUp() {
        inscripcion = new Inscripcion(1, 1, 1, LocalDate.of(2026, 5, 28), Constantes.ACTIVA);
    }

    @Test
    @Order(1)
    @DisplayName("Constructor crea inscripcion correctamente")
    void constructorTestOk() {
        assertNotNull(inscripcion);
    }

    @Test
    @Order(2)
    @DisplayName("getId devuelve el id correcto")
    void getIdTestOk() {
        assertEquals(1, inscripcion.getId());
    }

    @Test
    @Order(3)
    @DisplayName("getIdUsuario devuelve el id de usuario correcto")
    void getIdUsuarioTestOk() {
        assertEquals(1, inscripcion.getIdUsuario());
    }

    @Test
    @Order(4)
    @DisplayName("getIdActividad devuelve el id de actividad correcto")
    void getIdActividadTestOk() {
        assertEquals(1, inscripcion.getIdActividad());
    }

    @Test
    @Order(5)
    @DisplayName("getFecha devuelve una fecha no nula")
    void getFechaTestOk() {
        assertNotNull(inscripcion.getFecha());
    }

    @Test
    @Order(6)
    @DisplayName("getEstado devuelve el estado correcto")
    void getEstadoTestOk() {
        assertEquals(Constantes.ACTIVA, inscripcion.getEstado());
    }

    @Test
    @Order(7)
    @DisplayName("estaActiva devuelve true cuando la inscripcion esta activa")
    void estaActivaTestOk() {
        assertTrue(inscripcion.estaActiva());
    }

    @Test
    @Order(8)
    @DisplayName("estaActiva devuelve false cuando la inscripcion esta cancelada")
    void estaActivaTestCancelada() {
        inscripcion.cancelar();
        assertFalse(inscripcion.estaActiva());
    }

    @Test
    @Order(9)
    @DisplayName("cancelar cambia el estado a CANCELADA")
    void cancelarTestOk() {
        inscripcion.cancelar();
        assertEquals(Constantes.CANCELADA, inscripcion.getEstado());
    }

    @Test
    @Order(10)
    @DisplayName("equals devuelve true para inscripciones iguales")
    void equalsTestOk() {
        Inscripcion i2 = new Inscripcion(1, 1, 1, LocalDate.of(2026, 5, 28), Constantes.ACTIVA);
        assertEquals(inscripcion, i2);
    }

    @Test
    @Order(11)
    @DisplayName("equals devuelve false para inscripciones distintas")
    void equalsTestFalse() {
        Inscripcion i2 = new Inscripcion(2, 2, 2, LocalDate.of(2026, 5, 28), Constantes.CANCELADA);
        assertNotEquals(inscripcion, i2);
    }

    @Test
    @Order(12)
    @DisplayName("hashCode es consistente para la misma inscripcion")
    void hashCodeTestOk() {
        Inscripcion i2 = new Inscripcion(1, 1, 1, LocalDate.of(2026, 5, 28), Constantes.ACTIVA);
        assertEquals(inscripcion.hashCode(), i2.hashCode());
    }
}