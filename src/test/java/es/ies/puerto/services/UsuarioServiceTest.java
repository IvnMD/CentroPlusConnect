package es.ies.puerto.services;

import es.ies.puerto.models.Constantes;

import es.ies.puerto.models.Usuario;
import es.ies.puerto.repositories.sqlite.SqliteConnectionManager;
import es.ies.puerto.repositories.sqlite.UsuarioSqliteRepository;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioServiceTest {
    private static final Path DB = Path.of("target", "test-data", "usuarios-test.db");
    private UsuarioService service;

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(DB.getParent());
        Files.deleteIfExists(DB);
        service = new UsuarioService(new UsuarioSqliteRepository(new SqliteConnectionManager("jdbc:sqlite:" + DB)));
        service.save(new Usuario(1, "Ana Perez", "11111111A", "ana@email.com", "600111111", Constantes.ALUMNO));
        service.save(new Usuario(2, "Luis Ramos", "22222222B", "luis@email.com", "600222222", Constantes.SOCIO));
        service.save(new Usuario(3, "Marta Diaz", "33333333C", "marta@email.com", "600333333", Constantes.AMBOS));
    }

    private Usuario usuarioNuevo() {
        return new Usuario(10, "Carlos Ruiz", "44444444D", "carlos@email.com", "600444444", Constantes.ALUMNO);
    }

    @Test
    @Order(1)
    void findAllUsuariosOK() {
        assertEquals(3, service.findAll().size());
    }

    @Test
    @Order(2)
    void findAllUsuariosNoNull() {
        assertNotNull(service.findAll());
    }

    @Test
    @Order(3)
    void findAllUsuariosContieneAna() {
        assertEquals("Ana Perez", service.findAll().get(0).getNombre());
    }

    @Test
    @Order(4)
    void findAllUsuariosTrasGuardar() {
        service.save(usuarioNuevo());
        assertEquals(4, service.findAll().size());
    }

    @Test
    @Order(5)
    void findAllUsuariosTrasEliminar() {
        service.delete(1);
        assertEquals(2, service.findAll().size());
    }

    @Test
    @Order(6)
    void findByIdUsuarioOK() {
        assertEquals("Ana Perez", service.findById(1).getNombre());
    }

    @Test
    @Order(7)
    void findByIdUsuarioNoExiste() {
        assertNull(service.findById(999));
    }

    @Test
    @Order(8)
    void findByIdUsuarioIdCero() {
        assertNull(service.findById(0));
    }

    @Test
    @Order(9)
    void findByIdUsuarioIdNegativo() {
        assertNull(service.findById(-1));
    }

    @Test
    @Order(10)
    void findByIdUsuarioTipoCorrecto() {
        assertEquals(Constantes.SOCIO, service.findById(2).getTipoUsuario());
    }

    @Test
    @Order(11)
    void saveUsuarioOK() {
        assertTrue(service.save(usuarioNuevo()));
    }

    @Test
    @Order(12)
    void saveUsuarioNull() {
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
    }

    @Test
    @Order(13)
    void saveUsuarioDuplicado() {
        assertFalse(
                service.save(new Usuario(1, "Otro", "99999999Z", "otro@email.com", "600999999", Constantes.ALUMNO)));
    }

    @Test
    @Order(14)
    void saveUsuarioNombreVacio() {
        assertThrows(IllegalArgumentException.class, () -> service
                .save(new Usuario(10, "", "44444444D", "carlos@email.com", "600444444", Constantes.ALUMNO)));
    }

    @Test
    @Order(15)
    void saveUsuarioEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () -> service
                .save(new Usuario(10, "Carlos Ruiz", "44444444D", "carlos-email", "600444444", Constantes.ALUMNO)));
    }

    @Test
    @Order(16)
    void updateUsuarioOK() {
        assertTrue(service.update(
                new Usuario(1, "Ana Modificada", "11111111A", "ana@email.com", "600111111", Constantes.ALUMNO)));
    }

    @Test
    @Order(17)
    void updateUsuarioNull() {
        assertThrows(IllegalArgumentException.class, () -> service.update(null));
    }

    @Test
    @Order(18)
    void updateUsuarioNoExiste() {
        assertFalse(service.update(usuarioNuevo()));
    }

    @Test
    @Order(19)
    void updateUsuarioDniVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> service.update(new Usuario(1, "Ana Perez", "", "ana@email.com", "600111111", Constantes.ALUMNO)));
    }

    @Test
    @Order(20)
    void updateUsuarioTipoNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.update(new Usuario(1, "Ana Perez", "11111111A", "ana@email.com", "600111111", null)));
    }

    @Test
    @Order(21)
    void deleteUsuarioOK() {
        assertTrue(service.delete(1));
    }

    @Test
    @Order(22)
    void deleteUsuarioNoExiste() {
        assertFalse(service.delete(999));
    }

    @Test
    @Order(23)
    void deleteUsuarioIdCero() {
        assertFalse(service.delete(0));
    }

    @Test
    @Order(24)
    void deleteUsuarioIdNegativo() {
        assertFalse(service.delete(-1));
    }

    @Test
    @Order(25)
    void deleteUsuarioReduceLista() {
        service.delete(1);
        assertEquals(2, service.findAll().size());
    }
}
