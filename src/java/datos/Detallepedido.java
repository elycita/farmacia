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
@Table(name = "detallepedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallepedido.findAll", query = "SELECT d FROM Detallepedido d"),
    @NamedQuery(name = "Detallepedido.findByDetallePedidoID", query = "SELECT d FROM Detallepedido d WHERE d.detallePedidoID = :detallePedidoID"),
    @NamedQuery(name = "Detallepedido.findByCantidad", query = "SELECT d FROM Detallepedido d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detallepedido.findByPrecioUnitario", query = "SELECT d FROM Detallepedido d WHERE d.precioUnitario = :precioUnitario"),
    @NamedQuery(name = "Detallepedido.findByPedidoID", query = "SELECT d FROM Detallepedido d WHERE d.pedidoID = :pedidoID")})
public class Detallepedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "detallePedidoID")
    private Integer detallePedidoID;
    @Column(name = "Cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PrecioUnitario")
    private Double precioUnitario;
    @Column(name = "pedidoID")
    private Integer pedidoID;
    @JoinColumn(name = "ventaID", referencedColumnName = "ventaID")
    @ManyToOne
    private Venta ventaID;
    @JoinColumn(name = "medicamentoID", referencedColumnName = "medicamentoID")
    @ManyToOne
    private Medicamento medicamentoID;

    public Detallepedido() {
    }

    public Detallepedido(Integer detallePedidoID) {
        this.detallePedidoID = detallePedidoID;
    }

    public Integer getDetallePedidoID() {
        return detallePedidoID;
    }

    public void setDetallePedidoID(Integer detallePedidoID) {
        this.detallePedidoID = detallePedidoID;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getPedidoID() {
        return pedidoID;
    }

    public void setPedidoID(Integer pedidoID) {
        this.pedidoID = pedidoID;
    }

    public Venta getVentaID() {
        return ventaID;
    }

    public void setVentaID(Venta ventaID) {
        this.ventaID = ventaID;
    }

    public Medicamento getMedicamentoID() {
        return medicamentoID;
    }

    public void setMedicamentoID(Medicamento medicamentoID) {
        this.medicamentoID = medicamentoID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallePedidoID != null ? detallePedidoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallepedido)) {
            return false;
        }
        Detallepedido other = (Detallepedido) object;
        if ((this.detallePedidoID == null && other.detallePedidoID != null) || (this.detallePedidoID != null && !this.detallePedidoID.equals(other.detallePedidoID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "datos.Detallepedido[ detallePedidoID=" + detallePedidoID + " ]";
    }
    
}
