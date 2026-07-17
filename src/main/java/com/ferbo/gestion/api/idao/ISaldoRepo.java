package com.ferbo.gestion.api.idao;

import java.time.LocalDate;
import com.ferbo.gestion.api.model.Saldo;
import com.ferbo.gestion.core.model.cliente.Cliente;

public interface ISaldoRepo 
{
    public Saldo getSaldo(Cliente cliente, LocalDate fecha, String emisorRFC);
}
