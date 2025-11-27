package co.edu.udistrital.ApiNominaMunoz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import co.edu.udistrital.ApiNominaMunoz.model.NominaDTO;
import co.edu.udistrital.ApiNominaMunoz.repository.NominaRepository;

@Service
public class NominaService {

    private final NominaRepository repo = new NominaRepository();

    // Listar todas las nóminas
    public List<NominaDTO> listar() {
        List<NominaDTO> lista = repo.findAll();
        System.out.println("TOTAL REGISTROS LEIDOS DESDE JSON = " + lista.size());
        return lista;
    }

    // Buscar por ID del empleado
    public NominaDTO buscarId(int id) {
        for (NominaDTO n : repo.findAll()) {
            if (n.getEmpleadoDTO().getId_p() == id) {
                return n;
            }
        }
        return null;
    }

    // Crear
    public void agregar(NominaDTO nuevo) {
        if (nuevo != null) {
            List<NominaDTO> lista = repo.findAll();
            lista.add(nuevo);
            repo.saveAll(lista);
        }
    }

    // Actualizar
    public boolean actualizar(int id, NominaDTO actual) {
        List<NominaDTO> lista = repo.findAll();

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getEmpleadoDTO().getId_p() == id) {
                lista.set(i, actual);
                repo.saveAll(lista);
                return true;
            }
        }
        return false;
    }

    // Eliminar
    public boolean borrar(int id) {
        List<NominaDTO> lista = repo.findAll();
        boolean eliminado = lista.removeIf(n -> n.getEmpleadoDTO().getId_p() == id);

        if (eliminado) {
            repo.saveAll(lista);
            return true;
        }
        return false;
    }

    // Calcular nómina por ID
    public Map<String, Object> calPorId(int id) {

        List<NominaDTO> lista = repo.findAll();
        Map<String, Object> map = new HashMap<>();

        for (NominaDTO n : lista) {
            if (n.getEmpleadoDTO().getId_p() == id) {

                map.put("nomina", n);
                map.put("descSalud", n.descSalud());
                map.put("descPension", n.descPension());
                map.put("salarioMes", n.calSalarioMes());
                map.put("subsidioTransporte", n.subsidioTransp());
                map.put("netoPagar",
                        n.calSalarioMes()
                        - n.descPension()
                        - n.descSalud()
                        + n.subsidioTransp()
                );

                return map;
            }
        }
        return null;
    }

  
    // lo de puntos,sorteo

    public Map<String, Object> sorteoNomina(int numeroConteo) {

        List<NominaDTO> lista = repo.findAll();

        // Copia no daña el archivo JSON
        List<NominaDTO> copia = new ArrayList<>(lista);
        List<String> pasos = new ArrayList<>();

        int index = 0;
        int ronda = 1;

        while (copia.size() > 1) {
            index = (index + numeroConteo - 1) % copia.size();

            NominaDTO eliminado = copia.remove(index);
            pasos.add(ronda + "R -> elimina " + eliminado.getEmpleadoDTO().getNombres());
            ronda++;
        }

        NominaDTO ganador = copia.get(0);

        Map<String, Object> r = new HashMap<>();
        r.put("nominaPagar", ganador);
        r.put("nombreGanador", ganador.getEmpleadoDTO().getNombres());
        r.put("rondas", pasos);

        return r;
    }
}
