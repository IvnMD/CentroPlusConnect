package es.ies.puerto.repositories.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import es.ies.puerto.models.Actividad;
import es.ies.puerto.repositories.ActividadRepositoryInterface;

public class ActividadCsvRepository implements ActividadRepositoryInterface {
    private final Path path;

    public ActividadCsvRepository() {
        this(Path.of("actividades.csv"));
    }

    public ActividadCsvRepository(Path path) {
        this.path = path;
    }

    private Actividad parseLine(String line) {
        String[] v = line.split(";");
        return new Actividad(
                Integer.parseInt(v[0].strip()),
                v[1].strip(),
                v[2].strip(),
                Integer.parseInt(v[3].strip()),
                Double.parseDouble(v[4].strip()),
                Integer.parseInt(v[5].strip()),
                Integer.parseInt(v[6].strip()));
    }

    @Override
    public List<Actividad> findAll() {
        List<Actividad> actividades = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    actividades.add(parseLine(line));
                }
            }
        } catch (Exception e) {
            System.err.println("No se ha podido leer el fichero: " + path);
        }
        return actividades;
    }

    @Override
    public Actividad findById(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(path.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (Integer.parseInt(values[0].strip()) == id) {
                    return new Actividad(
                            Integer.parseInt(values[0].strip()),
                            values[1].strip(),
                            values[2].strip(),
                            Integer.parseInt(values[3].strip()),
                            Double.parseDouble(values[4].strip()),
                            Integer.parseInt(values[5].strip()),
                            Integer.parseInt(values[6].strip()));
                }
            }
        } catch (Exception e) {
            System.err.printf("No se ha podido leer el fichero %s%n", path);
        }
        return null;
    }

    @Override
    public boolean save(Actividad actividad) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toString(), true))) {
            bw.write(actividad.toCsvLine());
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar actividad: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Actividad actividad) {
        List<Actividad> actividades = findAll();
        boolean encontrada = false;
        for (int i = 0; i < actividades.size(); i++) {
            if (actividades.get(i).getId() == actividad.getId()) {
                actividades.set(i, actividad);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            return false;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toString()))) {
            for (Actividad a : actividades) {
                bw.write(a.toCsvLine());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("Error al actualizar el fichero CSV.", e);
        }
    }

    @Override
    public boolean delete(int id) {
        List<Actividad> actividades = findAll();
        boolean eliminada = actividades.removeIf(a -> a.getId() == id);
        if (!eliminada) {
            return false;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toString()))) {
            for (Actividad a : actividades) {
                bw.write(a.toCsvLine());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("Error al eliminar del fichero CSV.", e);
        }
    }

    private boolean writeAll(List<Actividad> actividades) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toString(), false))) {
            for (Actividad a : actividades) {
                bw.write(a.toCsvLine());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al escribir fichero: " + e.getMessage());
            return false;
        }
    }
}
