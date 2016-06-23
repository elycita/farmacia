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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author CONDORI
 */
@Entity
@Table(name = "compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compra.findAll", query = "SELECT c FROM Compra c"),
    @NamedQuery(name = "Compra.findByCompraID", query = "SELECT c FROM Compra c WHERE c.compraID = :compraID"),
    @NamedQuery(name = "Compra.findByFechaCompra", query = "SELECT c FROM Compra c WHERE c.fechaCompra = :fechaCompra"),
    @NamedQuery(name = "Compra.findByMontoTotal", query = "SELECT c FROM Compra c WHERE c.montoTotal = :montoTotal"),
    @NamedQuery(name = "Compra.findByDescuento", query = "SELECT c FROM Compra c WHERE c.descuento = :descuento"),
    @NamedQuery(name = "Compra.findByObservacion", query = "SELECT c FROM Compra c WHERE c.observacion = :observacion")})
public class Compra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "compraID")
    private Integer compraID;
    @Column(name = "FechaCompra")
    @Temporal(TemporalType.DATE)
    private Date fechaCompra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MontoTotal")
    private Double montoTotal;
    @Column(name = "Descuento")
    private Double descuento;
    @Size(max = 100)
    @Column(name = "Observacion")
    private String observacion;
    @JoinColumn(name = "usuarioID", referencedColumnName = "usuarioID")
    @ManyToOne
    private Usuario usuarioID;
    @OneToMany(mappedBy = "compraID")
    private Collection<Detallecompra> detallecompraCollection;
    @OneToMany(mappedBy = "compraID")
    private Collection<Proveedor> proveedorCollection;

    public Compra() {
    }

    public Compra(Integer compraID) {
        this.compraID = compraID;
    }

    public Integer getCompraID() {
        return compraID;
    }

    public void setCompraID(Integer compraID) {
        this.compraID = compraID;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Usuario getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Usuario usuarioID) {
        this.usuarioID = usuarioID;
    }

    @XmlTransient
    public Collection<Detallecompra> getDetallecompraCollection() {
        return detallecompraCollection;
    }

    public void setDetallecompraCollection(Collection<Detallecompra> detallecompraCollection) {
        this.detallecompraCollection = detallecompraCollection;
    }

    @XmlTransient
    public Collection<Proveedor> getProveedorCollection() {
        return proveedorCollection;
    }

    public void setProveedorCollection(Collection<Proveedor> proveedorCollection) {
        this.proveedorCollection = proveedorCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (compraID != null ? compraID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.compraID == null && other.compraID != null) || (this.compraID != null && !this.compraID.equals(other.compraID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "datos.Compra[ compraID=" + compraID + " ]";
    }
    
}
