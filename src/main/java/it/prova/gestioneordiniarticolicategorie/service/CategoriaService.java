package it.prova.gestioneordiniarticolicategorie.service;

import java.util.Date;
import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.articolo.ArticoloDAO;
import it.prova.gestioneordiniarticolicategorie.dao.categoria.CategoriaDAO;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;

public interface CategoriaService {
	public List<Categoria> listAll() throws Exception;

	public Categoria caricaSingoloElemento(Long id) throws Exception;

	public void aggiorna(Categoria categoriaInstance) throws Exception;

	public void inserisciNuovo(Categoria categoriaInstance) throws Exception;

	public void rimuovi(Long categoriaId) throws Exception;
	
	void aggiungiArticolo(Articolo articoloInstance, Categoria categoriaInstance) throws Exception;
	
	// per injection
	public void setCategoriaDAO(CategoriaDAO categoriaDAO);

	void rimuoviArticoloDaCategoria(Long idArticolo, Long idCategoria) throws Exception;
	
	public List<Categoria> trovaTuttiPerOrdine(Ordine ordineInput) throws Exception;
	
	public List<String> trovaTuttiCodiciPerMeseOrdine(Date dataInput) throws Exception;

	void setArticoloDAO(ArticoloDAO articoloDAO);
}
