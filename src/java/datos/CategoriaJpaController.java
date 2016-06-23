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
public class CategoriaJpaController implements Serializable {

    public CategoriaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) throws RollbackFailureException, Exception {
        if (categoria.getMedicamentoCollection() == null) {
            categoria.setMedicamentoCollection(new ArrayList<Medicamento>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Medicamento> attachedMedicamentoCollection = new ArrayList<Medicamento>();
            for (Medicamento medicamentoCollectionMedicamentoToAttach : categoria.getMedicamentoCollection()) {
                medicamentoCollectionMedicamentoToAttach = em.getReference(medicamentoCollectionMedicamentoToAttach.getClass(), medicamentoCollectionMedicamentoToAttach.getMedicamentoID());
                attachedMedicamentoCollection.add(medicamentoCollectionMedicamentoToAttach);
            }
            categoria.setMedicamentoCollection(attachedMedicamentoCollection);
            em.persist(categoria);
            for (Medicamento medicamentoCollectionMedicamento : categoria.getMedicamentoCollection()) {
                Categoria oldCategoriaIDOfMedicamentoCollectionMedicamento = medicamentoCollectionMedicamento.getCategoriaID();
                medicamentoCollectionMedicamento.setCategoriaID(categoria);
                medicamentoCollectionMedicamento = em.merge(medicamentoCollectionMedicamento);
                if (oldCategoriaIDOfMedicamentoCollectionMedicamento != null) {
                    oldCategoriaIDOfMedicamentoCollectionMedicamento.getMedicamentoCollection().remove(medicamentoCollectionMedicamento);
                    oldCategoriaIDOfMedicamentoCollectionMedicamento = em.merge(oldCategoriaIDOfMedicamentoCollectionMedicamento);
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

    public void edit(Categoria categoria) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getCategoriaID());
            Collection<Medicamento> medicamentoCollectionOld = persistentCategoria.getMedicamentoCollection();
            Collection<Medicamento> medicamentoCollectionNew = categoria.getMedicamentoCollection();
            Collection<Medicamento> attachedMedicamentoCollectionNew = new ArrayList<Medicamento>();
            for (Medicamento medicamentoCollectionNewMedicamentoToAttach : medicamentoCollectionNew) {
                medicamentoCollectionNewMedicamentoToAttach = em.getReference(medicamentoCollectionNewMedicamentoToAttach.getClass(), medicamentoCollectionNewMedicamentoToAttach.getMedicamentoID());
                attachedMedicamentoCollectionNew.add(medicamentoCollectionNewMedicamentoToAttach);
            }
            medicamentoCollectionNew = attachedMedicamentoCollectionNew;
            categoria.setMedicamentoCollection(medicamentoCollectionNew);
            categoria = em.merge(categoria);
            for (Medicamento medicamentoCollectionOldMedicamento : medicamentoCollectionOld) {
                if (!medicamentoCollectionNew.contains(medicamentoCollectionOldMedicamento)) {
                    medicamentoCollectionOldMedicamento.setCategoriaID(null);
                    medicamentoCollectionOldMedicamento = em.merge(medicamentoCollectionOldMedicamento);
                }
            }
            for (Medicamento medicamentoCollectionNewMedicamento : medicamentoCollectionNew) {
                if (!medicamentoCollectionOld.contains(medicamentoCollectionNewMedicamento)) {
                    Categoria oldCategoriaIDOfMedicamentoCollectionNewMedicamento = medicamentoCollectionNewMedicamento.getCategoriaID();
                    medicamentoCollectionNewMedicamento.setCategoriaID(categoria);
                    medicamentoCollectionNewMedicamento = em.merge(medicamentoCollectionNewMedicamento);
                    if (oldCategoriaIDOfMedicamentoCollectionNewMedicamento != null && !oldCategoriaIDOfMedicamentoCollectionNewMedicamento.equals(categoria)) {
                        oldCategoriaIDOfMedicamentoCollectionNewMedicamento.getMedicamentoCollection().remove(medicamentoCollectionNewMedicamento);
                        oldCategoriaIDOfMedicamentoCollectionNewMedicamento = em.merge(oldCategoriaIDOfMedicamentoCollectionNewMedicamento);
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
                Integer id = categoria.getCategoriaID();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getCategoriaID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            Collection<Medicamento> medicamentoCollection = categoria.getMedicamentoCollection();
            for (Medicamento medicamentoCollectionMedicamento : medicamentoCollection) {
                medicamentoCollectionMedicamento.setCategoriaID(null);
                medicamentoCollectionMedicamento = em.merge(medicamentoCollectionMedicamento);
            }
            em.remove(categoria);
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

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
