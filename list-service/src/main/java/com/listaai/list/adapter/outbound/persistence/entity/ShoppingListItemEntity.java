package com.listaai.list.adapter.outbound.persistence.entity;

import com.listaai.list.domain.enums.ItemUnit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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

}

