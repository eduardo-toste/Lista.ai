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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ItemUnit getUnit() {
        return unit;
    }

    public void setUnit(ItemUnit unit) {
        this.unit = unit;
    }

    public boolean isPurchased() {
        return purchased;
    }

}
