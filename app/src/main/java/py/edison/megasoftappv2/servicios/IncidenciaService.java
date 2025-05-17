package py.edison.megasoftappv2.servicios;

import py.edison.megasoftappv2.entidades.Incidencias;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaService {
    private List<Incidencias> incidencias;

    public IncidenciaService() {
        this.incidencias = new ArrayList<>();
    }

    public void registrarIncidencia(Incidencias incidencia) {
        incidencias.add(incidencia);
    }

    public List<Incidencias> obtenerIncidenciasPorFlete(String fleteId) {
        List<Incidencias> resultado = new ArrayList<>();
        for (Incidencias i : incidencias) {
            if (i.getFleteId().equals(fleteId)) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    public void marcarIncidenciaComoResuelta(String incidenciaId) {
        for (Incidencias i : incidencias) {
            if (i.getId().equals(incidenciaId)) {
                i.setResuelta(true);
                break;
            }
        }
    }
}