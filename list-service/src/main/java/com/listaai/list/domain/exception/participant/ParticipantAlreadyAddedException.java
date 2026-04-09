package com.listaai.list.domain.exception.participant;

public class ParticipantAlreadyAddedException extends RuntimeException {

    public ParticipantAlreadyAddedException() {
        super("Participant already exists");
    }

}
