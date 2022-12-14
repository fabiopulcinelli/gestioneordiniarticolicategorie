package it.prova.gestioneordiniarticolicategorie.dao.articolo;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.IBaseDAO;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public interface ArticoloDAO extends IBaseDAO<Articolo>{
	public Articolo findByIdFetchingCategorie(Long id) throws Exception;

	public void removeCategoria(Long idArticolo, Long idCategoria) throws Exception;
	
	public Long totalSumByCategoria(Categoria categoriaInput) throws Exception;
	
	public Long totalSumByDestinatarioOrdine(String destinatario) throws Exception;
	
	public List<Articolo> findAllByOrdineErorr() throws Exception;
}