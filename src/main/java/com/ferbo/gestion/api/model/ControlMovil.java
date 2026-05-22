package com.ferbo.gestion.api.model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "control_movil")
public class ControlMovil implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_ctrl_movil")
    private Integer id;

    @Size(max = 200)
    @Basic(optional = false)
    @Column(name = "nb_token", nullable = false, length = 200)
    private String token;

    @Column(name = "fh_expiracion")
    private LocalDate expiracion;

    @Column(name = "st_valido", nullable = false)
    private Boolean valido;
    
    @Size(max = 50)
    @Basic(optional = false)
    @Column(name = "nb_usuario_sistema", nullable = false, length = 50)
    private String usuarioSistema;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getExpiracion() {
        return expiracion;
    }

    public void setExpiracion(LocalDate expiracion) {
        this.expiracion = expiracion;
    }

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    public String getUsuarioSistema() {
        return usuarioSistema;
    }

    public void setUsuarioSistema(String usuarioSistema) {
        this.usuarioSistema = usuarioSistema;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ControlMovil other = (ControlMovil) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ControlMovil [id=" + id + ", expiracion=" + expiracion + "]";
    }

}
