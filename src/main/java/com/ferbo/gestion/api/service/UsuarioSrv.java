package com.ferbo.gestion.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.ferbo.gestion.api.idao.IUsuarioRepo;
import com.ferbo.gestion.core.model.sistema.Usuario;

@Service
public class UsuarioSrv 
{
    private static Logger log = LogManager.getLogger(UsuarioSrv.class);
    
    private final IUsuarioRepo usuarioDAO;
    
    public UsuarioSrv (IUsuarioRepo usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
    
    public Usuario buscarUsuarioPorNumero(String numEmpleado){
        return usuarioDAO.buscarUsuarioPorNumero(numEmpleado).orElseThrow(() -> new RuntimeException("Error, usuario no encontrado"));
    }
    
}
