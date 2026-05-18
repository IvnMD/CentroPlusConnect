package es.ies.puerto.repositories.csv;

import es.ies.puerto.models.Actividad;
import es.ies.puerto.repositories.ActividadRepositoryInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ActividadCsvRepository implements ActividadRepositoryInterface {
    private final Path path;
    // private File = new File path

    public ActividadCsvRepository() {
        path = null;
    }

    public ActividadCsvRepository(Path path) {
        this.path = path;
    }

    @Override
    public List<Actividad> findAll() {
        List<Actividad> actividades = new ArrayList<>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(path.toString()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(";");
                    actividades.add(new Actividad(Integer.parseInt(values[0].strip()),
                            values[1],
                            values[2],
                            Integer.parseInt(values[3].strip()),
                            Double.parseDouble(values[4].toString()),
                            Integer.parseInt(values[5].strip()),
                            Integer.parseInt(values[6].strip())));
                }
            }
        } catch (Exception e) {
            System.err.printf("No se ha podido leer el fichero:", path);
        }
        return actividades;
    }

    @Override
    public Actividad findById(int id) {
        Actividad actividad = new Actividad();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(path.toString()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    
                    String[] values = line.split(";");
                     actividad = new Actividad(Integer.parseInt(values[0].strip()),
                            values[1],
                            values[2],
                            Integer.parseInt(values[3].strip()),
                            Integer.parseInt(values[4].strip()),
                            Integer.parseInt(values[5].strip()),
                            Integer.parseInt(values[6].strip()));
                }
            }
            
        } catch (Exception e) {
            System.err.printf("No se ha podido leer el fichero:", path);
        }
        return actividad;
    }

    @Override
    public boolean save(Actividad actividad) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toString()))) {
            bw.write(actividad.toCsvLine());
            bw.newLine();
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("Error al guardar el fichero CSV.", e);
        }
    }

    @Override
    public boolean update(Actividad actividad) {
            throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public boolean delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}





























