package it.prova.gestioneordiniarticolicategorie.dao.categoria;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public class CategoriaDAOImpl implements CategoriaDAO{
	private EntityManager entityManager;

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Categoria> list() throws Exception {
		return entityManager.createQuery("from Categoria", Categoria.class).getResultList();
	}

	@Override
	public Categoria get(Long id) throws Exception {
		return entityManager.find(Categoria.class, id);
	}

	@Override
	public void update(Categoria o) throws Exception {
		if (o == null) {
			throw new Exception("Problema valore in input");
		}
		o = entityManager.merge(o);
	}

	@Override
	public void insert(Categoria o) throws Exception {
		if (o == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.persist(o);
	}

	@Override
	public void delete(Categoria o) throws Exception {
		if (o == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.remove(entityManager.merge(o));
	}
	
	@Override
	public Categoria findByIdFetchingArticoli(Long id) {
		TypedQuery<Categoria> query = entityManager.createQuery("select c FROM Categoria c left join fetch c.articoli a where c.id = ?1", Categoria.class);
		query.setParameter(1, id);
		return query.getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public void removeArticoli(Long idArticolo, Long idCategoria) throws Exception {
		entityManager.createNativeQuery("delete from articolo_categoria where articolo_id=?1 AND categoria_id=?2")
		.setParameter(1, idArticolo)
		.setParameter(2, idCategoria).executeUpdate();
	}

	@Override
	public List<Categoria> findAllByOrdine(Ordine ordineInput) throws Exception {
		TypedQuery<Categoria> query = entityManager
				.createQuery("SELECT DISTINCT c FROM Categoria c join fetch c.articoli a "
						+ "join fetch a.ordine o "
						+ "WHERE o.id = ?1", Categoria.class);
		query.setParameter(1, ordineInput.getId());
		return query.getResultList();
	}
	
	@Override
	public List<String> findAllCodiciByOrdineMonth(Date dataInput) throws Exception {
		TypedQuery<String> query = entityManager
				.createQuery("SELECT DISTINCT c.codice FROM Categoria c join c.articoli a "
						+ "join a.ordine o "
						+ "WHERE YEAR(o.dataSpedizione) = YEAR(?1) AND MONTH(o.dataSpedizione) = MONTH(?1)", String.class);
		query.setParameter(1, dataInput);
		
		return query.getResultList();
	}
}
