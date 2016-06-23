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
public class LaboratorioJpaController implements Serializable {

    public LaboratorioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Laboratorio laboratorio) throws RollbackFailureException, Exception {
        if (laboratorio.getMedicamentoCollection() == null) {
            laboratorio.setMedicamentoCollection(new ArrayList<Medicamento>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Medicamento> attachedMedicamentoCollection = new ArrayList<Medicamento>();
            for (Medicamento medicamentoCollectionMedicamentoToAttach : laboratorio.getMedicamentoCollection()) {
                medicamentoCollectionMedicamentoToAttach = em.getReference(medicamentoCollectionMedicamentoToAttach.getClass(), medicamentoCollectionMedicamentoToAttach.getMedicamentoID());
                attachedMedicamentoCollection.add(medicamentoCollectionMedicamentoToAttach);
            }
            laboratorio.setMedicamentoCollection(attachedMedicamentoCollection);
            em.persist(laboratorio);
            for (Medicamento medicamentoCollectionMedicamento : laboratorio.getMedicamentoCollection()) {
                Laboratorio oldLaboratorioIDOfMedicamentoCollectionMedicamento = medicamentoCollectionMedicamento.getLaboratorioID();
                medicamentoCollectionMedicamento.setLaboratorioID(laboratorio);
                medicamentoCollectionMedicamento = em.merge(medicamentoCollectionMedicamento);
                if (oldLaboratorioIDOfMedicamentoCollectionMedicamento != null) {
                    oldLaboratorioIDOfMedicamentoCollectionMedicamento.getMedicamentoCollection().remove(medicamentoCollectionMedicamento);
                    oldLaboratorioIDOfMedicamentoCollectionMedicamento = em.merge(oldLaboratorioIDOfMedicamentoCollectionMedicamento);
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

    public void edit(Laboratorio laboratorio) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Laboratorio persistentLaboratorio = em.find(Laboratorio.class, laboratorio.getLaboratorioID());
            Collection<Medicamento> medicamentoCollectionOld = persistentLaboratorio.getMedicamentoCollection();
            Collection<Medicamento> medicamentoCollectionNew = laboratorio.getMedicamentoCollection();
            Collection<Medicamento> attachedMedicamentoCollectionNew = new ArrayList<Medicamento>();
            for (Medicamento medicamentoCollectionNewMedicamentoToAttach : medicamentoCollectionNew) {
                medicamentoCollectionNewMedicamentoToAttach = em.getReference(medicamentoCollectionNewMedicamentoToAttach.getClass(), medicamentoCollectionNewMedicamentoToAttach.getMedicamentoID());
                attachedMedicamentoCollectionNew.add(medicamentoCollectionNewMedicamentoToAttach);
            }
            medicamentoCollectionNew = attachedMedicamentoCollectionNew;
            laboratorio.setMedicamentoCollection(medicamentoCollectionNew);
            laboratorio = em.merge(laboratorio);
            for (Medicamento medicamentoCollectionOldMedicamento : medicamentoCollectionOld) {
                if (!medicamentoCollectionNew.contains(medicamentoCollectionOldMedicamento)) {
                    medicamentoCollectionOldMedicamento.setLaboratorioID(null);
                    medicamentoCollectionOldMedicamento = em.merge(medicamentoCollectionOldMedicamento);
                }
            }
            for (Medicamento medicamentoCollectionNewMedicamento : medicamentoCollectionNew) {
                if (!medicamentoCollectionOld.contains(medicamentoCollectionNewMedicamento)) {
                    Laboratorio oldLaboratorioIDOfMedicamentoCollectionNewMedicamento = medicamentoCollectionNewMedicamento.getLaboratorioID();
                    medicamentoCollectionNewMedicamento.setLaboratorioID(laboratorio);
                    medicamentoCollectionNewMedicamento = em.merge(medicamentoCollectionNewMedicamento);
                    if (oldLaboratorioIDOfMedicamentoCollectionNewMedicamento != null && !oldLaboratorioIDOfMedicamentoCollectionNewMedicamento.equals(laboratorio)) {
                        oldLaboratorioIDOfMedicamentoCollectionNewMedicamento.getMedicamentoCollection().remove(medicamentoCollectionNewMedicamento);
                        oldLaboratorioIDOfMedicamentoCollectionNewMedicamento = em.merge(oldLaboratorioIDOfMedicamentoCollectionNewMedicamento);
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
                Integer id = laboratorio.getLaboratorioID();
                if (findLaboratorio(id) == null) {
                    throw new NonexistentEntityException("The laboratorio with id " + id + " no longer exists.");
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
            Laboratorio laboratorio;
            try {
                laboratorio = em.getReference(Laboratorio.class, id);
                laboratorio.getLaboratorioID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The laboratorio with id " + id + " no longer exists.", enfe);
            }
            Collection<Medicamento> medicamentoCollection = laboratorio.getMedicamentoCollection();
            for (Medicamento medicamentoCollectionMedicamento : medicamentoCollection) {
                medicamentoCollectionMedicamento.setLaboratorioID(null);
                medicamentoCollectionMedicamento = em.merge(medicamentoCollectionMedicamento);
            }
            em.remove(laboratorio);
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

    public List<Laboratorio> findLaboratorioEntities() {
        return findLaboratorioEntities(true, -1, -1);
    }

    public List<Laboratorio> findLaboratorioEntities(int maxResults, int firstResult) {
        return findLaboratorioEntities(false, maxResults, firstResult);
    }

    private List<Laboratorio> findLaboratorioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Laboratorio.class));
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

    public Laboratorio findLaboratorio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Laboratorio.class, id);
        } finally {
            em.close();
        }
    }

    public int getLaboratorioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Laboratorio> rt = cq.from(Laboratorio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
