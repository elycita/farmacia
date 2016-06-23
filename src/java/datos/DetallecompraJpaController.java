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
public class DetallecompraJpaController implements Serializable {

    public DetallecompraJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallecompra detallecompra) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medicamento medicamentoID = detallecompra.getMedicamentoID();
            if (medicamentoID != null) {
                medicamentoID = em.getReference(medicamentoID.getClass(), medicamentoID.getMedicamentoID());
                detallecompra.setMedicamentoID(medicamentoID);
            }
            Compra compraID = detallecompra.getCompraID();
            if (compraID != null) {
                compraID = em.getReference(compraID.getClass(), compraID.getCompraID());
                detallecompra.setCompraID(compraID);
            }
            em.persist(detallecompra);
            if (medicamentoID != null) {
                medicamentoID.getDetallecompraCollection().add(detallecompra);
                medicamentoID = em.merge(medicamentoID);
            }
            if (compraID != null) {
                compraID.getDetallecompraCollection().add(detallecompra);
                compraID = em.merge(compraID);
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

    public void edit(Detallecompra detallecompra) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Detallecompra persistentDetallecompra = em.find(Detallecompra.class, detallecompra.getDetalleCompraID());
            Medicamento medicamentoIDOld = persistentDetallecompra.getMedicamentoID();
            Medicamento medicamentoIDNew = detallecompra.getMedicamentoID();
            Compra compraIDOld = persistentDetallecompra.getCompraID();
            Compra compraIDNew = detallecompra.getCompraID();
            if (medicamentoIDNew != null) {
                medicamentoIDNew = em.getReference(medicamentoIDNew.getClass(), medicamentoIDNew.getMedicamentoID());
                detallecompra.setMedicamentoID(medicamentoIDNew);
            }
            if (compraIDNew != null) {
                compraIDNew = em.getReference(compraIDNew.getClass(), compraIDNew.getCompraID());
                detallecompra.setCompraID(compraIDNew);
            }
            detallecompra = em.merge(detallecompra);
            if (medicamentoIDOld != null && !medicamentoIDOld.equals(medicamentoIDNew)) {
                medicamentoIDOld.getDetallecompraCollection().remove(detallecompra);
                medicamentoIDOld = em.merge(medicamentoIDOld);
            }
            if (medicamentoIDNew != null && !medicamentoIDNew.equals(medicamentoIDOld)) {
                medicamentoIDNew.getDetallecompraCollection().add(detallecompra);
                medicamentoIDNew = em.merge(medicamentoIDNew);
            }
            if (compraIDOld != null && !compraIDOld.equals(compraIDNew)) {
                compraIDOld.getDetallecompraCollection().remove(detallecompra);
                compraIDOld = em.merge(compraIDOld);
            }
            if (compraIDNew != null && !compraIDNew.equals(compraIDOld)) {
                compraIDNew.getDetallecompraCollection().add(detallecompra);
                compraIDNew = em.merge(compraIDNew);
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
                Integer id = detallecompra.getDetalleCompraID();
                if (findDetallecompra(id) == null) {
                    throw new NonexistentEntityException("The detallecompra with id " + id + " no longer exists.");
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
            Detallecompra detallecompra;
            try {
                detallecompra = em.getReference(Detallecompra.class, id);
                detallecompra.getDetalleCompraID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallecompra with id " + id + " no longer exists.", enfe);
            }
            Medicamento medicamentoID = detallecompra.getMedicamentoID();
            if (medicamentoID != null) {
                medicamentoID.getDetallecompraCollection().remove(detallecompra);
                medicamentoID = em.merge(medicamentoID);
            }
            Compra compraID = detallecompra.getCompraID();
            if (compraID != null) {
                compraID.getDetallecompraCollection().remove(detallecompra);
                compraID = em.merge(compraID);
            }
            em.remove(detallecompra);
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

    public List<Detallecompra> findDetallecompraEntities() {
        return findDetallecompraEntities(true, -1, -1);
    }

    public List<Detallecompra> findDetallecompraEntities(int maxResults, int firstResult) {
        return findDetallecompraEntities(false, maxResults, firstResult);
    }

    private List<Detallecompra> findDetallecompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallecompra.class));
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

    public Detallecompra findDetallecompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallecompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallecompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallecompra> rt = cq.from(Detallecompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
