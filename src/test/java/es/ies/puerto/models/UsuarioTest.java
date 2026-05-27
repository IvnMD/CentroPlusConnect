package es.ies.puerto.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    private Usuario usuario() {
        return new Usuario(1, "Ana Pérez", "11111111A", "ana@email.com", "600111111", Constantes.ALUMNO);
    }

    @Test
    void constructorTestOk() {
        assertNotNull(usuario());
    }

    @Test
    void getIdTestOk() {
        assertEquals(1, usuario().getId());
    }

    @Test
    void getNombreTestOk() {
        assertEquals("Ana Pérez", usuario().getNombre());
    }

    @Test
    void getDniTestOk() {
        assertEquals("11111111A", usuario().getDni());
    }

    @Test
    void getEmailTestOk() {
        assertEquals("ana@email.com", usuario().getEmail());
    }

    @Test
    void getTelefonoTestOk() {
        assertEquals("600111111", usuario().getTelefono());
    }

    @Test
    void getTipoUsuarioTestOk() {
        assertEquals(Constantes.ALUMNO, usuario().getTipoUsuario());
    }

    @Test
    void equalsTestOk() {
        assertEquals(usuario(), usuario());
    }

    @Test
    void equalsTestFalse() {
        Usuario u2 = new Usuario(2, "Luis", "22222222B", "luis@email.com", "600222222", Constantes.SOCIO);
        assertNotEquals(usuario(), u2);
    }

    @Test
    void hashCodeTestOk() {
        assertEquals(usuario().hashCode(), usuario().hashCode());
    }
}