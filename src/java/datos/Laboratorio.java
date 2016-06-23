/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author CONDORI
 */
@Entity
@Table(name = "laboratorio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Laboratorio.findAll", query = "SELECT l FROM Laboratorio l"),
    @NamedQuery(name = "Laboratorio.findByLaboratorioID", query = "SELECT l FROM Laboratorio l WHERE l.laboratorioID = :laboratorioID"),
    @NamedQuery(name = "Laboratorio.findByNombre", query = "SELECT l FROM Laboratorio l WHERE l.nombre = :nombre")})
public class Laboratorio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "laboratorioID")
    private Integer laboratorioID;
    @Size(max = 50)
    @Column(name = "Nombre")
    private String nombre;
    @OneToMany(mappedBy = "laboratorioID")
    private Collection<Medicamento> medicamentoCollection;

    public Laboratorio() {
    }

    public Laboratorio(Integer laboratorioID) {
        this.laboratorioID = laboratorioID;
    }

    public Integer getLaboratorioID() {
        return laboratorioID;
    }

    public void setLaboratorioID(Integer laboratorioID) {
        this.laboratorioID = laboratorioID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Medicamento> getMedicamentoCollection() {
        return medicamentoCollection;
    }

    public void setMedicamentoCollection(Collection<Medicamento> medicamentoCollection) {
        this.medicamentoCollection = medicamentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (laboratorioID != null ? laboratorioID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Laboratorio)) {
            return false;
        }
        Laboratorio other = (Laboratorio) object;
        if ((this.laboratorioID == null && other.laboratorioID != null) || (this.laboratorioID != null && !this.laboratorioID.equals(other.laboratorioID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
