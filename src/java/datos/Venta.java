/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author CONDORI
 */
@Entity
@Table(name = "venta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
    @NamedQuery(name = "Venta.findByVentaID", query = "SELECT v FROM Venta v WHERE v.ventaID = :ventaID"),
    @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fecha = :fecha")})
public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ventaID")
    private Integer ventaID;
    @Column(name = "Fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @OneToMany(mappedBy = "ventaID")
    private Collection<Detallepedido> detallepedidoCollection;
    @JoinColumn(name = "usuarioID", referencedColumnName = "usuarioID")
    @ManyToOne
    private Usuario usuarioID;
    @JoinColumn(name = "clienteID", referencedColumnName = "clienteID")
    @ManyToOne
    private Cliente clienteID;

    public Venta() {
    }

    public Venta(Integer ventaID) {
        this.ventaID = ventaID;
    }

    public Integer getVentaID() {
        return ventaID;
    }

    public void setVentaID(Integer ventaID) {
        this.ventaID = ventaID;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @XmlTransient
    public Collection<Detallepedido> getDetallepedidoCollection() {
        return detallepedidoCollection;
    }

    public void setDetallepedidoCollection(Collection<Detallepedido> detallepedidoCollection) {
        this.detallepedidoCollection = detallepedidoCollection;
    }

    public Usuario getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Usuario usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Cliente getClienteID() {
        return clienteID;
    }

    public void setClienteID(Cliente clienteID) {
        this.clienteID = clienteID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ventaID != null ? ventaID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.ventaID == null && other.ventaID != null) || (this.ventaID != null && !this.ventaID.equals(other.ventaID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ventaID + "";
    }
    
}
