package com.ferbo.gestion.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.ferbo.gestion.api.dto.ValidacionSaldoDTO;
import com.ferbo.gestion.api.idao.ISaldoRepo;
import com.ferbo.gestion.api.model.Saldo;
import com.ferbo.gestion.core.model.cliente.Cliente;
import com.ferbo.gestion.core.model.cliente.detalle.CandadoSalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class SaldoSrv 
{
    private static Logger log = LogManager.getLogger(SaldoSrv.class);
    
    @Autowired
    private InventarioSrv inventarioSrv;
    
    @Autowired
    private CandadoSalidaSrv candadoSalidaSrv;
    
    @Autowired
    private ClienteSrv clienteSrv;
    
    private final ISaldoRepo iSaldoRepo;
    
    public SaldoSrv(ISaldoRepo iSaldoRepo)
    {
        this.iSaldoRepo = iSaldoRepo;
    }
    
    public Saldo obtenerSaldo(Cliente cliente, LocalDate fecha)
    {
        return iSaldoRepo.getSaldo(cliente, fecha, null);
    }
    
    public ValidacionSaldoDTO validarSaldo(Integer idCliente, LocalDate fecha)
    {
        ValidacionSaldoDTO validacionSaldoDTO = null;
        CandadoSalida candadoSalida = null;
        Saldo saldo = null;
        Cliente cliente = clienteSrv.buscarClientePorId(idCliente);
        
        boolean isSaldoVencido = false;
        boolean isHabilitarSalida = false;
        BigDecimal saldoVencido = null;
        BigDecimal cantidadInventario = null;
        String descripcion = null;
        
        saldo = this.obtenerSaldo(cliente, fecha);
        cantidadInventario = inventarioSrv.obtenerCantidad(idCliente, fecha);
        candadoSalida = candadoSalidaSrv.obtenerCandodoPorCliente(idCliente);
        
        if(saldo == null) {
            descripcion = "El cliente NO tiene saldos pendientes, puede retirar su mercancia.";
            log.info(descripcion);
            validacionSaldoDTO = asignarValidacion(true, BigDecimal.ZERO, descripcion);
            return validacionSaldoDTO;
        }
        
        log.info("Saldo: en plazo = {}, 15 días = {}, 30 días = {}, 60 días = {}, más de 60 días = {}", saldo.getEnPlazo(), saldo.getAtraso15dias(), saldo.getAtraso30dias(), saldo.getAtraso60dias(), saldo.getAtrasoMayor60dias());
        log.info("Cantidad en inventario: {} piezas.", cantidadInventario);
        
        saldoVencido = saldo.getAtraso8dias().add(saldo.getAtraso15dias()).add(saldo.getAtraso30dias()).add(saldo.getAtraso60dias()).add(saldo.getAtrasoMayor60dias());
        
        if(saldoVencido.compareTo(BigDecimal.ZERO) > 0) {
            isSaldoVencido = true;
            log.info("El cliente {} presenta un saldo vencido: {}", cliente.getNombre(), saldo.getSaldo());
        }
        
        isHabilitarSalida = candadoSalida.getHabilitado();
        
        if(isSaldoVencido && isHabilitarSalida && candadoSalida.getNumSalidas() > 0) {
            descripcion = String.format("El cliente tiene un saldo vencido, pero tiene %s salidas permitidas.", candadoSalida.getNumSalidas());
            log.info(descripcion);
            validacionSaldoDTO = asignarValidacion(isHabilitarSalida, saldoVencido, descripcion);
            return validacionSaldoDTO;
        }
        
        if(isSaldoVencido && isHabilitarSalida && candadoSalida.getNumSalidas() <= 0) {
            descripcion = String.format("El cliente tiene un saldo vencido, pero no tiene salidas permitidas(%s).", candadoSalida.getNumSalidas());
            log.info("El cliente tiene adeudos vencidos. Favor de contactar con el área de facturacíon");
            validacionSaldoDTO = asignarValidacion(isHabilitarSalida, saldoVencido, descripcion);
            return validacionSaldoDTO;
        }
        
        if(isSaldoVencido && isHabilitarSalida == false) {
            descripcion = "El cliente tiene adeudos vencidos. Favor de contactar con el área de facturación.";
            log.info(descripcion);
            validacionSaldoDTO = asignarValidacion(isHabilitarSalida, saldoVencido, descripcion);
            return validacionSaldoDTO;
        }
        
        if(isSaldoVencido == false) {
            //retorna el control sin acciones, debido a que SI tiene permitidas las salidas.
            descripcion = "El cliente NO tiene saldos vencidos, puede retirar su mercancia.";
            log.info(descripcion);
            validacionSaldoDTO = asignarValidacion(true, BigDecimal.ZERO, descripcion);
            return validacionSaldoDTO;
        }
        
        if(isHabilitarSalida && candadoSalida.getNumSalidas() > 0) {
            descripcion = String.format("El cliente %s presenta un saldo vencido (%s), pero tiene permitidas las salidas", cliente.getNombre(), saldo.getSaldo());
            log.info(descripcion);
            validacionSaldoDTO = asignarValidacion(isHabilitarSalida, saldoVencido, descripcion);
            return validacionSaldoDTO;
        }
        
        descripcion = "El cliente no tiene permitida la salida de mercancia porque presenta un adeudo. Favor de contactar al área de cobranza.";
        validacionSaldoDTO = asignarValidacion(false, saldoVencido, descripcion);
        return validacionSaldoDTO;
    }
    
    public ValidacionSaldoDTO asignarValidacion(Boolean isHabilitarSalida, BigDecimal saldoVencido, String descripcion)
    {
        ValidacionSaldoDTO validacionSaldoDTO = new ValidacionSaldoDTO();
        validacionSaldoDTO.setIsHabilitarSalida(isHabilitarSalida);
        validacionSaldoDTO.setSaldoVencido(saldoVencido);
        validacionSaldoDTO.setDescripcion(descripcion);
        
        return validacionSaldoDTO;
    }
    
}
