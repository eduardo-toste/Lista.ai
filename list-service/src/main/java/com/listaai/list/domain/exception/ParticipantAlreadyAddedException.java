package com.listaai.list.domain.exception;

public class ParticipantAlreadyAddedException extends RuntimeException {

    public ParticipantAlreadyAddedException() {
        super("Participant already exists");
    }

}
