package com.ferbo.gestion.api.service;

import java.util.List;
import java.util.stream.Collectors;
import com.ferbo.gestion.api.dto.ClienteDTO;
import com.ferbo.gestion.api.idao.IClienteRepo;
import com.ferbo.gestion.api.mapper.IClienteMapper;
import com.ferbo.gestion.core.model.cliente.Cliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteSrv 
{
    private static Logger log = LogManager.getLogger(ClienteSrv.class);
    
    private final IClienteRepo clienteRepo;
    
    @Autowired
    private IClienteMapper iClienteMapper;
    
    public ClienteSrv(IClienteRepo clienteRepo){
        this.clienteRepo = clienteRepo;
    }
    
    public List<ClienteDTO> buscarClientes()
    {
        return clienteRepo.buscarClientesActivos()
            .stream()
            .map(this::convertirCliente).collect(Collectors.toList());
    }
    
    public Cliente buscarClientePorId(Integer idCliente)
    {
        return clienteRepo.buscarPorId(idCliente).orElseThrow(() -> new RuntimeException("No se encontro el cliente con ese identificador"));
    }
    
    private ClienteDTO convertirCliente(Cliente cliente){
        return iClienteMapper.toDTO(cliente);
    }
    
}
