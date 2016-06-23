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
public class CompraJpaController implements Serializable {

    public CompraJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compra compra) throws RollbackFailureException, Exception {
        if (compra.getDetallecompraCollection() == null) {
            compra.setDetallecompraCollection(new ArrayList<Detallecompra>());
        }
        if (compra.getProveedorCollection() == null) {
            compra.setProveedorCollection(new ArrayList<Proveedor>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario usuarioID = compra.getUsuarioID();
            if (usuarioID != null) {
                usuarioID = em.getReference(usuarioID.getClass(), usuarioID.getUsuarioID());
                compra.setUsuarioID(usuarioID);
            }
            Collection<Detallecompra> attachedDetallecompraCollection = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraCollectionDetallecompraToAttach : compra.getDetallecompraCollection()) {
                detallecompraCollectionDetallecompraToAttach = em.getReference(detallecompraCollectionDetallecompraToAttach.getClass(), detallecompraCollectionDetallecompraToAttach.getDetalleCompraID());
                attachedDetallecompraCollection.add(detallecompraCollectionDetallecompraToAttach);
            }
            compra.setDetallecompraCollection(attachedDetallecompraCollection);
            Collection<Proveedor> attachedProveedorCollection = new ArrayList<Proveedor>();
            for (Proveedor proveedorCollectionProveedorToAttach : compra.getProveedorCollection()) {
                proveedorCollectionProveedorToAttach = em.getReference(proveedorCollectionProveedorToAttach.getClass(), proveedorCollectionProveedorToAttach.getProveedorID());
                attachedProveedorCollection.add(proveedorCollectionProveedorToAttach);
            }
            compra.setProveedorCollection(attachedProveedorCollection);
            em.persist(compra);
            if (usuarioID != null) {
                usuarioID.getCompraCollection().add(compra);
                usuarioID = em.merge(usuarioID);
            }
            for (Detallecompra detallecompraCollectionDetallecompra : compra.getDetallecompraCollection()) {
                Compra oldCompraIDOfDetallecompraCollectionDetallecompra = detallecompraCollectionDetallecompra.getCompraID();
                detallecompraCollectionDetallecompra.setCompraID(compra);
                detallecompraCollectionDetallecompra = em.merge(detallecompraCollectionDetallecompra);
                if (oldCompraIDOfDetallecompraCollectionDetallecompra != null) {
                    oldCompraIDOfDetallecompraCollectionDetallecompra.getDetallecompraCollection().remove(detallecompraCollectionDetallecompra);
                    oldCompraIDOfDetallecompraCollectionDetallecompra = em.merge(oldCompraIDOfDetallecompraCollectionDetallecompra);
                }
            }
            for (Proveedor proveedorCollectionProveedor : compra.getProveedorCollection()) {
                Compra oldCompraIDOfProveedorCollectionProveedor = proveedorCollectionProveedor.getCompraID();
                proveedorCollectionProveedor.setCompraID(compra);
                proveedorCollectionProveedor = em.merge(proveedorCollectionProveedor);
                if (oldCompraIDOfProveedorCollectionProveedor != null) {
                    oldCompraIDOfProveedorCollectionProveedor.getProveedorCollection().remove(proveedorCollectionProveedor);
                    oldCompraIDOfProveedorCollectionProveedor = em.merge(oldCompraIDOfProveedorCollectionProveedor);
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

    public void edit(Compra compra) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Compra persistentCompra = em.find(Compra.class, compra.getCompraID());
            Usuario usuarioIDOld = persistentCompra.getUsuarioID();
            Usuario usuarioIDNew = compra.getUsuarioID();
            Collection<Detallecompra> detallecompraCollectionOld = persistentCompra.getDetallecompraCollection();
            Collection<Detallecompra> detallecompraCollectionNew = compra.getDetallecompraCollection();
            Collection<Proveedor> proveedorCollectionOld = persistentCompra.getProveedorCollection();
            Collection<Proveedor> proveedorCollectionNew = compra.getProveedorCollection();
            if (usuarioIDNew != null) {
                usuarioIDNew = em.getReference(usuarioIDNew.getClass(), usuarioIDNew.getUsuarioID());
                compra.setUsuarioID(usuarioIDNew);
            }
            Collection<Detallecompra> attachedDetallecompraCollectionNew = new ArrayList<Detallecompra>();
            for (Detallecompra detallecompraCollectionNewDetallecompraToAttach : detallecompraCollectionNew) {
                detallecompraCollectionNewDetallecompraToAttach = em.getReference(detallecompraCollectionNewDetallecompraToAttach.getClass(), detallecompraCollectionNewDetallecompraToAttach.getDetalleCompraID());
                attachedDetallecompraCollectionNew.add(detallecompraCollectionNewDetallecompraToAttach);
            }
            detallecompraCollectionNew = attachedDetallecompraCollectionNew;
            compra.setDetallecompraCollection(detallecompraCollectionNew);
            Collection<Proveedor> attachedProveedorCollectionNew = new ArrayList<Proveedor>();
            for (Proveedor proveedorCollectionNewProveedorToAttach : proveedorCollectionNew) {
                proveedorCollectionNewProveedorToAttach = em.getReference(proveedorCollectionNewProveedorToAttach.getClass(), proveedorCollectionNewProveedorToAttach.getProveedorID());
                attachedProveedorCollectionNew.add(proveedorCollectionNewProveedorToAttach);
            }
            proveedorCollectionNew = attachedProveedorCollectionNew;
            compra.setProveedorCollection(proveedorCollectionNew);
            compra = em.merge(compra);
            if (usuarioIDOld != null && !usuarioIDOld.equals(usuarioIDNew)) {
                usuarioIDOld.getCompraCollection().remove(compra);
                usuarioIDOld = em.merge(usuarioIDOld);
            }
            if (usuarioIDNew != null && !usuarioIDNew.equals(usuarioIDOld)) {
                usuarioIDNew.getCompraCollection().add(compra);
                usuarioIDNew = em.merge(usuarioIDNew);
            }
            for (Detallecompra detallecompraCollectionOldDetallecompra : detallecompraCollectionOld) {
                if (!detallecompraCollectionNew.contains(detallecompraCollectionOldDetallecompra)) {
                    detallecompraCollectionOldDetallecompra.setCompraID(null);
                    detallecompraCollectionOldDetallecompra = em.merge(detallecompraCollectionOldDetallecompra);
                }
            }
            for (Detallecompra detallecompraCollectionNewDetallecompra : detallecompraCollectionNew) {
                if (!detallecompraCollectionOld.contains(detallecompraCollectionNewDetallecompra)) {
                    Compra oldCompraIDOfDetallecompraCollectionNewDetallecompra = detallecompraCollectionNewDetallecompra.getCompraID();
                    detallecompraCollectionNewDetallecompra.setCompraID(compra);
                    detallecompraCollectionNewDetallecompra = em.merge(detallecompraCollectionNewDetallecompra);
                    if (oldCompraIDOfDetallecompraCollectionNewDetallecompra != null && !oldCompraIDOfDetallecompraCollectionNewDetallecompra.equals(compra)) {
                        oldCompraIDOfDetallecompraCollectionNewDetallecompra.getDetallecompraCollection().remove(detallecompraCollectionNewDetallecompra);
                        oldCompraIDOfDetallecompraCollectionNewDetallecompra = em.merge(oldCompraIDOfDetallecompraCollectionNewDetallecompra);
                    }
                }
            }
            for (Proveedor proveedorCollectionOldProveedor : proveedorCollectionOld) {
                if (!proveedorCollectionNew.contains(proveedorCollectionOldProveedor)) {
                    proveedorCollectionOldProveedor.setCompraID(null);
                    proveedorCollectionOldProveedor = em.merge(proveedorCollectionOldProveedor);
                }
            }
            for (Proveedor proveedorCollectionNewProveedor : proveedorCollectionNew) {
                if (!proveedorCollectionOld.contains(proveedorCollectionNewProveedor)) {
                    Compra oldCompraIDOfProveedorCollectionNewProveedor = proveedorCollectionNewProveedor.getCompraID();
                    proveedorCollectionNewProveedor.setCompraID(compra);
                    proveedorCollectionNewProveedor = em.merge(proveedorCollectionNewProveedor);
                    if (oldCompraIDOfProveedorCollectionNewProveedor != null && !oldCompraIDOfProveedorCollectionNewProveedor.equals(compra)) {
                        oldCompraIDOfProveedorCollectionNewProveedor.getProveedorCollection().remove(proveedorCollectionNewProveedor);
                        oldCompraIDOfProveedorCollectionNewProveedor = em.merge(oldCompraIDOfProveedorCollectionNewProveedor);
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
                Integer id = compra.getCompraID();
                if (findCompra(id) == null) {
                    throw new NonexistentEntityException("The compra with id " + id + " no longer exists.");
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
            Compra compra;
            try {
                compra = em.getReference(Compra.class, id);
                compra.getCompraID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compra with id " + id + " no longer exists.", enfe);
            }
            Usuario usuarioID = compra.getUsuarioID();
            if (usuarioID != null) {
                usuarioID.getCompraCollection().remove(compra);
                usuarioID = em.merge(usuarioID);
            }
            Collection<Detallecompra> detallecompraCollection = compra.getDetallecompraCollection();
            for (Detallecompra detallecompraCollectionDetallecompra : detallecompraCollection) {
                detallecompraCollectionDetallecompra.setCompraID(null);
                detallecompraCollectionDetallecompra = em.merge(detallecompraCollectionDetallecompra);
            }
            Collection<Proveedor> proveedorCollection = compra.getProveedorCollection();
            for (Proveedor proveedorCollectionProveedor : proveedorCollection) {
                proveedorCollectionProveedor.setCompraID(null);
                proveedorCollectionProveedor = em.merge(proveedorCollectionProveedor);
            }
            em.remove(compra);
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

    public List<Compra> findCompraEntities() {
        return findCompraEntities(true, -1, -1);
    }

    public List<Compra> findCompraEntities(int maxResults, int firstResult) {
        return findCompraEntities(false, maxResults, firstResult);
    }

    private List<Compra> findCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compra.class));
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

    public Compra findCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compra.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compra> rt = cq.from(Compra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
