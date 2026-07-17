package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.api.model.OcupacionPlanta;
import java.time.LocalDate;
import java.util.List;

public interface IOcupacionPlantaRepo 
{
    public List<OcupacionPlanta> ocupacionPlantaCamara(LocalDate fecha, List<Integer> idsClientes, Integer idPlanta, Integer idCamara);
}
