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
@Table(name = "medicamento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medicamento.findAll", query = "SELECT m FROM Medicamento m"),
    @NamedQuery(name = "Medicamento.findByMedicamentoID", query = "SELECT m FROM Medicamento m WHERE m.medicamentoID = :medicamentoID"),
    @NamedQuery(name = "Medicamento.findByNombre", query = "SELECT m FROM Medicamento m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Medicamento.findByDescripcion", query = "SELECT m FROM Medicamento m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Medicamento.findByCantidad", query = "SELECT m FROM Medicamento m WHERE m.cantidad = :cantidad"),
    @NamedQuery(name = "Medicamento.findByPrecio", query = "SELECT m FROM Medicamento m WHERE m.precio = :precio"),
    @NamedQuery(name = "Medicamento.findByStock", query = "SELECT m FROM Medicamento m WHERE m.stock = :stock"),
    @NamedQuery(name = "Medicamento.findByStrockMinimo", query = "SELECT m FROM Medicamento m WHERE m.strockMinimo = :strockMinimo"),
    @NamedQuery(name = "Medicamento.findByStockMaximo", query = "SELECT m FROM Medicamento m WHERE m.stockMaximo = :stockMaximo"),
    @NamedQuery(name = "Medicamento.findByBajoreceta", query = "SELECT m FROM Medicamento m WHERE m.bajoreceta = :bajoreceta"),
    @NamedQuery(name = "Medicamento.findByFechaVencimiento", query = "SELECT m FROM Medicamento m WHERE m.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "Medicamento.findByUnidadMedida", query = "SELECT m FROM Medicamento m WHERE m.unidadMedida = :unidadMedida")})
public class Medicamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "medicamentoID")
    private Integer medicamentoID;
    @Size(max = 50)
    @Column(name = "Nombre")
    private String nombre;
    @Size(max = 100)
    @Column(name = "Descripcion")
    private String descripcion;
    @Column(name = "Cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Precio")
    private Double precio;
    @Column(name = "Stock")
    private Integer stock;
    @Column(name = "StrockMinimo")
    private Integer strockMinimo;
    @Column(name = "StockMaximo")
    private Integer stockMaximo;
    @Size(max = 10)
    @Column(name = "Bajoreceta")
    private String bajoreceta;
    @Column(name = "FechaVencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Size(max = 20)
    @Column(name = "UnidadMedida")
    private String unidadMedida;
    @OneToMany(mappedBy = "medicamentoID")
    private Collection<Detallecompra> detallecompraCollection;
    @OneToMany(mappedBy = "medicamentoID")
    private Collection<Detallepedido> detallepedidoCollection;
    @JoinColumn(name = "laboratorioID", referencedColumnName = "laboratorioID")
    @ManyToOne
    private Laboratorio laboratorioID;
    @JoinColumn(name = "categoriaID", referencedColumnName = "categoriaID")
    @ManyToOne
    private Categoria categoriaID;

    public Medicamento() {
    }

    public Medicamento(Integer medicamentoID) {
        this.medicamentoID = medicamentoID;
    }

    public Integer getMedicamentoID() {
        return medicamentoID;
    }

    public void setMedicamentoID(Integer medicamentoID) {
        this.medicamentoID = medicamentoID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStrockMinimo() {
        return strockMinimo;
    }

    public void setStrockMinimo(Integer strockMinimo) {
        this.strockMinimo = strockMinimo;
    }

    public Integer getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(Integer stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public String getBajoreceta() {
        return bajoreceta;
    }

    public void setBajoreceta(String bajoreceta) {
        this.bajoreceta = bajoreceta;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    @XmlTransient
    public Collection<Detallecompra> getDetallecompraCollection() {
        return detallecompraCollection;
    }

    public void setDetallecompraCollection(Collection<Detallecompra> detallecompraCollection) {
        this.detallecompraCollection = detallecompraCollection;
    }

    @XmlTransient
    public Collection<Detallepedido> getDetallepedidoCollection() {
        return detallepedidoCollection;
    }

    public void setDetallepedidoCollection(Collection<Detallepedido> detallepedidoCollection) {
        this.detallepedidoCollection = detallepedidoCollection;
    }

    public Laboratorio getLaboratorioID() {
        return laboratorioID;
    }

    public void setLaboratorioID(Laboratorio laboratorioID) {
        this.laboratorioID = laboratorioID;
    }

    public Categoria getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(Categoria categoriaID) {
        this.categoriaID = categoriaID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (medicamentoID != null ? medicamentoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicamento)) {
            return false;
        }
        Medicamento other = (Medicamento) object;
        if ((this.medicamentoID == null && other.medicamentoID != null) || (this.medicamentoID != null && !this.medicamentoID.equals(other.medicamentoID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  medicamentoID + " "+ nombre;
    }
    
}
