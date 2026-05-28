package es.ies.puerto.repositories;

import es.ies.puerto.models.Constantes;
import es.ies.puerto.models.Inscripcion;
import es.ies.puerto.repositories.sqlite.InscripcionSqliteRepository;
import es.ies.puerto.repositories.sqlite.SqliteConnectionManager;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests del repositorio InscripcionSqliteRepository")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InscripcionRepositoryTest {

    private InscripcionSqliteRepository repository;
    private static final LocalDate FECHA = LocalDate.of(2026, 5, 28);
    private static final String DB_URL = "jdbc:sqlite:target/test-inscripciones.db";

    @BeforeEach
    void setUp() throws Exception {
        Files.deleteIfExists(Path.of("target/test-inscripciones.db"));
        SqliteConnectionManager manager = new SqliteConnectionManager(DB_URL);
        repository = new InscripcionSqliteRepository(manager);
        repository.save(new Inscripcion(1, 1, 1, FECHA, Constantes.ACTIVA));
        repository.save(new Inscripcion(2, 1, 2, FECHA, Constantes.ACTIVA));
        repository.save(new Inscripcion(3, 2, 1, FECHA, Constantes.CANCELADA));
    }

    @Test
    @Order(1)
    @DisplayName("findAll devuelve lista no nula")
    void findAllTestNoNull() {
        assertNotNull(repository.findAll());
    }

    @Test
    @Order(2)
    @DisplayName("findAll devuelve todas las inscripciones")
    void findAllTestOk() {
        assertEquals(3, repository.findAll().size());
    }

    @Test
    @Order(3)
    @DisplayName("findById devuelve la inscripcion correcta")
    void findByIdTestOk() {
        Inscripcion i = repository.findById(1);
        assertNotNull(i);
        assertEquals(1, i.getIdUsuario());
    }

    @Test
    @Order(4)
    @DisplayName("findById devuelve null si no existe")
    void findByIdTestNotFound() {
        assertNull(repository.findById(999));
    }

    @Test
    @Order(5)
    @DisplayName("findById devuelve null si id es cero")
    void findByIdTestIdCero() {
        assertNull(repository.findById(0));
    }

    @Test
    @Order(6)
    @DisplayName("findByUsuario devuelve las inscripciones del usuario")
    void findByUsuarioTestOk() {
        assertEquals(2, repository.findByUsuario(1).size());
    }

    @Test
    @Order(7)
    @DisplayName("findByUsuario devuelve lista vacia si no tiene inscripciones")
    void findByUsuarioTestVacio() {
        assertTrue(repository.findByUsuario(999).isEmpty());
    }

    @Test
    @Order(8)
    @DisplayName("findByActividad devuelve las inscripciones de la actividad")
    void findByActividadTestOk() {
        assertEquals(2, repository.findByActividad(1).size());
    }

    @Test
    @Order(9)
    @DisplayName("findByActividad devuelve lista vacia si no tiene inscripciones")
    void findByActividadTestVacio() {
        assertTrue(repository.findByActividad(999).isEmpty());
    }

    @Test
    @Order(10)
    @DisplayName("save guarda una nueva inscripcion correctamente")
    void saveTestOk() {
        Inscripcion nueva = new Inscripcion(4, 2, 2, FECHA, Constantes.ACTIVA);
        assertTrue(repository.save(nueva));
    }

    @Test
    @Order(11)
    @DisplayName("save incrementa el numero de inscripciones")
    void saveTestIncrementaLista() {
        repository.save(new Inscripcion(4, 2, 2, FECHA, Constantes.ACTIVA));
        assertEquals(4, repository.findAll().size());
    }

    @Test
    @Order(12)
    @DisplayName("update actualiza una inscripcion existente")
    void updateTestOk() {
        Inscripcion actualizada = new Inscripcion(1, 1, 1, FECHA, Constantes.CANCELADA);
        assertTrue(repository.update(actualizada));
    }

    @Test
    @Order(13)
    @DisplayName("update refleja los cambios en findById")
    void updateTestCambiosReflejados() {
        repository.update(new Inscripcion(1, 1, 1, FECHA, Constantes.CANCELADA));
        assertEquals(Constantes.CANCELADA, repository.findById(1).getEstado());
    }

    @Test
    @Order(14)
    @DisplayName("update devuelve false si la inscripcion no existe")
    void updateTestNotFound() {
        Inscripcion noExiste = new Inscripcion(999, 1, 1, FECHA, Constantes.ACTIVA);
        assertFalse(repository.update(noExiste));
    }

    @Test
    @Order(15)
    @DisplayName("delete elimina una inscripcion existente")
    void deleteTestOk() {
        assertTrue(repository.delete(1));
    }

    @Test
    @Order(16)
    @DisplayName("delete devuelve false si la inscripcion no existe")
    void deleteTestNotFound() {
        assertFalse(repository.delete(999));
    }

    @Test
    @Order(17)
    @DisplayName("delete reduce el numero de inscripciones")
    void deleteTestReduceLista() {
        repository.delete(1);
        assertEquals(2, repository.findAll().size());
    }
}
