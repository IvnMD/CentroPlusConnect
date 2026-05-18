package es.ies.puerto.services;

import es.ies.puerto.models.Constantes;

import es.ies.puerto.models.*;
import es.ies.puerto.repositories.sqlite.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InscripcionServiceTest {
    private static final Path DB = Path.of("target", "test-data", "inscripciones-test.db");

    private UsuarioSqliteRepository usuarioRepository;
    private ActividadSqliteRepository actividadRepository;
    private InscripcionSqliteRepository inscripcionRepository;
    private InscripcionService service;

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(DB.getParent());
        Files.deleteIfExists(DB);
        SqliteConnectionManager manager = new SqliteConnectionManager("jdbc:sqlite:" + DB);

        usuarioRepository = new UsuarioSqliteRepository(manager);
        actividadRepository = new ActividadSqliteRepository(manager);
        inscripcionRepository = new InscripcionSqliteRepository(manager);

        usuarioRepository
                .save(new Usuario(1, "Ana Perez", "11111111A", "ana@email.com", "600111111", Constantes.ALUMNO));
        usuarioRepository
                .save(new Usuario(2, "Luis Ramos", "22222222B", "luis@email.com", "600222222", Constantes.SOCIO));
        actividadRepository.save(new Actividad(1, "Yoga", Constantes.DEPORTIVA, 60, 25.5, 15, 8));
        actividadRepository.save(new Actividad(2, "Java", Constantes.ACADEMICA, 90, 40.0, 20, 12));
        actividadRepository.save(new Actividad(3, "Spinning", Constantes.DEPORTIVA, 45, 18.0, 12, 12));
        inscripcionRepository.save(new Inscripcion(1, 1, 1, LocalDate.now(), Constantes.ACTIVA));

        service = new InscripcionService(inscripcionRepository, usuarioRepository, actividadRepository);
    }

    private Inscripcion nueva() {
        return new Inscripcion(10, 2, 2, LocalDate.now(), Constantes.ACTIVA);
    }

    @Test
    @Order(1)
    void findAllInscripcionesOK() {
        assertEquals(1, service.findAll().size());
    }

    @Test
    @Order(2)
    void findAllInscripcionesNoNull() {
        assertNotNull(service.findAll());
    }

    @Test
    @Order(3)
    void findAllInscripcionesContieneActiva() {
        assertEquals(Constantes.ACTIVA, service.findAll().get(0).getEstado());
    }

    @Test
    @Order(4)
    void findAllInscripcionesTrasGuardar() {
        service.save(nueva());
        assertEquals(2, service.findAll().size());
    }

    @Test
    @Order(5)
    void findAllInscripcionesTrasCancelarNoElimina() {
        service.cancelar(1);
        assertEquals(1, service.findAll().size());
    }

    @Test
    @Order(6)
    void findByIdInscripcionOK() {
        assertEquals(1, service.findById(1).getIdUsuario());
    }

    @Test
    @Order(7)
    void findByIdInscripcionNoExiste() {
        assertNull(service.findById(999));
    }

    @Test
    @Order(8)
    void findByIdInscripcionIdCero() {
        assertNull(service.findById(0));
    }

    @Test
    @Order(9)
    void findByIdInscripcionIdNegativo() {
        assertNull(service.findById(-1));
    }

    @Test
    @Order(10)
    void findByIdInscripcionEstadoActivo() {
        assertTrue(service.findById(1).estaActiva());
    }

    @Test
    @Order(11)
    void saveInscripcionOK() {
        assertTrue(service.save(nueva()));
    }

    @Test
    @Order(12)
    void saveInscripcionNull() {
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
    }

    @Test
    @Order(13)
    void saveInscripcionDuplicadaId() {
        assertFalse(service.save(new Inscripcion(1, 2, 2, LocalDate.now(), Constantes.ACTIVA)));
    }

    @Test
    @Order(14)
    void saveInscripcionUsuarioNoExiste() {
        assertFalse(service.save(new Inscripcion(10, 999, 2, LocalDate.now(), Constantes.ACTIVA)));
    }

    @Test
    @Order(15)
    void saveInscripcionActividadNoExiste() {
        assertFalse(service.save(new Inscripcion(10, 2, 999, LocalDate.now(), Constantes.ACTIVA)));
    }

    @Test
    @Order(16)
    void saveInscripcionDuplicadaActiva() {
        assertFalse(service.save(new Inscripcion(10, 1, 1, LocalDate.now(), Constantes.ACTIVA)));
    }

    @Test
    @Order(17)
    void saveInscripcionFechaFutura() {
        assertThrows(IllegalArgumentException.class,
                () -> service.save(new Inscripcion(10, 2, 2, LocalDate.now().plusDays(1), Constantes.ACTIVA)));
    }

    @Test
    @Order(18)
    void saveInscripcionActividadCompleta() {
        assertThrows(IllegalStateException.class,
                () -> service.save(new Inscripcion(10, 2, 3, LocalDate.now(), Constantes.ACTIVA)));
    }

    @Test
    @Order(19)
    void cancelarInscripcionOK() {
        assertTrue(service.cancelar(1));
    }

    @Test
    @Order(20)
    void cancelarInscripcionNoExiste() {
        assertFalse(service.cancelar(999));
    }

    @Test
    @Order(21)
    void cancelarInscripcionIdCero() {
        assertFalse(service.cancelar(0));
    }

    @Test
    @Order(22)
    void cancelarInscripcionDobleCancelacion() {
        assertTrue(service.cancelar(1));
        assertFalse(service.cancelar(1));
    }

    @Test
    @Order(23)
    void cancelarInscripcionLiberaPlaza() {
        service.cancelar(1);
        assertEquals(7, actividadRepository.findById(1).getPlazasOcupadas());
    }

    @Test
    @Order(24)
    void findByUsuarioOK() {
        assertEquals(1, service.findByUsuario(1).size());
    }

    @Test
    @Order(25)
    void findByUsuarioSinResultados() {
        assertEquals(0, service.findByUsuario(999).size());
    }

    @Test
    @Order(26)
    void findByUsuarioDespuesDeGuardar() {
        service.save(nueva());
        assertEquals(1, service.findByUsuario(2).size());
    }

    @Test
    @Order(27)
    void findByUsuarioNoNull() {
        assertNotNull(service.findByUsuario(1));
    }

    @Test
    @Order(28)
    void findByUsuarioDespuesDeCancelarSigueApareciendo() {
        service.cancelar(1);
        assertEquals(1, service.findByUsuario(1).size());
    }

    @Test
    @Order(29)
    void findByActividadOK() {
        assertEquals(1, service.findByActividad(1).size());
    }

    @Test
    @Order(30)
    void findByActividadSinResultados() {
        assertEquals(0, service.findByActividad(999).size());
    }

    @Test
    @Order(31)
    void findByActividadDespuesDeGuardar() {
        service.save(nueva());
        assertEquals(1, service.findByActividad(2).size());
    }

    @Test
    @Order(32)
    void findByActividadNoNull() {
        assertNotNull(service.findByActividad(1));
    }

    @Test
    @Order(33)
    void findByActividadDespuesDeCancelarSigueApareciendo() {
        service.cancelar(1);
        assertEquals(1, service.findByActividad(1).size());
    }
}
