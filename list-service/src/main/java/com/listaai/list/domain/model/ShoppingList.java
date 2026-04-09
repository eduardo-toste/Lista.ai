package com.listaai.list.domain.model;

import com.listaai.list.domain.enums.ItemUnit;
import com.listaai.list.domain.exception.ItemAlreadyAddedException;
import com.listaai.list.domain.exception.ItemNotFoundException;
import com.listaai.list.domain.exception.ParticipantAlreadyAddedException;
import com.listaai.list.domain.exception.ParticipantNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList {

    private Long id;
    private String name;
    private List<ShoppingListItem> items;
    private List<ShoppingListParticipant> participants;

    public ShoppingList(Long id, String name, List<ShoppingListItem> items, List<ShoppingListParticipant> participants) {
        this.id = id;
        this.name = name;
        this.items = items != null ? items : new ArrayList<>();
        this.participants = participants != null ? participants : new ArrayList<>();
    }

    public void addItem(ShoppingListItem item) {
        boolean itemAlreadyExists = this.items.stream()
                .anyMatch(existingItem -> existingItem.getName().equalsIgnoreCase(item.getName()));

        if (itemAlreadyExists) {
            throw new ItemAlreadyAddedException();
        }

        this.items.add(item);
    }

    public void removeItem(Long itemId) {
        ShoppingListItem item = findItemById(itemId);
        this.items.remove(item);
    }

    public void updateItem(Long itemId, String name, int quantity, ItemUnit unit) {
        ShoppingListItem item = findItemById(itemId);
        item.update(name, quantity, unit);
    }

    public void markItemAsPurchased(Long itemId) {
        ShoppingListItem item = findItemById(itemId);
        item.markAsPurchased();
    }

    public void unmarkItemAsPurchased(Long itemId) {
        ShoppingListItem item = findItemById(itemId);
        item.unmarkAsPurchased();
    }

    public void addParticipant(ShoppingListParticipant participant) {
        boolean participantAlreadyExists = this.participants.stream()
                .anyMatch(existingParticipant ->
                        existingParticipant.getPhoneNumber().equals(participant.getPhoneNumber()));

        if (participantAlreadyExists) {
            throw new ParticipantAlreadyAddedException();
        }

        this.participants.add(participant);
    }

    public void removeParticipant(Long participantId) {
        ShoppingListParticipant participant = findParticipantById(participantId);
        this.participants.remove(participant);
    }

    private ShoppingListItem findItemById(Long itemId) {
        return this.items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(ItemNotFoundException::new);
    }

    private ShoppingListParticipant findParticipantById(Long participantId) {
        return this.participants.stream()
                .filter(participant -> participant.getId().equals(participantId))
                .findFirst()
                .orElseThrow(ParticipantNotFoundException::new);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }

    public List<ShoppingListParticipant> getParticipants() {
        return participants;
    }

}
