package es.ies.puerto;

import es.ies.puerto.repositories.csv.ActividadCsvRepository;
import es.ies.puerto.services.ActividadService;

public class Main {
    public static void main(String[] args) {
        ActividadService service = new ActividadService(new ActividadCsvRepository());
        service.findAll().forEach(System.out::println);
    }
}
