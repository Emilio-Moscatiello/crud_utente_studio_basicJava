package com.emiliomoscatiello.controller;

import com.emiliomoscatiello.config.Database;
import com.emiliomoscatiello.dto.UtenteRequest;
import com.emiliomoscatiello.dto.UtenteResponse;
import com.emiliomoscatiello.service.UtenteService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * CONTROLLER - Riceve l'input dall'utente (console) e delega al Service.
 * In un'app MVC il controller gestisce l'interazione view-utente.
 * Qui la "view" è la console con menu testuale.
 */
public class UtenteController {

    private final UtenteService service = new UtenteService();
    private final Scanner scanner = new Scanner(System.in);

    public void run() throws SQLException {
        Database.initSchema(Database.getConnection());
        System.out.println("=== CRUD Utenti (Java puro, JDBC, MVC) ===\n");

        while (true) {
            mostraMenu();
            String scelta = scanner.nextLine().trim();
            switch (scelta) {
                case "1" -> create();
                case "2" -> findAll();
                case "3" -> findById();
                case "4" -> update();
                case "5" -> delete();
                case "0" -> {
                    System.out.println("Uscita.");
                    return;
                }
                default -> System.out.println("Scelta non valida.\n");
            }
        }
    }

    private void mostraMenu() {
        System.out.println("1. Crea utente");
        System.out.println("2. Elenco utenti");
        System.out.println("3. Cerca per id");
        System.out.println("4. Modifica utente");
        System.out.println("5. Elimina utente");
        System.out.println("0. Esci");
        System.out.print("Scelta: ");
    }

    private void create() throws SQLException {
        UtenteRequest req = leggiDatiUtente();
        try {
            UtenteResponse res = service.create(req);
            System.out.println("Creato: " + res.getNome() + " " + res.getCognome() + " (id=" + res.getId() + ")\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage() + "\n");
        }
    }

    private void findAll() throws SQLException {
        List<UtenteResponse> lista = service.findAll();
        if (lista.isEmpty()) {
            System.out.println("Nessun utente.\n");
        } else {
            for (UtenteResponse u : lista) {
                System.out
                        .println("  " + u.getId() + " | " + u.getNome() + " " + u.getCognome() + " | " + u.getEmail());
            }
            System.out.println();
        }
    }

    private void findById() throws SQLException {
        System.out.print("Id: ");
        String s = scanner.nextLine().trim();
        try {
            long id = Long.parseLong(s);
            UtenteResponse u = service.findById(id);
            System.out.println("  " + u.getId() + " | " + u.getNome() + " " + u.getCognome() + " | " + u.getEmail()
                    + " | " + u.getTelefono() + "\n");
        } catch (NumberFormatException e) {
            System.out.println("Id non valido.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage() + "\n");
        }
    }

    private void update() throws SQLException {
        System.out.print("Id da modificare: ");
        String s = scanner.nextLine().trim();
        try {
            long id = Long.parseLong(s);
            UtenteRequest req = leggiDatiUtente();
            UtenteResponse res = service.update(id, req);
            System.out.println("Aggiornato: " + res.getNome() + " " + res.getCognome() + "\n");
        } catch (NumberFormatException e) {
            System.out.println("Id non valido.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage() + "\n");
        }
    }

    private void delete() throws SQLException {
        System.out.print("Id da eliminare: ");
        String s = scanner.nextLine().trim();
        try {
            long id = Long.parseLong(s);
            service.delete(id);
            System.out.println("Eliminato.\n");
        } catch (NumberFormatException e) {
            System.out.println("Id non valido.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage() + "\n");
        }
    }

    private UtenteRequest leggiDatiUtente() {
        UtenteRequest req = new UtenteRequest();
        System.out.print("Nome: ");
        req.setNome(scanner.nextLine().trim());
        System.out.print("Cognome: ");
        req.setCognome(scanner.nextLine().trim());
        System.out.print("Email: ");
        req.setEmail(scanner.nextLine().trim());
        System.out.print("Telefono: ");
        req.setTelefono(scanner.nextLine().trim());
        return req;
    }
}

/*
 * 8. L’interfaccia utente: controller/UtenteController.java
 * Il controller gestisce l’interazione con l’utente tramite la console.
 * All’inizio di run chiama Database.initSchema per assicurarsi che la tabella
 * esista, poi entra in un ciclo che mostra il menu e gestisce la scelta.
 * Il menu offre: crea utente, elenco utenti, cerca per id, modifica, elimina,
 * esci. La scelta viene letta con Scanner.nextLine() e gestita con uno switch.
 * Ogni opzione richiama un metodo privato specifico.
 * create legge i dati con leggiDatiUtente, costruisce un UtenteRequest, chiama
 * service.create e stampa il risultato. In caso di IllegalArgumentException
 * (es. email duplicata), stampa il messaggio di errore invece di far terminare
 * il programma.
 * findAll riceve la lista di UtenteResponse e la stampa in modo leggibile. Se
 * la lista è vuota, viene mostrato un messaggio appropriato.
 * findById chiede l’id, lo converte in long, chiama service.findById e stampa
 * il risultato. Gestisce NumberFormatException (id non numerico) e
 * IllegalArgumentException (utente non trovato).
 * update chiede l’id e i nuovi dati, poi chiama service.update e mostra un
 * messaggio di conferma o errore.
 * delete chiede l’id, chiama service.delete e conferma l’operazione.
 * leggiDatiUtente crea un UtenteRequest, chiede nome, cognome, email e telefono
 * alla console, assegna i valori e restituisce l’oggetto. È usato sia in create
 * sia in update.
 * 9. in app.java
 */
