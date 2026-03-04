package com.emiliomoscatiello.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MODEL - Entità Utente, semplice POJO.
 * Rappresenta la struttura dati dell'utente nel dominio.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/*
 * Sono annotazioni di Lombok che generano automaticamente codice a
 * compile-time, così non devi scriverlo a mano.
 * 
 * @NoArgsConstructor
 * Genera il costruttore senza parametri.
 * 
 * @AllArgsConstructor
 * Genera un costruttore con tutti i campi della classe come parametri,
 * nell’ordine in cui sono dichiarati.
 */

public class Utente {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Utente(String nome, String cognome, String email, String telefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
    }
}

/*
 * 4. L’entità di dominio: model/Utente.java
 * Utente è un POJO (Plain Old Java Object). Non ha annotazioni JPA né
 * dipendenze esterne: solo campi e getter/setter generati da Lombok.
 * Ogni campo corrisponde a una colonna della tabella. id è l’identificatore.
 * nome e cognome sono i dati anagrafici. email identifica univocamente
 * l’utente. telefono è opzionale. createdAt e updatedAt tengono traccia di
 * creazione e aggiornamento.
 * Questa classe rappresenta l’utente nel dominio dell’applicazione. Viene usata
 * all’interno del repository e del service, ma non direttamente verso
 * l’esterno.
 * 5. nel dto
 */
