package it.prova.gestioneordiniarticolicategorie.test;

import it.prova.gestioneordiniarticolicategorie.service.OrdineService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
			
			// Nel persistence lascio sempre a create in modo da non sporcare la base dati
			// ed averne una nuova per ogni run
			
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
			
			testTrovaTuttiPerCategoria(ordineServiceInstance, categoriaServiceInstance);
			
			testTrovaTuttiPerOrdine(ordineServiceInstance, categoriaServiceInstance);
			
			testSommaTotalePerCategoria(articoloServiceInstance, categoriaServiceInstance);
			
			testTrovaOrdinePiuRecentePerCategoria(ordineServiceInstance, categoriaServiceInstance);
			
			testTrovaTuttiCodiciPerMeseOrdine(categoriaServiceInstance);
			
			testSommaTotalePerOrdineDestinatario(articoloServiceInstance);
			
			testTrovaTuttiIndirizziPerNumeroSerialeLike(ordineServiceInstance);
			
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

		Ordine ordineInstance = new Ordine("Prova","Prova", new Date(), new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2022"));
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
	
	// per i test di SELECT smetto di inserire dati a mano, mi prendo i dati gia inseriti sul db
	
	public static void testTrovaTuttiPerCategoria(OrdineService ordineServiceInstance, CategoriaService categoriaServiceInstance) throws Exception {
		System.out.println(".......testTrovaTuttiPerCategoria inizio.............");
		
		//prendo una categoria che so gia che e' linkata
		Categoria categoriaInstance = categoriaServiceInstance.listAll().get(2);
		if(categoriaInstance.getId() == null)
			throw new RuntimeException("testTrovaTuttiPerCategoria fallito: categoria non trovata ");
		
		List<Ordine> ordiniTrovati = ordineServiceInstance.trovaTuttiPerCategoria(categoriaInstance);
		// me ne aspetto uno quindi se diverso da uno eccezione
		if(ordiniTrovati.size() != 1)
			throw new RuntimeException("testTrovaTuttiPerCategoria fallito: numero record aspettati inesatto ");

		System.out.println(".......testTrovaTuttiPerCategoria fine: PASSED.............");
	}
	
	public static void testTrovaTuttiPerOrdine(OrdineService ordineServiceInstance, CategoriaService categoriaServiceInstance) throws Exception {
		System.out.println(".......testTrovaTuttiPerOrdine inizio.............");
		
		//prendo un ordine che so gia che e' linkato
		Ordine ordineInstance = ordineServiceInstance.listAll().get(2);
		if(ordineInstance.getId() == null)
			throw new RuntimeException("testTrovaTuttiPerOrdine fallito: ordine non trovato ");
		
		List<Categoria> caegorieTrovate = categoriaServiceInstance.trovaTuttiPerOrdine(ordineInstance);
		// me ne aspetto uno quindi se diverso da uno eccezione
		if(caegorieTrovate.size() != 1)
			throw new RuntimeException("testTrovaTuttiPerOrdine fallito: numero record aspettati inesatto ");

		System.out.println(".......testTrovaTuttiPerOrdine fine: PASSED.............");
	}
	
	public static void testSommaTotalePerCategoria(ArticoloService articoloServiceInstance, CategoriaService categoriaServiceInstance) throws Exception {
		System.out.println(".......testSommaTotalePerCategoria inizio.............");
		
		//prendo una categoria gia linkata
		Categoria categoriaInstance = categoriaServiceInstance.listAll().get(1);
		if(categoriaInstance.getId() == null)
			throw new RuntimeException("testSommaTotalePerCategoria fallito: categoria non trovata ");
		
		Long sommaArticoli = articoloServiceInstance.sommaTotalePerCategoria(categoriaInstance);
		// mi aspetto che la somma totale sia 50 quindi se diverso eccezione
		if(sommaArticoli != 50)
			throw new RuntimeException("testSommaTotalePerCategoria fallito: somma non conforme ");

		System.out.println(".......testSommaTotalePerCategoria fine: PASSED.............");
	}
	
	public static void testTrovaOrdinePiuRecentePerCategoria(OrdineService ordineServiceInstance, CategoriaService categoriaServiceInstance) throws Exception {
		System.out.println(".......testTrovaOrdinePiuRecentePerCategoria inizio.............");
		
		//prendo una categoria che so gia che e' linkata
		Categoria categoriaInstance = categoriaServiceInstance.listAll().get(2);
		if(categoriaInstance.getId() == null)
			throw new RuntimeException("testTrovaOrdinePiuRecentePerCategoria fallito: categoria non trovata ");
		
		Ordine ordineTrovato = ordineServiceInstance.trovaOrdinePiuRecentePerCategoria(categoriaInstance);
		
		if(ordineTrovato.getId() == null)
			throw new RuntimeException("testTrovaOrdinePiuRecentePerCategoria fallito: ordine non trovato");

		System.out.println(".......testTrovaOrdinePiuRecentePerCategoria fine: PASSED.............");
	}
	
	public static void testTrovaTuttiCodiciPerMeseOrdine(CategoriaService categoriaServiceInstance) throws Exception {
		System.out.println(".......testTrovaTuttiCodiciPerMeseOrdine inizio.............");
		
		// creo una data di anno e mese che ho
		Date dataInput = new Date();
		
		List<String> caegorieTrovate = categoriaServiceInstance.trovaTuttiCodiciPerMeseOrdine(dataInput);
		
		/*	stampa di test per vedere se stampa le categorie giuste
		for(String items:caegorieTrovate) {
			System.out.println(items);
		}*/
		
		// me ne aspetto uno quindi se diverso da uno eccezione
		if(caegorieTrovate.size() != 2)
			throw new RuntimeException("testTrovaTuttiCodiciPerMeseOrdine fallito: numero record aspettati inesatto ");

		System.out.println(".......testTrovaTuttiCodiciPerMeseOrdine fine: PASSED.............");
	}
	
	public static void testSommaTotalePerOrdineDestinatario(ArticoloService articoloServiceInstance) throws Exception {
		System.out.println(".......testSommaTotalePerOrdineDestinatario inizio.............");
		
		// inserisco nome di un destinatario gia esistente
		Long sommaArticoli = articoloServiceInstance.sommaTotalePerOrdineDestinatario("Mario Rossi");
		// mi aspetto che la somma totale sia 15 per Mario Rossi quindi se diverso eccezione
		if(sommaArticoli != 15)
			throw new RuntimeException("testSommaTotalePerOrdineDestinatario fallito: somma non conforme ");

		System.out.println(".......testSommaTotalePerOrdineDestinatario fine: PASSED.............");
	}
	
	public static void testTrovaTuttiIndirizziPerNumeroSerialeLike(OrdineService ordineServiceInstance) throws Exception {
		System.out.println(".......testTrovaTuttiIndirizziPerNumeroSerialeLike inizio.............");
		
		// inserisco come stringa che deve essere contentua nel numero seriale 'ova'
		List<String> ordiniTrovati = ordineServiceInstance.trovaTuttiIndirizziPerNumeroSerialeLike("ova");
		// mi aspetto che la somma totale sia 15 per Mario Rossi quindi se diverso eccezione
		if(ordiniTrovati.size() != 2)
			throw new RuntimeException("testTrovaTuttiIndirizziPerNumeroSerialeLike fallito: numero record aspettati inesatto ");

		/*	stampa di test per vedere se stampa gli indirizzi giusti
		for(String items:ordiniTrovati) {
			System.out.println(items);
		}*/
		
		System.out.println(".......testTrovaTuttiIndirizziPerNumeroSerialeLike fine: PASSED.............");
	}
	
	public static void testTrovaTuttiPerOrdineErrore(ArticoloService articoloServiceInstance) throws Exception {
System.out.println(".......testTrovaTuttiPerOrdineErrore inizio.............");
		
		List<Articolo> articoliTrovati = articoloServiceInstance.trovaTuttiPerOrdineErrore();
		// mi aspetto di trovare solo un articolo con date errate
		if(articoliTrovati.size() != 1)
			throw new RuntimeException("testTrovaTuttiPerOrdineErrore fallito: numero record aspettati inesatto ");
		
		System.out.println(".......testTrovaTuttiPerOrdineErrore fine: PASSED.............");
	}
}
