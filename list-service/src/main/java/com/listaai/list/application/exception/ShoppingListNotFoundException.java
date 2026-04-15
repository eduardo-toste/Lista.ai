package com.listaai.list.application.exception;

public class ShoppingListNotFoundException extends RuntimeException {

    public ShoppingListNotFoundException() {
        super("Shopping list not found");
    }

}
