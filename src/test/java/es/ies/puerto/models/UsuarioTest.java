package es.ies.puerto.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@DisplayName("Tests del modelo Usuario")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1, "Luis Luis Luis", "12345678Z", "ivan@email.com", "66655544", Constantes.ALUMNO);
    }

    @Test
    @Order(1)
    @DisplayName("Constructor crea usuario correctamente")
    void constructorTestOk() {
        assertNotNull(usuario);
    }

    @Test
    @Order(2)
    @DisplayName("getId devuelve el id correcto")
    void getIdTestOk() {
        assertEquals(1, usuario.getId());
    }

    @Test
    @Order(3)
    @DisplayName("getNombre devuelve el nombre correcto")
    void getNombreTestOk() {
        assertEquals("Luis Luis Luis", usuario.getNombre());
    }

    @Test
    @Order(4)
    @DisplayName("getDni devuelve el DNI correcto")
    void getDniTestOk() {
        assertEquals("12345678Z", usuario.getDni());
    }

    @Test
    @Order(5)
    @DisplayName("getEmail devuelve el email correcto")
    void getEmailTestOk() {
        assertEquals("ivan@email.com", usuario.getEmail());
    }

    @Test
    @Order(6)
    @DisplayName("getTelefono devuelve el teléfono correcto")
    void getTelefonoTestOk() {
        assertEquals("66655544", usuario.getTelefono());
    }

    @Test
    @Order(7)
    @DisplayName("getTipoUsuario devuelve el tipo correcto")
    void getTipoUsuarioTestOk() {
        assertEquals(Constantes.ALUMNO, usuario.getTipoUsuario());
    }

    @Test
    @Order(8)
    @DisplayName("equals devuelve true para usuarios iguales")
    void equalsTestOk() {
        Usuario u2 = new Usuario(1, "Luis ", "11111111A", "ivan@email.com", "66655544", Constantes.ALUMNO);
        assertEquals(usuario, u2);
    }

    @Test
    @Order(9)
    @DisplayName("equals devuelve false para usuarios distintos")
    void equalsTestFalse() {
        Usuario u2 = new Usuario(2, "Juan Garcia Lopez", "98765432A", "luis@email.com", "777888999", Constantes.SOCIO);
        assertNotEquals(usuario, u2);
    }

    @Test
    @Order(10)
    @DisplayName("hashCode es consistente para el mismo usuario")
    void hashCodeTestOk() {
        Usuario u2 = new Usuario(1, "Luis Luis Luis", "12345678Z", "ivan@email.com", "66655544", Constantes.ALUMNO);
        assertEquals(usuario.hashCode(), u2.hashCode());
    }
}