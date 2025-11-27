package co.edu.udistrital.ApiNominaMunoz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import co.edu.udistrital.ApiNominaMunoz.model.NominaDTO;
import co.edu.udistrital.ApiNominaMunoz.repository.NominaRepository;

@Service
public class NominaService {

    private final NominaRepository repo = new NominaRepository();

    public List<NominaDTO> listar() {
    List<NominaDTO> lista = repo.findAll();
    System.out.println("TOTAL REGISTROS LEIDOS DESDE JSON = " + lista.size());
    return lista;
}


    public NominaDTO buscarId(int id) {
        for (NominaDTO n : repo.findAll()) {
            if (n.getEmpleadoDTO().getId_p() == id) {
                System.out.println("encontrado");
                return n;
            }
        }
        System.out.println("no existe ");
        return null;
    }

    public void agregar(NominaDTO nuevo) {
        if (nuevo != null) {
            List<NominaDTO> lista = repo.findAll();
            lista.add(nuevo);
            repo.saveAll(lista);
            System.out.println("nuevo agregado");
        } else {
            System.out.println("nuevo es null no se agrega");
        }
    }

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

    public boolean borrar(int id) {
        List<NominaDTO> lista = repo.findAll();
        boolean eliminar = lista.removeIf(n -> n.getEmpleadoDTO().getId_p() == id);

        if (eliminar) {
            repo.saveAll(lista);
            return true;
        }
        return false;
    }

    // Entrega resultado nomina
    public Map<String, Object> calPorId(int id) {

        List<NominaDTO> lista = repo.findAll();
        Map<String, Object> map = new HashMap<>();

        for (NominaDTO n : lista) {
            if (n.getEmpleadoDTO().getId_p() == id) {

                System.out.println("encontrado");

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

        System.out.println("no existe");
        return null;
    }
}

