/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import datos.exceptions.NonexistentEntityException;
import datos.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author CONDORI
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws RollbackFailureException, Exception {
        if (cliente.getVentaCollection() == null) {
            cliente.setVentaCollection(new ArrayList<Venta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Venta> attachedVentaCollection = new ArrayList<Venta>();
            for (Venta ventaCollectionVentaToAttach : cliente.getVentaCollection()) {
                ventaCollectionVentaToAttach = em.getReference(ventaCollectionVentaToAttach.getClass(), ventaCollectionVentaToAttach.getVentaID());
                attachedVentaCollection.add(ventaCollectionVentaToAttach);
            }
            cliente.setVentaCollection(attachedVentaCollection);
            em.persist(cliente);
            for (Venta ventaCollectionVenta : cliente.getVentaCollection()) {
                Cliente oldClienteIDOfVentaCollectionVenta = ventaCollectionVenta.getClienteID();
                ventaCollectionVenta.setClienteID(cliente);
                ventaCollectionVenta = em.merge(ventaCollectionVenta);
                if (oldClienteIDOfVentaCollectionVenta != null) {
                    oldClienteIDOfVentaCollectionVenta.getVentaCollection().remove(ventaCollectionVenta);
                    oldClienteIDOfVentaCollectionVenta = em.merge(oldClienteIDOfVentaCollectionVenta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getClienteID());
            Collection<Venta> ventaCollectionOld = persistentCliente.getVentaCollection();
            Collection<Venta> ventaCollectionNew = cliente.getVentaCollection();
            Collection<Venta> attachedVentaCollectionNew = new ArrayList<Venta>();
            for (Venta ventaCollectionNewVentaToAttach : ventaCollectionNew) {
                ventaCollectionNewVentaToAttach = em.getReference(ventaCollectionNewVentaToAttach.getClass(), ventaCollectionNewVentaToAttach.getVentaID());
                attachedVentaCollectionNew.add(ventaCollectionNewVentaToAttach);
            }
            ventaCollectionNew = attachedVentaCollectionNew;
            cliente.setVentaCollection(ventaCollectionNew);
            cliente = em.merge(cliente);
            for (Venta ventaCollectionOldVenta : ventaCollectionOld) {
                if (!ventaCollectionNew.contains(ventaCollectionOldVenta)) {
                    ventaCollectionOldVenta.setClienteID(null);
                    ventaCollectionOldVenta = em.merge(ventaCollectionOldVenta);
                }
            }
            for (Venta ventaCollectionNewVenta : ventaCollectionNew) {
                if (!ventaCollectionOld.contains(ventaCollectionNewVenta)) {
                    Cliente oldClienteIDOfVentaCollectionNewVenta = ventaCollectionNewVenta.getClienteID();
                    ventaCollectionNewVenta.setClienteID(cliente);
                    ventaCollectionNewVenta = em.merge(ventaCollectionNewVenta);
                    if (oldClienteIDOfVentaCollectionNewVenta != null && !oldClienteIDOfVentaCollectionNewVenta.equals(cliente)) {
                        oldClienteIDOfVentaCollectionNewVenta.getVentaCollection().remove(ventaCollectionNewVenta);
                        oldClienteIDOfVentaCollectionNewVenta = em.merge(oldClienteIDOfVentaCollectionNewVenta);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getClienteID();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getClienteID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            Collection<Venta> ventaCollection = cliente.getVentaCollection();
            for (Venta ventaCollectionVenta : ventaCollection) {
                ventaCollectionVenta.setClienteID(null);
                ventaCollectionVenta = em.merge(ventaCollectionVenta);
            }
            em.remove(cliente);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
