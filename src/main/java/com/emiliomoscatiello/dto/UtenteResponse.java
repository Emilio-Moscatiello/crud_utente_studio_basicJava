package com.emiliomoscatiello.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO - Dati in uscita verso il client.
 * Il service restituisce questi dati al controller.
 */
@Getter
@Setter
@NoArgsConstructor
public class UtenteResponse {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

/*
 * 5. I contratti di dati: dto/UtenteRequest e UtenteResponse
 * I DTO (Data Transfer Object) definiscono cosa entra e cosa esce
 * dall’applicazione.
 * UtenteRequest contiene solo nome, cognome, email, telefono. Non ha id né
 * date, perché sono dati di creazione o modifica inseriti dall’utente. Il
 * controller riempie questo oggetto e lo passa al service.
 * UtenteResponse ha tutti i campi, incluso id, createdAt e updatedAt. Viene
 * usato quando si restituiscono dati all’utente (es. dopo una lettura o un
 * salvataggio). Così l’utente vede l’entità completa con le date gestite dal
 * sistema.
 * Separare Request e Response permette di cambiare la struttura del dominio
 * senza modificare i contratti con l’esterno, e viceversa.
 * 6. nel repository
 */
