package com.listaai.list.domain.exception.list;

public class EmptyShoppingListCannotBeSharedException extends RuntimeException {

    public EmptyShoppingListCannotBeSharedException() {
        super("Empty list can't be shared");
    }

}
