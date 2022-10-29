package it.prova.gestioneordiniarticolicategorie.service;

import java.util.List;

import it.prova.gestioneordiniarticolicategorie.dao.articolo.ArticoloDAO;
import it.prova.gestioneordiniarticolicategorie.dao.categoria.CategoriaDAO;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;

public interface ArticoloService {
	public List<Articolo> listAll() throws Exception;

	public Articolo caricaSingoloElemento(Long id) throws Exception;

	public void aggiorna(Articolo articoloInstance) throws Exception;

	public void inserisciNuovo(Articolo articoloInstance) throws Exception;

	public void rimuovi(Long articoloId) throws Exception;
	
	public void aggiungiCategoria(Articolo articoloInstance, Categoria categoriaInstance) throws Exception;
	
	public void rimuoviCategoriaDaArticolo(Long idArticolo, Long idCategoria) throws Exception;
	
	public Long sommaTotalePerCategoria(Categoria categoriaInput) throws Exception;
	
	// per injection
	public void setArticoloDAO(ArticoloDAO articoloDAO);
	
	public void setCategoriaDAO(CategoriaDAO categoriaDAO);
}
