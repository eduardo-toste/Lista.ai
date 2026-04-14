package com.listaai.list.adapter.outbound.persistence.mapper;

import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListEntity;
import com.listaai.list.adapter.outbound.persistence.entity.ShoppingListItemEntity;
import com.listaai.list.domain.model.ShoppingList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingListPersistenceMapper {

    private final ShoppingListItemPersistenceMapper shoppingListItemPersistenceMapper;
    private final ShoppingListParticipantPersistenceMapper shoppingListParticipantPersistenceMapper;

    public ShoppingListEntity toEntity(ShoppingList domain) {
        List<ShoppingListItemEntity> items = domain.getItems().stream()
                .map(shoppingListItemPersistenceMapper::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));

        ShoppingListEntity entity = ShoppingListEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .items(items)
                .participants(domain.getParticipants().stream()
                        .map(shoppingListParticipantPersistenceMapper::toEntity)
                        .toList())
                .build();

        entity.getItems().forEach(item -> item.setShoppingList(entity));

        return entity;
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
