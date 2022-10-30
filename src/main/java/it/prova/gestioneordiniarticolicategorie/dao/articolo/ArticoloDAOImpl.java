package it.prova.gestioneordiniarticolicategorie.dao.articolo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
public class ArticoloDAOImpl implements ArticoloDAO{
	private EntityManager entityManager;

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Articolo> list() throws Exception {
		return entityManager.createQuery("from Articolo", Articolo.class).getResultList();
	}

	@Override
	public Articolo get(Long id) throws Exception {
		return entityManager.find(Articolo.class, id);
	}

	@Override
	public void update(Articolo o) throws Exception {
		if (o == null) {
			throw new Exception("Problema valore in input");
		}
		o = entityManager.merge(o);
	}

	@Override
	public void insert(Articolo o) throws Exception {
		if (o == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.persist(o);
	}

	@Override
	public void delete(Articolo o) throws Exception {
		if (o == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.remove(entityManager.merge(o));
	}

	@Override
	public Articolo findByIdFetchingCategorie(Long id) {
		TypedQuery<Articolo> query = entityManager.createQuery("select a FROM Articolo a left join fetch a.categorie c where a.id = ?1", Articolo.class);
		query.setParameter(1, id);
		return query.getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public void removeCategoria(Long idArticolo, Long idCategoria) throws Exception {
		entityManager.createNativeQuery("delete from articolo_categoria where articolo_id=?1 AND categoria_id=?2")
		.setParameter(1, idArticolo)
		.setParameter(2, idCategoria).executeUpdate();
	}

	@Override
	public Long totalSumByCategoria(Categoria categoriaInput) throws Exception {
		TypedQuery<Long> query = entityManager
				.createQuery("SELECT SUM(a.prezzoSingolo) FROM Articolo a join a.categorie c "
						+ "WHERE c.id = ?1", Long.class);
		query.setParameter(1, categoriaInput.getId());
		return query.getSingleResult();
	}

	@Override
	public Long totalSumByDestinatarioOrdine(String destinatario) throws Exception {
		TypedQuery<Long> query = entityManager
				.createQuery("SELECT SUM(a.prezzoSingolo) FROM Articolo a join a.ordine o "
						+ "WHERE o.nomeDestinatario = ?1", Long.class);
		query.setParameter(1, destinatario);
		return query.getSingleResult();
	}

	@Override
	public List<Articolo> findAllByOrdineErorr() throws Exception {
		TypedQuery<Articolo> query = entityManager
				.createQuery("SELECT a FROM Articolo a join a.ordine o "
						+ "WHERE dataspedizione > datascadenza", Articolo.class);
		
		return query.getResultList();
	}
}
