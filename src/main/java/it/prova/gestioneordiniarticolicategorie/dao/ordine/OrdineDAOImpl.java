package it.prova.gestioneordiniarticolicategorie.dao.ordine;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestioneordiniarticolicategorie.exception.OrdiniArticoliCategorieException;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public class OrdineDAOImpl implements OrdineDAO{
	private EntityManager entityManager;

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Ordine> list() throws Exception {
		return entityManager.createQuery("from Ordine", Ordine.class).getResultList();
	}

	@Override
	public Ordine get(Long id) throws Exception {
		return entityManager.find(Ordine.class, id);
	}

	@Override
	public void update(Ordine o) throws Exception {
		if (o == null) {
			throw new Exception("Problema valore in input");
		}
		o = entityManager.merge(o);
	}

	@Override
	public void insert(Ordine o) throws Exception {
		if (o == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.persist(o);
	}

	@Override
	public void delete(Ordine o) throws OrdiniArticoliCategorieException  {
		if (o == null || o.getArticoli().size() > 0) {
			throw new OrdiniArticoliCategorieException("NON PUOI RIMUOVERE ORDINI CON CATEGORIE LINKATE");
		}
		entityManager.remove(entityManager.merge(o));
	}

	@Override
	public List<Ordine> findAllByCategoria(Categoria categoriaInput) throws Exception {
		TypedQuery<Ordine> query = entityManager
				.createQuery("SELECT o FROM Ordine o join fetch o.articoli a "
						+ "join fetch a.categorie c "
						+ "WHERE c.id = ?1", Ordine.class);
		query.setParameter(1, categoriaInput.getId());
		return query.getResultList();
	}

	@Override
	public Ordine findMostRecentOrdineByCategoria(Categoria categoriaInput) throws Exception {
		TypedQuery<Ordine> query = entityManager
				.createQuery("SELECT o FROM Ordine o join fetch o.articoli a "
						+ "join fetch a.categorie c "
						+ "WHERE c.id = ?1 ORDER BY dataspedizione DESC", Ordine.class);
		query.setParameter(1, categoriaInput.getId());
		return query.getSingleResult();
	}

	@Override
	public List<String> findAllIndirizziByNumeroSerialeLike(String stringa) throws Exception {
		TypedQuery<String> query = entityManager
				.createQuery("SELECT DISTINCT o.indirizzoSpedizione FROM Ordine o "
						+ "join o.articoli a "
						+ "WHERE a.numeroSeriale LIKE ?1", String.class);
		query.setParameter(1, "%" + stringa + "%");
		return query.getResultList();
	}
}
