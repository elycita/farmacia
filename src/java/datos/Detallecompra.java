/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author CONDORI
 */
@Entity
@Table(name = "detallecompra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallecompra.findAll", query = "SELECT d FROM Detallecompra d"),
    @NamedQuery(name = "Detallecompra.findByDetalleCompraID", query = "SELECT d FROM Detallecompra d WHERE d.detalleCompraID = :detalleCompraID"),
    @NamedQuery(name = "Detallecompra.findByCantidad", query = "SELECT d FROM Detallecompra d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detallecompra.findByPrecioCompra", query = "SELECT d FROM Detallecompra d WHERE d.precioCompra = :precioCompra")})
public class Detallecompra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "detalleCompraID")
    private Integer detalleCompraID;
    @Column(name = "Cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PrecioCompra")
    private Double precioCompra;
    @JoinColumn(name = "medicamentoID", referencedColumnName = "medicamentoID")
    @ManyToOne
    private Medicamento medicamentoID;
    @JoinColumn(name = "compraID", referencedColumnName = "compraID")
    @ManyToOne
    private Compra compraID;

    public Detallecompra() {
    }

    public Detallecompra(Integer detalleCompraID) {
        this.detalleCompraID = detalleCompraID;
    }

    public Integer getDetalleCompraID() {
        return detalleCompraID;
    }

    public void setDetalleCompraID(Integer detalleCompraID) {
        this.detalleCompraID = detalleCompraID;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Medicamento getMedicamentoID() {
        return medicamentoID;
    }

    public void setMedicamentoID(Medicamento medicamentoID) {
        this.medicamentoID = medicamentoID;
    }

    public Compra getCompraID() {
        return compraID;
    }

    public void setCompraID(Compra compraID) {
        this.compraID = compraID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleCompraID != null ? detalleCompraID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallecompra)) {
            return false;
        }
        Detallecompra other = (Detallecompra) object;
        if ((this.detalleCompraID == null && other.detalleCompraID != null) || (this.detalleCompraID != null && !this.detalleCompraID.equals(other.detalleCompraID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return detalleCompraID + "";
    }
    
}
