package com.emiliomoscatiello;

import com.emiliomoscatiello.controller.UtenteController;
import java.sql.SQLException;

/**
 * Entry point - avvia il CRUD da console.
 */
public class App {

    public static void main(String[] args) {
        try {
            new UtenteController().run();
        } catch (SQLException e) {
            System.err.println("Errore database: " + e.getMessage());
        }
    }
}

/*
 * 1. Il punto di ingresso: App.java
 * L’applicazione inizia in App.java. Il metodo main crea un UtenteController,
 * chiama run() e gestisce eventuali SQLException. Se la connessione al database
 * fallisce o il driver non è disponibile, l’errore viene stampato e il
 * programma termina.
 * 2. nel pom.xml
 * 
 * 
 * 
 * 9. Il flusso complessivo
 * L’ordine di creazione e collegamento è questo:
 * pom.xml – definisce le dipendenze e il contesto di build.
 * Database – fornisce connessione e creazione schema; è usato da Repository e
 * Controller.
 * Utente – entità di dominio usata da Repository e Service.
 * UtenteRequest e UtenteResponse – DTO usati da Service e Controller.
 * UtenteRepository – usa Database e Utente, espone operazioni CRUD.
 * UtenteService – usa Repository, Utente e DTO, applica le regole di business.
 * UtenteController – usa Service, DTO e Database (solo per init), gestisce
 * l’interazione console.
 * App – avvia tutto creando il Controller e chiamando run.
 * Quando l’utente sceglie “Crea utente”, il Controller legge i dati, crea un
 * UtenteRequest e chiama service.create. Il Service controlla l’email, crea un
 * Utente, lo salva tramite il Repository con una INSERT, riceve l’oggetto con
 * id e date, lo trasforma in UtenteResponse e lo restituisce al Controller, che
 * lo mostra in console.
 * La stessa logica si applica a lettura, aggiornamento ed eliminazione: il
 * Controller riceve input e mostra output, il Service applica le regole e
 * orchestra le operazioni, il Repository esegue le query SQL e mappa i
 * risultati sugli oggetti Utente.
 */
