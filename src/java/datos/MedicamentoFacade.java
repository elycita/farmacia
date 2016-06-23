/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author CONDORI
 */
@Stateless
public class MedicamentoFacade extends AbstractFacade<Medicamento> {
    @PersistenceContext(unitName = "softwaredefarmaciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MedicamentoFacade() {
        super(Medicamento.class);
    }
    
}
