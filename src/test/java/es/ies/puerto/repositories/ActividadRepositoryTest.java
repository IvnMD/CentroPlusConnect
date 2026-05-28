package es.ies.puerto.repositories;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import es.ies.puerto.models.Actividad;
import es.ies.puerto.repositories.csv.ActividadCsvRepository;

@DisplayName("Tests del repositorio ActividadCsvRepository")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActividadRepositoryTest {

    private ActividadCsvRepository repository;
    private static final Path CSV = Path.of("target/test-actividades.csv");

    @BeforeEach
    void setUp() throws Exception {
        Files.deleteIfExists(CSV);
        Files.createFile(CSV);
        repository = new ActividadCsvRepository(CSV);
        repository.save(new Actividad(1, "Yoga", "DEPORTIVA", 60, 25.50, 15, 8));
        repository.save(new Actividad(2, "Programación Java", "ACADEMICA", 90, 40.00, 20, 12));
        repository.save(new Actividad(3, "Spinning", "DEPORTIVA", 45, 18.00, 12, 12));
    }

    @Test
    @Order(1)
    @DisplayName("findAll devuelve lista no nula")
    void findAllTestNoNull() {
        assertNotNull(repository.findAll());
    }

    @Test
    @Order(2)
    @DisplayName("findAll devuelve todas las actividades")
    void findAllTestOk() {
        assertEquals(3, repository.findAll().size());
    }

    @Test
    @Order(3)
    @DisplayName("findById devuelve la actividad correcta")
    void findByIdTestOk() {
        Actividad a = repository.findById(1);
        assertNotNull(a);
        assertEquals("Yoga", a.getNombre());
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
    @DisplayName("save guarda una nueva actividad correctamente")
    void saveTestOk() {
        Actividad nueva = new Actividad(4, "Inglés técnico", "ACADEMICA", 60, 30.00, 18, 6);
        assertTrue(repository.save(nueva));
    }

    @Test
    @Order(7)
    @DisplayName("save incrementa el numero de actividades")
    void saveTestIncrementaLista() {
        repository.save(new Actividad(4, "Inglés técnico", "ACADEMICA", 60, 30.00, 18, 6));
        assertEquals(4, repository.findAll().size());
    }

    @Test
    @Order(8)
    @DisplayName("update actualiza una actividad existente")
    void updateTestOk() {
        Actividad actualizada = new Actividad(1, "Yoga Avanzado", "DEPORTIVA", 75, 30.00, 15, 8);
        assertTrue(repository.update(actualizada));
    }

    @Test
    @Order(9)
    @DisplayName("update refleja los cambios en findById")
    void updateTestCambiosReflejados() {
        repository.update(new Actividad(1, "Yoga Avanzado", "DEPORTIVA", 75, 30.00, 15, 8));
        assertEquals("Yoga Avanzado", repository.findById(1).getNombre());
    }

    @Test
    @Order(10)
    @DisplayName("update devuelve false si la actividad no existe")
    void updateTestNotFound() {
        Actividad noExiste = new Actividad(999, "Fantasma", "DEPORTIVA", 60, 10.00, 10, 0);
        assertFalse(repository.update(noExiste));
    }

    @Test
    @Order(11)
    @DisplayName("delete elimina una actividad existente")
    void deleteTestOk() {
        assertTrue(repository.delete(1));
    }

    @Test
    @Order(12)
    @DisplayName("delete devuelve false si la actividad no existe")
    void deleteTestNotFound() {
        assertFalse(repository.delete(999));
    }

    @Test
    @Order(13)
    @DisplayName("delete reduce el numero de actividades")
    void deleteTestReduceLista() {
        repository.delete(1);
        assertEquals(2, repository.findAll().size());
    }
}