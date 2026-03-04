package com.emiliomoscatiello.repository;

import com.emiliomoscatiello.config.Database;
import com.emiliomoscatiello.model.Utente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY - Accesso ai dati tramite JDBC puro.
 * Esegue le query SQL e mappa ResultSet -> Utente.
 */
public class UtenteRepository {

    public Utente save(Utente utente) throws SQLException {
        String sql = "INSERT INTO utenti (nome, cognome, email, telefono, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            /*
             * Statement.RETURN_GENERATED_KEYS è una costante di Statement che indica al
             * database: “dopo l’INSERT, voglio poter leggere i valori generati
             * automaticamente (come l’id)”.
             * Quando fai un INSERT in una tabella con colonna BIGSERIAL, l’id viene
             * generato dal database. Per sapere quale id è stato assegnato al record appena
             * inserito, devi chiedere esplicitamente di restituire le chiavi generate.
             * Passando Statement.RETURN_GENERATED_KEYS alla creazione dello Statement, il
             * driver chiede al database di restituire le chiavi generate. Poi
             * getGeneratedKeys() restituisce un ResultSet con questi valori (per una
             * colonna, tipicamente un solo valore: l’id).
             * Senza RETURN_GENERATED_KEYS, getGeneratedKeys() restituirebbe un ResultSet
             * vuoto e non potresti ottenere l’id del record appena inserito.
             */
            ps.setString(1, utente.getNome());
            /*
             * setString(int parameterIndex, String x) imposta il parameterIndex-esimo ? con
             * la stringa x.
             * 1 indica il primo ? nella query.
             * utente.getNome() è il valore da inserire, preso dall’oggetto Utente.
             * Quindi: ps.setString(1, utente.getNome()); dice al PreparedStatement:
             * > “Nel primo ? della query, usa la stringa che ottieni da utente.getNome()”.
             */
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, utente.getTelefono());
            LocalDateTime now = LocalDateTime.now();
            ps.setObject(5, now);
            ps.setObject(6, now);
            ps.executeUpdate();
            /*
             * Esegue la query di modifica (INSERT, UPDATE o DELETE).
             * Restituisce il numero di righe interessate (es. 1 per un INSERT singolo). Non
             * restituisce dati: per leggere i risultati si usa executeQuery(), per chiavi
             * generate si usa getGeneratedKeys() dopo un executeUpdate().
             */
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                utente.setId(rs.getLong(1));
                utente.setCreatedAt(now);
                utente.setUpdatedAt(now);
            }
            /*
             * getGeneratedKeys() restituisce un ResultSet con le chiavi generate
             * dall’ultima INSERT eseguita con quel PreparedStatement.
             * Nel tuo caso c’è un solo valore: l’id assegnato dal database (colonna
             * BIGSERIAL).
             * if (rs.next()) {
             * next() sposta il cursore sulla prima riga (se esiste).
             * Restituisce true se c’è una riga → entri nell’if
             * Restituisce false se il ResultSet è vuoto → salti l’if
             * L’if serve perché tecnicamente potrebbe non esserci alcuna chiave restituita.
             * utente.setId(rs.getLong(1));
             * rs.getLong(1) legge il valore della prima colonna del ResultSet come long
             * (l’id generato).
             * utente.setId(...) imposta quell’id sull’oggetto Utente, così puoi usarlo
             * subito (es. per mostrarlo o salvarlo).
             * utente.setCreatedAt(now); e utente.setUpdatedAt(now);
             * Qui non si leggono dal database: si usano le variabili già impostate in Java.
             * Per il record appena creato, created_at e updated_at sono uguali, quindi si
             * usa lo stesso valore now per entrambi e si aggiorna l’oggetto Utente in
             * memoria.
             */
        }
        return utente;
    }

    public List<Utente> findAll() throws SQLException {
        List<Utente> lista = new ArrayList<>();
        String sql = "SELECT id, nome, cognome, email, telefono, created_at, updated_at FROM utenti ORDER BY id";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                /*
                 * conn è un oggetto Connection che rappresenta la connessione al database.
                 * prepareStatement(sql) è un metodo di Connection che crea un PreparedStatement
                 * a partire dalla stringa SQL passata come argomento.
                 * Con prepareStatement(sql) usi i placeholder ? e imposti i valori
                 * separatamente.
                 */
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public Optional<Utente> findById(Long id) throws SQLException {
        String sql = "SELECT id, nome, cognome, email, telefono, created_at, updated_at FROM utenti WHERE id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            /*
             * È un metodo del PreparedStatement. Assegna il valore id (tipo long) al primo
             * ? nella query SQL.
             * 1 – Indice del placeholder (il primo ? ha indice 1)
             * id – Valore numerico long da inserire al posto del ?
             * 
             */
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                    /*
                     * mapRow(rs) – Metodo che trasforma la riga corrente del ResultSet in un
                     * oggetto Utente (nome, cognome, email, ecc.).
                     * Optional.of(...) – Crea un Optional che contiene quel Utente. Indica che il
                     * valore c’è e non è null.
                     */
                }
            }
        }
        return Optional.empty();
        /*
         * Optional.empty() è un Optional senza valore. Si usa quando non hai nulla da
         * restituire ma vuoi comunque un Optional invece di null.
         */
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM utenti WHERE email = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsByEmailAndIdNot(String email, Long id) throws SQLException {
        String sql = "SELECT 1 FROM utenti WHERE email = ? AND id != ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setLong(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public Utente update(Utente utente) throws SQLException {
        String sql = "UPDATE utenti SET nome = ?, cognome = ?, email = ?, telefono = ?, updated_at = ? WHERE id = ?";
        LocalDateTime now = LocalDateTime.now();
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, utente.getTelefono());
            ps.setObject(5, now);
            ps.setLong(6, utente.getId());
            ps.executeUpdate();
            utente.setUpdatedAt(now);
        }
        return utente;
    }

    public boolean deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM utenti WHERE id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT 1 FROM utenti WHERE id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Utente mapRow(ResultSet rs) throws SQLException {
        Utente u = new Utente();
        u.setId(rs.getLong("id"));
        u.setNome(rs.getString("nome"));
        u.setCognome(rs.getString("cognome"));
        u.setEmail(rs.getString("email"));
        u.setTelefono(rs.getString("telefono"));
        u.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        u.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        return u;
    }
    /*
     * Il metodo mapRow fa una sola cosa: prende i dati di una riga del database e
     * li mette dentro un oggetto Java Utente.
     * In pratica:
     * Il database restituisce righe in un formato che Java non sa usare
     * direttamente (ResultSet)
     * mapRow legge i valori di quella riga (id, nome, cognome, ecc.) e li copia nei
     * campi di un oggetto Utente
     * In questo modo ottieni un Utente pronto da usare nel tuo codice Java invece
     * di lavorare con il ResultSet
     * In sintesi: trasforma una riga del database in un oggetto Utente.
     */
}

/*
 * 6. L’accesso ai dati: repository/UtenteRepository.java
 * UtenteRepository traduce le operazioni CRUD in istruzioni SQL e gestisce il
 * mapping tra database e oggetti Utente.
 * Ogni metodo apre una connessione con Database.getConnection(), usa
 * try-with-resources per chiuderla e chiude anche PreparedStatement e
 * ResultSet. In questo modo non ci sono connessioni lasciate aperte.
 * Il metodo save esegue un INSERT. Con Statement.RETURN_GENERATED_KEYS il
 * database restituisce l’id generato (BIGSERIAL). Il repository legge questo
 * valore e lo imposta sull’oggetto Utente, insieme a createdAt e updatedAt.
 * findAll esegue una SELECT ordinata per id e, per ogni riga, chiama mapRow per
 * costruire un Utente.
 * findById usa una SELECT con WHERE id = ? e restituisce un Optional<Utente>,
 * vuoto se non trova righe.
 * existsByEmail e existsByEmailAndIdNot eseguono SELECT 1 per verificare
 * l’esistenza di record. Sono usate dal service per validare l’unicità
 * dell’email prima di creare o aggiornare.
 * update fa una UPDATE impostando i campi modificati e updated_at. deleteById
 * esegue una DELETE e restituisce true se è stata eliminata almeno una riga.
 * mapRow converte una riga di ResultSet in un Utente, leggendo le colonne per
 * nome. rs.getObject("created_at", LocalDateTime.class) mappa direttamente i
 * timestamp SQL alle date Java.
 * 7. nel service
 */
