/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import datos.exceptions.NonexistentEntityException;
import datos.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author CONDORI
 */
public class DetallepedidoJpaController implements Serializable {

    public DetallepedidoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallepedido detallepedido) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Venta ventaID = detallepedido.getVentaID();
            if (ventaID != null) {
                ventaID = em.getReference(ventaID.getClass(), ventaID.getVentaID());
                detallepedido.setVentaID(ventaID);
            }
            Medicamento medicamentoID = detallepedido.getMedicamentoID();
            if (medicamentoID != null) {
                medicamentoID = em.getReference(medicamentoID.getClass(), medicamentoID.getMedicamentoID());
                detallepedido.setMedicamentoID(medicamentoID);
            }
            em.persist(detallepedido);
            if (ventaID != null) {
                ventaID.getDetallepedidoCollection().add(detallepedido);
                ventaID = em.merge(ventaID);
            }
            if (medicamentoID != null) {
                medicamentoID.getDetallepedidoCollection().add(detallepedido);
                medicamentoID = em.merge(medicamentoID);
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

    public void edit(Detallepedido detallepedido) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallepedido persistentDetallepedido = em.find(Detallepedido.class, detallepedido.getDetallePedidoID());
            Venta ventaIDOld = persistentDetallepedido.getVentaID();
            Venta ventaIDNew = detallepedido.getVentaID();
            Medicamento medicamentoIDOld = persistentDetallepedido.getMedicamentoID();
            Medicamento medicamentoIDNew = detallepedido.getMedicamentoID();
            if (ventaIDNew != null) {
                ventaIDNew = em.getReference(ventaIDNew.getClass(), ventaIDNew.getVentaID());
                detallepedido.setVentaID(ventaIDNew);
            }
            if (medicamentoIDNew != null) {
                medicamentoIDNew = em.getReference(medicamentoIDNew.getClass(), medicamentoIDNew.getMedicamentoID());
                detallepedido.setMedicamentoID(medicamentoIDNew);
            }
            detallepedido = em.merge(detallepedido);
            if (ventaIDOld != null && !ventaIDOld.equals(ventaIDNew)) {
                ventaIDOld.getDetallepedidoCollection().remove(detallepedido);
                ventaIDOld = em.merge(ventaIDOld);
            }
            if (ventaIDNew != null && !ventaIDNew.equals(ventaIDOld)) {
                ventaIDNew.getDetallepedidoCollection().add(detallepedido);
                ventaIDNew = em.merge(ventaIDNew);
            }
            if (medicamentoIDOld != null && !medicamentoIDOld.equals(medicamentoIDNew)) {
                medicamentoIDOld.getDetallepedidoCollection().remove(detallepedido);
                medicamentoIDOld = em.merge(medicamentoIDOld);
            }
            if (medicamentoIDNew != null && !medicamentoIDNew.equals(medicamentoIDOld)) {
                medicamentoIDNew.getDetallepedidoCollection().add(detallepedido);
                medicamentoIDNew = em.merge(medicamentoIDNew);
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
                Integer id = detallepedido.getDetallePedidoID();
                if (findDetallepedido(id) == null) {
                    throw new NonexistentEntityException("The detallepedido with id " + id + " no longer exists.");
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
            Detallepedido detallepedido;
            try {
                detallepedido = em.getReference(Detallepedido.class, id);
                detallepedido.getDetallePedidoID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallepedido with id " + id + " no longer exists.", enfe);
            }
            Venta ventaID = detallepedido.getVentaID();
            if (ventaID != null) {
                ventaID.getDetallepedidoCollection().remove(detallepedido);
                ventaID = em.merge(ventaID);
            }
            Medicamento medicamentoID = detallepedido.getMedicamentoID();
            if (medicamentoID != null) {
                medicamentoID.getDetallepedidoCollection().remove(detallepedido);
                medicamentoID = em.merge(medicamentoID);
            }
            em.remove(detallepedido);
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

    public List<Detallepedido> findDetallepedidoEntities() {
        return findDetallepedidoEntities(true, -1, -1);
    }

    public List<Detallepedido> findDetallepedidoEntities(int maxResults, int firstResult) {
        return findDetallepedidoEntities(false, maxResults, firstResult);
    }

    private List<Detallepedido> findDetallepedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallepedido.class));
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

    public Detallepedido findDetallepedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallepedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallepedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallepedido> rt = cq.from(Detallepedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
