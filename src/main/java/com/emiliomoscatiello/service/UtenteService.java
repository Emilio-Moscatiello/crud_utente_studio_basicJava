package com.emiliomoscatiello.service;

import com.emiliomoscatiello.dto.UtenteRequest;
import com.emiliomoscatiello.dto.UtenteResponse;
import com.emiliomoscatiello.model.Utente;
import com.emiliomoscatiello.repository.UtenteRepository;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE - Logica di business.
 * Riceve i DTO dal controller, usa il repository per il DB,
 * applica le regole e restituisce i DTO al controller.
 */
public class UtenteService {

    private final UtenteRepository repository = new UtenteRepository();

    public UtenteResponse create(UtenteRequest request) throws SQLException {
        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Esiste già un utente con email: " + request.getEmail());
        }
        Utente u = new Utente(request.getNome(), request.getCognome(), request.getEmail(), request.getTelefono());
        u = repository.save(u);
        return toResponse(u);
    }

    public List<UtenteResponse> findAll() throws SQLException {
        List<UtenteResponse> result = new ArrayList<>();
        for (Utente u : repository.findAll()) {
            result.add(toResponse(u));
        }
        return result;
    }

    public UtenteResponse findById(Long id) throws SQLException {
        return repository.findById(id)
                .map(this::toResponse)
                /*
                 * La forma è: oggetto::nomeMetodo oppure Classe::nomeMetodo.
                 * this::toResponse → metodo toResponse sull’istanza corrente
                 * È una abbreviazione per le lambda quando passi semplicemente un argomento a
                 * un metodo. Invece di:
                 * .map(x -> this.toResponse(x))
                 * scrivi:
                 * .map(this::toResponse)
                 * Più corto e leggibile.
                 */
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con id: " + id));
    }

    public UtenteResponse update(Long id, UtenteRequest request) throws SQLException {
        Utente u = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con id: " + id));
        if (repository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("Esiste già un utente con email: " + request.getEmail());
        }
        u.setNome(request.getNome());
        u.setCognome(request.getCognome());
        u.setEmail(request.getEmail());
        u.setTelefono(request.getTelefono());
        u = repository.update(u);
        return toResponse(u);
    }

    public void delete(Long id) throws SQLException {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Utente non trovato con id: " + id);
        }
        repository.deleteById(id);
    }

    private UtenteResponse toResponse(Utente u) {
        UtenteResponse r = new UtenteResponse();
        r.setId(u.getId());
        r.setNome(u.getNome());
        r.setCognome(u.getCognome());
        r.setEmail(u.getEmail());
        r.setTelefono(u.getTelefono());
        r.setCreatedAt(u.getCreatedAt());
        r.setUpdatedAt(u.getUpdatedAt());
        return r;
    }
}

/*
 * 7. La logica di business: service/UtenteService.java
 * UtenteService coordina le operazioni e applica le regole di business. Non
 * accede al database direttamente, ma solo tramite il repository. Riceve e
 * restituisce DTO, e usa internamente gli oggetti Utente.
 * Nel metodo create, prima controlla se esiste già un utente con la stessa
 * email. Se sì, lancia IllegalArgumentException. Altrimenti crea un Utente dai
 * campi della request, lo salva tramite il repository e restituisce un
 * UtenteResponse tramite toResponse.
 * findAll recupera tutti gli utenti e li trasforma in UtenteResponse.
 * findById usa repository.findById(id). Se l’Optional è vuoto, lancia
 * un’eccezione con un messaggio esplicito. Se è pieno, converte l’Utente in
 * UtenteResponse.
 * Nel metodo update controlla che l’utente esista e che la nuova email non sia
 * già usata da un altro utente (escludendo l’id in modifica). Se tutto è
 * valido, aggiorna i campi dell’Utente, chiama il repository per la UPDATE e
 * restituisce un UtenteResponse.
 * delete verifica che l’utente esista prima di cancellarlo; in caso contrario
 * lancia un’eccezione.
 * Il metodo toResponse copia i campi da un Utente a un UtenteResponse. È
 * privato e usato da tutti i metodi che devono restituire un utente.
 * 8. nel controller
 */
