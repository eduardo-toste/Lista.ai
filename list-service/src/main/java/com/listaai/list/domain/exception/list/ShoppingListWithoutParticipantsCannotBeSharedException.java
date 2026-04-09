package com.listaai.list.domain.exception.list;

public class ShoppingListWithoutParticipantsCannotBeSharedException extends RuntimeException {

    public ShoppingListWithoutParticipantsCannotBeSharedException() {
        super("List without participants can't be shared");
    }

}
