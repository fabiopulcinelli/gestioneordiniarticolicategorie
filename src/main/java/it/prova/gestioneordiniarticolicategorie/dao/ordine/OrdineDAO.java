package it.prova.gestioneordiniarticolicategorie.dao.ordine;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.IBaseDAO;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public interface OrdineDAO  extends IBaseDAO<Ordine>{
	public List<Ordine> findAllByCategoria(Categoria categoriaInput) throws Exception;
	
	public Ordine findMostRecentOrdineByCategoria(Categoria categoriaInput) throws Exception;
	
	public List<String> findAllIndirizziByNumeroSerialeLike(String stringa) throws Exception;
}
