package com.listaai.list.adapter.outbound.persistence.mapper;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListEntity;
import com.listaai.list.domain.model.ShoppingList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingListPersistenceMapper {

    private final ShoppingListItemPersistenceMapper shoppingListItemPersistenceMapper;
    private final ShoppingListParticipantPersistenceMapper shoppingListParticipantPersistenceMapper;

    public ShoppingListEntity toEntity(ShoppingList domain) {
        return new ShoppingListEntity(
                domain.getId(),
                domain.getName(),
                domain.getItems().stream()
                        .map(shoppingListItemPersistenceMapper::toEntity)
                        .toList(),
                domain.getParticipants().stream()
                        .map(shoppingListParticipantPersistenceMapper::toEntity)
                        .toList()
        );
    }

    public ShoppingList toDomain(ShoppingListEntity entity) {
        return new ShoppingList(
                entity.getId(),
                entity.getName(),
                entity.getItems().stream()
                        .map(shoppingListItemPersistenceMapper::toDomain)
                        .toList(),
                entity.getParticipants().stream()
                        .map(shoppingListParticipantPersistenceMapper::toDomain)
                        .toList()
        );
    }

}
