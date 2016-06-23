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
public class MedicamentoJpaController implements Serializable {

    public MedicamentoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medicamento medicamento) throws RollbackFailureException, Exception {
        if (medicamento.getDetallecompraCollection() == null) {
            medicamento.setDetallecompraCollection(new ArrayList<Detallecompra>());
        }
        if (medicamento.getDetallepedidoCollection() == null) {
            medicamento.setDetallepedidoCollection(new ArrayList<Detallepedido>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Laboratorio laboratorioID = medicamento.getLaboratorioID();
            if (laboratorioID != null) {
                laboratorioID = em.getReference(laboratorioID.getClass(), laboratorioID.getLaboratorioID());
                medicamento.setLaboratorioID(laboratorioID);
            }
            Categoria categoriaID = medicamento.getCategoriaID();
            if (categoriaID != null) {
                categoriaID = em.getReference(categoriaID.getClass(), categoriaID.getCategoriaID());
                medicamento.setCategoriaID(categoriaID);
            }
            Collection<Detallecompra> attachedDetallecompraCollection = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraCollectionDetallecompraToAttach : medicamento.getDetallecompraCollection()) {
                detallecompraCollectionDetallecompraToAttach = em.getReference(detallecompraCollectionDetallecompraToAttach.getClass(), detallecompraCollectionDetallecompraToAttach.getDetalleCompraID());
                attachedDetallecompraCollection.add(detallecompraCollectionDetallecompraToAttach);
            }
            medicamento.setDetallecompraCollection(attachedDetallecompraCollection);
            Collection<Detallepedido> attachedDetallepedidoCollection = new ArrayList<Detallepedido>();
            for (Detallepedido detallepedidoCollectionDetallepedidoToAttach : medicamento.getDetallepedidoCollection()) {
                detallepedidoCollectionDetallepedidoToAttach = em.getReference(detallepedidoCollectionDetallepedidoToAttach.getClass(), detallepedidoCollectionDetallepedidoToAttach.getDetallePedidoID());
                attachedDetallepedidoCollection.add(detallepedidoCollectionDetallepedidoToAttach);
            }
            medicamento.setDetallepedidoCollection(attachedDetallepedidoCollection);
            em.persist(medicamento);
            if (laboratorioID != null) {
                laboratorioID.getMedicamentoCollection().add(medicamento);
                laboratorioID = em.merge(laboratorioID);
            }
            if (categoriaID != null) {
                categoriaID.getMedicamentoCollection().add(medicamento);
                categoriaID = em.merge(categoriaID);
            }
            for (Detallecompra detallecompraCollectionDetallecompra : medicamento.getDetallecompraCollection()) {
                Medicamento oldMedicamentoIDOfDetallecompraCollectionDetallecompra = detallecompraCollectionDetallecompra.getMedicamentoID();
                detallecompraCollectionDetallecompra.setMedicamentoID(medicamento);
                detallecompraCollectionDetallecompra = em.merge(detallecompraCollectionDetallecompra);
                if (oldMedicamentoIDOfDetallecompraCollectionDetallecompra != null) {
                    oldMedicamentoIDOfDetallecompraCollectionDetallecompra.getDetallecompraCollection().remove(detallecompraCollectionDetallecompra);
                    oldMedicamentoIDOfDetallecompraCollectionDetallecompra = em.merge(oldMedicamentoIDOfDetallecompraCollectionDetallecompra);
                }
            }
            for (Detallepedido detallepedidoCollectionDetallepedido : medicamento.getDetallepedidoCollection()) {
                Medicamento oldMedicamentoIDOfDetallepedidoCollectionDetallepedido = detallepedidoCollectionDetallepedido.getMedicamentoID();
                detallepedidoCollectionDetallepedido.setMedicamentoID(medicamento);
                detallepedidoCollectionDetallepedido = em.merge(detallepedidoCollectionDetallepedido);
                if (oldMedicamentoIDOfDetallepedidoCollectionDetallepedido != null) {
                    oldMedicamentoIDOfDetallepedidoCollectionDetallepedido.getDetallepedidoCollection().remove(detallepedidoCollectionDetallepedido);
                    oldMedicamentoIDOfDetallepedidoCollectionDetallepedido = em.merge(oldMedicamentoIDOfDetallepedidoCollectionDetallepedido);
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

    public void edit(Medicamento medicamento) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medicamento persistentMedicamento = em.find(Medicamento.class, medicamento.getMedicamentoID());
            Laboratorio laboratorioIDOld = persistentMedicamento.getLaboratorioID();
            Laboratorio laboratorioIDNew = medicamento.getLaboratorioID();
            Categoria categoriaIDOld = persistentMedicamento.getCategoriaID();
            Categoria categoriaIDNew = medicamento.getCategoriaID();
            Collection<Detallecompra> detallecompraCollectionOld = persistentMedicamento.getDetallecompraCollection();
            Collection<Detallecompra> detallecompraCollectionNew = medicamento.getDetallecompraCollection();
            Collection<Detallepedido> detallepedidoCollectionOld = persistentMedicamento.getDetallepedidoCollection();
            Collection<Detallepedido> detallepedidoCollectionNew = medicamento.getDetallepedidoCollection();
            if (laboratorioIDNew != null) {
                laboratorioIDNew = em.getReference(laboratorioIDNew.getClass(), laboratorioIDNew.getLaboratorioID());
                medicamento.setLaboratorioID(laboratorioIDNew);
            }
            if (categoriaIDNew != null) {
                categoriaIDNew = em.getReference(categoriaIDNew.getClass(), categoriaIDNew.getCategoriaID());
                medicamento.setCategoriaID(categoriaIDNew);
            }
            Collection<Detallecompra> attachedDetallecompraCollectionNew = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraCollectionNewDetallecompraToAttach : detallecompraCollectionNew) {
                detallecompraCollectionNewDetallecompraToAttach = em.getReference(detallecompraCollectionNewDetallecompraToAttach.getClass(), detallecompraCollectionNewDetallecompraToAttach.getDetalleCompraID());
                attachedDetallecompraCollectionNew.add(detallecompraCollectionNewDetallecompraToAttach);
            }
            detallecompraCollectionNew = attachedDetallecompraCollectionNew;
            medicamento.setDetallecompraCollection(detallecompraCollectionNew);
            Collection<Detallepedido> attachedDetallepedidoCollectionNew = new ArrayList<Detallepedido>();
            for (Detallepedido detallepedidoCollectionNewDetallepedidoToAttach : detallepedidoCollectionNew) {
                detallepedidoCollectionNewDetallepedidoToAttach = em.getReference(detallepedidoCollectionNewDetallepedidoToAttach.getClass(), detallepedidoCollectionNewDetallepedidoToAttach.getDetallePedidoID());
                attachedDetallepedidoCollectionNew.add(detallepedidoCollectionNewDetallepedidoToAttach);
            }
            detallepedidoCollectionNew = attachedDetallepedidoCollectionNew;
            medicamento.setDetallepedidoCollection(detallepedidoCollectionNew);
            medicamento = em.merge(medicamento);
            if (laboratorioIDOld != null && !laboratorioIDOld.equals(laboratorioIDNew)) {
                laboratorioIDOld.getMedicamentoCollection().remove(medicamento);
                laboratorioIDOld = em.merge(laboratorioIDOld);
            }
            if (laboratorioIDNew != null && !laboratorioIDNew.equals(laboratorioIDOld)) {
                laboratorioIDNew.getMedicamentoCollection().add(medicamento);
                laboratorioIDNew = em.merge(laboratorioIDNew);
            }
            if (categoriaIDOld != null && !categoriaIDOld.equals(categoriaIDNew)) {
                categoriaIDOld.getMedicamentoCollection().remove(medicamento);
                categoriaIDOld = em.merge(categoriaIDOld);
            }
            if (categoriaIDNew != null && !categoriaIDNew.equals(categoriaIDOld)) {
                categoriaIDNew.getMedicamentoCollection().add(medicamento);
                categoriaIDNew = em.merge(categoriaIDNew);
            }
            for (Detallecompra detallecompraCollectionOldDetallecompra : detallecompraCollectionOld) {
                if (!detallecompraCollectionNew.contains(detallecompraCollectionOldDetallecompra)) {
                    detallecompraCollectionOldDetallecompra.setMedicamentoID(null);
                    detallecompraCollectionOldDetallecompra = em.merge(detallecompraCollectionOldDetallecompra);
                }
            }
            for (Detallecompra detallecompraCollectionNewDetallecompra : detallecompraCollectionNew) {
                if (!detallecompraCollectionOld.contains(detallecompraCollectionNewDetallecompra)) {
                    Medicamento oldMedicamentoIDOfDetallecompraCollectionNewDetallecompra = detallecompraCollectionNewDetallecompra.getMedicamentoID();
                    detallecompraCollectionNewDetallecompra.setMedicamentoID(medicamento);
                    detallecompraCollectionNewDetallecompra = em.merge(detallecompraCollectionNewDetallecompra);
                    if (oldMedicamentoIDOfDetallecompraCollectionNewDetallecompra != null && !oldMedicamentoIDOfDetallecompraCollectionNewDetallecompra.equals(medicamento)) {
                        oldMedicamentoIDOfDetallecompraCollectionNewDetallecompra.getDetallecompraCollection().remove(detallecompraCollectionNewDetallecompra);
                        oldMedicamentoIDOfDetallecompraCollectionNewDetallecompra = em.merge(oldMedicamentoIDOfDetallecompraCollectionNewDetallecompra);
                    }
                }
            }
            for (Detallepedido detallepedidoCollectionOldDetallepedido : detallepedidoCollectionOld) {
                if (!detallepedidoCollectionNew.contains(detallepedidoCollectionOldDetallepedido)) {
                    detallepedidoCollectionOldDetallepedido.setMedicamentoID(null);
                    detallepedidoCollectionOldDetallepedido = em.merge(detallepedidoCollectionOldDetallepedido);
                }
            }
            for (Detallepedido detallepedidoCollectionNewDetallepedido : detallepedidoCollectionNew) {
                if (!detallepedidoCollectionOld.contains(detallepedidoCollectionNewDetallepedido)) {
                    Medicamento oldMedicamentoIDOfDetallepedidoCollectionNewDetallepedido = detallepedidoCollectionNewDetallepedido.getMedicamentoID();
                    detallepedidoCollectionNewDetallepedido.setMedicamentoID(medicamento);
                    detallepedidoCollectionNewDetallepedido = em.merge(detallepedidoCollectionNewDetallepedido);
                    if (oldMedicamentoIDOfDetallepedidoCollectionNewDetallepedido != null && !oldMedicamentoIDOfDetallepedidoCollectionNewDetallepedido.equals(medicamento)) {
                        oldMedicamentoIDOfDetallepedidoCollectionNewDetallepedido.getDetallepedidoCollection().remove(detallepedidoCollectionNewDetallepedido);
                        oldMedicamentoIDOfDetallepedidoCollectionNewDetallepedido = em.merge(oldMedicamentoIDOfDetallepedidoCollectionNewDetallepedido);
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
                Integer id = medicamento.getMedicamentoID();
                if (findMedicamento(id) == null) {
                    throw new NonexistentEntityException("The medicamento with id " + id + " no longer exists.");
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
            Medicamento medicamento;
            try {
                medicamento = em.getReference(Medicamento.class, id);
                medicamento.getMedicamentoID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicamento with id " + id + " no longer exists.", enfe);
            }
            Laboratorio laboratorioID = medicamento.getLaboratorioID();
            if (laboratorioID != null) {
                laboratorioID.getMedicamentoCollection().remove(medicamento);
                laboratorioID = em.merge(laboratorioID);
            }
            Categoria categoriaID = medicamento.getCategoriaID();
            if (categoriaID != null) {
                categoriaID.getMedicamentoCollection().remove(medicamento);
                categoriaID = em.merge(categoriaID);
            }
            Collection<Detallecompra> detallecompraCollection = medicamento.getDetallecompraCollection();
            for (Detallecompra detallecompraCollectionDetallecompra : detallecompraCollection) {
                detallecompraCollectionDetallecompra.setMedicamentoID(null);
                detallecompraCollectionDetallecompra = em.merge(detallecompraCollectionDetallecompra);
            }
            Collection<Detallepedido> detallepedidoCollection = medicamento.getDetallepedidoCollection();
            for (Detallepedido detallepedidoCollectionDetallepedido : detallepedidoCollection) {
                detallepedidoCollectionDetallepedido.setMedicamentoID(null);
                detallepedidoCollectionDetallepedido = em.merge(detallepedidoCollectionDetallepedido);
            }
            em.remove(medicamento);
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

    public List<Medicamento> findMedicamentoEntities() {
        return findMedicamentoEntities(true, -1, -1);
    }

    public List<Medicamento> findMedicamentoEntities(int maxResults, int firstResult) {
        return findMedicamentoEntities(false, maxResults, firstResult);
    }

    private List<Medicamento> findMedicamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicamento.class));
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

    public Medicamento findMedicamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medicamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicamento> rt = cq.from(Medicamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
