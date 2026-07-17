package com.ferbo.gestion.api.idao;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IInventarioRepo 
{
    public BigDecimal getCantidad(Integer idCliente, LocalDate fecha);
}
