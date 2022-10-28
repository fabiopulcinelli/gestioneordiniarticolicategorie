package it.prova.gestioneordiniarticolicategorie.test;

import it.prova.gestioneordiniarticolicategorie.service.OrdineService;

import java.util.Date;

import it.prova.gestioneordiniarticolicategorie.dao.EntityManagerUtil;
import it.prova.gestioneordiniarticolicategorie.exception.OrdiniArticoliCategorieException;
import it.prova.gestioneordiniarticolicategorie.model.Articolo;
import it.prova.gestioneordiniarticolicategorie.model.Categoria;
import it.prova.gestioneordiniarticolicategorie.model.Ordine;
import it.prova.gestioneordiniarticolicategorie.service.MyServiceFactory;
import it.prova.gestioneordiniarticolicategorie.service.ArticoloService;
import it.prova.gestioneordiniarticolicategorie.service.CategoriaService;

public class MyTest {
	public static void main(String[] args) {
		OrdineService ordineServiceInstance = MyServiceFactory.getOrdineServiceInstance();
		ArticoloService articoloServiceInstance = MyServiceFactory.getArticoloServiceInstance();
		CategoriaService categoriaServiceInstance = MyServiceFactory.getCategoriaServiceInstance();
		
		try {

			System.out.println("In tabella Ordine ci sono " + ordineServiceInstance.listAll().size() + " elementi.");
			System.out.println("In tabella Articolo ci sono " + articoloServiceInstance.listAll().size() + " elementi.");
			System.out.println("In tabella Categoria ci sono " + categoriaServiceInstance.listAll().size() + " elementi.");
			System.out.println(
					"**************************** inizio batteria di test ********************************************");
			System.out.println(
					"*************************************************************************************************");
			
			testInserimentoNuovoOrdine(ordineServiceInstance);
			
			testAggiornamentoOrdineEsistente(ordineServiceInstance);
			
			testInserimentoNuovoArticolo(articoloServiceInstance, ordineServiceInstance);
			
			testAggiornamentoArticoloEsistente(articoloServiceInstance);
			
			testRimozioneArticoloLegatoAdUnOrdine(articoloServiceInstance, ordineServiceInstance);
			
			testInserimentoNuovoCategoria(categoriaServiceInstance);
			
			testAggiornamentoCategoriaEsistente(categoriaServiceInstance);
			
			testAggiungiCategoriaAdArticolo(articoloServiceInstance, categoriaServiceInstance, ordineServiceInstance);
			
			testAggiungiArticoloACategoria(articoloServiceInstance, categoriaServiceInstance, ordineServiceInstance);
			
			testRimuoviArticolo(articoloServiceInstance, categoriaServiceInstance, ordineServiceInstance);
			
			testRimuoviCategoria(articoloServiceInstance, categoriaServiceInstance, ordineServiceInstance);
			
			testRimuoviOrdine(articoloServiceInstance, ordineServiceInstance);
			
			System.out.println(
					"****************************** fine batteria di test ********************************************");
			System.out.println(
					"*************************************************************************************************");
			System.out.println("In tabella Ordine ci sono " + ordineServiceInstance.listAll().size() + " elementi.");
			System.out.println("In tabella Articolo ci sono " + articoloServiceInstance.listAll().size() + " elementi.");
			System.out.println("In tabella Categoria ci sono " + categoriaServiceInstance.listAll().size() + " elementi.");

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// questa è necessaria per chiudere tutte le connessioni quindi rilasciare il
			// main
			EntityManagerUtil.shutdown();
		}
	}
	
	public static void testInserimentoNuovoOrdine(OrdineService ordineServiceInstance) throws Exception{
		System.out.println(".......testInserimentoNuovoOrdine inizio.............");

		Ordine ordineInstance = new Ordine("Prova","Prova", new Date(), new Date());
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		if (ordineInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoOrdine fallito ");

		System.out.println(".......testInserimentoNuovoOrdine fine: PASSED.............");
	}
	
	public static void testAggiornamentoOrdineEsistente(OrdineService ordineServiceInstance) throws Exception{
		System.out.println(".......testAggiornamentoOrdineEsistente inizio.............");
		
		Ordine ordineDaAggiornare = ordineServiceInstance.listAll().get(0);
		if (ordineDaAggiornare.getId() == null)
			throw new RuntimeException("testAggiornamentoOrdineEsistente fallito ");
		
		Ordine temp = ordineServiceInstance.caricaSingoloElemento(ordineDaAggiornare.getId());
		temp.setNomeDestinatario("Mario Rossi");
		temp.setIndirizzoSpedizione("Via Mosca 52");
		ordineServiceInstance.aggiorna(temp);
		if (temp.getId() == null)
			throw new RuntimeException("testAggiornamentoOrdineEsistente fallito ");

		System.out.println(".......testAggiornamentoOrdineEsistente fine: PASSED.............");
	}
	
	public static void testInserimentoNuovoArticolo(ArticoloService articoloServiceInstance, OrdineService ordineServiceInstance) throws Exception{
		System.out.println(".......testInserimentoNuovoArticolo inizio.............");

		//mi prendo il primo ordine inserito perche' un articolo deve per forza avere un ordine
		Ordine primoOrdine = ordineServiceInstance.listAll().get(0);
		if (primoOrdine.getId() == null)
			throw new RuntimeException("testInserimentoNuovoArticolo fallito ");
		
		Articolo articoloInstance = new Articolo("Prova","Prova",0,new Date(), primoOrdine);
		articoloServiceInstance.inserisciNuovo(articoloInstance);
		if (articoloInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoArticolo fallito ");

		System.out.println(".......testInserimentoNuovoArticolo fine: PASSED.............");
	}
	
	public static void testAggiornamentoArticoloEsistente(ArticoloService articoloServiceInstance) throws Exception{
		System.out.println(".......testAggiornamentoArticoloEsistente inizio.............");
		
		Articolo articoloDaAggiornare = articoloServiceInstance.listAll().get(0);
		if (articoloDaAggiornare.getId() == null)
			throw new RuntimeException("testAggiornamentoArticoloEsistente fallito ");
		
		Articolo temp = articoloServiceInstance.caricaSingoloElemento(articoloDaAggiornare.getId());
		temp.setDescrizione("Articolo1");
		temp.setNumeroSeriale("XD1234");
		temp.setPrezzoSingolo(15);
		articoloServiceInstance.aggiorna(temp);
		if (temp.getId() == null)
			throw new RuntimeException("testAggiornamentoArticoloEsistente fallito ");

		System.out.println(".......testAggiornamentoArticoloEsistente fine: PASSED.............");
	}
	
	public static void testRimozioneArticoloLegatoAdUnOrdine(ArticoloService articoloServiceInstance, OrdineService ordineServiceInstance) throws Exception{
		System.out.println(".......testRimozioneArticoloLegatoAdUnOrdine inizio.............");
		
		//inserisco un nuovo articolo collegato al primo ordine
		Ordine primoOrdine = ordineServiceInstance.listAll().get(0);
		if (primoOrdine.getId() == null)
			throw new RuntimeException("testRimozioneArticoloLegatoAdUnOrdine fallito ");
		
		Articolo articoloInstance = new Articolo("Prova","Prova",0,new Date(), primoOrdine);
		articoloServiceInstance.inserisciNuovo(articoloInstance);
		if (articoloInstance.getId() == null)
			throw new RuntimeException("testRimozioneArticoloLegatoAdUnOrdine fallito ");
		
		//cancello il nuovo articolo creato e controllo che l-ordine sia rimasto
		articoloServiceInstance.rimuovi(articoloInstance.getId());
		if (articoloServiceInstance.caricaSingoloElemento(articoloInstance.getId()) != null)
			throw new RuntimeException("testRimozioneArticoloLegatoAdUnOrdine fallito ");
		
		//controllo che l-ordine sia rimasto
		if(!ordineServiceInstance.listAll().get(0).getId().equals(primoOrdine.getId())) {
			throw new RuntimeException("testRimozioneArticoloLegatoAdUnOrdine fallito ");
		}
		
		System.out.println(".......testRimozioneArticoloLegatoAdUnOrdine fine: PASSED.............");
	}
	
	public static void testInserimentoNuovoCategoria(CategoriaService categoriaServiceInstance) throws Exception{
		System.out.println(".......testInserimentoNuovoCategoria inizio.............");
		
		Categoria categoriaInstance = new Categoria("Prova","Prova");
		categoriaServiceInstance.inserisciNuovo(categoriaInstance);
		if (categoriaInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoCategoria fallito ");

		System.out.println(".......testInserimentoNuovoCategoria fine: PASSED.............");
	}
	
	public static void testAggiornamentoCategoriaEsistente(CategoriaService categoriaServiceInstance) throws Exception{
		System.out.println(".......testAggiornamentoCategoriaEsistente inizio.............");
		
		Categoria categoriaDaAggiornare = categoriaServiceInstance.listAll().get(0);
		if (categoriaDaAggiornare.getId() == null)
			throw new RuntimeException("testAggiornamentoCategoriaEsistente fallito ");
		
		Categoria temp = categoriaServiceInstance.caricaSingoloElemento(categoriaDaAggiornare.getId());
		temp.setDescrizione("Categoria1");
		temp.setCodice("AA22");
		categoriaServiceInstance.aggiorna(temp);
		if (temp.getId() == null)
			throw new RuntimeException("testAggiornamentoCategoriaEsistente fallito ");

		System.out.println(".......testAggiornamentoCategoriaEsistente fine: PASSED.............");
	}
	
	public static void testAggiungiCategoriaAdArticolo(ArticoloService articoloServiceInstance, CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception{
		System.out.println(".......testAggiungiCategoriaAdArticolo inizio.............");

		// inserisco una nuova categoria
		Categoria categoriaInstance = new Categoria("Categoria2","Prova2");
		categoriaServiceInstance.inserisciNuovo(categoriaInstance);
		if (categoriaInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoCategoria fallito ");
		
		// inserisco un nuovo articolo ma mi serve inserire anche un nuovo ordine
		Ordine ordineInstance = new Ordine("Ordine2","Prova2", new Date(), new Date());
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		if (ordineInstance.getId() == null)
			throw new RuntimeException("testAggiungiCategoriaAdArticolo fallito ");
		
		Articolo articoloInstance = new Articolo("Articolo2","Prova2",50 ,new Date(), ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance);
		if (articoloInstance.getId() == null)
			throw new RuntimeException("testAggiungiCategoriaAdArticolo fallito ");

		// collego le categorie ad un articolo
		articoloServiceInstance.aggiungiCategoria(articoloInstance, categoriaInstance);

		System.out.println(".......testAggiungiCategoriaAdArticolo fine: PASSED.............");
	}
	
	public static void testAggiungiArticoloACategoria(ArticoloService articoloServiceInstance, CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception{
		System.out.println(".......testAggiungiArticoloACategoria inizio.............");

		// inserisco una nuova categoria
		Categoria categoriaInstance = new Categoria("Categoria3","Prova3");
		categoriaServiceInstance.inserisciNuovo(categoriaInstance);
		if (categoriaInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoCategoria fallito ");
		
		// inserisco un nuovo articolo ma mi serve inserire anche un nuovo ordine
		Ordine ordineInstance = new Ordine("Ordine3","Prova3", new Date(), new Date());
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		if (ordineInstance.getId() == null)
			throw new RuntimeException("testAggiungiArticoloACategoria fallito ");
		
		Articolo articoloInstance = new Articolo("Articolo3","Prova3",75 ,new Date(), ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloInstance);
		if (articoloInstance.getId() == null)
			throw new RuntimeException("testAggiungiArticoloACategoria fallito ");

		// collego le categorie ad un articolo
		articoloServiceInstance.aggiungiCategoria(articoloInstance, categoriaInstance);

		System.out.println(".......testAggiungiArticoloACategoria fine: PASSED.............");
	}
	
	public static void testRimuoviArticolo(ArticoloService articoloServiceInstance, CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception{
		System.out.println(".......testRimuoviArticolo inizio.............");
		
		// carico un categoria e lo associo ad un nuovo articolo
		Categoria categoriaEsistenteSuDb = categoriaServiceInstance.listAll().get(1);
		if (categoriaEsistenteSuDb == null)
			throw new RuntimeException("testRimuoviArticolo fallito: categoria inesistente ");

		// inserisco un nuovo articolo ma mi serve inserire anche un nuovo ordine
		Ordine ordineInstance = new Ordine("Ordine4","Prova4", new Date(), new Date());
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		if (ordineInstance.getId() == null)
			throw new RuntimeException("testRimuoviArticolo fallito ");
		
		// mi creo un articolo inserendolo direttamente su db lascio ordine a null solo per questo test
		Articolo articoloNuovo = new Articolo("Articolo4","Prova4",75 ,new Date(), ordineInstance);
		articoloServiceInstance.inserisciNuovo(articoloNuovo);
		if (articoloNuovo.getId() == null)
			throw new RuntimeException("testRimuoviArticolo fallito: articolo non inserito ");
		articoloServiceInstance.aggiungiCategoria(articoloNuovo, categoriaEsistenteSuDb);

		// ora ricarico il record e provo a disassociare il categoria
		Articolo articoloReloaded = articoloServiceInstance.caricaSingoloElemento(articoloNuovo.getId());
				
		//per prima cosa scollego l'articolo dalla categoria
		articoloServiceInstance.rimuoviCategoriaDaArticolo(articoloReloaded.getId(), categoriaEsistenteSuDb.getId());
		
		//ora che ho rimosso il collegamento posso rimuovere articolo lasciando la categoria intatta
		articoloServiceInstance.rimuovi(articoloReloaded.getId());
		if (articoloReloaded.getId() == null)
			throw new RuntimeException("testRimuoviArticolo fallito: rimozione fallita ");
		
		System.out.println(".......testRimuoviArticolo fine: PASSED.............");
	}
	
	public static void testRimuoviCategoria(ArticoloService articoloServiceInstance, CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception{
		System.out.println(".......testRimuoviCategoria inizio.............");
		
		// carico un articolo e lo associo ad un nuovo categoria
		Categoria categoriaNuovo = new Categoria("codice5","descrizione5");
		categoriaServiceInstance.inserisciNuovo(categoriaNuovo);
		if (categoriaNuovo.getId() == null)
			throw new RuntimeException("testRimuoviCategoria fallito: categoria non inserito ");
		
		Articolo articoloEsistenteSuDb = articoloServiceInstance.listAll().get(0);
		if (articoloEsistenteSuDb == null)
			throw new RuntimeException("testRimuoviCategoria fallito: articolo inesistente ");
		categoriaServiceInstance.aggiungiArticolo(articoloEsistenteSuDb, categoriaNuovo);

		// ora ricarico il record e provo a disassociare il categoria
		Categoria categoriaReloaded = categoriaServiceInstance.caricaSingoloElemento(categoriaNuovo.getId());
				
		//per prima cosa scollego l'articolo dalla categoria
		categoriaServiceInstance.rimuoviArticoloDaCategoria(articoloEsistenteSuDb.getId(), categoriaReloaded.getId());
		
		//ora che ho rimosso il collegamento posso rimuovere articolo lasciando la categoria intatta
		categoriaServiceInstance.rimuovi(categoriaReloaded.getId());
		if (categoriaReloaded.getId() == null)
			throw new RuntimeException("testRimuoviCategoria fallito: rimozione fallita ");
		
		System.out.println(".......testRimuoviCategoria fine: PASSED.............");
	}
	
	public static void testRimuoviOrdine(ArticoloService articoloServiceInstance, OrdineService ordineServiceInstance) throws Exception, OrdiniArticoliCategorieException {
		System.out.println(".......testRimuoviOrdine inizio.............");
		
		Ordine ordineInstance = new Ordine("Ordine5","Prova5", new Date(), new Date());
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		if (ordineInstance.getId() == null)
			throw new RuntimeException("testRimuoviArticolo fallito ");
		
		Articolo articoloNuovo = new Articolo("Articolo6","Prova6",75 ,new Date(), ordineInstance);
		
		// SE levi questo commento lancia eccezzione custom perche' non puoi rimuovere un ordine
		// con una categoria!
		//articoloServiceInstance.inserisciNuovo(articoloNuovo);
		
		ordineServiceInstance.rimuovi(ordineInstance.getId());
		if (ordineInstance.getId() == null)
			throw new RuntimeException("testRimuoviArticolo fallito: rimozione fallita ");
		
		System.out.println(".......testRimuoviOrdine fine: PASSED.............");
	}
}
