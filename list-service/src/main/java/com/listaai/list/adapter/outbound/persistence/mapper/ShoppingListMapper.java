package com.listaai.list.adapter.outbound.persistence.mapper;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListEntity;
import com.listaai.list.domain.model.ShoppingList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingListMapper {

    private final ShoppingListItemMapper shoppingListItemMapper;
    private final ShoppingListParticipantMapper shoppingListParticipantMapper;

    public ShoppingListEntity toEntity(ShoppingList domain) {
        return new ShoppingListEntity(
                domain.getId(),
                domain.getName(),
                domain.getItems().stream()
                        .map(shoppingListItemMapper::toEntity)
                        .toList(),
                domain.getParticipants().stream()
                        .map(shoppingListParticipantMapper::toEntity)
                        .toList()
        );
    }

    public ShoppingList toDomain(ShoppingListEntity entity) {
        return new ShoppingList(
                entity.getId(),
                entity.getName(),
                entity.getItems().stream()
                        .map(shoppingListItemMapper::toDomain)
                        .toList(),
                entity.getParticipants().stream()
                        .map(shoppingListParticipantMapper::toDomain)
                        .toList()
        );
    }

}
