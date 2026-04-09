package com.listaai.list.domain.exception;

public class ParticipantNotFoundException extends RuntimeException {

    public ParticipantNotFoundException() {
        super("Participant not found");
    }

}
