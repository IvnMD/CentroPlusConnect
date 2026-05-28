package es.ies.puerto.services;

import es.ies.puerto.models.*;
import es.ies.puerto.repositories.sqlite.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio InscripcionService con Mockito")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InscripcionServiceTest {

    @Mock private InscripcionSqliteRepository inscripcionRepository;
    @Mock private UsuarioSqliteRepository usuarioRepository;
    @Mock private ActividadSqliteRepository actividadRepository;

    @InjectMocks
    private InscripcionService service;

    private Usuario ana()       { return new Usuario(1, "Ana Perez",  "11111111A", "ana@email.com",  "600111111", Constantes.ALUMNO); }
    private Usuario luis()      { return new Usuario(2, "Luis Ramos", "22222222B", "luis@email.com", "600222222", Constantes.SOCIO);  }
    private Actividad yoga()    { return new Actividad(1, "Yoga",     Constantes.DEPORTIVA, 60, 25.5, 15, 8);  }
    private Actividad java()    { return new Actividad(2, "Java",     Constantes.ACADEMICA, 90, 40.0, 20, 12); }
    private Actividad spinning(){ return new Actividad(3, "Spinning", Constantes.DEPORTIVA, 45, 18.0, 12, 12); }
    private Inscripcion activa(){ return new Inscripcion(1, 1, 1, LocalDate.now(), Constantes.ACTIVA); }
    private Inscripcion nueva() { return new Inscripcion(10, 2, 2, LocalDate.now(), Constantes.ACTIVA); }

    // ── findAll ──────────────────────────────────────────────────────────────

    @Test @Order(1)
    @DisplayName("findAll devuelve inscripciones")
    void findAllOk() {
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertEquals(1, service.findAll().size());
    }

    @Test @Order(2)
    @DisplayName("findAll no devuelve null")
    void findAllNoNull() {
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertNotNull(service.findAll());
    }

    @Test @Order(3)
    @DisplayName("findAll primer elemento tiene estado ACTIVA")
    void findAllContieneActiva() {
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertEquals(Constantes.ACTIVA, service.findAll().get(0).getEstado());
    }

    // ── findById ─────────────────────────────────────────────────────────────

    @Test @Order(4)
    @DisplayName("findById devuelve inscripcion correcta")
    void findByIdOk() {
        when(inscripcionRepository.findById(1)).thenReturn(activa());
        assertEquals(1, service.findById(1).getIdUsuario());
    }

    @Test @Order(5)
    @DisplayName("findById devuelve null si no existe")
    void findByIdNotFound() {
        when(inscripcionRepository.findById(999)).thenReturn(null);
        assertNull(service.findById(999));
    }

    @Test @Order(6)
    @DisplayName("findById devuelve null si id es cero")
    void findByIdIdCero() {
        assertNull(service.findById(0));
    }

    @Test @Order(7)
    @DisplayName("findById devuelve null si id es negativo")
    void findByIdIdNegativo() {
        assertNull(service.findById(-1));
    }

    @Test @Order(8)
    @DisplayName("findById inscripcion esta activa")
    void findByIdEstaActiva() {
        when(inscripcionRepository.findById(1)).thenReturn(activa());
        assertTrue(service.findById(1).estaActiva());
    }

    // ── save ─────────────────────────────────────────────────────────────────

    @Test @Order(9)
    @DisplayName("save guarda inscripcion correctamente")
    void saveOk() {
        when(usuarioRepository.findById(2)).thenReturn(luis());
        when(actividadRepository.findById(2)).thenReturn(java());
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        when(inscripcionRepository.save(nueva())).thenReturn(true);
        assertTrue(service.save(nueva()));
    }

    @Test @Order(10)
    @DisplayName("save lanza excepcion si inscripcion es null")
    void saveNull() {
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
    }

    @Test @Order(11)
    @DisplayName("save devuelve false si id duplicado")
    void saveDuplicadaId() {
        when(inscripcionRepository.save(any())).thenReturn(false);
        when(usuarioRepository.findById(anyInt())).thenReturn(ana());
        when(actividadRepository.findById(anyInt())).thenReturn(yoga());
        when(inscripcionRepository.findAll()).thenReturn(List.of());
        assertFalse(service.save(new Inscripcion(1, 2, 2, LocalDate.now(), Constantes.ACTIVA)));
    }

    @Test @Order(12)
    @DisplayName("save devuelve false si usuario no existe")
    void saveUsuarioNoExiste() {
        when(usuarioRepository.findById(999)).thenReturn(null);
        assertFalse(service.save(new Inscripcion(10, 999, 2, LocalDate.now(), Constantes.ACTIVA)));
    }

    @Test @Order(13)
    @DisplayName("save devuelve false si actividad no existe")
    void saveActividadNoExiste() {
        when(usuarioRepository.findById(2)).thenReturn(luis());
        when(actividadRepository.findById(999)).thenReturn(null);
        assertFalse(service.save(new Inscripcion(10, 2, 999, LocalDate.now(), Constantes.ACTIVA)));
    }

    @Test @Order(14)
    @DisplayName("save devuelve false si inscripcion activa duplicada")
    void saveDuplicadaActiva() {
        when(usuarioRepository.findById(1)).thenReturn(ana());
        when(actividadRepository.findById(1)).thenReturn(yoga());
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertFalse(service.save(new Inscripcion(10, 1, 1, LocalDate.now(), Constantes.ACTIVA)));
    }

    @Test @Order(15)
    @DisplayName("save lanza excepcion si fecha futura")
    void saveFechaFutura() {
        assertThrows(IllegalArgumentException.class,
                () -> service.save(new Inscripcion(10, 2, 2, LocalDate.now().plusDays(1), Constantes.ACTIVA)));
    }

    @Test @Order(16)
    @DisplayName("save lanza excepcion si actividad completa")
    void saveActividadCompleta() {
        when(usuarioRepository.findById(2)).thenReturn(luis());
        when(actividadRepository.findById(3)).thenReturn(spinning());
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertThrows(IllegalStateException.class,
                () -> service.save(new Inscripcion(10, 2, 3, LocalDate.now(), Constantes.ACTIVA)));
    }

    // ── cancelar ─────────────────────────────────────────────────────────────

    @Test @Order(17)
    @DisplayName("cancelar cancela inscripcion correctamente")
    void cancelarOk() {
        Inscripcion i = activa();
        when(inscripcionRepository.findById(1)).thenReturn(i);
        when(inscripcionRepository.update(any())).thenReturn(true);
        when(actividadRepository.findById(1)).thenReturn(yoga());
        when(actividadRepository.update(any())).thenReturn(true);
        assertTrue(service.cancelar(1));
    }

    @Test @Order(18)
    @DisplayName("cancelar devuelve false si no existe")
    void cancelarNotFound() {
        when(inscripcionRepository.findById(999)).thenReturn(null);
        assertFalse(service.cancelar(999));
    }

    @Test @Order(19)
    @DisplayName("cancelar devuelve false si id es cero")
    void cancelarIdCero() {
        assertFalse(service.cancelar(0));
    }

    @Test @Order(20)
    @DisplayName("cancelar doble cancelacion devuelve false")
    void cancelarDoble() {
        Inscripcion cancelada = new Inscripcion(1, 1, 1, LocalDate.now(), Constantes.CANCELADA);
        when(inscripcionRepository.findById(1))
                .thenReturn(activa())
                .thenReturn(cancelada);
        when(inscripcionRepository.update(any())).thenReturn(true);
        when(actividadRepository.findById(1)).thenReturn(yoga());
        when(actividadRepository.update(any())).thenReturn(true);
        assertTrue(service.cancelar(1));
        assertFalse(service.cancelar(1));
    }

    // ── findByUsuario ─────────────────────────────────────────────────────────

    @Test @Order(21)
    @DisplayName("findByUsuario devuelve inscripciones del usuario")
    void findByUsuarioOk() {
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertEquals(1, service.findByUsuario(1).size());
    }

    @Test @Order(22)
    @DisplayName("findByUsuario devuelve lista vacia si no tiene inscripciones")
    void findByUsuarioSinResultados() {
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertEquals(0, service.findByUsuario(999).size());
    }

    @Test @Order(23)
    @DisplayName("findByUsuario no devuelve null")
    void findByUsuarioNoNull() {
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertNotNull(service.findByUsuario(1));
    }

    // ── findByActividad ───────────────────────────────────────────────────────

    @Test @Order(24)
    @DisplayName("findByActividad devuelve inscripciones de la actividad")
    void findByActividadOk() {
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertEquals(1, service.findByActividad(1).size());
    }

    @Test @Order(25)
    @DisplayName("findByActividad devuelve lista vacia si no hay inscripciones")
    void findByActividadSinResultados() {
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertEquals(0, service.findByActividad(999).size());
    }

    @Test @Order(26)
    @DisplayName("findByActividad no devuelve null")
    void findByActividadNoNull() {
        when(inscripcionRepository.findAll()).thenReturn(List.of(activa()));
        assertNotNull(service.findByActividad(1));
    }
}