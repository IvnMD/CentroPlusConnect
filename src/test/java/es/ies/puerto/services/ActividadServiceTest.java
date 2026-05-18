package es.ies.puerto.services;

import es.ies.puerto.models.Constantes;

import es.ies.puerto.models.Actividad;
import es.ies.puerto.repositories.csv.ActividadCsvRepository;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActividadServiceTest {
    private static final Path TEST_CSV = Path.of("target", "test-data", "actividades.csv");
    private ActividadService service;

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(TEST_CSV.getParent());
        Files.writeString(TEST_CSV,
                "1;Yoga;DEPORTIVA;60;25.5;15;8\n" +
                        "2;Programacion Java;ACADEMICA;90;40.0;20;12\n" +
                        "3;Spinning;DEPORTIVA;45;18.0;12;12\n" +
                        "4;Pilates;DEPORTIVA;50;22.0;10;0\n");
        service = new ActividadService(new ActividadCsvRepository(TEST_CSV));
    }

    private Actividad actividadNueva() {
        return new Actividad(10, "Natacion", Constantes.DEPORTIVA, 60, 30.0, 20, 5);
    }

    @Test
    @Order(1)
    void findAllActividadesOK() {
        assertEquals(4, service.findAll().size());
    }

    @Test
    @Order(2)
    void findAllActividadesNoNull() {
        assertNotNull(service.findAll());
    }

    @Test
    @Order(3)
    void findAllActividadesContieneYoga() {
        assertEquals("Yoga", service.findAll().get(0).getNombre());
    }

    @Test
    @Order(4)
    void findAllActividadesContieneActividadCompleta() {
        assertTrue(service.findAll().stream().anyMatch(Actividad::estaCompleta));
    }

    @Test
    @Order(5)
    void findAllActividadesDespuesDeGuardar() {
        service.save(actividadNueva());
        assertEquals(5, service.findAll().size());
    }

    @Test
    @Order(6)
    void findByIdActividadOK() {
        assertEquals("Yoga", service.findById(1).getNombre());
    }

    @Test
    @Order(7)
    void findByIdActividadNoExiste() {
        assertNull(service.findById(999));
    }

    @Test
    @Order(8)
    void findByIdActividadIdCero() {
        assertNull(service.findById(0));
    }

    @Test
    @Order(9)
    void findByIdActividadIdNegativo() {
        assertNull(service.findById(-1));
    }

    @Test
    @Order(10)
    void findByIdActividadCompleta() {
        assertTrue(service.findById(3).estaCompleta());
    }

    @Test
    @Order(11)
    void saveActividadOK() {
        assertTrue(service.save(actividadNueva()));
    }

    @Test
    @Order(12)
    void saveActividadNull() {
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
    }

    @Test
    @Order(13)
    void saveActividadDuplicada() {
        assertFalse(service.save(new Actividad(1, "Yoga duplicado", Constantes.DEPORTIVA, 60, 10, 10, 1)));
    }

    @Test
    @Order(14)
    void saveActividadNombreVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> service.save(new Actividad(10, "", Constantes.DEPORTIVA, 60, 10, 10, 1)));
    }

    @Test
    @Order(15)
    void saveActividadPrecioNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> service.save(new Actividad(10, "Natacion", Constantes.DEPORTIVA, 60, -1, 10, 1)));
    }

    @Test
    @Order(16)
    void updateActividadOK() {
        assertTrue(service.update(new Actividad(1, "Yoga avanzado", Constantes.DEPORTIVA, 75, 30, 15, 8)));
    }

    @Test
    @Order(17)
    void updateActividadNull() {
        assertThrows(IllegalArgumentException.class, () -> service.update(null));
    }

    @Test
    @Order(18)
    void updateActividadNoExiste() {
        assertFalse(service.update(actividadNueva()));
    }

    @Test
    @Order(19)
    void updateActividadDuracionInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> service.update(new Actividad(1, "Yoga", Constantes.DEPORTIVA, 0, 10, 10, 1)));
    }

    @Test
    @Order(20)
    void updateActividadPlazasOcupadasMayorQueMaximas() {
        assertThrows(IllegalArgumentException.class,
                () -> service.update(new Actividad(1, "Yoga", Constantes.DEPORTIVA, 60, 10, 10, 11)));
    }

    @Test
    @Order(21)
    void deleteActividadOK() {
        assertTrue(service.delete(1));
    }

    @Test
    @Order(22)
    void deleteActividadNoExiste() {
        assertFalse(service.delete(999));
    }

    @Test
    @Order(23)
    void deleteActividadIdCero() {
        assertFalse(service.delete(0));
    }

    @Test
    @Order(24)
    void deleteActividadIdNegativo() {
        assertFalse(service.delete(-1));
    }

    @Test
    @Order(25)
    void deleteActividadReduceLista() {
        service.delete(1);
        assertEquals(3, service.findAll().size());
    }

    @Test
    @Order(26)
    void reservarPlazaOK() {
        assertTrue(service.reservarPlaza(1));
        assertEquals(9, service.findById(1).getPlazasOcupadas());
    }

    @Test
    @Order(27)
    void reservarPlazaActividadNoExiste() {
        assertFalse(service.reservarPlaza(999));
    }

    @Test
    @Order(28)
    void reservarPlazaActividadCompleta() {
        assertThrows(IllegalStateException.class, () -> service.reservarPlaza(3));
    }

    @Test
    @Order(29)
    void reservarPlazaIdCero() {
        assertFalse(service.reservarPlaza(0));
    }

    @Test
    @Order(30)
    void reservarPlazaMantieneMaximas() {
        service.reservarPlaza(1);
        assertEquals(15, service.findById(1).getPlazasMaximas());
    }

    @Test
    @Order(31)
    void cancelarPlazaOK() {
        assertTrue(service.cancelarPlaza(1));
        assertEquals(7, service.findById(1).getPlazasOcupadas());
    }

    @Test
    @Order(32)
    void cancelarPlazaActividadNoExiste() {
        assertFalse(service.cancelarPlaza(999));
    }

    @Test
    @Order(33)
    void cancelarPlazaSinPlazasOcupadas() {
        assertThrows(IllegalStateException.class, () -> service.cancelarPlaza(4));
    }

    @Test
    @Order(34)
    void cancelarPlazaIdCero() {
        assertFalse(service.cancelarPlaza(0));
    }

    @Test
    @Order(35)
    void cancelarPlazaNoModificaMaximas() {
        service.cancelarPlaza(1);
        assertEquals(15, service.findById(1).getPlazasMaximas());
    }

    @Test
    @Order(36)
    void calcularIngresosTotalesOK() {
        assertEquals(900.0, service.calcularIngresosTotales(), 0.001);
    }

    @Test
    @Order(37)
    void calcularIngresosTotalesTrasGuardar() {
        service.save(actividadNueva());
        assertEquals(1050.0, service.calcularIngresosTotales(), 0.001);
    }

    @Test
    @Order(38)
    void calcularIngresosTotalesTrasReservar() {
        service.reservarPlaza(1);
        assertEquals(925.5, service.calcularIngresosTotales(), 0.001);
    }

    @Test
    @Order(39)
    void calcularIngresosTotalesTrasCancelar() {
        service.cancelarPlaza(1);
        assertEquals(874.5, service.calcularIngresosTotales(), 0.001);
    }

    @Test
    @Order(40)
    void calcularIngresosTotalesSinActividades() {
        service.delete(1);
        service.delete(2);
        service.delete(3);
        service.delete(4);
        assertEquals(0, service.calcularIngresosTotales(), 0.001);
    }

    @Test
    @Order(41)
    void findCompletasOK() {
        assertEquals(1, service.findCompletas().size());
    }

    @Test
    @Order(42)
    void findCompletasContieneSpinning() {
        assertEquals("Spinning", service.findCompletas().get(0).getNombre());
    }

    @Test
    @Order(43)
    void findCompletasTrasEliminarCompleta() {
        service.delete(3);
        assertEquals(0, service.findCompletas().size());
    }

    @Test
    @Order(44)
    void findCompletasTrasCrearCompleta() {
        service.save(new Actividad(10, "Boxeo", Constantes.DEPORTIVA, 60, 20, 10, 10));
        assertEquals(2, service.findCompletas().size());
    }

    @Test
    @Order(45)
    void findCompletasNoNull() {
        assertNotNull(service.findCompletas());
    }
}
