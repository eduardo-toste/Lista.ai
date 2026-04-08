package com.listaai.list.domain.model;

import com.listaai.list.domain.enums.ItemUnit;

import java.util.List;

public class ShoppingList {

    private Long id;
    private String name;
    private List<ShoppingListItem> items;
    private List<ShoppingListParticipant> participants;

    public ShoppingList(Long id, String name, List<ShoppingListItem> items, List<ShoppingListParticipant> participants) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }

    public List<ShoppingListParticipant> getParticipants() {
        return participants;
    }

}
