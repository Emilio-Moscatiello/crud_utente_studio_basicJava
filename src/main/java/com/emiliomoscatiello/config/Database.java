package com.emiliomoscatiello.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestione connessione al database PostgreSQL via JDBC puro.
 * Database: crud_utente_studio
 */
public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/crud_utente_studio";
    private static final String USER = System.getenv().getOrDefault("PG_USER", "postgres");
    private static final String PASSWORD = System.getenv().getOrDefault("PG_PASSWORD", "1234");

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL non trovato", e);
        }
    }

    /*
     * Class.forName() è un metodo statico di java.lang.Class che carica una classe
     * a partire dal suo nome (stringa) e restituisce l’oggetto Class
     * corrispondente.
     * È il modo classico per assicurarsi che il driver JDBC sia caricato prima di
     * fare connessioni. Senza questo caricamento esplicito (o senza che il driver
     * si carichi automaticamente), DriverManager.getConnection() potrebbe non
     * trovare un driver adatto e sollevare un’eccezione.
     * Class.forName() non è obbligatorio in progetti moderni, ma è una pratica
     * diffusa e chiara per registrare esplicitamente il driver prima di usare
     * DriverManager.getConnection().
     */

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /*
     * DriverManager è una classe di Java (in java.sql) che gestisce i driver JDBC e
     * le connessioni al database.
     * È quindi il punto centrale per ottenere connessioni JDBC senza dover gestire
     * direttamente il driver.
     * Il DriverManager è il meccanismo standard di Java per ottenere connessioni al
     * database tramite JDBC. Non è obbligatorio in senso assoluto (puoi usare
     * direttamente i driver o framework sopra JDBC), ma è la soluzione di base che
     * si usa quasi sempre, almeno nei progetti che parlano direttamente con JDBC.
     */

    public static void initSchema(Connection conn) throws SQLException {
        try (var stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS utenti (
                        id BIGSERIAL PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        cognome VARCHAR(100) NOT NULL,
                        email VARCHAR(150) NOT NULL UNIQUE,
                        telefono VARCHAR(20),
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP
                    )
                    """);
        }
    }

    /*
     * conn.createStatement() crea uno Statement, l’oggetto usato per eseguire
     * istruzioni SQL
     * stmt.execute(...) esegue la stringa SQL passata come argomento
     * try-with-resources garantisce che Statement venga chiuso correttamente anche
     * in caso di eccezione.
     */
}

/*
 * 3. L’infrastruttura: config/Database.java
 * Database.java incapsula la connessione al database. Contiene URL, utente e
 * password. Utente e password possono essere letti da variabili d’ambiente
 * (PG_USER, PG_PASSWORD), altrimenti si usano i valori di default.
 * Nel blocco static viene caricato il driver PostgreSQL con Class.forName. Se
 * la libreria non è nel classpath, viene lanciata una RuntimeException subito
 * all’avvio.
 * getConnection() apre una nuova connessione tramite
 * DriverManager.getConnection. Ogni chiamata crea una connessione nuova; in
 * un’applicazione più complessa si userebbe un pool di connessioni.
 * initSchema esegue la creazione della tabella utenti tramite CREATE TABLE IF
 * NOT EXISTS. Così la struttura esiste sempre, senza creare errori se la
 * tabella è già presente. I campi sono: id (BIGSERIAL, chiave primaria), nome,
 * cognome, email (UNIQUE), telefono, created_at, updated_at.
 * 4. in model
 */
