package com.listaai.list.adapter.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "lists")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

}
