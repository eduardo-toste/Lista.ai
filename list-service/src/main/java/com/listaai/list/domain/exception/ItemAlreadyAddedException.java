package com.listaai.list.domain.exception;

public class ItemAlreadyAddedException extends RuntimeException {

    public ItemAlreadyAddedException() {
        super("Item already exists");
    }

}
