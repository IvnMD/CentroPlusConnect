package es.ies.puerto.services;

import es.ies.puerto.models.Actividad;
import es.ies.puerto.models.Constantes;
import es.ies.puerto.repositories.csv.ActividadCsvRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio ActividadService con Mockito")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActividadServiceTest {

    @Mock
    private ActividadCsvRepository repository;

    @InjectMocks
    private ActividadService service;

    private Actividad yoga()       { return new Actividad(1, "Yoga",      Constantes.DEPORTIVA,  60, 25.5, 15, 8);  }
    private Actividad java()       { return new Actividad(2, "Programacion Java", Constantes.ACADEMICA, 90, 40.0, 20, 12); }
    private Actividad spinning()   { return new Actividad(3, "Spinning",  Constantes.DEPORTIVA,  45, 18.0, 12, 12); }
    private Actividad pilates()    { return new Actividad(4, "Pilates",   Constantes.DEPORTIVA,  50, 22.0, 10, 0);  }
    private Actividad nueva()      { return new Actividad(10, "Natacion", Constantes.DEPORTIVA,  60, 30.0, 20, 5);  }

    // ── findAll ──────────────────────────────────────────────────────────────

    @Test @Order(1)
    @DisplayName("findAll devuelve lista con todas las actividades")
    void findAllOk() {
        when(repository.findAll()).thenReturn(List.of(yoga(), java(), spinning(), pilates()));
        assertEquals(4, service.findAll().size());
    }

    @Test @Order(2)
    @DisplayName("findAll no devuelve null")
    void findAllNoNull() {
        when(repository.findAll()).thenReturn(List.of(yoga()));
        assertNotNull(service.findAll());
    }

    @Test @Order(3)
    @DisplayName("findAll primer elemento es Yoga")
    void findAllContieneYoga() {
        when(repository.findAll()).thenReturn(List.of(yoga(), java(), spinning(), pilates()));
        assertEquals("Yoga", service.findAll().get(0).getNombre());
    }

    @Test @Order(4)
    @DisplayName("findAll contiene al menos una actividad completa")
    void findAllContieneCompleta() {
        when(repository.findAll()).thenReturn(List.of(yoga(), java(), spinning(), pilates()));
        assertTrue(service.findAll().stream().anyMatch(Actividad::estaCompleta));
    }

    // ── findById ─────────────────────────────────────────────────────────────

    @Test @Order(5)
    @DisplayName("findById devuelve la actividad correcta")
    void findByIdOk() {
        when(repository.findById(1)).thenReturn(yoga());
        assertEquals("Yoga", service.findById(1).getNombre());
    }

    @Test @Order(6)
    @DisplayName("findById devuelve null si no existe")
    void findByIdNotFound() {
        when(repository.findById(999)).thenReturn(null);
        assertNull(service.findById(999));
    }

    @Test @Order(7)
    @DisplayName("findById devuelve null si id es cero")
    void findByIdIdCero() {
        assertNull(service.findById(0));
    }

    @Test @Order(8)
    @DisplayName("findById devuelve null si id es negativo")
    void findByIdIdNegativo() {
        assertNull(service.findById(-1));
    }

    @Test @Order(9)
    @DisplayName("findById devuelve actividad completa")
    void findByIdCompleta() {
        when(repository.findById(3)).thenReturn(spinning());
        assertTrue(service.findById(3).estaCompleta());
    }

    // ── save ─────────────────────────────────────────────────────────────────

    @Test @Order(10)
    @DisplayName("save guarda una actividad correctamente")
    void saveOk() {
        when(repository.save(nueva())).thenReturn(true);
        assertTrue(service.save(nueva()));
    }

    @Test @Order(11)
    @DisplayName("save lanza excepcion si actividad es null")
    void saveNull() {
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
    }

    @Test @Order(12)
    @DisplayName("save devuelve false si actividad duplicada")
    void saveDuplicada() {
        when(repository.save(any(Actividad.class))).thenReturn(false);
        assertFalse(service.save(new Actividad(1, "Yoga duplicado", Constantes.DEPORTIVA, 60, 10, 10, 1)));
    }

    @Test @Order(13)
    @DisplayName("save lanza excepcion si nombre vacio")
    void saveNombreVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> service.save(new Actividad(10, "", Constantes.DEPORTIVA, 60, 10, 10, 1)));
    }

    @Test @Order(14)
    @DisplayName("save lanza excepcion si precio negativo")
    void savePrecioNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> service.save(new Actividad(10, "Natacion", Constantes.DEPORTIVA, 60, -1, 10, 1)));
    }

    // ── update ───────────────────────────────────────────────────────────────

    @Test @Order(15)
    @DisplayName("update actualiza una actividad correctamente")
    void updateOk() {
        when(repository.update(any(Actividad.class))).thenReturn(true);
        assertTrue(service.update(new Actividad(1, "Yoga avanzado", Constantes.DEPORTIVA, 75, 30, 15, 8)));
    }

    @Test @Order(16)
    @DisplayName("update lanza excepcion si actividad es null")
    void updateNull() {
        assertThrows(IllegalArgumentException.class, () -> service.update(null));
    }

    @Test @Order(17)
    @DisplayName("update devuelve false si la actividad no existe")
    void updateNotFound() {
        when(repository.update(nueva())).thenReturn(false);
        assertFalse(service.update(nueva()));
    }

    @Test @Order(18)
    @DisplayName("update lanza excepcion si duracion invalida")
    void updateDuracionInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> service.update(new Actividad(1, "Yoga", Constantes.DEPORTIVA, 0, 10, 10, 1)));
    }

    @Test @Order(19)
    @DisplayName("update lanza excepcion si plazas ocupadas > maximas")
    void updatePlazasOcupadasMayorQueMaximas() {
        assertThrows(IllegalArgumentException.class,
                () -> service.update(new Actividad(1, "Yoga", Constantes.DEPORTIVA, 60, 10, 10, 11)));
    }

    // ── delete ───────────────────────────────────────────────────────────────

    @Test @Order(20)
    @DisplayName("delete elimina una actividad correctamente")
    void deleteOk() {
        when(repository.delete(1)).thenReturn(true);
        assertTrue(service.delete(1));
    }

    @Test @Order(21)
    @DisplayName("delete devuelve false si no existe")
    void deleteNotFound() {
        when(repository.delete(999)).thenReturn(false);
        assertFalse(service.delete(999));
    }

    @Test @Order(22)
    @DisplayName("delete devuelve false si id es cero")
    void deleteIdCero() {
        assertFalse(service.delete(0));
    }

    @Test @Order(23)
    @DisplayName("delete devuelve false si id es negativo")
    void deleteIdNegativo() {
        assertFalse(service.delete(-1));
    }

    // ── reservarPlaza / cancelarPlaza ────────────────────────────────────────

    @Test @Order(24)
    @DisplayName("reservarPlaza incrementa plazas ocupadas")
    void reservarPlazaOk() {
        Actividad a = yoga();
        when(repository.findById(1)).thenReturn(a);
        when(repository.update(any())).thenReturn(true);
        assertTrue(service.reservarPlaza(1));
        assertEquals(9, a.getPlazasOcupadas());
    }

    @Test @Order(25)
    @DisplayName("reservarPlaza devuelve false si actividad no existe")
    void reservarPlazaNotFound() {
        when(repository.findById(999)).thenReturn(null);
        assertFalse(service.reservarPlaza(999));
    }

    @Test @Order(26)
    @DisplayName("reservarPlaza lanza excepcion si actividad completa")
    void reservarPlazaCompleta() {
        when(repository.findById(3)).thenReturn(spinning());
        assertThrows(IllegalStateException.class, () -> service.reservarPlaza(3));
    }

    @Test @Order(27)
    @DisplayName("reservarPlaza devuelve false si id es cero")
    void reservarPlazaIdCero() {
        assertFalse(service.reservarPlaza(0));
    }

    @Test @Order(28)
    @DisplayName("cancelarPlaza decrementa plazas ocupadas")
    void cancelarPlazaOk() {
        Actividad a = yoga();
        when(repository.findById(1)).thenReturn(a);
        when(repository.update(any())).thenReturn(true);
        assertTrue(service.cancelarPlaza(1));
        assertEquals(7, a.getPlazasOcupadas());
    }

    @Test @Order(29)
    @DisplayName("cancelarPlaza devuelve false si actividad no existe")
    void cancelarPlazaNotFound() {
        when(repository.findById(999)).thenReturn(null);
        assertFalse(service.cancelarPlaza(999));
    }

    @Test @Order(30)
    @DisplayName("cancelarPlaza lanza excepcion si sin plazas ocupadas")
    void cancelarPlazaSinOcupadas() {
        when(repository.findById(4)).thenReturn(pilates());
        assertThrows(IllegalStateException.class, () -> service.cancelarPlaza(4));
    }

    @Test @Order(31)
    @DisplayName("cancelarPlaza devuelve false si id es cero")
    void cancelarPlazaIdCero() {
        assertFalse(service.cancelarPlaza(0));
    }

    // ── calcularIngresos / findCompletas ─────────────────────────────────────

    @Test @Order(32)
    @DisplayName("calcularIngresosTotales calcula correctamente")
    void calcularIngresosOk() {
        when(repository.findAll()).thenReturn(List.of(yoga(), java(), spinning(), pilates()));
        assertEquals(900.0, service.calcularIngresosTotales(), 0.001);
    }

    @Test @Order(33)
    @DisplayName("calcularIngresosTotales con lista vacia devuelve cero")
    void calcularIngresosSinActividades() {
        when(repository.findAll()).thenReturn(List.of());
        assertEquals(0, service.calcularIngresosTotales(), 0.001);
    }

    @Test @Order(34)
    @DisplayName("findCompletas devuelve solo las completas")
    void findCompletasOk() {
        when(repository.findAll()).thenReturn(List.of(yoga(), java(), spinning(), pilates()));
        assertEquals(1, service.findCompletas().size());
    }

    @Test @Order(35)
    @DisplayName("findCompletas contiene Spinning")
    void findCompletasContieneSpinning() {
        when(repository.findAll()).thenReturn(List.of(yoga(), java(), spinning(), pilates()));
        assertEquals("Spinning", service.findCompletas().get(0).getNombre());
    }

    @Test @Order(36)
    @DisplayName("findCompletas no devuelve null")
    void findCompletasNoNull() {
        when(repository.findAll()).thenReturn(List.of(yoga()));
        assertNotNull(service.findCompletas());
    }
}