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
public class DetallepedidoFacade extends AbstractFacade<Detallepedido> {
    @PersistenceContext(unitName = "softwaredefarmaciaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetallepedidoFacade() {
        super(Detallepedido.class);
    }
    
}
