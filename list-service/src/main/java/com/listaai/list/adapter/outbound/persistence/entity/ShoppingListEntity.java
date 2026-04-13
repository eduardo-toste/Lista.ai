package com.listaai.list.adapter.outbound.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "lists")
public class ShoppingListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingListItemEntity> items;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "list_participants",
            joinColumns = @JoinColumn(name = "list_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<ShoppingListParticipantEntity> participants;

    public ShoppingListEntity(Long id, String name, List<ShoppingListItemEntity> items, List<ShoppingListParticipantEntity> participants) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.participants = participants;
    }

    public ShoppingListEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ShoppingListItemEntity> getItems() {
        return items;
    }

    public List<ShoppingListParticipantEntity> getParticipants() {
        return participants;
    }
}
