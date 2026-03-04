package com.emiliomoscatiello.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO - Dati in ingresso per create/update.
 * Il controller passa questi dati al service.
 */
@Getter
@Setter
@NoArgsConstructor
public class UtenteRequest {

    private String nome;
    private String cognome;
    private String email;
    private String telefono;
}

/*
 * 5. I contratti di dati: dto/UtenteRequest e UtenteResponse
 * I DTO (Data Transfer Object) definiscono cosa entra e cosa esce
 * dall’applicazione.
 * UtenteRequest (dati in ingresso) contiene solo nome, cognome, email,
 * telefono. Non ha id né
 * date, perché sono dati di creazione o modifica inseriti dall’utente. Il
 * controller riempie questo oggetto e lo passa al service.
 * UtenteResponse (dati in uscita) ha tutti i campi, incluso id, createdAt e
 * updatedAt. Viene
 * usato quando si restituiscono dati all’utente (es. dopo una lettura o un
 * salvataggio). Così l’utente vede l’entità completa con le date gestite dal
 * sistema.
 * Se usi Request (dati in ingresso) e Response (dati in uscita) separati dal
 * Model (l’entità di dominio), puoi modificare il Model senza cambiare come
 * l’esterno invia o riceve i dati. E puoi cambiare come l’esterno comunica
 * senza dover modificare il Model.
 * 6. nel repository
 */
