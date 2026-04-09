package com.listaai.list.domain.model;

import com.listaai.list.domain.enums.ItemUnit;

public class ShoppingListItem {

    private Long id;
    private String name;
    private int quantity;
    private ItemUnit unit;
    private boolean purchased;

    public ShoppingListItem(Long id, String name, int quantity, ItemUnit unit, boolean purchased) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.purchased = purchased;
    }

    public ShoppingListItem(Long id, String name, int quantity, ItemUnit unit) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.purchased = false;
    }

    public void update(String name, int quantity, ItemUnit unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public void markAsPurchased() {
        this.purchased = true;
    }

    public void unmarkAsPurchased() {
        this.purchased = false;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public ItemUnit getUnit() {
        return unit;
    }

    public boolean isPurchased() {
        return purchased;
    }

}
