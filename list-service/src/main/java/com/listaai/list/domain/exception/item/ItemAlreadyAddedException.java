package com.listaai.list.domain.exception.item;

public class ItemAlreadyAddedException extends RuntimeException {

    public ItemAlreadyAddedException() {
        super("Item already exists");
    }

}
