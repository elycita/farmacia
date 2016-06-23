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
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws RollbackFailureException, Exception {
        if (usuario.getCompraCollection() == null) {
            usuario.setCompraCollection(new ArrayList<Compra>());
        }
        if (usuario.getVentaCollection() == null) {
            usuario.setVentaCollection(new ArrayList<Venta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Compra> attachedCompraCollection = new ArrayList<Compra>();
            for (Compra compraCollectionCompraToAttach : usuario.getCompraCollection()) {
                compraCollectionCompraToAttach = em.getReference(compraCollectionCompraToAttach.getClass(), compraCollectionCompraToAttach.getCompraID());
                attachedCompraCollection.add(compraCollectionCompraToAttach);
            }
            usuario.setCompraCollection(attachedCompraCollection);
            Collection<Venta> attachedVentaCollection = new ArrayList<Venta>();
            for (Venta ventaCollectionVentaToAttach : usuario.getVentaCollection()) {
                ventaCollectionVentaToAttach = em.getReference(ventaCollectionVentaToAttach.getClass(), ventaCollectionVentaToAttach.getVentaID());
                attachedVentaCollection.add(ventaCollectionVentaToAttach);
            }
            usuario.setVentaCollection(attachedVentaCollection);
            em.persist(usuario);
            for (Compra compraCollectionCompra : usuario.getCompraCollection()) {
                Usuario oldUsuarioIDOfCompraCollectionCompra = compraCollectionCompra.getUsuarioID();
                compraCollectionCompra.setUsuarioID(usuario);
                compraCollectionCompra = em.merge(compraCollectionCompra);
                if (oldUsuarioIDOfCompraCollectionCompra != null) {
                    oldUsuarioIDOfCompraCollectionCompra.getCompraCollection().remove(compraCollectionCompra);
                    oldUsuarioIDOfCompraCollectionCompra = em.merge(oldUsuarioIDOfCompraCollectionCompra);
                }
            }
            for (Venta ventaCollectionVenta : usuario.getVentaCollection()) {
                Usuario oldUsuarioIDOfVentaCollectionVenta = ventaCollectionVenta.getUsuarioID();
                ventaCollectionVenta.setUsuarioID(usuario);
                ventaCollectionVenta = em.merge(ventaCollectionVenta);
                if (oldUsuarioIDOfVentaCollectionVenta != null) {
                    oldUsuarioIDOfVentaCollectionVenta.getVentaCollection().remove(ventaCollectionVenta);
                    oldUsuarioIDOfVentaCollectionVenta = em.merge(oldUsuarioIDOfVentaCollectionVenta);
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

    public void edit(Usuario usuario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getUsuarioID());
            Collection<Compra> compraCollectionOld = persistentUsuario.getCompraCollection();
            Collection<Compra> compraCollectionNew = usuario.getCompraCollection();
            Collection<Venta> ventaCollectionOld = persistentUsuario.getVentaCollection();
            Collection<Venta> ventaCollectionNew = usuario.getVentaCollection();
            Collection<Compra> attachedCompraCollectionNew = new ArrayList<Compra>();
            for (Compra compraCollectionNewCompraToAttach : compraCollectionNew) {
                compraCollectionNewCompraToAttach = em.getReference(compraCollectionNewCompraToAttach.getClass(), compraCollectionNewCompraToAttach.getCompraID());
                attachedCompraCollectionNew.add(compraCollectionNewCompraToAttach);
            }
            compraCollectionNew = attachedCompraCollectionNew;
            usuario.setCompraCollection(compraCollectionNew);
            Collection<Venta> attachedVentaCollectionNew = new ArrayList<Venta>();
            for (Venta ventaCollectionNewVentaToAttach : ventaCollectionNew) {
                ventaCollectionNewVentaToAttach = em.getReference(ventaCollectionNewVentaToAttach.getClass(), ventaCollectionNewVentaToAttach.getVentaID());
                attachedVentaCollectionNew.add(ventaCollectionNewVentaToAttach);
            }
            ventaCollectionNew = attachedVentaCollectionNew;
            usuario.setVentaCollection(ventaCollectionNew);
            usuario = em.merge(usuario);
            for (Compra compraCollectionOldCompra : compraCollectionOld) {
                if (!compraCollectionNew.contains(compraCollectionOldCompra)) {
                    compraCollectionOldCompra.setUsuarioID(null);
                    compraCollectionOldCompra = em.merge(compraCollectionOldCompra);
                }
            }
            for (Compra compraCollectionNewCompra : compraCollectionNew) {
                if (!compraCollectionOld.contains(compraCollectionNewCompra)) {
                    Usuario oldUsuarioIDOfCompraCollectionNewCompra = compraCollectionNewCompra.getUsuarioID();
                    compraCollectionNewCompra.setUsuarioID(usuario);
                    compraCollectionNewCompra = em.merge(compraCollectionNewCompra);
                    if (oldUsuarioIDOfCompraCollectionNewCompra != null && !oldUsuarioIDOfCompraCollectionNewCompra.equals(usuario)) {
                        oldUsuarioIDOfCompraCollectionNewCompra.getCompraCollection().remove(compraCollectionNewCompra);
                        oldUsuarioIDOfCompraCollectionNewCompra = em.merge(oldUsuarioIDOfCompraCollectionNewCompra);
                    }
                }
            }
            for (Venta ventaCollectionOldVenta : ventaCollectionOld) {
                if (!ventaCollectionNew.contains(ventaCollectionOldVenta)) {
                    ventaCollectionOldVenta.setUsuarioID(null);
                    ventaCollectionOldVenta = em.merge(ventaCollectionOldVenta);
                }
            }
            for (Venta ventaCollectionNewVenta : ventaCollectionNew) {
                if (!ventaCollectionOld.contains(ventaCollectionNewVenta)) {
                    Usuario oldUsuarioIDOfVentaCollectionNewVenta = ventaCollectionNewVenta.getUsuarioID();
                    ventaCollectionNewVenta.setUsuarioID(usuario);
                    ventaCollectionNewVenta = em.merge(ventaCollectionNewVenta);
                    if (oldUsuarioIDOfVentaCollectionNewVenta != null && !oldUsuarioIDOfVentaCollectionNewVenta.equals(usuario)) {
                        oldUsuarioIDOfVentaCollectionNewVenta.getVentaCollection().remove(ventaCollectionNewVenta);
                        oldUsuarioIDOfVentaCollectionNewVenta = em.merge(oldUsuarioIDOfVentaCollectionNewVenta);
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
                Integer id = usuario.getUsuarioID();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getUsuarioID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Collection<Compra> compraCollection = usuario.getCompraCollection();
            for (Compra compraCollectionCompra : compraCollection) {
                compraCollectionCompra.setUsuarioID(null);
                compraCollectionCompra = em.merge(compraCollectionCompra);
            }
            Collection<Venta> ventaCollection = usuario.getVentaCollection();
            for (Venta ventaCollectionVenta : ventaCollection) {
                ventaCollectionVenta.setUsuarioID(null);
                ventaCollectionVenta = em.merge(ventaCollectionVenta);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
