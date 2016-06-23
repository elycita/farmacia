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
public class VentaJpaController implements Serializable {

    public VentaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venta venta) throws RollbackFailureException, Exception {
        if (venta.getDetallepedidoCollection() == null) {
            venta.setDetallepedidoCollection(new ArrayList<Detallepedido>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario usuarioID = venta.getUsuarioID();
            if (usuarioID != null) {
                usuarioID = em.getReference(usuarioID.getClass(), usuarioID.getUsuarioID());
                venta.setUsuarioID(usuarioID);
            }
            Cliente clienteID = venta.getClienteID();
            if (clienteID != null) {
                clienteID = em.getReference(clienteID.getClass(), clienteID.getClienteID());
                venta.setClienteID(clienteID);
            }
            Collection<Detallepedido> attachedDetallepedidoCollection = new ArrayList<Detallepedido>();
            for (Detallepedido detallepedidoCollectionDetallepedidoToAttach : venta.getDetallepedidoCollection()) {
                detallepedidoCollectionDetallepedidoToAttach = em.getReference(detallepedidoCollectionDetallepedidoToAttach.getClass(), detallepedidoCollectionDetallepedidoToAttach.getDetallePedidoID());
                attachedDetallepedidoCollection.add(detallepedidoCollectionDetallepedidoToAttach);
            }
            venta.setDetallepedidoCollection(attachedDetallepedidoCollection);
            em.persist(venta);
            if (usuarioID != null) {
                usuarioID.getVentaCollection().add(venta);
                usuarioID = em.merge(usuarioID);
            }
            if (clienteID != null) {
                clienteID.getVentaCollection().add(venta);
                clienteID = em.merge(clienteID);
            }
            for (Detallepedido detallepedidoCollectionDetallepedido : venta.getDetallepedidoCollection()) {
                Venta oldVentaIDOfDetallepedidoCollectionDetallepedido = detallepedidoCollectionDetallepedido.getVentaID();
                detallepedidoCollectionDetallepedido.setVentaID(venta);
                detallepedidoCollectionDetallepedido = em.merge(detallepedidoCollectionDetallepedido);
                if (oldVentaIDOfDetallepedidoCollectionDetallepedido != null) {
                    oldVentaIDOfDetallepedidoCollectionDetallepedido.getDetallepedidoCollection().remove(detallepedidoCollectionDetallepedido);
                    oldVentaIDOfDetallepedidoCollectionDetallepedido = em.merge(oldVentaIDOfDetallepedidoCollectionDetallepedido);
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

    public void edit(Venta venta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Venta persistentVenta = em.find(Venta.class, venta.getVentaID());
            Usuario usuarioIDOld = persistentVenta.getUsuarioID();
            Usuario usuarioIDNew = venta.getUsuarioID();
            Cliente clienteIDOld = persistentVenta.getClienteID();
            Cliente clienteIDNew = venta.getClienteID();
            Collection<Detallepedido> detallepedidoCollectionOld = persistentVenta.getDetallepedidoCollection();
            Collection<Detallepedido> detallepedidoCollectionNew = venta.getDetallepedidoCollection();
            if (usuarioIDNew != null) {
                usuarioIDNew = em.getReference(usuarioIDNew.getClass(), usuarioIDNew.getUsuarioID());
                venta.setUsuarioID(usuarioIDNew);
            }
            if (clienteIDNew != null) {
                clienteIDNew = em.getReference(clienteIDNew.getClass(), clienteIDNew.getClienteID());
                venta.setClienteID(clienteIDNew);
            }
            Collection<Detallepedido> attachedDetallepedidoCollectionNew = new ArrayList<Detallepedido>();
            for (Detallepedido detallepedidoCollectionNewDetallepedidoToAttach : detallepedidoCollectionNew) {
                detallepedidoCollectionNewDetallepedidoToAttach = em.getReference(detallepedidoCollectionNewDetallepedidoToAttach.getClass(), detallepedidoCollectionNewDetallepedidoToAttach.getDetallePedidoID());
                attachedDetallepedidoCollectionNew.add(detallepedidoCollectionNewDetallepedidoToAttach);
            }
            detallepedidoCollectionNew = attachedDetallepedidoCollectionNew;
            venta.setDetallepedidoCollection(detallepedidoCollectionNew);
            venta = em.merge(venta);
            if (usuarioIDOld != null && !usuarioIDOld.equals(usuarioIDNew)) {
                usuarioIDOld.getVentaCollection().remove(venta);
                usuarioIDOld = em.merge(usuarioIDOld);
            }
            if (usuarioIDNew != null && !usuarioIDNew.equals(usuarioIDOld)) {
                usuarioIDNew.getVentaCollection().add(venta);
                usuarioIDNew = em.merge(usuarioIDNew);
            }
            if (clienteIDOld != null && !clienteIDOld.equals(clienteIDNew)) {
                clienteIDOld.getVentaCollection().remove(venta);
                clienteIDOld = em.merge(clienteIDOld);
            }
            if (clienteIDNew != null && !clienteIDNew.equals(clienteIDOld)) {
                clienteIDNew.getVentaCollection().add(venta);
                clienteIDNew = em.merge(clienteIDNew);
            }
            for (Detallepedido detallepedidoCollectionOldDetallepedido : detallepedidoCollectionOld) {
                if (!detallepedidoCollectionNew.contains(detallepedidoCollectionOldDetallepedido)) {
                    detallepedidoCollectionOldDetallepedido.setVentaID(null);
                    detallepedidoCollectionOldDetallepedido = em.merge(detallepedidoCollectionOldDetallepedido);
                }
            }
            for (Detallepedido detallepedidoCollectionNewDetallepedido : detallepedidoCollectionNew) {
                if (!detallepedidoCollectionOld.contains(detallepedidoCollectionNewDetallepedido)) {
                    Venta oldVentaIDOfDetallepedidoCollectionNewDetallepedido = detallepedidoCollectionNewDetallepedido.getVentaID();
                    detallepedidoCollectionNewDetallepedido.setVentaID(venta);
                    detallepedidoCollectionNewDetallepedido = em.merge(detallepedidoCollectionNewDetallepedido);
                    if (oldVentaIDOfDetallepedidoCollectionNewDetallepedido != null && !oldVentaIDOfDetallepedidoCollectionNewDetallepedido.equals(venta)) {
                        oldVentaIDOfDetallepedidoCollectionNewDetallepedido.getDetallepedidoCollection().remove(detallepedidoCollectionNewDetallepedido);
                        oldVentaIDOfDetallepedidoCollectionNewDetallepedido = em.merge(oldVentaIDOfDetallepedidoCollectionNewDetallepedido);
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
                Integer id = venta.getVentaID();
                if (findVenta(id) == null) {
                    throw new NonexistentEntityException("The venta with id " + id + " no longer exists.");
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
            Venta venta;
            try {
                venta = em.getReference(Venta.class, id);
                venta.getVentaID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            Usuario usuarioID = venta.getUsuarioID();
            if (usuarioID != null) {
                usuarioID.getVentaCollection().remove(venta);
                usuarioID = em.merge(usuarioID);
            }
            Cliente clienteID = venta.getClienteID();
            if (clienteID != null) {
                clienteID.getVentaCollection().remove(venta);
                clienteID = em.merge(clienteID);
            }
            Collection<Detallepedido> detallepedidoCollection = venta.getDetallepedidoCollection();
            for (Detallepedido detallepedidoCollectionDetallepedido : detallepedidoCollection) {
                detallepedidoCollectionDetallepedido.setVentaID(null);
                detallepedidoCollectionDetallepedido = em.merge(detallepedidoCollectionDetallepedido);
            }
            em.remove(venta);
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

    public List<Venta> findVentaEntities() {
        return findVentaEntities(true, -1, -1);
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return findVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> findVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venta.class));
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

    public Venta findVenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venta> rt = cq.from(Venta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
