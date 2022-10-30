package it.prova.gestioneordiniarticolicategorie.dao.categoria;

import java.util.Date;
import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.IBaseDAO;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public interface CategoriaDAO extends IBaseDAO<Categoria>{

	public Categoria findByIdFetchingArticoli(Long idCategoria) throws Exception;

	public void removeArticoli(Long idArticolo, Long idCategoria) throws Exception;
	
	public List<Categoria> findAllByOrdine(Ordine ordineInput) throws Exception;
	
	public List<String> findAllCodiciByOrdineMonth(Date dataInput) throws Exception;
}