package com.listaai.list.adapter.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "participants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ShoppingListParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private List<ShoppingListEntity> shoppingLists;

}

