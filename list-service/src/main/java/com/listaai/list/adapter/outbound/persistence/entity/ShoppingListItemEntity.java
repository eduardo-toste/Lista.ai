package com.listaai.list.adapter.outbound.persistence.entity;

import com.listaai.list.domain.enums.ItemUnit;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class ShoppingListItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;
    private ItemUnit unit;
    private boolean purchased;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private ShoppingListEntity shoppingList;

    public ShoppingListItemEntity(Long id, String name, int quantity, ItemUnit unit, boolean purchased) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.purchased = purchased;
    }

    public ShoppingListItemEntity() {
    }

    public Long getId() {
        return id;
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

