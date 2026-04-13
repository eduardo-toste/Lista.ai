package com.listaai.list.adapter.outbound.persistence.entity;

import com.listaai.list.domain.model.ShoppingList;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "participants")
public class ShoppingListParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private List<ShoppingListEntity> shoppingLists;

    public ShoppingListParticipantEntity(Long id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public ShoppingListParticipantEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

