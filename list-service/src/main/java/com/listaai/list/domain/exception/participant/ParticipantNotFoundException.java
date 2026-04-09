package com.listaai.list.domain.exception.participant;

public class ParticipantNotFoundException extends RuntimeException {

    public ParticipantNotFoundException() {
        super("Participant not found");
    }

}
