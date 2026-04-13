package com.listaai.list.adapter.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "participants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShoppingListParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private List<ShoppingListEntity> shoppingLists;

}

