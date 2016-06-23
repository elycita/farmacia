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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author CONDORI
 */
@Entity
@Table(name = "proveedor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedor.findAll", query = "SELECT p FROM Proveedor p"),
    @NamedQuery(name = "Proveedor.findByProveedorID", query = "SELECT p FROM Proveedor p WHERE p.proveedorID = :proveedorID"),
    @NamedQuery(name = "Proveedor.findByNombre", query = "SELECT p FROM Proveedor p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Proveedor.findByNit", query = "SELECT p FROM Proveedor p WHERE p.nit = :nit"),
    @NamedQuery(name = "Proveedor.findByTelefono", query = "SELECT p FROM Proveedor p WHERE p.telefono = :telefono")})
public class Proveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "proveedorID")
    private Integer proveedorID;
    @Size(max = 50)
    @Column(name = "Nombre")
    private String nombre;
    @Size(max = 25)
    @Column(name = "Nit")
    private String nit;
    @Column(name = "Telefono")
    private Integer telefono;
    @JoinColumn(name = "compraID", referencedColumnName = "compraID")
    @ManyToOne
    private Compra compraID;

    public Proveedor() {
    }

    public Proveedor(Integer proveedorID) {
        this.proveedorID = proveedorID;
    }

    public Integer getProveedorID() {
        return proveedorID;
    }

    public void setProveedorID(Integer proveedorID) {
        this.proveedorID = proveedorID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
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
        hash += (proveedorID != null ? proveedorID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.proveedorID == null && other.proveedorID != null) || (this.proveedorID != null && !this.proveedorID.equals(other.proveedorID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return proveedorID + nombre ;
    }
    
}
