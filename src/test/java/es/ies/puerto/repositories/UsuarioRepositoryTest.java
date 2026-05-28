package es.ies.puerto.repositories;

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

import es.ies.puerto.models.Constantes;
import es.ies.puerto.models.Usuario;
import es.ies.puerto.repositories.sqlite.SqliteConnectionManager;
import es.ies.puerto.repositories.sqlite.UsuarioSqliteRepository;

@DisplayName("Tests del repositorio UsuarioSqliteRepository")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioRepositoryTest {

    private UsuarioSqliteRepository repository;

    @BeforeEach
    void setUp() {
        SqliteConnectionManager manager = new SqliteConnectionManager("jdbc:sqlite::memory:");
        repository = new UsuarioSqliteRepository(manager);
        repository.save(new Usuario(1, "Ana Pérez", "11111111A", "ana@email.com", "600111111", Constantes.ALUMNO));
        repository.save(new Usuario(2, "Luis Ramos", "22222222B", "luis@email.com", "600222222", Constantes.SOCIO));
    }

    @Test
    @Order(1)
    @DisplayName("findAll devuelve lista no nula")
    void findAllTestNoNull() {
        assertNotNull(repository.findAll());
    }

    @Test
    @Order(2)
    @DisplayName("findAll devuelve todos los usuarios")
    void findAllTestOk() {
        assertEquals(2, repository.findAll().size());
    }

    @Test
    @Order(3)
    @DisplayName("findById devuelve el usuario correcto")
    void findByIdTestOk() {
        Usuario u = repository.findById(1);
        assertNotNull(u);
        assertEquals("Ana Pérez", u.getNombre());
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
    @DisplayName("save guarda un nuevo usuario correctamente")
    void saveTestOk() {
        Usuario nuevo = new Usuario(3, "Marta Díaz", "33333333C", "marta@email.com", "600333333", Constantes.AMBOS);
        assertTrue(repository.save(nuevo));
    }

    @Test
    @Order(7)
    @DisplayName("save incrementa el numero de usuarios")
    void saveTestIncrementaLista() {
        Usuario nuevo = new Usuario(3, "Marta Díaz", "33333333C", "marta@email.com", "600333333", Constantes.AMBOS);
        repository.save(nuevo);
        assertEquals(3, repository.findAll().size());
    }

    @Test
    @Order(8)
    @DisplayName("update actualiza un usuario existente")
    void updateTestOk() {
        Usuario actualizado = new Usuario(1, "Ana García", "11111111A", "ana@email.com", "600111111", Constantes.ALUMNO);
        assertTrue(repository.update(actualizado));
    }

    @Test
    @Order(9)
    @DisplayName("update refleja los cambios en findById")
    void updateTestCambiosReflejados() {
        Usuario actualizado = new Usuario(1, "Ana García", "11111111A", "ana@email.com", "600111111", Constantes.ALUMNO);
        repository.update(actualizado);
        assertEquals("Ana García", repository.findById(1).getNombre());
    }

    @Test
    @Order(10)
    @DisplayName("update devuelve false si el usuario no existe")
    void updateTestNotFound() {
        Usuario noExiste = new Usuario(999, "Fantasma", "99999999Z", "x@x.com", "600000000", Constantes.ALUMNO);
        assertFalse(repository.update(noExiste));
    }

    @Test
    @Order(11)
    @DisplayName("delete elimina un usuario existente")
    void deleteTestOk() {
        assertTrue(repository.delete(1));
    }

    @Test
    @Order(12)
    @DisplayName("delete devuelve false si el usuario no existe")
    void deleteTestNotFound() {
        assertFalse(repository.delete(999));
    }

    @Test
    @Order(13)
    @DisplayName("delete reduce el numero de usuarios")
    void deleteTestReduceLista() {
        repository.delete(1);
        assertEquals(1, repository.findAll().size());
    }
}